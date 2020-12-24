import { getDepartmentTree, getListByCompanyId } from '@/api/department'

const getDefaultState = () => {
  return {}
}

const state = getDefaultState()

const mutations = {
}

const actions = {
  /**
   * 查询组织架构树
   * @param {*} params
   */
  getDepartmentTree({ commit }, params) {
    return new Promise((resolve, reject) => {
      getDepartmentTree(params)
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
   * 根据公司查询部门列表
   * @param {*} params
   */
  getListByCompanyId({ commit }, params) {
    return new Promise((resolve, reject) => {
      getListByCompanyId(params)
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

