import request from '@/utils/request'

/**
 * 获取所有的一级代码信息
 */
export function getFirstDictAll(params) {
  return request.post('/firstDict/getFirstDictAll', params, false)
}
