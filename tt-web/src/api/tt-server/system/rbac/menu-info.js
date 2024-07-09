import request from '@/utils/request'

const PATH = '/api/rbac/menu'

/**
 * 获取系统菜单
 * @returns {*}
 */
export function getSystemMenuTree() {
  return request({
    url: `${PATH}/tree`,
    method: 'get'
  })
}

/**
 * 获取该角色下已授予的系统菜单
 * @param sysRoleId
 * @returns {*}
 */
export function getCheckedSystemMenus(sysRoleId) {
  return request({
    url: `${PATH}/assigned-list/${sysRoleId}`,
    method: 'get'
  })
}

/**
 * 给该角色授予菜单
 * @param role
 * @returns {*}
 */
export function assignSystemMenus(role) {
  return request({
    url: `${PATH}/assign`,
    method: 'put',
    data: role
  })
}

/**
 * 获取系统菜单翻页
 * @param data
 * @returns {*}
 */
export function getSystemMenuPage(data) {
  return request({
    url: `${PATH}/page`,
    method: 'post',
    data
  })
}

/**
 * 获取系统菜单集合
 * @param data
 * @returns {*}
 */
export function getSystemMenuList(data) {
  return request({
    url: `${PATH}/list`,
    method: 'get',
    params: data
  })
}

/**
 * 按Id获取系统菜单
 * @param sysMenuId
 * @returns {*}
 */
export function getSystemMenuById(sysMenuId) {
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
export function addSystemMenu(data) {
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
export function updateSystemMenu(data) {
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
export function deleteSystemMenu(data) {
  return request({
    url: `${PATH}/delete`,
    method: 'delete',
    data
  })
}
