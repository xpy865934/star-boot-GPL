import request from '@/utils/request'

export function login(data) {
  return request.post('/user/login', data, true)
}

export function getInfo(token) {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}

/**
 * 注册
 * @param params
 * @returns {*}
 */
export function register(params) {
  return request.post('/common/register', params, true)
}
