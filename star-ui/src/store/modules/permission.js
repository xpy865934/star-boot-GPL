import { asyncRoutes, constantRoutes } from '@/router'
import store from '../../store'

/**
 * 通过meta.access判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(permissions, route) {
  if (route.access && permissions) {
    return permissions.some(permission => route.access.includes(permission))
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles, permissions) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(permissions, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles, permissions)
      }
      res.push(tmp)
    }
  })

  return res
}

const permission = {
  namespaced: true,
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    }
  },
  actions: {
    generateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { roles, permissions } = data
        let accessedRoutes = []
        // 管理员id为1 跳过所有权限匹配
        if (store.getters.userCode === 'admin') {
          accessedRoutes = asyncRoutes
        } else {
          accessedRoutes = filterAsyncRoutes(asyncRoutes, roles, permissions)
        }
        commit('SET_ROUTES', accessedRoutes)
        debugger
        resolve(accessedRoutes)
      })
    }
  }
}

export default permission
