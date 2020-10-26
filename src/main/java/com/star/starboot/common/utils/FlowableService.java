package com.star.starboot.common.utils;

import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.service.FlowService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.formula.functions.T;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
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

    /**
     *       * 启动流程
     *       *
     *       * @param processKey  流程定义key(流程图ID)
     *       * @param businessKey 业务key
     *       * @param map         参数键值对
     *       * @return 流程实例ID
     *       * @Author 包海鹏
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcess(String processKey, String businessKey, Map<String, Object> map,String userId) {
        Authentication.setAuthenticatedUserId(userId);
        // 流程创建会把businessKey放在流程变量里面
        map.put("businessKey",businessKey);
        // 创建流程默认第一个申请节点审批人为流程创建人
        map.put("sqr",userId);
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
        flowService.updateBusinessData(table, tableId, businessKey, processInstance.getProcessInstanceId(), processInstance.getProcessDefinitionId(),taskIds, taskNames);
        return processInstance;
    }

    /**
     * 创建流程并且完成第一个任务
     * @param processKey
     * @param businessKey
     * @param map
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void startAndConplete(String processKey, String businessKey, Map<String, Object> map,String userId){
        ProcessInstance processInstance = this.startProcess(processKey, businessKey, map, userId);
        this.complete(processInstance.getProcessInstanceId(),businessKey,userId ,"同意");
    }

    /**
     * 终止流程
     * @param processInstanceId
     * @param reason
     */
    @Transactional(rollbackFor = Exception.class)
    public void stopProcess(String processInstanceId, String reason){
        runtimeService.deleteProcessInstance(processInstanceId,reason);
    }

    /**
     * 通过userId 查询当前需要完成的任务
     * @param processInstanceId
     * @param businessKey
     */
    public List<Task> completeByUserId(String processInstanceId, String businessKey){
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        String userId = userInfo.getUserId();
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).processInstanceBusinessKey(businessKey).or().taskAssignee(userId).taskCandidateUser(userId).endOr().active().list();
        return taskList;
    }


    /**
     * 通过roleId 查询当前需要完成的任务
     * @param processInstanceId
     * @param businessKey
     */
    public List<Task> completeByRoleId(String processInstanceId, String businessKey){
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
     * @param businessKey
     * @param approvalComments
     */
    @Transactional(rollbackFor = Exception.class)
    public void complete(String processInstanceId, String businessKey, String userId,String approvalComments){
        List<Task> usersTasks = this.completeByUserId(processInstanceId, businessKey);
//        List<Task> rolesTasks = this.completeByRoleId(processInstanceId, businessKey);
        List<Task> result = new ArrayList<>();
        result.addAll(usersTasks);
//        result.addAll(rolesTasks);
        Map<String, Object> map = new HashedMap();
        map.put("approval_comments",approvalComments);
        // 去重
        Set<Task> taskSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        taskSet.addAll(result);
        List<Task> tasks = new ArrayList<>(taskSet);
        if(!StringUtils.isEmpty(tasks) && tasks.size()>0){
            for (Task task: tasks) {
                taskService.setAssignee(task.getId(),userId);
                taskService.setVariablesLocal(task.getId(), map);
                taskService.complete(task.getId(), null);
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
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        // 更新任务节点相关信息
        flowService.updateBusinessTaskData(table, tableId, businessKey,taskIds, taskNames);
    }


    /**
     * 退回上一节点任务
     * @param businessKey
     * @param approvalComments
     */
    @Transactional(rollbackFor = Exception.class)
    public void backLastNode(String processInstanceId, String businessKey, String userId,String approvalComments){
        List<Task> usersTasks = this.completeByUserId(processInstanceId, businessKey);
//        List<Task> rolesTasks = this.completeByRoleId(processInstanceId, businessKey);
        List<Task> result = new ArrayList<>();
        result.addAll(usersTasks);
//        result.addAll(rolesTasks);
        Map<String, Object> map = new HashedMap();
        map.put("approval_comments",approvalComments);
        // 去重
        Set<Task> taskSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        taskSet.addAll(result);
        List<Task> tasks = new ArrayList<>(taskSet);
        if(!StringUtils.isEmpty(tasks) && tasks.size()>0){
            for (Task task: tasks) {
                Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                // 获取当前节点的activityId,即xml中每个标签的ID
                String currentActivityId = execution.getActivityId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                FlowNode flowNode = (FlowNode)bpmnModel.getFlowElement(currentActivityId);
                SequenceFlow sequenceFlow = flowNode.getIncomingFlows().get(0);
                // 获取上一个节点的activityId
                String sourceRef = sequenceFlow.getSourceRef();


                taskService.setAssignee(task.getId(),userId);
                taskService.setVariablesLocal(task.getId(), map);
                // 流程回退到上一个节点，审批人继续审批
                runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId)
                        .moveActivityIdTo(currentActivityId,sourceRef).changeState();
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
        String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
        // 更新任务节点相关信息
        flowService.updateBusinessTaskData(table, tableId, businessKey,taskIds, taskNames);
    }

    /**
     * 根据用户id查询待办列表
     * @param userId
     */
    public List<Task> getCurrentTaskByUserId(String processDefinitionKey, String userId){
        // taskCandidateUser 会同时查询候选人和候选组  and ( LINK.USER_ID_ = ? or LINK.GROUP_ID_ IN ( ? ) ) ) )
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).or().taskAssignee(userId).taskCandidateUser(userId).endOr().orderByTaskCreateTime().desc().list();
        return taskList;
    }

    /**
     * 根据角色id查询待办列表
     * @param roleIds
     */
    public List<Task> getCurrentTaskByRoleIds(String processDefinitionKey,Set<String> roleIds){
        // taskCandidateGroup 只会查询候选组  and ( LINK.GROUP_ID_ IN ( ? ) ) ) )
        List<Task> taskList = new ArrayList<>();
        for (String roleId: roleIds) {
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
     * @param userId
     */
    public List<String> getCurrentBusinessKey(String processDefinitionKey, String userId, Set<String> roleIds){
        List<String> list = new ArrayList<>();
        // 用户id待办
        List<Task> userTaskList = this.getCurrentTaskByUserId(processDefinitionKey, userId);
        for (Task task: userTaskList) {
            Object businessKey = taskService.getVariable(task.getId(), "businessKey");
            if(!StringUtils.isEmpty(businessKey)){
                list.add(taskService.getVariable(task.getId(),"businessKey").toString());
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
     * @param userId
     */
    public List<HistoricTaskInstance> getHistoryTaskByUserId(String processDefinitionKey, String userId){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(processDefinitionKey).taskAssignee(userId).orderByHistoricTaskInstanceEndTime().desc().list();
        return list;
    }


    /**
     * 根据用户id查询已办列表的businessKey
     * @param userId
     */
    public List<String> getHistoryBusinessKeyByUserId(String processDefinitionKey,String userId){
        List<String> list = new ArrayList<>();
        List<HistoricTaskInstance> historyTaskList = this.getHistoryTaskByUserId(processDefinitionKey,userId);
        for (HistoricTaskInstance historicTaskInstance: historyTaskList) {
            HistoricVariableInstance businessKey = historyService.createHistoricVariableInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).variableName("businessKey").singleResult();
            if(!StringUtils.isEmpty(businessKey.getValue())){
                list.add(businessKey.getValue().toString());
            }
        }
        return list;
    }

    /**
     * 根据用户id获取所有的待办和已办businessKey
     * @param userId
     * @return
     */
    public List<String> getAllTask(String processDefinitionKey, String userId,Set<String> roleIds){
        // 待办
        List<String> currentBusinessKey = this.getCurrentBusinessKey(processDefinitionKey,userId,roleIds);
        // 已办
        List<String> historyBusinessKey = this.getHistoryBusinessKeyByUserId(processDefinitionKey,userId);
        List<String> result = Stream.of(currentBusinessKey, historyBusinessKey)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if(StringUtils.isEmpty(result) || result.size() == 0){
            return null;
        }
        return result;
    }
}
