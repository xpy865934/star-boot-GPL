import {
  getDepartmentTree
} from '@/api/department'

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
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

