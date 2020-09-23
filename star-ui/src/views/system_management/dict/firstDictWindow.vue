<template>
  <el-dialog :title="title" :visible="addRowVisible" :width="dialogWidth.small" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="firstDictForm" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('dict.parentDictName')" prop="dictName">
            <el-input v-model="form.dictName" :size="formSize" :style="{ 'width': formFieldWidth }" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('dict.dictCode')" prop="firstDictCode">
            <el-input v-model="form.firstDictCode" :placeholder="$t('common.pleaseInput')+$t('dict.dictCode')" :size="formSize" :style="{ 'width': formFieldWidth }" :disabled="firstDictCodeDisabled" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('dict.dictName')" prop="firstDictName">
            <el-input v-model="form.firstDictName" :placeholder="$t('common.pleaseInput')+$t('dict.dictName')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('dict.dictDs')" prop="firstDictDs">
            <el-input
              v-model="form.firstDictDs"
              type="textarea"
              :rows="2"
              :placeholder="$t('common.pleaseInput')+$t('dict.dictDs')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!disabled" slot="footer">
      <el-button @click="cancle">取 消</el-button>
      <el-button type="primary" @click="ok">保 存</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'RoleAdd',
  props: {
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
    }
  },
  data() {
    return {
      dictCodeReadonly: false,
      // 表单form
      form: {},
      // 公共配置
      formSize: this.$config.formSize,
      buttonSize: this.$config.buttonSize,
      formFieldWidth: this.$config.formFieldWidth,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      dialogEsc: this.$config.dialogEsc,
      dialogClick: this.$config.dialogClick,
      dialogWidth: this.$config.dialogWidth,
      companyTid: '',
      rules: {
        firstDictCode: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('dict.dictCode'), trigger: 'change' }
        ],
        firstDictName: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('dict.dictName'), trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    // 标题
    title: function() {
      if (this.disabled) {
        // 查看
        return this.$t('common.view')
      } else {
        if (this.form.firstDictId) {
          // 修改
          return this.$t('common.edit')
        } else {
          // 新增
          return this.$t('common.add')
        }
      }
    },
    firstDictCodeDisabled: function() {
      if (this.$isEmpty(this.form.firstDictId)) {
        return false
      }
      return true
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
    }
  },
  mounted() {
  },
  created() {},
  destroyed() {},
  methods: {
    clearFields() {
      this.$refs['firstDictForm'].resetFields()
    },
    cancle() {
      this.$emit('update', false)
    },
    ok() {
      this.$refs['firstDictForm'].validate((valid) => {
        if (valid) {
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
