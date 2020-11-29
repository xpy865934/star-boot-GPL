import Vue from 'vue'

import './assets/icon/iconfont.css'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
// import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n
import { Message } from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // Internationalization

import '@/icons' // icon
import '@/permission' // permission control

import config from '@/config' // config

import { isEmpty, isIntNum, isNumber, decimalPercentConvert, formatPercent, formatThousandth, translateFirstDict, translateUserName, getFileUrl } from '@/utils/common'

// 自定义组件
// 分页   // 暂时封装到了表格中，没有单独使用
// import Pager from './xcomponents/Pager'
// Vue.use(Pager)
// 表格
import XTable from './xcomponents/XTable'
Vue.use(XTable)

// k-form-design
import KFormDesign from 'k-form-design'
import 'k-form-design/lib/k-form-design.css'
Vue.use(KFormDesign)

Vue.use(ElementUI, {
  i18n: (key, value) => i18n.t(key, value)
})

/**
 * 注册全局变量
 */
Vue.prototype.$config = config
Vue.prototype.$isEmpty = isEmpty
Vue.prototype.$isIntNum = isIntNum
Vue.prototype.$isNumber = isNumber
Vue.prototype.$formatPercent = formatPercent
Vue.prototype.$formatThousandth = formatThousandth
Vue.prototype.$decimalPercentConvert = decimalPercentConvert
Vue.prototype.$translateFirstDict = translateFirstDict
Vue.prototype.$translateUserName = translateUserName
Vue.prototype.$getFileUrl = getFileUrl

// 重写$messgae
Vue.prototype.$message = function(data) {
  Message({
    type: data.type,
    message: data.message,
    duration: config.messageDuration
  })
}

// 权限校验
Vue.prototype.$access = function(permission) {
  const permissions = store.getters.permissions
  if (permission) {
    if (permissions.indexOf(permission) > -1) {
      return true
    } else {
      return false
    }
  } else {
    return true
  }
}

// 日期格式化
Vue.prototype.$DateFormat = function(date, fmt) { // author: meizz
  var o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  }
  if (/(y+)/.test(fmt)) { fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length)) }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) { fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length))) }
  }
  return fmt
}

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
// TODO
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
// Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
