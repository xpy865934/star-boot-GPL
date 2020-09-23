import { queryPager, save, update, queryFirstDictPager, saveFirstDict, updateFirstDict } from '@/api/dict'

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
   * 保存字典信息
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
   * 更新字典信息
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
  },
  /**
   * 一级数据字典分页查询
   * @param {*} params
   */
  queryFirstDictPager({ commit }, params) {
    return new Promise((resolve, reject) => {
      queryFirstDictPager(params)
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
   * 保存一级字典信息
   * @param {*} params
   */
  saveFirstDict({ commit }, params) {
    return new Promise((resolve, reject) => {
      saveFirstDict(params)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  /**
   * 更新一级字典信息
   * @param {*} params
   */
  updateFirstDict({ commit }, params) {
    return new Promise((resolve, reject) => {
      updateFirstDict(params)
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

