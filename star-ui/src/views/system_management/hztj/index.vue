<template>
  <div class="app-container">
    <iframe width="100%" height="100%" seamless frameBorder="0" scrolling="no" :src="src" />
  </div>
</template>

<script>
export default {
  name: 'PermissionExcel',
  components: {
  },
  data() {
    return {
      src: this.$config.UREPORT_BASE_API + '/preview?_u=file:' + encodeURIComponent('数据汇总') + '.ureport.xml' + '&_i=1&_r=1'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  mounted() {

  },
  created() {

  },
  destroyed() {

  },
  methods: {

  }
}
</script>
