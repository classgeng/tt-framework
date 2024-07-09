import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
import componentsRouter from './modules/components'
import chartsRouter from './modules/charts'
import tableRouter from './modules/table'
import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * 常量路由，没有权限要求，所有角色可以访问
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        component: () => import('@/views/tt-server/home/index'),
        name: 'Home',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/system-monitor',
    component: Layout,
    redirect: 'noRedirect',
    name: '/system-monitor',
    alwaysShow: true,
    meta: { title: '系统监控', icon: 'lock' },
    children: [
      {
        path: 'operate-system',
        component: () => import('@/views/tt-server/system-monitor/operate-system/index'),
        name: '/system-monitor/operate-system',
        alwaysShow: true,
        meta: { title: '操作系统', icon: 'lock' },
        children: [
          {
            path: 'os-info',
            component: () => import('@/views/tt-server/system-monitor/operate-system/os-info/index'),
            name: '/system-monitor/operate-system/os-info',
            meta: { title: '系统信息', icon: 'edit' }
          },
          {
            path: 'file-system',
            component: () => import('@/views/tt-server/system-monitor/operate-system/file-system/index'),
            name: '/system-monitor/operate-system/file-system',
            meta: { title: '文件系统', icon: 'edit' }
          },
          {
            path: 'system-services',
            component: () => import('@/views/tt-server/system-monitor/operate-system/system-services/index'),
            name: '/system-monitor/operate-system/system-services',
            meta: { title: '系统服务', icon: 'edit' }
          },
          {
            path: 'processes-info',
            component: () => import('@/views/tt-server/system-monitor/operate-system/processes-info/index'),
            name: '/system-monitor/operate-system/processes-info',
            meta: { title: '进程信息', icon: 'edit' }
          },
          {
            path: 'thread-list',
            component: () => import('@/views/tt-server/system-monitor/operate-system/thread-list/index'),
            name: '/system-monitor/operate-system/thread-list',
            meta: { title: '线程列表', icon: 'edit' }
          }
        ]
      },
      {
        path: 'network-management',
        component: () => import('@/views/tt-server/system-monitor/network-management/index'),
        name: '/system-monitor/network-management',
        alwaysShow: true,
        meta: { title: '网络管理', icon: 'lock' },
        children: [
          {
            path: 'ip-statistics',
            component: () => import('@/views/tt-server/system-monitor/network-management/ip-statistics/index'),
            name: '/system-monitor/network-management/ip-statistics',
            meta: { title: 'IP统计信息', icon: 'edit' }
          },
          {
            path: 'network-interfaces',
            component: () => import('@/views/tt-server/system-monitor/network-management/network-interfaces/index'),
            name: '/system-monitor/network-management/network-interfaces',
            meta: { title: '网络接口', icon: 'edit' }
          },
          {
            path: 'network-params',
            component: () => import('@/views/tt-server/system-monitor/network-management/network-params/index'),
            name: '/system-monitor/network-management/network-params',
            meta: { title: '网络参数', icon: 'edit' }
          },
          {
            path: 'druid-monitor',
            component: () => import('@/views/tt-server/system-monitor/network-management/druid-monitor/index'),
            name: '/system-monitor/network-management/druid-monitor',
            meta: { title: 'SQL监控', icon: 'edit' }
          },
          {
            path: 'redis-monitor',
            component: () => import('@/views/tt-server/system-monitor/network-management/redis-monitor/index'),
            name: '/system-monitor/network-management/redis-monitor',
            meta: { title: 'Redis监控', icon: 'edit' }
          }
        ]
      },
      {
        path: 'hardware-management',
        component: () => import('@/views/tt-server/system-monitor/hardware-management/index'),
        name: '/system-monitor/hardware-management',
        alwaysShow: true,
        meta: { title: '硬件管理', icon: 'lock' },
        children: [
          {
            path: 'cpu-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/cpu-info/index'),
            name: '/system-monitor/hardware-management/cpu-info',
            meta: { title: '处理器信息', icon: 'edit' }
          },
          {
            path: 'memory-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/memory-info/index'),
            name: '/system-monitor/hardware-management/memory-info',
            meta: { title: '内存信息', icon: 'edit' }
          },
          {
            path: 'graphic-card-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/graphic-card-info/index'),
            name: '/system-monitor/hardware-management/graphic-card-info',
            meta: { title: '显卡信息', icon: 'edit' }
          },
          {
            path: 'sound-card-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/sound-card-info/index'),
            name: '/system-monitor/hardware-management/sound-card-info',
            meta: { title: '声卡信息', icon: 'edit' }
          },
          {
            path: 'disk-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/disk-info/index'),
            name: '/system-monitor/hardware-management/disk-info',
            meta: { title: '磁盘信息', icon: 'edit' }
          },
          {
            path: 'sensor-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/sensor-info/index'),
            name: '/system-monitor/hardware-management/sensor-info',
            meta: { title: '传感器信息', icon: 'edit' }
          },
          {
            path: 'power-source-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/power-source-info/index'),
            name: '/system-monitor/hardware-management/power-source-info',
            meta: { title: '电源信息', icon: 'edit' }
          },
          {
            path: 'usb-device',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/usb-device/index'),
            name: '/system-monitor/hardware-management/usb-device',
            meta: { title: 'USB接口设备', icon: 'edit' }
          },
          {
            path: 'display-info',
            component: () => import('@/views/tt-server/system-monitor/hardware-management/display-info/index'),
            name: '/system-monitor/hardware-management/display-info',
            meta: { title: '显示设备', icon: 'edit' }
          }
        ]
      }
    ]
  },
  {
    path: '/research-lab',
    component: Layout,
    redirect: 'noRedirect',
    name: '/research-lab',
    alwaysShow: true,
    meta: { title: '研发平台', icon: 'lock' },
    children: [
      {
        path: 'tool-lib',
        component: () => import('@/views/tt-server/research-lab/tool-lib/index'),
        name: '/research-lab/tool-lib',
        alwaysShow: true,
        meta: { title: '工具库', icon: 'lock' },
        children: [
          {
            path: 'mysql-visualize',
            component: () => import('@/views/tt-server/research-lab/tool-lib/mysql-visualize/index'),
            name: '/research-lab/tool-lib/mysql-visualize',
            meta: { title: 'MySQL可视化', icon: 'edit' }
          },
          {
            path: 'random-number',
            component: () => import('@/views/tt-server/research-lab/tool-lib/random-number/index'),
            name: '/research-lab/tool-lib/random-number',
            meta: { title: '随机数生成', icon: 'edit' }
          }
        ]
      },
      {
        path: 'front-end',
        component: () => import('@/views/tt-server/research-lab/front-end/index'),
        name: '/research-lab/front-end',
        alwaysShow: true,
        meta: { title: '前端', icon: 'lock' },
        children: [
          // {
          //   path: 'index-db',
          //   component: () => import('@/views/tt-server/research-lab/front-end/index-db/index'),
          //   name: '/research-lab/front-end/index-db',
          //   meta: { title: 'IndexDB', icon: 'edit' }
          // }
        ]
      }
    ]
  }
]

export const constantRoutes_backup = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'Dashboard', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/documentation',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/documentation/index'),
        name: 'Documentation',
        meta: { title: 'Documentation', icon: 'documentation', affix: true }
      }
    ]
  },
  {
    path: '/guide',
    component: Layout,
    redirect: '/guide/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/guide/index'),
        name: 'Guide',
        meta: { title: 'Guide', icon: 'guide', noCache: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'Profile', icon: 'user', noCache: true }
      }
    ]
  },
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/page',
    alwaysShow: true, // will always show the root menu
    name: 'Permission',
    meta: {
      title: 'Permission',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/permission/page'),
        name: 'PagePermission',
        meta: {
          title: 'Page Permission',
          roles: ['admin'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'directive',
        component: () => import('@/views/permission/directive'),
        name: 'DirectivePermission',
        meta: {
          title: 'Directive Permission'
          // if do not set roles, means: this page does not require permission
        }
      },
      {
        path: 'role',
        component: () => import('@/views/permission/role'),
        name: 'RolePermission',
        meta: {
          title: 'Role Permission',
          roles: ['admin']
        }
      }
    ]
  },

  {
    path: '/icon',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/icons/index'),
        name: 'Icons',
        meta: { title: 'Icons', icon: 'icon', noCache: true }
      }
    ]
  },

  /** when your routing map is too long, you can split it into small modules **/
  /* 可以分割放置过多的路由 */
  componentsRouter,
  chartsRouter,
  nestedRouter,
  tableRouter,

  {
    path: '/example',
    component: Layout,
    redirect: '/example/list',
    name: 'Example',
    meta: {
      title: 'Example',
      icon: 'el-icon-s-help'
    },
    children: [
      {
        path: 'create',
        component: () => import('@/views/example/create'),
        name: 'CreateArticle',
        meta: { title: 'Create Article', icon: 'edit' }
      },
      {
        path: 'edit/:id(\\d+)',
        component: () => import('@/views/example/edit'),
        name: 'EditArticle',
        meta: { title: 'Edit Article', noCache: true, activeMenu: '/example/list' },
        hidden: true
      },
      {
        path: 'list',
        component: () => import('@/views/example/list'),
        name: 'ArticleList',
        meta: { title: 'Article List', icon: 'list' }
      }
    ]
  },

  {
    path: '/tab',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/tab/index'),
        name: 'Tab',
        meta: { title: 'Tab', icon: 'tab' }
      }
    ]
  },

  {
    path: '/error',
    component: Layout,
    redirect: 'noRedirect',
    name: 'ErrorPages',
    meta: {
      title: 'Error Pages',
      icon: '404'
    },
    children: [
      {
        path: '401',
        component: () => import('@/views/error-page/401'),
        name: 'Page401',
        meta: { title: '401', noCache: true }
      },
      {
        path: '404',
        component: () => import('@/views/error-page/404'),
        name: 'Page404',
        meta: { title: '404', noCache: true }
      }
    ]
  },

  {
    path: '/error-log',
    component: Layout,
    children: [
      {
        path: 'log',
        component: () => import('@/views/error-log/index'),
        name: 'ErrorLog',
        meta: { title: 'Error Log', icon: 'bug' }
      }
    ]
  },

  {
    path: '/excel',
    component: Layout,
    redirect: '/excel/export-excel',
    name: 'Excel',
    meta: {
      title: 'Excel',
      icon: 'excel'
    },
    children: [
      {
        path: 'export-excel',
        component: () => import('@/views/excel/export-excel'),
        name: 'ExportExcel',
        meta: { title: 'Export Excel' }
      },
      {
        path: 'export-selected-excel',
        component: () => import('@/views/excel/select-excel'),
        name: 'SelectExcel',
        meta: { title: 'Export Selected' }
      },
      {
        path: 'export-merge-header',
        component: () => import('@/views/excel/merge-header'),
        name: 'MergeHeader',
        meta: { title: 'Merge Header' }
      },
      {
        path: 'upload-excel',
        component: () => import('@/views/excel/upload-excel'),
        name: 'UploadExcel',
        meta: { title: 'Upload Excel' }
      }
    ]
  },

  {
    path: '/zip',
    component: Layout,
    redirect: '/zip/download',
    alwaysShow: true,
    name: 'Zip',
    meta: { title: 'Zip', icon: 'zip' },
    children: [
      {
        path: 'download',
        component: () => import('@/views/zip/index'),
        name: 'ExportZip',
        meta: { title: 'Export Zip' }
      }
    ]
  },

  {
    path: '/pdf',
    component: Layout,
    redirect: '/pdf/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/pdf/index'),
        name: 'PDF',
        meta: { title: 'PDF', icon: 'pdf' }
      }
    ]
  },
  {
    path: '/pdf/download',
    component: () => import('@/views/pdf/download'),
    hidden: true
  },

  {
    path: '/theme',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/theme/index'),
        name: 'Theme',
        meta: { title: 'Theme', icon: 'theme' }
      }
    ]
  },

  {
    path: '/clipboard',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/clipboard/index'),
        name: 'ClipboardDemo',
        meta: { title: 'Clipboard', icon: 'clipboard' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://github.com/PanJiaChen/vue-element-admin',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  }
]

/**
 * 异步路由，根据用户的角色集合动态加载
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/system',
    component: Layout,
    redirect: 'noRedirect',
    name: '/system',
    alwaysShow: true,
    meta: { title: '系统管理', icon: 'lock' },
    children: [
      {
        path: 'rbac',
        component: () => import('@/views/tt-server/system/rbac/index'),
        name: '/system/rbac',
        meta: { title: '权限维护', icon: 'lock' },
        children: [
          {
            path: 'user-info',
            component: () => import('@/views/tt-server/system/rbac/user-info/index'),
            name: '/system/rbac/user-info',
            meta: { title: '用户管理', icon: 'edit' }
          },
          {
            path: 'role-info',
            component: () => import('@/views/tt-server/system/rbac/role-info/index'),
            name: '/system/rbac/role-info',
            meta: { title: '角色管理', icon: 'edit' }
          },
          {
            path: 'menu-info',
            component: () => import('@/views/tt-server/system/rbac/menu-info/index'),
            name: '/system/rbac/menu-info',
            meta: { title: '菜单管理', icon: 'edit' }
          },
          {
            path: 'privilege-info',
            component: () => import('@/views/tt-server/system/rbac/privilege-info/index'),
            name: '/system/rbac/privilege-info',
            meta: { title: '权限管理', icon: 'edit' }
          }
        ]
      },
      {
        path: 'common',
        component: () => import('@/views/tt-server/system/common/index'),
        name: '/system/common',
        alwaysShow: true,
        meta: { title: '通用管理', icon: 'lock' },
        children: [
          {
            path: 'dict-info',
            component: () => import('@/views/tt-server/system/common/dict-info/index'),
            name: '/system/common/dict-info',
            meta: { title: '字典管理', icon: 'edit' }
          },
          {
            path: 'area-info',
            component: () => import('@/views/tt-server/system/common/area-info/index'),
            name: '/system/common/area-info',
            meta: { title: '区域管理', icon: 'edit' }
          },
          {
            path: 'system-param',
            component: () => import('@/views/tt-server/system/common/system-param/index'),
            name: '/system/common/system-param',
            meta: { title: '系统参数', icon: 'edit' }
          },
          {
            path: 'system-log',
            component: () => import('@/views/tt-server/system/common/system-log/index'),
            name: '/system/common/system-log',
            meta: { title: '系统日志', icon: 'edit' }
          },
          {
            path: 'system-api',
            component: () => import('@/views/tt-server/system/common/system-api/index'),
            name: '/system/common/system-api',
            meta: { title: '系统接口', icon: 'edit' }
          }
        ]
      }
    ]
  },
  {
    path: '/experiment',
    component: Layout,
    redirect: 'noRedirect',
    name: '/experiment',
    alwaysShow: true,
    meta: { title: '实验功能', icon: 'lock' },
    children: [
      {
        path: 'data-auth-test',
        component: () => import('@/views/tt-server/experiment/data-auth-test/index'),
        name: '/experiment/data-auth-test',
        meta: { title: '数据授权测试', icon: 'edit' }
      }
    ]
  }
]

export const lastRoute = [
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
