import request from '@/utils/request'

/**
 * 分页查询字典信息
 */
export function queryPager(params) {
  return request.post('/dictionary/queryPager', params, false)
}

/**
 * 保存字典信息
 */
export function save(params) {
  return request.post('/dictionary/save', params, true)
}

/**
 * 更新字典信息
 */
export function update(params) {
  return request.post('/dictionary/update', params, true)
}

/**
 * 分页查询一级数据字典信息
 */
export function queryFirstDictPager(params) {
  return request.post('/firstDict/queryPager', params, false)
}

/**
 * 保存一级字典信息
 */
export function saveFirstDict(params) {
  return request.post('/firstDict/save', params, true)
}

/**
 * 更新一级字典信息
 */
export function updateFirstDict(params) {
  return request.post('/firstDict/update', params, true)
}
