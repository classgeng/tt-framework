import request from '@/utils/request'

const PATH = '/api/rbac/permit'

/**
 * 获取系统菜单翻页
 * @param data
 * @returns {*}
 */
export function getSystemPermitPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 按Id获取系统菜单
 * @param sysMenuId
 * @returns {*}
 */
export function getSystemPermitById(sysMenuId) {
  return request({
    url: `${PATH}/${sysMenuId}`,
    method: 'get'
  })
}

/**
 * 新增系统菜单
 * @param data
 * @returns {*}
 */
export function addSystemPermit(data) {
  return request({
    url: `${PATH}/insert`,
    method: 'put',
    data
  })
}

/**
 * 更新系统菜单
 * @param data
 * @returns {*}
 */
export function updateSystemPermit(data) {
  return request({
    url: `${PATH}/update`,
    method: 'post',
    data
  })
}

/**
 * 删除系统菜单
 * @param data
 * @returns {*}
 */
export function deleteSystemPermit(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}

/**
 * 获取系统权限集合
 * @returns {*}
 */
export function getSystemPermitList() {
  return request({
    url: `${PATH}/list`,
    method: 'get'
  })
}

/**
 * 获取系统权限集合(这个菜单下已分配的)
 * @param menuId
 * @returns {*}
 */
export function getAllocatedSystemPermitList(menuId) {
  return request({
    url: `${PATH}/allocated-list/${menuId}`,
    method: 'get'
  })
}

/**
 * 分配权限
 * @param data
 * @returns {*}
 */
export function allocateSystemPermitList(data) {
  return request({
    url: `${PATH}/allocate`,
    method: 'put',
    data
  })
}

/**
 * 获取授予该角色的权限集合
 * @param sysRoleId
 * @returns {*}
 */
export function getAssignableSystemPermitList(sysRoleId) {
  return request({
    url: `${PATH}/assignable-list/${sysRoleId}`,
    method: 'get'
  })
}

/**
 * 获取授予该角色的权限集合
 * @param sysRoleId
 * @returns {*}
 */
export function getAssignedSystemPermitList(sysRoleId) {
  return request({
    url: `${PATH}/assigned-list/${sysRoleId}`,
    method: 'get'
  })
}

/**
 * 授予该角色权限集合
 * @param role
 * @returns {*}
 */
export function assignSystemPermits(role) {
  return request({
    url: `${PATH}/assign`,
    method: 'put',
    data: role
  })
}

