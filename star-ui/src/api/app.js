import request from '@/utils/request'

/**
 * 获取所有的一级代码信息
 */
export function getFirstDictAll(params) {
  return request.post('/firstDict/getFirstDictAll', params, false)
}

/**
 * 获取所有的用户信息
 */
export function getUserList(params) {
  return request.post('/users/queryList', params, false)
}

/**
 * 获取所有的用户信息
 */
export function getFLowNodes(params) {
  return request.post('/common/getFLowNodes', params, false)
}

