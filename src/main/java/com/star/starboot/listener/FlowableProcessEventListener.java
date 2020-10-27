package com.star.starboot.listener;

import com.star.starboot.constant.SystemConstant;
import org.apache.commons.collections.map.HashedMap;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.listener
 * @Description: flowabl流程完成监听器
 * @Author: xpy
 * @Date: Created in 2020年10月27日 3:35 下午
 */
@Component
public class FlowableProcessEventListener implements FlowableEventListener {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        // 流程完成
        if(FlowableEngineEventType.PROCESS_COMPLETED.name().equals(flowableEvent.getType().name())){
             // 更新流程状态为完成状态 更新数据状态
            // 获取当前流程变量  不能使用runtimeService.getVariables 一旦最后一步完成之后就无法查询该流程实例
//            List<HistoricVariableInstance> HistoricVariableInstanceList = historyService.createHistoricVariableInstanceQuery().
//                    processInstanceId(((FlowableEntityEventImpl) flowableEvent).getProcessInstanceId()).excludeTaskVariables().list();
//            Map<String, Object> variables = new HashedMap();
//            for (HistoricVariableInstance variable: HistoricVariableInstanceList) {
//                variables.put(variable.getVariableName(),variable.getValue());
//            }
//
//            // 更新相关联的业务表数据
//            String table = variables.get("table").toString();
//            String tableId = variables.get("table_id").toString();
//            // 获取当前任务节点
//            List<Task> list = taskService.createTaskQuery().processInstanceId(((FlowableEntityEventImpl) flowableEvent).getProcessInstanceId()).active().list();
//            String taskIds = list.stream().map(Task::getId).collect(Collectors.joining(","));
//            String taskNames = list.stream().map(Task::getName).collect(Collectors.joining(","));
//            // 更新任务节点相关信息
//            if(StringUtils.isEmpty(taskIds)){
//                // 流程结束
//                taskNames = "结束";
//            }
//            flowService.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, SystemConstant.PROCESS_APPROVING);

        } else {

        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
