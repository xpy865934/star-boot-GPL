import request from '@/utils/request'

export function login(data) {
  return request.post('/common/login', data, true)
}

/**
 * 注册
 * @param params
 * @returns {*}
 */
export function register(params) {
  return request.post('/common/register', params, true)
}

/**
 * 获取用户登录信息
 * @param {*} userCode
 * @param {*} password
 * @param {*} cCode
 */
export function getUserInfo(params) {
  return request.post('/users/getUserInfo', params, false)
}

/**
 * 用户退出
 */
export function logout() {
  return request.get('/common/logout', {}, false)
}

/**
 * 用户分页查询
 */
export function queryPager(params) {
  return request.post('/users/queryPager', params, false)
}

/**
 * 删除用户
 * @param {*} data
 */
export function deleteById(data) {
  return request.get('/users/deleteById/' + data.userId, {}, true)
}

/**
 * 更新密码
 */
export function changePassword(params) {
  return request.post('/users/changePassword', params, true)
}
