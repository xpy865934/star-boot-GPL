package com.star.starboot.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.listener
 * @Description: flowabl流程执行监听器
 * @Author: xpy
 * @Date: Created in 2020年10月27日 5:13 下午
 */
@Component
public class FlowableExecutionEventListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {

    }
}
