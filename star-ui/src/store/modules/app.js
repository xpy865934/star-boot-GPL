import Cookies from 'js-cookie'
import { getFirstDictAll, getUserList } from '@/api/app'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus')
      ? !!+Cookies.get('sidebarStatus')
      : true,
    withoutAnimation: false
  },
  device: 'desktop',
  firstDict: {},
  userList: []
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
  initStore(params) {
    this.dispatch('app/getFirstDictAll', {})
    this.dispatch('app/getUserList', {})
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
