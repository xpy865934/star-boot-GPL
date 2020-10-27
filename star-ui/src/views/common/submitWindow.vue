<template>
  <el-dialog class="xpy_dialog" :append-to-body="true" :title="title" :visible="addRowVisible" :width="dialogWidth.large" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="form" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('common.approvalComments')" prop="approvalComments">
            <el-input v-model="form.approvalComments" type="textarea" :autosize="{ minRows: 3, maxRows: 6 }" :maxlength="100" show-word-limit :placeholder="$t('common.pleaseInput')+$t('common.approvalComments')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-divider content-position="center">流程审批</el-divider>
    <el-row>
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
    <div style="text-align:right">
      <el-button type="primary" :size="buttonSize" @click="ok('submit')">提 交</el-button>
      <el-button type="primary" :size="buttonSize" @click="ok('back')">退 回</el-button>
    </div>
    <div>
      <el-divider content-position="center">业务数据</el-divider>
      <component :is="currentComponent" :row="viewForm" :disabled="true" action="view" />
    </div>
    <div v-if="!disabled" slot="footer">
      <el-button :size="buttonSize" @click="cancle">关闭</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'SubmitWindow',
  components: {
    customerInfoWindow: () => import('../customerInfo/window')
  },
  props: {
    row: {
      type: Object,
      default: function() {
        return {}
      }
    },
    businessKey: {
      type: String,
      default: function() {
        return ''
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
      currentComponent: 'customerInfoWindow',
      // 表单form
      form: {},
      viewForm: {},
      // 公共配置
      formSize: this.$config.formSize,
      buttonSize: this.$config.buttonSize,
      formFieldWidth: this.$config.formFieldWidth,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      dialogEsc: this.$config.dialogEsc,
      dialogClick: this.$config.dialogClick,
      dialogWidth: this.$config.dialogWidth,
      flowActive: 0,
      flowNodeList: [],
      rules: {
        approvalComments: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('common.approvalComments'), trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    // 标题
    title: function() {
      return '流程提交'
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
      this.viewForm = JSON.parse(JSON.stringify(v))
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
    clearFields() {
      this.$refs['form'].resetFields()
    },
    cancle() {
      this.$emit('update', false)
    },
    ok(value) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.form.businessKey = this.viewForm[this.businessKey]
          this.form.submitState = value
          this.$emit('update', false, this.form)
        } else {
          this.$message({
            type: 'error',
            message: this.$t('common.formNotValid')
          })
          return false
        }
      })
    },
    /**
* 关闭之前的调用，需要更新父组件，进行关闭模态框
*/
    beforClose() {
      this.$emit('update', false)
    }
  }
}
</script>
