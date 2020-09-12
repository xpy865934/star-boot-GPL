import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

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
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
    affix: true                  if true, the tag will affix in the tags-view
    noCache: true                if true, the page will no be cached(default is false)
  }
 */

/**
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
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  }
]

export const asyncRoutes = [
  // 系统管理
  {
    path: '/system_management',
    component: Layout,
    access: ['systemManagement'],
    meta: {
      title: 'systemManagement',
      icon: 'el-icon-s-tools'
    },
    children: [
      // 菜单管理
      {
        path: '/resources',
        component: () =>
          import('@/views/system_management/resources/index'),
        name: 'resources',
        //  access: ["resources"],
        meta: {
          title: 'resources'
          // if do not set roles, means: this page does not require permission
        }
      },
      // 角色数据
      {
        path: '/roles',
        component: () => import('@/views/system_management/roles/index'),
        name: 'roles',
        access: ['roles'],
        meta: {
          title: 'roles'
          // if do not set roles, means: this page does not require permission
        }
      },
      // 用户管理
      {
        path: '/users',
        component: () => import('@/views/system_management/users/index'),
        name: 'users',
        access: ['users'],
        meta: {
          title: 'users'
          // if do not set roles, means: this page does not require permission
        }
      },
      // 汇总统计
      {
        path: '/hztj',
        component: () => import('@/views/system_management/hztj/index'),
        name: 'hztj',
        access: ['hztj'],
        meta: {
          title: 'hztj'
          // if do not set roles, means: this page does not require permission
        }
      },
      // 自定义报表设计
      {
        path: '/ureport_designer',
        component: () =>
          import('@/views/system_management/ureport/ureport_designer'),
        name: 'ureportDesigner',
        access: ['ureportDesigner'],
        meta: {
          title: 'ureportDesigner'
          // if do not set roles, means: this page does not require permission
        }
      }
    ]
  },

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
