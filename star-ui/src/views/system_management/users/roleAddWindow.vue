<template>
  <el-dialog :title="title" :visible="addRowVisible" :width="dialogWidth.large" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="form" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="6">
          <el-form-item :label="$t('users.role')" prop="roles">
            <el-select v-model="form.roles" multiple filterable :placeholder="$t('common.pleaseSelect')+$t('users.role')" :size="formSize" :style="{ 'width': formFieldWidth }">
              <el-option
                v-for="item in rolesList"
                :key="item.roleId"
                :label="item.roleName"
                :value="item.roleId"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!disabled" slot="footer">
      <el-button @click="cancle">取 消</el-button>
      <el-button type="primary" @click="ok">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'UserManagementRoleAdd',
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

      title: '分配角色',

      rolesList: [],
      rules: {
        roles: [
          { type: Array, required: true, message: this.$t('common.pleaseSelect') + this.$t('users.role'), trigger: 'change' }
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
    row(v) {
      this.form = JSON.parse(JSON.stringify(v))
      if (this.form.companyId && this.form.userId) {
        this.getRolesList(this.form.userId, this.form.companyId)
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
    ok() {
      this.$refs['form'].validate((valid) => {
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
    },
    getRolesList(userId, companyId) {
      this.$store.dispatch('roles/getRoleListByCompanyId', { companyId: companyId }).then((data) => {
        this.rolesList = data
        // 获取用户已有角色信息
        this.getUserRoleList(userId)
      }).catch(() => {
      })
    },
    getUserRoleList(userId) {
      this.$store.dispatch('roles/getRolesByUserId', { userId: userId }).then((data) => {
        for (let i = 0; i < data.length; i++) {
          this.form.roles.push(data[i].roleId)
        }
      }).catch(() => {
      })
    }
  }
}
</script>
