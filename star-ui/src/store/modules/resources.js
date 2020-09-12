import { queryList, updateUsed, deleteById, save, update } from '@/api/resources'

const getDefaultState = () => {
  return {}
}

const state = getDefaultState()

const mutations = {
}

const actions = {
  /**
   * 查询资源信息列表
   * @param {*} params
   */
  queryList({ commit }, params) {
    return new Promise((resolve, reject) => {
      queryList(params)
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
   * 更新是否启用
   * @param {*} params
   */
  updateUsed({ commit }, params) {
    return new Promise((resolve, reject) => {
      updateUsed(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  /**
   * 删除资源信息
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
   * 保存菜单资源信息
   * @param {*} params
   */
  save({ commit }, params) {
    return new Promise((resolve, reject) => {
      save(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  /**
   * 更新菜单资源信息
   * @param {*} params
   */
  update({ commit }, params) {
    return new Promise((resolve, reject) => {
      update(params)
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

