import request from '@/utils/request'

/**
 * 分页查询系统日志信息
 */
export function queryPager(params) {
  return request.post('/sysLog/queryPager', params, false)
}
