import { queryPager, deleteById, save, update, getResourcesByRoleTid, updateRoleResources } from '@/api/roles'

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
   * 保存角色信息
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
   * 更新角色信息
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
   * 删除角色
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
   * 根据角色id获取角色资源信息
   * @param {*} params
   */
  getResourcesByRoleTid({ commit }, params) {
    return new Promise((resolve, reject) => {
      getResourcesByRoleTid(params)
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
   * 更新角色资源信息
   * @param {*} params
   */
  updateRoleResources({ commit }, params) {
    return new Promise((resolve, reject) => {
      updateRoleResources(params)
        .then(response => {
          const data = response.data
          resolve(data)
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

