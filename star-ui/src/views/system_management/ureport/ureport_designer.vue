<template>
  <div class="app-container" style="width='100%' height='100%'">
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
      src: this.$config.UREPORT_BASE_API + '/designer'
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
