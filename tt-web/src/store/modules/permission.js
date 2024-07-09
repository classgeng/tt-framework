import { asyncRoutes, constantRoutes } from '@/router'

/**
 * 使用meta对象的role属性 决定当前用户是否具备此权限
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  /* 存在meta对象 和meta对象中的roles属性, 如果没有直接放行 */
  if (route.meta && route.meta.roles) {
    /* 取出角色集合判断是否包含任意一个 */
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 通过递归过滤异步路由表 逐层判断权限
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      /* 超级管理员直接赋予全部路由 */
      if (roles.includes('admin')) {
        accessedRoutes = asyncRoutes || []
      } else {
        /* 按角色集合过滤路由 */
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
