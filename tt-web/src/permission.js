import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { removeWatermark, setWaterMark } from '@/utils/watermark'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

/**
 * no redirect whitelist
 * 白名单，没有权限控制的路由地址
 * @type {string[]}
 */
const whiteList = ['/login', '/auth-redirect']

/**
 * 在路由跳转之前，勾子函数的控制
 */
router.beforeEach(async(to, from, next) => {
  /* 启动进度条 start progress bar */
  NProgress.start()

  /* 设置系统标签文本信息 set page title  */
  document.title = getPageTitle(to.meta.title)

  /* 从Cookie里面提取令牌 determine whether the user has logged in */
  const hasToken = getToken()

  /* 令牌存在 */
  if (hasToken) {
    /* 访问登录页时直接放行 */
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      /**
       * 判断当前用户的角色集合是否存在
       * determine whether the user has obtained his permission roles through getInfo
       * @type {any}
       */
      // const hasRoles = store.getters.roles && store.getters.roles.length > 0
      /* 存在则放行路由 */

      // 确定用户是否已通过getInfo获得其用户
      const hasName = !!store.getters.name

      if (hasName) {
        next()
      } else {
        /* 没有则重新获取用户信息 */
        try {
          /**
           * // get user info
           * // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
           * 根据令牌重新获取用户信息，只要角色集合
           */
          const { menus, userAccount, userName } = await store.dispatch('user/getInfo')
          setWaterMark(userAccount, userName)
          /**
           * generate accessible routes map based on roles
           * 创建可访问的路由表
           * @type {any}
           */
          // const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          const accessRoutes = await store.dispatch('menu/getAccessMenus', menus)

          /**
           * dynamically add accessible routes
           * 动态添加路由
           */
          router.addRoutes(accessRoutes)

          /**
           * 如果用户状态持续，保持路由不会丢掉历史记录
           * // hack method to ensure that addRoutes is complete
           * // set the replace: true, so the navigation will not leave a history record
           */
          next({ ...to, replace: true })
        } catch (error) {
          /**
           * 如果用户信息无法获取，重定向，要求用户重新登录
           * remove token and go to login page to re-login
           */
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* 如果没有令牌，检查是否是白名单的路由 has no token*/
    removeWatermark()
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      /* 没有权限，也不是白名单，重定向到登录页面 */
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

/* 每次跳转之后的回调勾子 */
router.afterEach(() => {
  /* finish progress bar 进度条加载完成 */
  NProgress.done()
})
