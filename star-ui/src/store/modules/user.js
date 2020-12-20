import { login, register, getUserInfo, logout, queryPager, deleteById, changePassword } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { getCurrentBrowser, getOs } from '../../utils/common'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    userId: '',
    userCode: '',
    userName: '',
    userSex: '',
    departmentId: '',
    departmentName: '',
    companyPosition: '',
    email: '',
    token: getToken(),
    tel: '',
    departmentCode: '',
    companyId: '',
    companyName: '',
    comapnyCode: '',
    roles: [],
    permissions: [],
    uploadToken: {
      token: getToken(),
      client: 'client:' + getCurrentBrowser(),
      os: 'os:' + getOs()
    },
    msgUnReadCount: 0
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER: (state, user) => {
    if (user.userId) {
      state.userId = user.userId
    }
    if (user.userCode) {
      state.userCode = user.userCode
    }
    if (user.userName) {
      state.userName = user.userName
    }
    if (user.userSex) {
      state.userSex = user.userSex
    }
    if (user.departmentId) {
      state.departmentId = user.departmentId
    }
    if (user.departmentName) {
      state.departmentName = user.departmentName
    }
    if (user.companyPosition) {
      state.companyPosition = user.companyPosition
    }
    if (user.email) {
      state.email = user.email
    }
    if (user.tel) {
      state.tel = user.tel
    }
    if (user.departmentCode) {
      state.departmentCode = user.departmentCode
    }
    if (user.companyId) {
      state.companyId = user.companyId
    }
    if (user.companyName) {
      state.companyName = user.companyName
    }
    if (user.comapnyCode) {
      state.comapnyCode = user.comapnyCode
    }
    if (user.roles) {
      state.roles = user.roles
    }
    if (user.permissions) {
      state.permissions = user.permissions
    }
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(userInfo)
        .then(response => {
          const { data } = response
          commit('SET_TOKEN', data.token)
          setToken(data.token)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  register({ commit }, params) {
    return new Promise((resolve, reject) => {
      register(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  /**
   * 根据token获取用户信息
   * @param {*} param0
   * @param {*} token
   */
  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getUserInfo()
        .then(response => {
          const data = response.data
          commit('SET_USER', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  /**
   * 登出
   * @param {*} param0
   */
  logout({ commit }) {
    return new Promise((resolve, reject) => {
      logout()
        .then(response => {
          removeToken()
          commit('RESET_STATE')
          resetRouter()
          resolve()
        })
        .catch(error => {
          removeToken()
          commit('RESET_STATE')
          resetRouter()
          reject(error)
        })
    })
  },

  /**
   * 分页查询
   * @param {*} params
   */
  queryPager({ commit }, params) {
    return new Promise((resolve, reject) => {
      queryPager(params)
        .then(response => {
          const data = response.data
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  /**
   * 分页查询
   * @param {*} params
   */
  deleteById({ commit }, params) {
    return new Promise((resolve, reject) => {
      deleteById(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  /**
   * 修改密码
   * @param {*} param0
   * @param {*} params
   */
  changePassword({ commit }, params) {
    return new Promise((resolve, reject) => {
      changePassword(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

