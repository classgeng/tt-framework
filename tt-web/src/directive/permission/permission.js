import store from '@/store'

function checkPermission(el, binding) {
  // const { value } = binding
  // const roles = store.getters && store.getters.roles
  //
  // if (value && value instanceof Array) {
  //   if (value.length > 0) {
  //     const permissionRoles = value
  //
  //     const hasPermission = roles.some(role => {
  //       return permissionRoles.includes(role)
  //     })
  //
  //     if (!hasPermission) {
  //       el.parentNode && el.parentNode.removeChild(el)
  //     }
  //   }
  // } else {
  //   throw new Error(`need roles! Like v-permission="['admin','editor']"`)
  // }
  const { value } = binding
  const permits = store.getters && store.getters.permits
  if (!!value && value instanceof Array && value.length > 0) {
    const requirePermits = value
    console.log(`需要的权限值：${requirePermits}`)

    /* 校验是否存在 */
    const hasPermit = permits.some(permit => requirePermits.includes(permit))
    /* 如果不存在则直接删除标记元素 */
    if (!hasPermit) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error(`需要提供校验的权限! 如 v-permission="['xxx@xxx','xxx@xxx']"`)
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
