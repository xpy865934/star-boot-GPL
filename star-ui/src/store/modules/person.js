import { save, update, query } from '@/api/person'

const getDefaultState = () => {
  return {

  }
}

const state = getDefaultState()

const mutations = {

}

const actions = {
  save({ commit }, data) {
    return new Promise((resolve, reject) => {
      save(data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  update({ commit }, data) {
    return new Promise((resolve, reject) => {
      update(data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  query({ commit }, data) {
    return new Promise((resolve, reject) => {
      query(data)
        .then(response => {
          resolve(response.data)
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

