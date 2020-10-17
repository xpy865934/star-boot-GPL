import Cookies from 'js-cookie'
import { getFirstDictAll } from '@/api/app'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus')
      ? !!+Cookies.get('sidebarStatus')
      : true,
    withoutAnimation: false
  },
  device: 'desktop',
  firstDict: {}
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
  initStore(params) {
    this.dispatch('app/getFirstDictAll', {})
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
