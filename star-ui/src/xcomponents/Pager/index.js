import XPager from './pager.vue'
// 这里是重点
const Pager = {
  install: function(Vue) {
    Vue.component('Pager', XPager)
  }
}
export default Pager
