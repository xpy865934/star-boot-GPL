package com.star.starboot.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.starboot.system.entity.Company;
import com.star.starboot.system.entity.Flow;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 流程信息 Mapper 接口
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface FlowMapper extends BaseMapper<Flow> {

    /**
     * 更新业务数据
     * @param table
     * @param tableId
     * @param businessKey
     * @param processInstanceId
     * @param processDefinitionId
     * @param taskIds
     * @param taskNames
     */
    void updateBusinessData(@Param("table") String table, @Param("tableId") String tableId, @Param("businessKey") String businessKey,
                            @Param("processInstanceId") String processInstanceId, @Param("processDefinitionId") String processDefinitionId,
                            @Param("taskIds") String taskIds, @Param("taskNames") String taskNames, @Param("taskKeys") String taskKeys, @Param("assigneeIds") String assigneeIds, @Param("assigneeNames") String assigneeNames, @Param("processState") Integer processState);

    /**
     * 更新业务数据任务相关信息
     * @param table
     * @param tableId
     * @param businessKey
     * @param taskIds
     * @param taskNames
     */
    void updateBusinessTaskData(@Param("table") String table, @Param("tableId") String tableId, @Param("businessKey") String businessKey,
                                @Param("taskIds") String taskIds, @Param("taskNames") String taskNames, @Param("taskKeys") String taskKeys, @Param("assigneeIds") String assigneeIds, @Param("assigneeNames") String assigneeNames, @Param("processState") Integer processState, @Param("assignee") String assignee);
}
