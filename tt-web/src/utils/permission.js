import store from '@/store'

/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission(value) {
  if (value && value instanceof Array && value.length > 0) {
    const roles = store.getters && store.getters.roles
    const permissionRoles = value

    const hasPermission = roles.some(role => {
      return permissionRoles.includes(role)
    })
    return hasPermission
  } else {
    console.error(`need roles! Like v-permission="['admin','editor']"`)
    return false
  }
}

/**
 * 校验是否允许访问
 * @param {Array} value
 * @returns {boolean}
 */
export function checkPermit(value) {
  if (value && value instanceof Array && value.length > 0) {
    const permits = store.getters && store.getters.permits
    const requirePermits = value
    console.log(`要求权限值: ${requirePermits}`)
    const hasPermit = permits.some(permit => requirePermits.includes(permit))
    console.log(`是否存在: ${hasPermit}`)
    return hasPermit
  } else {
    console.error(`need roles! Like v-permission="['admin','editor']"`)
    return false
  }
}
