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
export function loginOut() {
  return request.get('/common/loginOut', {}, false)
}

/**
 * 用户退出
 */
export function queryPager(params) {
  return request.post('/users/queryPager', params, false)
}
