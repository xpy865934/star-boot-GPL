<template>
  <el-dialog :title="title" :visible="addRowVisible" :width="dialogWidth.small" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="resourcesForm" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="24">
          <el-form-item label="上级资源编号" prop="parentResourcesNum">
            <el-input v-model="form.parentResourcesNum" :size="formSize" :style="{ 'width': formFieldWidth }" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资源编号" prop="resourcesNum">
            <el-input v-model="form.resourcesNum" :placeholder="$t('common.pleaseInput')+'资源编号'" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资源名称" prop="resourcesName">
            <el-input v-model="form.resourcesName" :placeholder="$t('common.pleaseInput')+'资源名称'" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资源类型" prop="resourcesType">
            <el-select v-model="form.resourcesType" :placeholder="$t('common.pleaseSelect')+'资源类型'" :size="formSize" :style="{ 'width': formFieldWidth }">
              <el-option
                :key="1"
                label="菜单"
                :value="1"
              />
              <el-option
                :key="2"
                label="按钮"
                :value="2"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资源说明" prop="resourcesDs">
            <el-input
              v-model="form.resourcesDs"
              type="textarea"
              :rows="2"
              :placeholder="$t('common.pleaseInput')+'资源说明'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资源代码" prop="resourcesCode">
            <el-input
              v-model="form.resourcesCode"
              type="textarea"
              :rows="2"
              :placeholder="$t('common.pleaseInput')+'资源代码'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="排序号" prop="pxh">
            <el-input v-model="form.pxh" :placeholder="$t('common.pleaseInput')+'排序号'" :size="formSize" :style="{ 'width': formFieldWidth }" />
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
  name: 'ResourcesAdd',
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
        companyName: [
          { required: true, message: this.$t('common.pleaseSelect') + this.$t('roleManagement.companyName'), trigger: 'change' }
        ],
        roleName: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('roleManagement.roleName'), trigger: 'change' }
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
        if (this.form.tid) {
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
    }
  },
  mounted() {
  },
  created() {},
  destroyed() {},
  methods: {
    clearFields() {
      this.$refs['resourcesForm'].resetFields()
    },
    cancle() {
      this.$emit('update', false)
    },
    ok() {
      this.$refs['resourcesForm'].validate((valid) => {
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
