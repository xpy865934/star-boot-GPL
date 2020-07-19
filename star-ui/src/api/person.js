import request from '@/utils/request'

export function save(data) {
  return request.post('/userBasicInfo/save', data, true)
}

export function update(data) {
  return request.post('/userBasicInfo/update', data, true)
}

export function query(data) {
  return request.post('/userBasicInfo/query', data, true)
}
