import request from '@/utils/request'

export function save(data) {
  return request.post('/userBasicInfo/save', data, true)
}
