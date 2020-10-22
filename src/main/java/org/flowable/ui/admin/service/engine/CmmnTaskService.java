package org.flowable.ui.admin.service.engine;

import org.springframework.stereotype.Service;

/**
 * 覆盖jar包中的CmmnTaskService的定义，修改Bean的定义名称,避免和org.flowable.cmmn.api.CmmnTaskService命名冲突
 * Service for invoking Flowable REST services.
 */
@Service("adminCmmnTaskService")
public class CmmnTaskService {}
