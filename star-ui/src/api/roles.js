import request from '@/utils/request'

/**
 * 分页查询角色信息
 */
export function queryPager(params) {
  return request.post('/roles/queryPager', params, false)
}

/**
 * 删除角色信息
 * @param {*} data
 */
export function deleteById(data) {
  return request.get('/roles/deleteById/' + data.roleId, {}, true)
}

/**
 * 保存角色信息
 */
export function save(params) {
  return request.post('/roles/save', params, true)
}

/**
 * 更新角色信息
 */
export function update(params) {
  return request.post('/roles/update', params, true)
}

/**
 * 根据角色id获取角色资源
 */
export function getResourcesByRoleTid(params) {
  return request.post('/roles/getResourcesByRoleTid', params, true)
}

/**
 * 更新角色资源信息
 */
export function updateRoleResources(params) {
  return request.post('/roles/updateRoleResources', params, true)
}

/**
 * 根据公司id获取角色列表
 * @param {*} params
 */
export function getRoleListByCompanyId(params) {
  return request.post('/roles/getListByCompantId', params, true)
}

/**
 * 根据用户id获取角色信息
 * @param {*} params
 */
export function getRolesByUserId(params) {
  return request.post('/usersReRoles/getRolesByUserId', params, true)
}

/**
 * 保存用户角色信息
 * @param {*} params
 */
export function saveUserReRoles(params) {
  return request.post('/usersReRoles/save', params, true)
}
