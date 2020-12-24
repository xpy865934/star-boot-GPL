<template>
  <el-dialog :title="title" :visible="addRowVisible" :width="dialogWidth.large" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="form" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="6">
          <el-form-item :label="$t('users.userCode')" prop="userCode">
            <el-input v-model="form.userCode" :placeholder="$t('common.pleaseInput')+$t('users.userCode')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.userName')" prop="userName">
            <el-input v-model="form.userName" :placeholder="$t('common.pleaseInput')+$t('users.userName')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.userSex')" prop="userSex">
            <el-select v-model="form.userSex" filterable :placeholder="$t('common.pleaseSelect')+$t('users.userSex')" :size="formSize" :style="{ width: formFieldWidth }">
              <el-option
                v-for="item in firstDict.sex"
                :key="item.firstDictCode"
                :label="item.firstDictName"
                :value="item.firstDictCode"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.tel')" prop="tel">
            <el-input v-model="form.tel" :placeholder="$t('common.pleaseInput')+$t('users.tel')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.working')" prop="working">
            <el-select v-model="form.working" filterable :placeholder="$t('common.pleaseSelect')+$t('users.working')" :size="formSize" :style="{ width: formFieldWidth }">
              <el-option
                v-for="item in firstDict.working"
                :key="item.firstDictCode"
                :label="item.firstDictName"
                :value="item.firstDictCode"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.company')" prop="companyId">
            <el-select v-model="form.companyId" filterable :placeholder="$t('common.pleaseSelect')+$t('users.company')" :size="formSize" :style="{ 'width': formFieldWidth }" @change="companyChange">
              <el-option
                v-for="item in companyList"
                :key="item.companyId"
                :label="item.companyName"
                :value="item.companyId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.department')" prop="departmentId">
            <el-select v-model="form.departmentId" filterable :placeholder="$t('common.pleaseSelect')+$t('users.department')" :size="formSize" :style="{ 'width': formFieldWidth }">
              <el-option
                v-for="item in departmentList"
                :key="item.departmentId"
                :label="item.departmentName"
                :value="item.departmentId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.companyPosition')" prop="companyPosition">
            <el-input v-model="form.companyPosition" :placeholder="$t('common.pleaseInput')+$t('users.companyPosition')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.birthday')" prop="birthday">
            <el-date-picker
              v-model="form.birthday"
              type="date"
              :placeholder="$t('common.pleaseSelect')+$t('users.birthday')"
              :size="formSize"
              :style="{ 'width': formFieldWidth }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.credentialsCode')" prop="credentialsCode">
            <el-input v-model="form.credentialsCode" :placeholder="$t('common.pleaseInput')+$t('users.credentialsCode')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.email')" prop="email">
            <el-input v-model="form.email" :placeholder="$t('common.pleaseInput')+$t('users.email')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.qq')" prop="qq">
            <el-input v-model="form.qq" :placeholder="$t('common.pleaseInput')+$t('users.qq')" :size="formSize" :style="{ 'width': formFieldWidth }" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('users.wx')" prop="wx">
            <el-input v-model="form.wx" :placeholder="$t('common.pleaseInput')+$t('users.wx')" :size="formSize" :style="{ 'width': formFieldWidth }" />
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
import { mapState } from 'vuex'
export default {
  name: 'UserManagementAdd',
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

      departmentList: [],
      rules: {
        userCode: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('users.userCode'), trigger: 'change' }
        ],
        userName: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('users.userName'), trigger: 'change' }
        ],
        userSex: [
          { required: true, message: this.$t('common.pleaseSelect') + this.$t('users.userSex'), trigger: 'change' }
        ],
        tel: [
          { required: true, message: this.$t('common.pleaseInput') + this.$t('users.tel'), trigger: 'change' }
        ],
        working: [
          { required: true, message: this.$t('common.pleaseSelect') + this.$t('users.working'), trigger: 'change' }
        ],
        companyId: [
          { required: true, message: this.$t('common.pleaseSelect') + this.$t('users.company'), trigger: 'change' }
        ],
        departmentId: [
          { required: true, message: this.$t('common.pleaseSelect') + this.$t('users.department'), trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      firstDict: (state) => state.app.firstDict,
      companyList: (state) => state.app.companyList
    }),
    // 标题
    title: function() {
      if (this.disabled) {
        // 查看
        return this.$t('common.view')
      } else {
        if (this.form.userId) {
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
      // 数据通过后台查询过来
      if (v.userId) {
        this.form.userId = v.userId
        this.$store.dispatch('user/queryById', { userId: v.userId }).then((res) => {
          this.form = JSON.parse(JSON.stringify(res))
          this.companyChange(this.form.companyId)
        }).catch(() => {
        })
      } else {
        this.form = JSON.parse(JSON.stringify(v))
      }
    }
  },
  mounted() {},
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
    /**
     * 公司选择事件
     */
    companyChange(val) {
      this.$store.dispatch('department/getListByCompanyId', { companyId: val }).then((data) => {
        this.departmentList = data
      }).catch(() => {
      })
    }
  }
}
</script>
