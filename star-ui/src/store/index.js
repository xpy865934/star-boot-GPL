import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import roles from './modules/roles'
import resources from './modules/resources'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    roles,
    permission,
    resources,
    tagsView
  },
  getters
})

export default store
