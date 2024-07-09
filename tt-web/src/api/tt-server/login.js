import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/sys/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/api/sys/user-info',
    method: 'get',
    params: { 'user-token': token }
  })
}

export function logout() {
  return request({
    url: '/api/sys/logout',
    method: 'get'
  })
}
