import request from '@/utils/request'

export function save(data) {
  return request.post('/uploadData/save', data, true)
}

export function update(data) {
  return request.post('/uploadData/update', data, true)
}

export function upload(data) {
  return request.post('/uploadData/upload', data, true)
}

export function queryByDate(data) {
  return request.post('/uploadData/queryByDate', data, true)
}

export function queryPager(data) {
  return request.post('/uploadData/queryPager', data, true)
}
