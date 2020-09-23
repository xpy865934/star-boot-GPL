<template>
  <div class="app-container">
    <split-pane split="vertical" :default-percent="30" class-name="resizer-class">
      <template slot="paneL">
        <div class="left-container">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ $t('department.cardName') }}</span>
              <el-button :type="addButton" :size="buttonSize" style="float: right;" icon="el-icon-plus" @click="addDepartment">新增</el-button>
            </div>
            <el-tree
              ref="departmentTree"
              :data="departmentData"
              default-expand-all
              :expand-on-click-node="false"
              node-key="departmentCode"
              highlight-current
              :props="defaultProps"
              @node-click="checkChange"
            />
          </el-card>
        </div>
      </template>
      <template slot="paneR">
        <div class="left-container">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ $t('department.cardName') }}</span>
              <!-- 该按钮属于占位，控制两侧头部高度相等 -->
              <el-button :type="addButton" :size="buttonSize" style="float: right;visibility:hidden" icon="el-icon-plus" />
            </div>
            <el-form ref="departmentForm" :model="departmentForm" :size="formSize" :label-position="formLabelPosition" :label-width="formLabelWidth">
              <el-row>
                <el-col :span="8">
                  <el-form-item :label="$t('department.departmentName')" prop="departmentName">
                    <el-input v-model="departmentForm.departmentName" :placeholder="$t('common.pleaseInput')+$t('department.departmentName')" :size="formSize" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="$t('department.departmentCode')" prop="departmentCode">
                    <el-input v-model="departmentForm.departmentCode" :placeholder="$t('common.pleaseInput')+$t('department.departmentCode')" :size="formSize" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="$t('department.parentDepartmentName')" prop="parentDepartmentName">
                    <el-input v-model="departmentForm.parentDepartmentName" :placeholder="$t('common.pleaseInput')+$t('orgManagement.parentDepartmentName')" :size="formSize" readonly />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
            <el-row class="search-btn">
              <el-button :type="saveButton" :size="buttonSize" @click="updateDepartment">{{ $t('common.save') }}</el-button>
              <el-button :type="deletedButton" :size="buttonSize" @click="deleteDepartment">{{ $t('common.delete') }}</el-button>
            </el-row>
          </el-card>
        </div>
      </template>
    </split-pane>
    <!-- <org-management-add ref="addOrgManagement" :row="editRow" :add-row-visible="addRowVisible" :disabled="addRowDisabled" @update="addRowUpdate(arguments)" /> -->
  </div>
</template>
<script>
import splitPane from 'vue-splitpane'
export default {
  name: 'Department',
  components: { splitPane },
  data() {
    return {
      urlList: {
        getDepartmentTree: 'department/getDepartmentTree',
        save: 'roles/save',
        update: 'roles/update'
      },
      // 公共配置
      formSize: this.$config.formSize,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      buttonSize: this.$config.buttonSize,
      searchButton: this.$config.searchButton,
      resetButton: this.$config.resetButton,
      addButton: this.$config.addButton,
      saveButton: this.$config.saveButton,
      deletedButton: this.$config.deletedButton,

      defaultProps: {
        children: 'children',
        label: 'label'
      },
      departmentData: [],
      departmentForm: {},
      editRow: {},
      addRowVisible: false,
      addRowDisabled: false
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
    this.getDepartmentTree()
  },
  created() {

  },
  destroyed() {

  },
  methods: {
    addDepartment() {
      var selectNode = this.$refs.departmentTree.getCurrentNode()
      if (selectNode) {
        this.addRowVisible = true
        this.addRowDisabled = false
      } else {
        // 必须选择一个公司或者部门节点
        this.$message({
          type: 'info',
          message: this.$t('orgManagement.mustSelectOrg')
        })
      }
    },
    /**
     * 获取组织架构信息
     */
    getDepartmentTree() {
      this.$refs['departmentTree'].setCurrentKey('001')
      this.$refs['departmentTree'].setChecked('001', true)
      this.$store.dispatch(this.urlList.getDepartmentTree, {}).then((data) => {
        this.departmentData = data
        this.departmentForm = data[0]
      }).catch(() => {
      })
    },
    /**
     * 保存组织架构详细信息
     */
    updateOrg() {
      this.$store.dispatch('updateOrgDepartment', this.orgForm).then((data) => {
        if (data.code === 200) {
          // 修改成功
          this.$message({
            type: 'success',
            message: this.$t('common.editSuccess')
          })
          // 重新获取部门树信息
          this.getOrgDepartment()
        } else {
          // 修改失败
          this.$message({
            type: 'error',
            message: data.msg
          })
        }
      }).catch(() => {
        // 修改失败
        this.$message({
          type: 'error',
          message: this.$t('common.editFailed')
        })
      })
    },
    /**
     * 删除该组织部门
     */
    deleteOrg() {
      // 不是最后叶子的部门，不能删除
      if (this.orgForm.children) {
        // 删除失败
        this.$message({
          type: 'error',
          message: this.$t('orgManagement.cantdeleteNotLast')
        })
        return
      }
      this.$store.dispatch('deleteOrgDepartment', this.orgForm).then((data) => {
        // 删除成功
        this.$message({
          type: 'success',
          message: this.$t('common.deleteSuccess')
        })
        // 重新获取部门树信息
        this.getOrgDepartment()
      }).catch(() => {})
    },
    addRowUpdate(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第一个参数是显不显示对话框
      this.addRowVisible = val[0]
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        var selectNode = this.$refs.departmentTree.getCurrentNode()
        if (selectNode.companyTid) {
          bean.companyTid = selectNode.companyTid
          bean.pid = selectNode.tid
        } else {
          bean.companyTid = selectNode.tid
          bean.pid = -1
        }
        // 修改
        this.$store.dispatch('saveOrgDepartment', bean).then((res) => {
          // 修改成功
          this.$message({
            type: 'success',
            message: this.$t('common.editSuccess')
          })
          // 重新获取数据
          this.getOrgDepartment()
        }).catch(() => {})
      }
      // 清空
      this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
      this.$refs.addOrgManagement.clearFields()
    },
    /**
     * 选择部门时改变右侧数据
     */
    checkChange(data, checked, child) {
      this.departmentForm = data
    }
  }
}
</script>
<style  scoped>
  .left-container {
    height: 100%;
  }
.text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .box-card {
      height: 100%;
  }
</style>
