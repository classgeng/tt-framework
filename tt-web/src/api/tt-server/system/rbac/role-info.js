import request from '@/utils/request'

const PATH = '/api/rbac/role'

/**
 * 获取系统角色翻页
 * @param data
 * @returns {*}
 */
export function getSystemRolePage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取系统角色
 * @param sysRoleId
 * @returns {*}
 */
export function getSystemRoleById(sysRoleId) {
  return request({
    url: `${PATH}/${sysRoleId}`,
    method: 'get'
  })
}

/**
 * 新增系统角色
 * @param data
 * @returns {*}
 */
export function addSystemRole(data) {
  return request({
    url: `${PATH}/insert`,
    method: 'put',
    data
  })
}

/**
 * 更新系统角色
 * @param data
 * @returns {*}
 */
export function updateSystemRole(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除系统角色
 * @param data
 * @returns {*}
 */
export function deleteSystemRole(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}

/**
 * 赋予该用户角色集合
 * @param data
 * @returns {*}
 */
export function assignSystemRoleList(data) {
  return request({
    url: `${PATH}/assign`,
    method: 'put',
    data
  })
}

/**
 * 获取所有角色集合
 * @returns {*}
 */
export function getSystemRoleList() {
  return request({
    url: `${PATH}/list`,
    method: 'get'
  })
}

/**
 * 获取所有角色集合(该用户下的)
 * @param userId 用户id
 * @returns {*}
 */
export function getAssignedSystemRoleList(userId) {
  return request({
    url: `${PATH}/assigned-list/${userId}`,
    method: 'get'
  })
}
