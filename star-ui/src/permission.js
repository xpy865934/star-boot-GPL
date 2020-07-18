import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// permission judge function
function hasPermission(permissions, permission) {
  if (!permission) return true
  return permissions.some(perm => permission.indexOf(perm) >= 0)
}

const whiteList = ['/login', '/register'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.userCode) {
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        if (hasPermission(store.getters.permissions, to.meta.access)) {
          next()
        } else {
          next({ path: '/401', replace: true, query: { noGoBack: true }})
        }
      } else {
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('user/getUserInfo')
          .then(data => {
            // 拉取user_info
            const roles = data.roles // note: roles must be a object array! such as: [{id: '1', name: 'editor'}, {id: '2', name: 'developer'}]
            const permissions = data.permissions // note: roles must be a object array! such as: [{id: '1', name: 'editor'}, {id: '2', name: 'developer'}]
            store.dispatch('permission/generateRoutes', { roles, permissions })
              .then(accessRoutes => {
                // 获取系统中需要初始化的store
                store.dispatch('app/initStore', { roles, permissions })

                // 根据roles权限生成可访问的路由表
                debugger
                router.addRoutes(accessRoutes) // 动态添加可访问路由表
                next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
              })
          })
          .catch(err => {
            store.dispatch('user/loginOut').then(() => {
              console.log(err)
              Message.error('获取用户权限信息失败')
              next(`/login?redirect=${to.path}`)
              NProgress.done()
            })
          })
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
