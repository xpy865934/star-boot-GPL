import { save, update, queryByDate, upload } from '@/api/uploadData'

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
  upload({ commit }, data) {
    return new Promise((resolve, reject) => {
      upload(data)
        .then(response => {
          resolve(response)
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  queryByDate({ commit }, data) {
    return new Promise((resolve, reject) => {
      queryByDate(data)
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

