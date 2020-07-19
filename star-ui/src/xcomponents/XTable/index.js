import Xtable from './XTable'
// 这里是重点
const XTable = {
  install: function(Vue) {
    // 此处的x-table是组件使用的名字
    Vue.component('x-table', Xtable)
  }
}
export default XTable
