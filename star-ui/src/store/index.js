// import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import roles from './modules/roles'
import dict from './modules/dict'
import syslog from './modules/syslog'
import department from './modules/department'
import resources from './modules/resources'
import customerInfo from './modules/customerInfo'
import customerHouses from './modules/customerHouses'
import customerHousesProject from './modules/customerHousesProject'
import afterSales from './modules/afterSales'
import equipment from './modules/equipment'
import commission from './modules/commission'
import message from './modules/message'

// Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    roles,
    permission,
    resources,
    dict,
    department,
    syslog,
    tagsView,
    customerInfo,
    customerHouses,
    customerHousesProject,
    afterSales,
    equipment,
    commission,
    message
  },
  getters
})

export default store
