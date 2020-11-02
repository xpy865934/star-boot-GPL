<template>
  <el-dialog class="xpy_dialog" :append-to-body="appendToBody" :title="title" :visible="addRowVisible" :width="dialogWidth.large" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-divider v-if="disabled" content-position="center">流程审批</el-divider>
    <el-row v-if="disabled">
      <el-col :span="22" :offset="1">
        <el-steps :active="flowActive" finish-status="success">
          <el-step
            v-for="(item, index) in flowNodeList"
            :key="index"
            :title="item.nodeNameAndUserName"
            :description="item.approvalComments"
          />
        </el-steps>
      </el-col>
    </el-row>
    <component :is="currentWindow" ref="window" :row="form" :disabled="disabled" :action="action" @update="addRowUpdate(arguments)" />
    <div v-if="!disabled" slot="footer">
      <el-button :size="buttonSize" @click="cancle">取 消</el-button>
      <el-button type="primary" :size="buttonSize" @click="ok">保 存</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'DialogWindow',
  components: {
    customerInfoWindow: () => import('../customerInfo/window'),
    customerInfoHousesWindow: () => import('../customerInfo/housesWindow')
  },
  props: {
    appendToBody: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    currentWindow: {
      type: String,
      default: function() {
        return ''
      }
    },
    row: {
      type: Object,
      default: function() {
        return {}
      }
    },
    addRowVisible: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    disabled: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    action: {
      type: String,
      default: function() {
        return ''
      }
    }
  },
  data() {
    return {
      // 公共配置
      buttonSize: this.$config.buttonSize,
      dialogEsc: this.$config.dialogEsc,
      dialogClick: this.$config.dialogClick,
      dialogWidth: this.$config.dialogWidth,
      flowActive: 0,
      flowNodeList: [],
      form: null
    }
  },
  computed: {
    // 标题
    title: function() {
      if (this.disabled) {
        // 查看
        return this.$t('common.view')
      } else {
        if (this.action === 'edit') {
          // 修改
          return this.$t('common.edit')
        } else {
          // 新增
          return this.$t('common.add')
        }
      }
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    },

    row(v) {
      this.form = JSON.parse(JSON.stringify(v))
      if (v.processInstanceId) {
        this.$store.dispatch('app/getFLowNodes', { processInstanceId: v.processInstanceId }).then((res) => {
          this.flowNodeList = res
          // 判断是否全部未激活，则表示流程结束
          let flag = true
          for (let i = 0; i < res.length; i++) {
            if (res[i].active) {
              this.flowActive = i
              flag = false
              break
            }
          }
          if (flag) {
            this.flowActive = res.length
          }
        }).catch(() => {
        })
      }
    }
  },
  mounted() {
  },
  created() {},
  destroyed() {},
  methods: {
    /**
* 关闭之前的调用，需要更新父组件，进行关闭模态框
*/
    beforClose() {
      this.$emit('update', false)
    },
    clearFields() {
      this.$refs.window.clearFields()
    },
    cancle() {
      this.$refs.window.cancle()
    },
    ok() {
      this.$refs.window.ok()
    },
    addRowUpdate(value) {
      this.$emit('update', value[0], value[1])
    }
  }
}
</script>
