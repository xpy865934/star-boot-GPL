import request from '@/utils/request'

/**
 * 分页查询
 */
export function getUserMessagePager(params) {
  return request.post('/message/getUserMessagePager', params, false)
}

/**
 * 更新消息为已读
 */
export function upadteUserMessageRead(params) {
  return request.post('/message/upadteUserMessageRead', params, false)
}

/**
 * 一键已读
 */
export function updateAllRead(params) {
  return request.post('/message/updateAllRead', params, false)
}

