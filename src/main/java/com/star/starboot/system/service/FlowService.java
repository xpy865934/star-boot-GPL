package com.star.starboot.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starboot.system.entity.Flow;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 公司信息 服务类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
public interface FlowService extends IService<Flow> {

    /**
     * 更新业务表数据
     * @param table
     * @param tableId
     * @param businessKey
     * @param processInstanceId
     * @param processDefinitionId
     * @param taskIds
     * @param taskNames
     */
    void updateBusinessData(String table, String tableId, String businessKey, String processInstanceId, String processDefinitionId, String taskIds, String taskNames);

    /**
     * 更新业务表任务相关数据
     * @param table
     * @param tableId
     * @param businessKey
     * @param taskIds
     * @param taskNames
     */
    void updateBusinessTaskData(String table, String tableId, String businessKey, String taskIds, String taskNames);
}
