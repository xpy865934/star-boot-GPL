import Cookies from 'js-cookie'
import { getFirstDictAll, getUserList, getFLowNodes, downloadFile, getCompanyList } from '@/api/app'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus')
      ? !!+Cookies.get('sidebarStatus')
      : true,
    withoutAnimation: false
  },
  device: 'desktop',
  firstDict: {},
  userList: [],
  companyList: []
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_FIRST_DICT: (state, firstDict) => {
    for (let i = 0; i < firstDict.length; i++) {
      if (state.firstDict[firstDict[i].dictCode]) {
        state.firstDict[firstDict[i].dictCode].push(firstDict[i])
      } else {
        state.firstDict[firstDict[i].dictCode] = []
        state.firstDict[firstDict[i].dictCode].push(firstDict[i])
      }
    }
  },
  SET_USER_LIST: (state, userList) => {
    state.userList = userList
  },
  SET_COMPANY_LIST: (state, companyList) => {
    state.companyList = companyList
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  /**
   * 获取所有的一级代码信息
   * @param {*} param0
   * @param {*} params
   */
  getFirstDictAll({ commit }, params) {
    return new Promise((resolve, reject) => {
      getFirstDictAll(params)
        .then(response => {
          const data = response.data
          commit('SET_FIRST_DICT', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  /**
   * 获取所有的用户信息
   * @param {*} param0
   * @param {*} params
   */
  getUserList({ commit }, params) {
    return new Promise((resolve, reject) => {
      getUserList(params)
        .then(response => {
          const data = response.data
          commit('SET_USER_LIST', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  /**
   * 获取所有的流程节点
   * @param {*} param0
   * @param {*} params
   */
  getFLowNodes({ commit }, params) {
    return new Promise((resolve, reject) => {
      getFLowNodes(params)
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
   * 下载文件
   * @param {*} param0
   * @param {*} params
   */
  downloadFile({ commit }, params) {
    return new Promise((resolve, reject) => {
      downloadFile(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  // 获取公司列表
  getCompanyList({ commit }, params) {
    return new Promise((resolve, reject) => {
      getCompanyList(params)
        .then(response => {
          const data = response.data
          commit('SET_COMPANY_LIST', data)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  initStore(params) {
    this.dispatch('app/getFirstDictAll', {})
    this.dispatch('app/getUserList', {})
    this.dispatch('app/getCompanyList', {})
    this.dispatch('customerHouses/queryCustomerHousesInfo', {})
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
