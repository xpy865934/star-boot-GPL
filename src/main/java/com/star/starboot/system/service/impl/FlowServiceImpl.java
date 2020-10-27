package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.system.dao.FlowMapper;
import com.star.starboot.system.entity.Flow;
import com.star.starboot.system.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程信息 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {

    @Autowired
    private FlowMapper flowMapper;

    @Override
    public void updateBusinessData(String table, String tableId, String businessKey, String processInstanceId, String processDefinitionId, String taskIds, String taskNames, Integer processState) {
        flowMapper.updateBusinessData(table, tableId, businessKey, processInstanceId, processDefinitionId, taskIds, taskNames, processState);
    }

    @Override
    public void updateBusinessTaskData(String table, String tableId, String businessKey, String taskIds, String taskNames, Integer processState) {
        flowMapper.updateBusinessTaskData(table, tableId, businessKey, taskIds, taskNames, processState);
    }
}
