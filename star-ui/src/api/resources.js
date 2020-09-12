import request from '@/utils/request'

/**
 * 查询资源信息列表
 */
export function queryList(params) {
  return request.post('/resources/queryList', params, true)
}

/**
 * 更新是否启用
 */
export function updateUsed(params) {
  return request.post('/resources/updateUsed', params, true)
}

/**
 * 删除资源数据
 */
export function deleteById(params) {
  return request.get('/resources/deleteById/' + params, params, true)
}

/**
 * 保存菜单资源信息
 */
export function save(params) {
  return request.post('/resources/save', params, true)
}

/**
 * 更新菜单资源信息
 */
export function update(params) {
  return request.post('/resources/update', params, true)
}
