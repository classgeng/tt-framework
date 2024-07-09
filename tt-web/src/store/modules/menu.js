import { asyncRoutes, constantRoutes, lastRoute } from '@/router/index'

const state = {
  routes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = routes
  }
}

/**
 * 递归路由集合，和用户的菜单进行比较，符合则打开，反之关闭
 * @param routes
 * @param srvMenus
 */
function generateMenus(routes, srvMenus) {
  for (let i = 0; i < routes.length; i++) {
    const routeItem = routes[i]
    let showItem = false
    for (let j = 0; j < srvMenus.length; j++) {
      const srvItem = srvMenus[j]

      // 前后端数据通过 serPath 属性来匹配
      if (routeItem.name !== undefined && routeItem.name === srvItem['menuRoute'] && srvItem['isShow'] === true) {
        showItem = true
        routes[i]['hidden'] = false
        break
      }
    }
    if (showItem === false) {
      routes[i]['hidden'] = true
    }

    if (routeItem['children'] !== undefined && routeItem['children'].length > 0) {
      generateMenus(routes[i]['children'], srvMenus)
    }
  }
}

const actions = {
  getAccessMenus({ commit }, menus) {
    return new Promise(resolve => {
      const pushRouter = asyncRoutes
      generateMenus(pushRouter, menus)
      const routeArr = []
      console.log(constantRoutes)
      routeArr.push(...constantRoutes)
      routeArr.push(...pushRouter)
      routeArr.push(...lastRoute)
      commit('SET_ROUTES', routeArr)
      console.log(routeArr)

      /* 返回全部匹配的路由 */
      resolve(routeArr)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
