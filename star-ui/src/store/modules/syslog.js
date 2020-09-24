import { queryPager } from '@/api/syslog'

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
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

