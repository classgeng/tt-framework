import { login, logout, getInfo } from '@API/tt-server/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  permissions: [],
  menus: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_MENUS: (state, menus) => {
    state.menus = menus
  },
  SET_PERMISSION: (state, permissions) => {
    state.permissions = permissions
  }
}

const actions = {
  /**
   * user login
   * 用户登陆
   * @param commit
   * @param userInfo
   * @returns {Promise<unknown>}
   */
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      const param = { userUserName: username.trim(), userPassword: password }
      login(param).then(response => {
        const { data } = response
        /* 保存Token VueX一份， Cookie一份 */
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  /**
   * get user info
   * 获取用户信息
   * @param commit
   * @param state
   * @returns {Promise<unknown>}
   */
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          /* reject('Verification failed, please Login again.') */
          reject('认证失败，请重新登录')
        }

        const { menuTree, roles, userName, avatar, introduction, permitList } = data

        // roles must be a non-empty array
        // if (!roles || roles.length <= 0) {
        //   reject('getInfo: roles must be a non-null array!')
        // }

        commit('SET_ROLES', roles)
        commit('SET_MENUS', menuTree)
        commit('SET_NAME', userName)
        commit('SET_AVATAR', avatar)
        commit('SET_PERMISSION', permitList)
        commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  /**
   * user logout
   * 用户退出
   * @param commit
   * @param state
   * @param dispatch
   * @returns {Promise<unknown>}
   */
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_MENUS', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  /**
   * remove token
   * 清除token
   * @param commit
   * @returns {Promise<unknown>}
   */
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_MENUS', [])
      commit('SET_NAME', '')
      commit('SET_AVATAR', '')
      commit('SET_INTRODUCTION', '')
      commit('SET_PERMISSION', [])
      removeToken()
      resolve()
    })
  },

  /**
   * dynamically modify permissions
   * 动态更改权限集合
   * @param commit
   * @param dispatch
   * @param role
   * @returns {Promise<void>}
   */
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    /* 只获取角色集合 */
    const { roles, menus } = await dispatch('getInfo')
    console.log(menus)

    /* 重置路由 */
    resetRouter()

    /**
     * generate accessible routes map based on roles
     * 根据角色 创建可访问的路由表
     * @type {*}
     */
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    /**
     *  dynamically add accessible routes
     *  动态添加路由表
     */
    router.addRoutes(accessRoutes)

    /**
     * 清除所有视图？ （浏览过的和已缓存的视图）
     * reset visited views and cached views
     */
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
