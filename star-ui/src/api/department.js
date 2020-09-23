import request from '@/utils/request'

/**
 * 获取组织架构树
 */
export function getDepartmentTree(params) {
  return request.get('/department/getDepartmentTree', params, false)
}
