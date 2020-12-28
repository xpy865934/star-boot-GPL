package com.star.starboot.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.starboot.common.entity.AbstractEntity;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.customer.dto.CustomerHousesProjectDto;
import com.star.starboot.customer.service.CustomerHousesProjectService;
import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.dto.RolesDto;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Flow;
import com.star.starboot.system.entity.Roles;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.entity.UsersReRoles;
import com.star.starboot.system.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntityImpl;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.common.utils
 * @Description: flowable工具类
 * @Author: xpy
 * @Date: Created in 2020年10月22日 3:23 下午
 */
@Service
public class FlowableUtils {

    @Autowired
    private MessageService messageService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private UsersReRolesService usersReRolesService;

    /**
     * * 启动流程
     * *
     * * @param processKey  流程定义key(流程图ID)
     * * @param businessKey 业务key
     * * @param map         参数键值对
     * * @return 流程实例ID
     * * @Author xpy
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcess(String processInstanceName, String processKey, String businessKey, Map<String, Object> map, String userId) {
        Authentication.setAuthenticatedUserId(userId);
        // 流程创建会把businessKey放在流程变量里面
        map.put("businessKey", businessKey);
        // 创建流程默认第一个申请节点审批人为流程创建人
        map.put("sqr", userId);
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        processInstanceBuilder.processDefinitionKey(processKey);
        processInstanceBuilder.businessKey(businessKey);
        processInstanceBuilder.variables(map);
        processInstanceBuilder.name(processInstanceName);
        ProcessInstance processInstance = processInstanceBuilder.start();
        Authentication.setAuthenticatedUserId(null);

        // 获取当前流程变量
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getProcessInstanceId());
        // 更新相关联的业务表数据
        String table = variables.get("table").toString();
        String tableId = variables.get("table_id").toString();
        // 获取当前任务节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).includeIdentityLinks().active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        String taskKeys = list.stream().map(Task::getTaskDefinitionKey).collect(Collectors.joining(","));
        Map<String, String> taskAssigneeIds = getTaskAssigneeIds(list);
        String assigneeNames = getTaskAssigneeNames(list);
        flowService.updateBusinessData(table, tableId, businessKey, processInstance.getProcessInstanceId(), processInstance.getProcessDefinitionId(), taskIds, taskNames, taskKeys, taskAssigneeIds.get("assigneeIds"), assigneeNames, taskAssigneeIds.get("currentAssigneeType") ,SystemConstant.PROCESS_START);
        return processInstance;
    }

    /**
     * 创建流程并且完成第一个任务
     *
     * @param processKey
     * @param businessKey
     * @param map
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void startAndComplete(String processInstanceName, String processKey, String businessKey, Map<String, Object> map, String userId) {
        ProcessInstance processInstance = this.startProcess(processInstanceName, processKey, businessKey, map, userId);
        this.taskComplete(processInstance.getProcessInstanceId(), businessKey, "同意");
    }

    /**
     * 终止流程
     *
     * @param processInstanceId
     * @param reason
     */
    @Transactional(rollbackFor = Exception.class)
    public void stopProcess(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    /**
     * 通过userId 查询当前需要完成的任务
     *
     * @param processInstanceId
     * @param businessKey
     */
    public List<Task> completeByUserId(String processInstanceId, String businessKey) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        String userId = userInfo.getUserId();
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).processInstanceBusinessKey(businessKey).or().taskAssignee(userId).taskCandidateUser(userId).endOr().active().list();
        return taskList;
    }


    /**
     * 通过roleId 查询当前需要完成的任务
     *
     * @param processInstanceId
     * @param businessKey
     */
    public List<Task> completeByRoleId(String processInstanceId, String businessKey) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        Set<String> roles = userInfo.getRoles();
        Iterator<String> it = roles.iterator();
        List<Task> taskList = new ArrayList<>();
        while (it.hasNext()) {
            List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).processInstanceBusinessKey(businessKey).taskCandidateGroup(it.next()).active().list();
            taskList.addAll(list);
        }
        // 去重
        Set<Task> taskSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        taskSet.addAll(taskList);
        return new ArrayList<>(taskSet);
    }

    /**
     * 完成任务
     *
     * @param businessKey
     * @param approvalComments
     */
    @Transactional(rollbackFor = Exception.class)
    public void taskComplete(String processInstanceId, String businessKey, String approvalComments) {
        String userId = ShiroUtils.build().getUserInfo().getUserId();
        Integer processState = SystemConstant.PROCESS_APPROVING;
        String lastAssignee = userId;
        List<Task> usersTasks = this.completeByUserId(processInstanceId, businessKey);
//        List<Task> rolesTasks = this.completeByRoleId(processInstanceId, businessKey);
        List<Task> result = new ArrayList<>();
        result.addAll(usersTasks);
//        result.addAll(rolesTasks);
        Map<String, Object> map = new HashedMap();
        map.put("approval_comments", approvalComments);
        // 去重
        Set<Task> taskSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        taskSet.addAll(result);
        List<Task> tasks = new ArrayList<>(taskSet);
        if (!StringUtils.isEmpty(tasks) && tasks.size() > 0) {
            for (Task task : tasks) {
                taskService.setAssignee(task.getId(), userId);
                taskService.setVariablesLocal(task.getId(), map);
                taskService.complete(task.getId(), null);
            }
        } else {
            // 所有任务都无法完成的情况，则没有权限
            throw new BusinessException("处理人不是当前用户，无权限操作");
        }


        // 获取当前流程变量  不能使用runtimeService.getVariables 一旦最后一步完成之后就无法查询该流程实例
        List<HistoricVariableInstance> HistoricVariableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).excludeTaskVariables().list();
        Map<String, Object> variables = new HashedMap();
        for (HistoricVariableInstance variable : HistoricVariableInstanceList) {
            variables.put(variable.getVariableName(), variable.getValue());
        }

        // 更新相关联的业务表数据
        String table = variables.get("table").toString();
        String tableId = variables.get("table_id").toString();
        // 获取当前任务节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).includeIdentityLinks().active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        String taskKeys = list.stream().map(Task::getTaskDefinitionKey).collect(Collectors.joining(","));
        Map<String, String> taskAssigneeIds = getTaskAssigneeIds(list);
        String assigneeNames = getTaskAssigneeNames(list);
        // 更新任务节点相关信息
        if (StringUtils.isEmpty(taskIds)) {
            // 流程结束
            taskNames = "结束";
            processState = SystemConstant.PROCESS_COMPLETE;
            // 流程结束后不允许撤回，设置上一审批人为null
            lastAssignee = null;
        }
        flowService.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, taskKeys, taskAssigneeIds.get("assigneeIds"), assigneeNames, taskAssigneeIds.get("currentAssigneeType"), processState, lastAssignee);

        // 发送通知给下一审批人
        Map<String, String> msgMap = this.generateAppMsg(processInstanceId, businessKey, SystemConstant.APPROVAL);
        List<String> toslist = new ArrayList<>();
        if("candidate".equals(taskAssigneeIds.get("currentAssigneeType"))){
            List<String> roleslist = Arrays.asList(taskAssigneeIds.get("assigneeIds").split(","));
            for (String roleId: roleslist) {
                LambdaQueryWrapper<UsersReRoles> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UsersReRoles::getRoleId, roleId);
                List<UsersReRoles> usersReRoles = usersReRolesService.list(wrapper);
                for (UsersReRoles usersReRole :usersReRoles) {
                    toslist.add(usersReRole.getUserId());
                }
            }
        } else  if("assignee".equals(taskAssigneeIds.get("currentAssigneeType"))){
            toslist = Arrays.asList(taskAssigneeIds.get("assigneeIds").split(","));
        }
        messageService.sendMessage(toslist,userId,SystemConstant.FLOWABLEMSG,msgMap.get("title"),msgMap.get("secondTitle"),msgMap.get("content"),msgMap.get("bindTable"),msgMap.get("dataId"));
    }

    /**
     * 完成任务
     *
     * @param businessKey
     * @param approvalComments
     */
    @Transactional(rollbackFor = Exception.class)
    public void complete(AbstractEntity abstractEntity, String businessKey, String approvalComments) {
        checkFlowPermission(abstractEntity);
        this.taskComplete(abstractEntity.getProcessInstanceId(),businessKey,approvalComments);
    }


    /**
     * 退回上一节点任务
     *
     * @param businessKey
     * @param approvalComments
     */
    @Transactional(rollbackFor = Exception.class)
    public void backLastNode(AbstractEntity abstractEntity, String businessKey, String approvalComments) {
        checkFlowPermission(abstractEntity);
        Integer processState = SystemConstant.PROCESS_APPROVING;
        String processInstanceId = abstractEntity.getProcessInstanceId();
        String userId = ShiroUtils.build().getUserInfo().getUserId();
        String sourceRef = "";
        List<Task> usersTasks = this.completeByUserId(processInstanceId, businessKey);
//        List<Task> rolesTasks = this.completeByRoleId(processInstanceId, businessKey);
        List<Task> result = new ArrayList<>();
        result.addAll(usersTasks);
//        result.addAll(rolesTasks);
        Map<String, Object> map = new HashedMap();
        map.put("approval_comments", "退回：" + approvalComments);
        // 去重
        Set<Task> taskSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        taskSet.addAll(result);
        List<Task> tasks = new ArrayList<>(taskSet);
        if (!StringUtils.isEmpty(tasks) && tasks.size() > 0) {
            for (Task task : tasks) {
                Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                // 获取当前节点的activityId,即xml中每个标签的ID
                String currentActivityId = execution.getActivityId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(currentActivityId);
                SequenceFlow sequenceFlow = flowNode.getIncomingFlows().get(0);
                // 获取上一个节点的activityId
                sourceRef = sequenceFlow.getSourceRef();


                taskService.setAssignee(task.getId(), userId);
                taskService.setVariablesLocal(task.getId(), map);
                // 流程回退到上一个节点，审批人继续审批
                runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId)
                        .moveActivityIdTo(currentActivityId, sourceRef).changeState();
            }
        } else {
            // 所有任务都无法完成的情况，则没有权限
            throw new BusinessException("处理人不是当前用户，无权限操作");
        }


        // 获取当前流程变量
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        // 更新相关联的业务表数据
        String table = variables.get("table").toString();
        String tableId = variables.get("table_id").toString();
        // 获取当前任务节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).includeIdentityLinks().active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        String taskKeys = list.stream().map(Task::getTaskDefinitionKey).collect(Collectors.joining(","));
        Map<String, String> taskAssigneeIds = getTaskAssigneeIds(list);
        String assigneeNames = getTaskAssigneeNames(list);
        // 退回到第一个节点
        if ("sq".equals(sourceRef)) {
            processState = SystemConstant.PROCESS_START;
        }
        // 更新任务节点相关信息
        flowService.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, taskKeys, taskAssigneeIds.get("assigneeIds"), assigneeNames, taskAssigneeIds.get("currentAssigneeType"), processState, null);

        // 发送通知给下一审批人
        Map<String, String> msgMap = this.generateAppMsg(processInstanceId, businessKey, SystemConstant.BACK);
        List<String> toslist = new ArrayList<>();
        if("candidate".equals(taskAssigneeIds.get("currentAssigneeType"))){
            List<String> roleslist = Arrays.asList(taskAssigneeIds.get("assigneeIds").split(","));
            for (String roleId: roleslist) {
                LambdaQueryWrapper<UsersReRoles> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UsersReRoles::getRoleId, roleId);
                List<UsersReRoles> usersReRoles = usersReRolesService.list(wrapper);
                for (UsersReRoles usersReRole :usersReRoles) {
                    toslist.add(usersReRole.getUserId());
                }
            }
        } else  if("assignee".equals(taskAssigneeIds.get("currentAssigneeType"))){
            toslist = Arrays.asList(taskAssigneeIds.get("assigneeIds").split(","));
        }
        messageService.sendMessage(toslist,userId,SystemConstant.FLOWABLEMSG,msgMap.get("title"),msgMap.get("secondTitle"),msgMap.get("content"),msgMap.get("bindTable"),msgMap.get("dataId"));
    }

    /**
     * 撤回任务
     *
     * @param processInstanceId
     * @param businessKey
     */
    @Transactional(rollbackFor = Exception.class)
    public void recallNode(String processInstanceId, String businessKey, String lastAssignee) {
        Integer processState = SystemConstant.PROCESS_APPROVING;
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        // 判断当前人是不是上一审批人
        if (!userInfo.getUserId().equals(lastAssignee)) {
            throw new BusinessException("上一处理人不是当前用户，无权限操作");
        }
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).processInstanceBusinessKey(businessKey).active().list();
        Map<String, Object> map = new HashedMap();
        String sourceRef = "";
        map.put("approval_comments", userInfo.getUserName() + "自主撤回");
        // 去重
        Set<Task> taskSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        taskSet.addAll(taskList);
        List<Task> tasks = new ArrayList<>(taskSet);
        if (!StringUtils.isEmpty(tasks) && tasks.size() > 0) {
            for (Task task : tasks) {
                Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                // 获取当前节点的activityId,即xml中每个标签的ID
                String currentActivityId = execution.getActivityId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(currentActivityId);
                SequenceFlow sequenceFlow = flowNode.getIncomingFlows().get(0);
                // 获取上一个节点的activityId
                sourceRef = sequenceFlow.getSourceRef();

                taskService.setAssignee(task.getId(), userInfo.getUserId());
                taskService.setVariablesLocal(task.getId(), map);
                // 流程回退到上一个节点，审批人继续审批
                runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId)
                        .moveActivityIdTo(currentActivityId, sourceRef).changeState();
            }
        } else {
            // 所有任务都无法完成的情况，则没有权限
            throw new BusinessException("流程已审批，无法撤回");
        }

        // 获取当前流程变量
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        // 更新相关联的业务表数据
        String table = variables.get("table").toString();
        String tableId = variables.get("table_id").toString();
        // 获取当前任务节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).includeIdentityLinks().active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        String taskKeys = list.stream().map(Task::getTaskDefinitionKey).collect(Collectors.joining(","));
        Map<String, String> taskAssigneeIds = getTaskAssigneeIds(list);
        String assigneeNames = getTaskAssigneeNames(list);
        // 撤回到第一个节点
        if ("sq".equals(sourceRef)) {
            processState = SystemConstant.PROCESS_START;
        }
        // 更新任务节点相关信息
        flowService.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, taskKeys, taskAssigneeIds.get("assigneeIds"), assigneeNames, taskAssigneeIds.get("currentAssigneeType"), processState, null);
    }

    /**
     * 根据用户id查询待办列表
     *
     * @param userId
     */
    public List<Task> getCurrentTaskByUserId(String processDefinitionKey, String userId) {
        // taskCandidateUser 会同时查询候选人和候选组  and ( LINK.USER_ID_ = ? or LINK.GROUP_ID_ IN ( ? ) ) ) )
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).or().taskAssignee(userId).taskCandidateUser(userId).endOr().orderByTaskCreateTime().desc().list();
        return taskList;
    }

    /**
     * 根据角色id查询待办列表
     *
     * @param roleIds
     */
    public List<Task> getCurrentTaskByRoleIds(String processDefinitionKey, Set<String> roleIds) {
        // taskCandidateGroup 只会查询候选组  and ( LINK.GROUP_ID_ IN ( ? ) ) ) )
        List<Task> taskList = new ArrayList<>();
        for (String roleId : roleIds) {
            List<Task> task = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskCandidateGroup(roleId).orderByTaskCreateTime().desc().list();
            taskList.addAll(task);
        }
        // 去重
        List<Task> result = taskList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(o -> o.getId()))), ArrayList::new));
        return result;
    }


    /**
     * 查询待办列表的businessKey
     *
     * @param userId
     */
    public List<String> getCurrentBusinessKey(String processDefinitionKey, String userId, Set<String> roleIds) {
        List<String> list = new ArrayList<>();
        // 用户id待办
        List<Task> userTaskList = this.getCurrentTaskByUserId(processDefinitionKey, userId);
        for (Task task : userTaskList) {
            Object businessKey = taskService.getVariable(task.getId(), "businessKey");
            if (!StringUtils.isEmpty(businessKey)) {
                list.add(taskService.getVariable(task.getId(), "businessKey").toString());
            }
        }
        // 角色待办  用户id查询待办已经包含角色查询
//        List<Task> roleTaskList = this.getCurrentTaskByRoleIds(processDefinitionKey, roleIds);
//        for (Task task: roleTaskList) {
//            Object businessKey = taskService.getVariable(task.getId(), "businessKey");
//            if(!StringUtils.isEmpty(businessKey)){
//                list.add(taskService.getVariable(task.getId(),"businessKey").toString());
//            }
//        }
        List<String> result = list.stream().distinct().collect(Collectors.toList());
        return result;
    }

    /**
     * 根据用户id查询已办列表
     *
     * @param userId
     */
    public List<HistoricTaskInstance> getHistoryTaskByUserId(String processDefinitionKey, String userId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(processDefinitionKey).taskAssignee(userId).orderByHistoricTaskInstanceEndTime().desc().list();
        return list;
    }


    /**
     * 根据用户id查询已办列表的businessKey
     *
     * @param userId
     */
    public List<String> getHistoryBusinessKeyByUserId(String processDefinitionKey, String userId) {
        List<String> list = new ArrayList<>();
        List<HistoricTaskInstance> historyTaskList = this.getHistoryTaskByUserId(processDefinitionKey, userId);
        for (HistoricTaskInstance historicTaskInstance : historyTaskList) {
            HistoricVariableInstance businessKey = historyService.createHistoricVariableInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).variableName("businessKey").singleResult();
            if (!StringUtils.isEmpty(businessKey.getValue())) {
                list.add(businessKey.getValue().toString());
            }
        }
        return list;
    }

    /**
     * 根据用户id获取所有的待办和已办businessKey
     *
     * @param userId
     * @return
     */
    public List<String> getAllTask(String processDefinitionKey, String userId, Set<String> roleIds) {
        // 待办
        List<String> currentBusinessKey = this.getCurrentBusinessKey(processDefinitionKey, userId, roleIds);
        // 已办
        List<String> historyBusinessKey = this.getHistoryBusinessKeyByUserId(processDefinitionKey, userId);
        List<String> result = Stream.of(currentBusinessKey, historyBusinessKey)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if (StringUtils.isEmpty(result) || result.size() == 0) {
            return null;
        }
        return result;
    }

    public List<Flow> getFLowNodes(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 结果
        List<Flow> result = new ArrayList<>();
        // 流程全部节点
        List<Flow> flowList = new ArrayList<>();
        List<Process> processes = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId()).getProcesses();
        for (Process process : processes) {
            Collection<FlowElement> flowElements = process.getFlowElements();
            if (CollectionUtils.isNotEmpty(flowElements)) {
                for (FlowElement flowElement : flowElements) {
                    if (flowElement instanceof UserTask) {
                        Flow flow = new Flow();
                        flow.setNodeName(flowElement.getName()).setNodeId(flowElement.getId());

                        // 查询当前的处理人，候选人，或者候选组
                        if ("${sqr}".equals(((UserTask) flowElement).getAssignee())) {
                            // 第一个申请节点，如果设置的是sqr
                            String sqr = String.valueOf(historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName("sqr").singleResult().getValue());
                            Users assignee = usersService.getById(sqr);
                            if (!StringUtils.isEmpty(assignee)) {
                                flow.setAssignee(assignee.getUserName());
                            }
                        } else {
                            // 设置指定处理人不为空
                            if (!StringUtils.isEmpty(((UserTask) flowElement).getAssignee())) {
                                Users assignee = usersService.getById(((UserTask) flowElement).getAssignee());
                                if (!StringUtils.isEmpty(assignee)) {
                                    flow.setAssignee(assignee.getUserName());
                                }
                            }
                            // 设置candidateUsers不为空
                            if (CollectionUtil.isNotEmpty(((UserTask) flowElement).getCandidateUsers())) {
                                List<String> candidateUsers = ((UserTask) flowElement).getCandidateUsers();
                                List<UsersDto> users = usersService.getByIds(candidateUsers);
                                String assignee = "";
                                if (!StringUtils.isEmpty(users) && users.size() > 0) {
                                    for (int i = 0; i < users.size(); i++) {
                                        if (i == 0) {
                                            assignee += users.get(i).getUserName();
                                        } else {
                                            assignee += "," + users.get(i).getUserName();
                                        }
                                    }
                                }
                                flow.setAssignee(assignee);
                            }
                            // 设置candidateGroups不为空
                            if (CollectionUtil.isNotEmpty(((UserTask) flowElement).getCandidateGroups())) {
                                List<String> candidateGroups = ((UserTask) flowElement).getCandidateGroups();
                                List<RolesDto> roles = rolesService.getByIds(candidateGroups);
                                String assignee = "";
                                if (!StringUtils.isEmpty(roles) && roles.size() > 0) {
                                    for (int i = 0; i < roles.size(); i++) {
                                        if (i == 0) {
                                            assignee += roles.get(i).getRoleName();
                                        } else {
                                            assignee += "," + roles.get(i).getRoleName();
                                        }
                                    }
                                }
                                flow.setAssignee(assignee);
                            }
                        }
                        flow.setNodeNameAndUserName(flow.getNodeName() + ":" + flow.getAssignee());
                        flowList.add(flow);
                    }
                    if (flowElement instanceof SubProcess) {
                    }
                }
            }
        }
        List<HistoricTaskInstance> historicTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
        for (int i = 0; i < historicTaskList.size(); i++) {
            // 最后一个判断当前最后的流程是否已经结束，未结束的不放入结果 是因为当前节点还未审批，所有使用流程图中的审批人或者候选人或候选组
            if ((i == (historicTaskList.size() - 1)) && StringUtils.isEmpty(historicTaskList.get(i).getEndTime())) {
                continue;
            }
            HistoricTaskInstance historicTask = historicTaskList.get(i);
            HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().taskId(historicTask.getId()).variableName("approval_comments").singleResult();
            String approvalComments = "";
            if (!StringUtils.isEmpty(variableInstance)) {
                // variableInstance 可能会出现null的情况，因为当前节点会出现在historyService里面，但是还没有审批
                approvalComments = String.valueOf(variableInstance.getValue());
            }
            Flow flow = new Flow();
            flow.setNodeId(historicTask.getTaskDefinitionKey());
            flow.setNodeName(historicTask.getName());
            flow.setApprovalComments(approvalComments);
            Users assignee = usersService.getById(historicTask.getAssignee());
            if (!StringUtils.isEmpty(assignee)) {
                flow.setAssignee(assignee.getUserName());
            }
            flow.setNodeNameAndUserName(flow.getNodeName() + ":" + flow.getAssignee());
            result.add(flow);
        }

        // 取出流程剩余节点
        HistoricTaskInstance lastTask = historicTaskList.get(historicTaskList.size() - 1);
        if (!StringUtils.isEmpty(lastTask.getEndTime())) {
            // 流程已经结束
//            result.get(result.size() - 1).setActive(true);
        } else {
            // 流程未结束
            boolean isTarget = false;
            for (Flow flow : flowList) {
                if (flow.getNodeId().equals(lastTask.getTaskDefinitionKey())) {
                    isTarget = true;
                    flow.setActive(true);
                }
                if (isTarget) {
                    result.add(flow);
                }
            }
        }
        return result;
    }

    /**
     * 获取任务的处理人id,list为当前流程同一时间需要处理的任务列表
     *
     * @param list
     * @return
     */
    public Map<String, String> getTaskAssigneeIds(List<Task> list) {
        Map<String, String> map = new HashedMap();
        // TODO 此处设置类型 未考虑到多节点会签，目前全部是一个节点按顺序执行，不会出现同一条数据多个任务节点
        String type = null;
        List<String> result = new ArrayList<>();
        for (Task task : list) {
            if (!StringUtils.isEmpty(task.getAssignee())) {
                type = "assignee";
                result.add(task.getAssignee());
                continue;
            }
            List<IdentityLinkEntityImpl> identityLinks = (List<IdentityLinkEntityImpl>) task.getIdentityLinks();
            if (CollectionUtil.isNotEmpty(identityLinks)) {
                for (IdentityLinkEntityImpl identity : identityLinks) {
                    if (!StringUtils.isEmpty(identity.getUserId())) {
                        type = "assignee";
                        result.add(identity.getUserId());
                    } else if (!StringUtils.isEmpty(identity.getGroupId())) {
                        type = "candidate";
                        result.add(identity.getGroupId());
                    }
                }
            }
        }
        map.put("currentAssigneeType", type);
        map.put("assigneeIds", String.join(",", result));
        return map;
    }

    /**
     * 获取任务的处理人名称或者角色名称,list为当前流程同一时间需要处理的任务列表
     *
     * @param list
     * @return
     */
    public String getTaskAssigneeNames(List<Task> list) {
        List<String> result = new ArrayList<>();
        for (Task task : list) {
            if (!StringUtils.isEmpty(task.getAssignee())) {
                Users user = usersService.getById(task.getAssignee());
                result.add(user.getUserName());
                continue;
            }
            List<IdentityLinkEntityImpl> identityLinks = (List<IdentityLinkEntityImpl>) task.getIdentityLinks();
            if (CollectionUtil.isNotEmpty(identityLinks)) {
                for (IdentityLinkEntityImpl identity : identityLinks) {
                    if (!StringUtils.isEmpty(identity.getUserId())) {
                        Users user = usersService.getById(identity.getUserId());
                        result.add(user.getUserName());
                    } else if (!StringUtils.isEmpty(identity.getGroupId())) {
                        Roles role = rolesService.getById(identity.getGroupId());
                        result.add(role.getRoleName());
                    }
                }
            }
        }
        return String.join(",", result);
    }

    /**
     * 检查当前数据处理人是否对应
     * @param abstractEntity
     */
    public void checkFlowPermission(AbstractEntity abstractEntity) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        // 判断权限
        if (!StringUtils.isEmpty(abstractEntity.getCurrentAssignee())) {
            if (abstractEntity.getCurrentAssignee().indexOf(userInfo.getUserId()) > -1) {
                return;
            } else {
                Set<String> roles = userInfo.getRoles();
                boolean hasPermission = false;
                for (String str : roles) {
                    if(abstractEntity.getCurrentAssignee().indexOf(str)>-1){
                        hasPermission = true;
                    }
                }
                if(hasPermission){
                    return;
                }
                throw new BusinessException("无权限操作");
            }
        } else {
            throw new BusinessException("无权限操作");
        }
    }

    /**
     * 生成app发送的消息
     * @return
     */
    private Map<String, String> generateAppMsg(String processInstanceId,String businessKey, String type){
        Map<String, String> result = new HashedMap();
        result.put("title","");
        result.put("secondTitle","");
        result.put("content","");
        result.put("bindTable","");
        result.put("dataId",businessKey);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionKey = historicProcessInstance.getProcessDefinitionKey();
        switch (processDefinitionKey){
            case SystemConstant.AFTERSALES:
                AfterSalesService afterSalesService = ContextUtils.getBean(AfterSalesService.class);
                AfterSalesDto afterSalesDto = afterSalesService.queryById(businessKey);
                result.put("title",type + "-售后服务:" + afterSalesDto.getTaskNames());
                result.put("secondTitle",afterSalesDto.getCustomerUnique());
                result.put("content","(" + afterSalesDto.getCustomerUnique()+")有新的售后服务需要您处理，请及时登录系统处理！");
                result.put("bindTable","t_after_sales");
                break;
            case SystemConstant.PROJECT:
                CustomerHousesProjectService customerHousesProjectService = ContextUtils.getBean(CustomerHousesProjectService.class);
                CustomerHousesProjectDto customerHousesProjectDto = customerHousesProjectService.queryById(businessKey);
                result.put("title",type + "-" +customerHousesProjectDto.getTaskNames());
                result.put("secondTitle",customerHousesProjectDto.getCustomerUnique());
                result.put("content","(" + customerHousesProjectDto.getCustomerUnique()+")有新的项目流程需要您处理，请及时登录系统处理！");
                result.put("bindTable","t_customer_houses_project");
                break;
            default:
                break;
        }
        return result;
    }
}
