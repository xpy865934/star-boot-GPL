import request from '@/utils/request'

/**
 * 获取组织架构树
 */
export function getDepartmentTree(params) {
  return request.get('/department/getDepartmentTree', params, false)
}

/**
 * 根据公司查询部门列表
 */
export function getListByCompanyId(params) {
  return request.post('/department/getListByCompanyId', params, false)
}
