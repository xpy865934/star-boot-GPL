<template>
  <div>
    <el-form ref="form" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('changePassword.oldPassword')" prop="oldPassword">
            <el-input v-model="form.oldPassword" :placeholder="$t('common.pleaseInput')+$t('changePassword.oldPassword')" :size="formSize" :style="{ 'width': formFieldWidth }" type="password" :show-password="true" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('changePassword.password')" prop="password">
            <el-input v-model="form.password" :placeholder="$t('common.pleaseInput')+$t('changePassword.password')" :size="formSize" :style="{ 'width': formFieldWidth }" type="password" :show-password="true" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('changePassword.confirmPassword')" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" :placeholder="$t('common.pleaseInput')+$t('changePassword.confirmPassword')" :size="formSize" :style="{ 'width': formFieldWidth }" type="password" :show-password="true" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'ChangePasswordWindow',
  props: {
    action: {
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
    disabled: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    return {
      // 表单form
      form: {},
      // 公共配置
      formSize: this.$config.formSize,
      buttonSize: this.$config.buttonSize,
      formFieldWidth: this.$config.formFieldWidth,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      dialogWidth: this.$config.dialogWidth,
      rules: {
        oldPassword: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('changePassword.oldPassword'), trigger: 'blur' }
        ],
        password: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('changePassword.password'), trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('changePassword.confirmPassword'), trigger: 'blur' }
        ]
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
    row: {
      // 表示对象中属性变化的处理函数，这个函数只能叫这个名字
      handler(v) {
        this.form = JSON.parse(JSON.stringify(v))
      },
      immediate: true,
      deep: false // 表示开启深度监听
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
    ok() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.password !== this.form.confirmPassword) {
            this.$message({
              type: 'error',
              message: '两次密码输入一致，请重新输入'
            })
            return
          }
          this.$emit('update', false, this.form)
        } else {
          this.$message({
            type: 'error',
            message: this.$t('common.formNotValid')
          })
          return false
        }
      })
    }
  }
}
</script>
