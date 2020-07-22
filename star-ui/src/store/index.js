import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import person from './modules/person'
import uploadData from './modules/uploadData'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    permission,
    person,
    uploadData,
    tagsView
  },
  getters
})

export default store
