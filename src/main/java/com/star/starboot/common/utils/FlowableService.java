package com.star.starboot.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.dto.RolesDto;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Flow;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.service.FlowService;
import com.star.starboot.system.service.RolesService;
import com.star.starboot.system.service.UsersService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
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
public class FlowableService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

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

    /**
     * * 启动流程
     * *
     * * @param processKey  流程定义key(流程图ID)
     * * @param businessKey 业务key
     * * @param map         参数键值对
     * * @return 流程实例ID
     * * @Author 包海鹏
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcess(String processKey, String businessKey, Map<String, Object> map, String userId) {
        Authentication.setAuthenticatedUserId(userId);
        // 流程创建会把businessKey放在流程变量里面
        map.put("businessKey", businessKey);
        // 创建流程默认第一个申请节点审批人为流程创建人
        map.put("sqr", userId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, map);
        Authentication.setAuthenticatedUserId(null);

        // 获取当前流程变量
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getProcessInstanceId());
        // 更新相关联的业务表数据
        String table = variables.get("table").toString();
        String tableId = variables.get("table_id").toString();
        // 获取当前任务节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        flowService.updateBusinessData(table, tableId, businessKey, processInstance.getProcessInstanceId(), processInstance.getProcessDefinitionId(), taskIds, taskNames, SystemConstant.PROCESS_START);
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
    public void startAndConplete(String processKey, String businessKey, Map<String, Object> map, String userId) {
        ProcessInstance processInstance = this.startProcess(processKey, businessKey, map, userId);
        this.complete(processInstance.getProcessInstanceId(), businessKey, userId, "同意");
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
    public void complete(String processInstanceId, String businessKey, String userId, String approvalComments) {
        Integer processState = SystemConstant.PROCESS_APPROVING;
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
        for (HistoricVariableInstance variable: HistoricVariableInstanceList) {
            variables.put(variable.getVariableName(),variable.getValue());
        }

        // 更新相关联的业务表数据
        String table = variables.get("table").toString();
        String tableId = variables.get("table_id").toString();
        // 获取当前任务节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        // 更新任务节点相关信息
        if(StringUtils.isEmpty(taskIds)){
            // 流程结束
            taskNames = "结束";
            processState = SystemConstant.PROCESS_COMPLETE;
        }
        flowService.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, processState);
    }


    /**
     * 退回上一节点任务
     *
     * @param businessKey
     * @param approvalComments
     */
    @Transactional(rollbackFor = Exception.class)
    public void backLastNode(String processInstanceId, String businessKey, String userId, String approvalComments) {
        Integer processState = SystemConstant.PROCESS_APPROVING;
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
                Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                // 获取当前节点的activityId,即xml中每个标签的ID
                String currentActivityId = execution.getActivityId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(currentActivityId);
                SequenceFlow sequenceFlow = flowNode.getIncomingFlows().get(0);
                // 获取上一个节点的activityId
                String sourceRef = sequenceFlow.getSourceRef();


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
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
        String taskKeys = list.stream().map(Task::getTaskDefinitionKey).collect(Collectors.joining(","));
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        // 退回到第一个节点
        if("sq".equals(taskKeys)){
            processState = SystemConstant.PROCESS_START;
        }
        // 更新任务节点相关信息
        flowService.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, processState);
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
                            if(!StringUtils.isEmpty(assignee)) {
                                flow.setAssignee(assignee.getUserName());
                            }
                        } else {
                            // 设置指定处理人不为空
                            if (!StringUtils.isEmpty(((UserTask) flowElement).getAssignee())) {
                                Users assignee = usersService.getById(((UserTask) flowElement).getAssignee());
                                if(!StringUtils.isEmpty(assignee)) {
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
                                        }else {
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
                                        }else {
                                            assignee += "," + roles.get(i).getRoleName();
                                        }
                                    }
                                }
                                flow.setAssignee(assignee);
                            }
                        }
                        flow.setNodeNameAndUserName(flow.getNodeName()+":"+ flow.getAssignee());
                        flowList.add(flow);
                    }
                    if (flowElement instanceof SubProcess) {
                    }
                }
            }
        }
        List<HistoricTaskInstance> historicTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
        for (int i = 0; i < historicTaskList.size() ; i++) {
            // 最后一个判断当前最后的流程是否已经结束，未结束的不放入结果 是因为当前节点还未审批，所有使用流程图中的审批人或者候选人或候选组
            if((i == (historicTaskList.size()-1)) && StringUtils.isEmpty(historicTaskList.get(i).getEndTime())){
                continue;
            }
            HistoricTaskInstance historicTask = historicTaskList.get(i);
            HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().taskId(historicTask.getId()).variableName("approval_comments").singleResult();
            String approvalComments = "";
            if(!StringUtils.isEmpty(variableInstance)){
                // variableInstance 可能会出现null的情况，因为当前节点会出现在historyService里面，但是还没有审批
                approvalComments = String.valueOf(variableInstance.getValue());
            }
            Flow flow = new Flow();
            flow.setNodeId(historicTask.getTaskDefinitionKey());
            flow.setNodeName(historicTask.getName());
            flow.setApprovalComments(approvalComments);
            Users assignee = usersService.getById(historicTask.getAssignee());
            if(!StringUtils.isEmpty(assignee)) {
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
}
