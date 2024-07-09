import request from '@/utils/request'

const PATH = '/api/rbac/user'

/**
 * 获取系统用户翻页
 * @param data
 * @returns {*}
 */
export function getSystemUserPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取系统用户
 * @param sysUserId
 * @returns {*}
 */
export function getSystemUserById(sysUserId) {
  return request({
    url: `${PATH}/${sysUserId}`,
    method: 'get'
  })
}

/**
 * 新增系统用户
 * @param data
 * @returns {*}
 */
export function addSystemUser(data) {
  return request({
    url: `${PATH}/insert`,
    method: 'put',
    data
  })
}

/**
 * 更新系统用户
 * @param data
 * @returns {*}
 */
export function updateSystemUser(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除系统用户
 * @param data
 * @returns {*}
 */
export function deleteSystemUser(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}
