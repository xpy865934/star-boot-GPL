import request from '@/utils/request'
import { getFileUrl } from '../utils/common'

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

/**
 * 下载文件
 * @param params
 */
export function downloadFile(params) {
  var url = getFileUrl('/file/downloadFile', params.fileId)
  window.open(url)
}

