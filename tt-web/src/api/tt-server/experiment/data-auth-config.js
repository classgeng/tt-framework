import request from '@/utils/request'

const PATH = '/api/data-auth-config'

/**
 * 获取数据授权集合(这个用户的)
 * @param userId
 * @returns {*}
 */
export function getAssignableDataAuthList(userId) {
  return request({
    url: `${PATH}/assignable-list/${userId}`,
    method: 'get'
  })
}

/**
 * 获取数据授权集合(这个用户的)
 * @param userId
 * @returns {*}
 */
export function getAssignedDataAuthList(userId) {
  return request({
    url: `${PATH}/assigned-list/${userId}`,
    method: 'get'
  })
}

/**
 * 数据权限授予
 * @param data
 * @returns {*}
 */
export function dataAuthAssign(data) {
  return request({
    url: `${PATH}/assign`,
    method: 'put',
    data
  })
}
