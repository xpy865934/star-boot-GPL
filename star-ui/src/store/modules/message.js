import { getUserMessagePager, upadteUserMessageRead, updateAllRead } from '@/api/message'

const getDefaultState = () => {
  return {}
}

const state = getDefaultState()

const mutations = {
}

const actions = {
  /**
   * 分页查询
   * @param {*} params
   */
  getUserMessagePager({ commit }, params) {
    return new Promise((resolve, reject) => {
      getUserMessagePager(params)
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
   * 更新消息为已读
   * @param {*} params
   */
  upadteUserMessageRead({ commit }, params) {
    return new Promise((resolve, reject) => {
      upadteUserMessageRead(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  /**
   * 一键已读
   * @param {*} params
   */
  updateAllRead({ commit }, params) {
    return new Promise((resolve, reject) => {
      updateAllRead(params)
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

