<template>
  <div class="app-container">
    <!-- 顶部搜索条件 -->
    <el-collapse v-model="activeNames">
      <el-collapse-item :title="$t('common.searchCondition')" name="1">
        <el-form ref="usersSearchForm" :model="searchForm" :size="formSize" :inline="true" :label-position="formLabelPosition" :label-width="formLabelWidth">
          <el-form-item :label="$t('users.userCode')" prop="userCode">
            <el-input v-model="searchForm.userCode" :placeholder="$t('common.pleaseInput')+$t('users.userCode')" :size="formSize" />
          </el-form-item>
          <el-form-item :label="$t('users.userName')" prop="userName">
            <el-input v-model="searchForm.userName" :placeholder="$t('common.pleaseInput')+$t('users.userName')" :size="formSize" />
          </el-form-item>
          <el-form-item :label="$t('users.tel')" prop="tel">
            <el-input v-model="searchForm.tel" :placeholder="$t('common.pleaseInput')+$t('users.tel')" :size="formSize" />
          </el-form-item>
        </el-form>
        <el-row class="search-btn">
          <el-button :type="searchButton" :size="buttonSize" @click="ok">{{ $t('common.search') }}</el-button>
          <span class="bank15" />
          <span class="bank15" />
          <span class="bank15" />
          <el-button :type="resetButton" :size="buttonSize" @click="reset">{{ $t('common.reset') }}</el-button>
        </el-row>
      </el-collapse-item>
    </el-collapse>
    <el-row class="collapse-next-row">
      <el-button v-if="$access('users_save')" :type="addButton" :size="buttonSize" @click="add">{{ $t('common.newAdd') }}</el-button>
    </el-row>
    <x-table
      id="xTable"
      :total="total"
      :options="options"
      :pagination="pagination"
      :columns="columns"
      :operates="operates"
      :list="tableData"
      @handleSizeChange="handleSizeChange"
      @handleIndexChange="handleIndexChange"
      @handleSelectionChange="handleSelectionChange"
    />
    <user-management-add ref="addUserManagement" :row="editRow" :add-row-visible="addRowVisible" :disabled="addRowDisabled" @update="addRowUpdate(arguments)" />
    <user-management-role-add ref="addUserManagementRole" :row="editRowRole" :add-row-visible="addRowVisibleRole" @update="addRowUpdateRole(arguments)" />
  </div>
</template>

<script>
import userManagementAdd from './window'
import userManagementRoleAdd from './roleAddWindow'
export default {
  name: 'Users',
  components: {
    userManagementAdd,
    userManagementRoleAdd
  },
  data() {
    return {
      urlList: {
        queryPager: 'user/queryPager'
      },
      // 控制是否显示顶部搜索
      activeNames: ['1'],
      // 搜索form
      searchForm: {},
      // 用户控制新增修改
      editRow: {},
      // 显示新增修改对话框
      addRowVisible: false,
      // 控制表单是否可以操作
      addRowDisabled: false,

      // 角色数据
      editRowRole: {},
      addRowVisibleRole: false,

      // 表格数据
      tableData: [],
      // table 的参数
      options: {
        stripe: true, // 是否为斑马纹 table
        loading: false, // 是否添加表格loading加载动画
        highlightCurrentRow: true, // 是否支持当前行高亮显示
        mutiSelect: true, // 是否支持列表项选中功能
        showIndex: true // 是否显示序号列
      },

      // 公共配置
      formSize: this.$config.formSize,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      buttonSize: this.$config.buttonSize,
      searchButton: this.$config.searchButton,
      resetButton: this.$config.resetButton,
      addButton: this.$config.addButton,

      // 分页
      total: 0,
      // 分页参数
      pagination: {
        pageIndex: 1,
        pageSize: 10
      },

      // 表格列
      columns: [
        {
          // 医生姓名
          prop: 'userCode',
          label: this.$t('users.userCode'),
          align: 'center',
          minWidth: 100
        },
        {
          // 医院名称
          prop: 'userName',
          label: this.$t('users.userName'),
          align: 'center',
          minWidth: 100
        },
        {
          // 邮箱
          prop: 'email',
          label: this.$t('users.email'),
          align: 'center',
          minWidth: 100
        },
        {
          // 联系方式
          prop: 'tel',
          label: this.$t('users.tel'),
          align: 'center',
          minWidth: 100
        },
        {
          // 最后一次登录ip
          prop: 'lastLoginIp',
          label: this.$t('users.lastLoginIp'),
          align: 'center',
          minWidth: 120
        },
        {
          // 最后登录日期
          prop: 'lastLoginDate',
          label: this.$t('users.lastLoginDate'),
          align: 'center',
          minWidth: 100
        }
      ],
      // 操作列
      operates: {
        width: 300,
        fixed: 'right',
        list: [
          {
            // 查看
            label: this.$t('common.view'),
            type: 'primary',
            icon: 'el-icon-view',
            show: true,
            method: (index, row) => {
              // 查看
              this.editRow = row
              this.addRowVisible = true
              this.addRowDisabled = true
            }
          },
          {
            // 编辑
            label: this.$t('common.edit'),
            type: 'primary',
            showFun: (index, row) => {
              if (this.$access('users_update')) {
                return true
              } else {
                return false
              }
            },
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 编辑
              this.editRow = row
              this.addRowVisible = true
              this.addRowDisabled = false
            }
          },
          {
            // 分配角色
            label: this.$t('users.role'),
            type: 'primary',
            showFun: (index, row) => {
              if (this.$access('usersReRoles_save')) {
                return true
              } else {
                return false
              }
            },
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 分配角色
              this.editRowRole = row
              this.addRowVisibleRole = true
            }
          },
          {
            // 删除
            label: this.$t('common.delete'),
            type: 'danger',
            showFun: (index, row) => {
              if (this.$access('users_deleteById')) {
                return true
              } else {
                return false
              }
            },
            method: (index, row) => {
              // 删除
              this.$confirm(this.$t('common.comfirmDelete'), this.$t('common.info'), {
                confirmButtonText: this.$t('common.comfirm'),
                cancelButtonText: this.$t('common.cancle'),
                type: 'error'
              }).then(() => {
                // 删除成功
                this.$store.dispatch('user/deleteById', { userId: row.userId }).then((res) => {
                  // 重新查询
                  this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
                }).catch(() => {
                })
              }).catch(() => {
              })
            }
          }
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
    }
  },
  mounted() {
    // 获取表格数据
    this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
  },
  created() {

  },
  destroyed() {

  },
  methods: {
    add() {
      this.editRow = {}
      this.addRowVisible = true
      this.addRowDisabled = false
    },
    /**
     * 查询
     */
    ok() {
      this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
    },
    /**
     * 重置
     */
    reset() {
      // 需要将表单searchForm置为空，否则会出现，点击重置之后部分其他输入框无法输入，输入之前的输入框是，值又显示出来  参考：https://blog.csdn.net/nidunlove/article/details/100975379
      this.searchForm = {}
      this.$refs['usersSearchForm'].resetFields()
    },
    /**
     * 获取表格分页数据
     */
    getPagerData(current, size, bean) {
      const params = {
        'current': current,
        'size': size,
        'bean': bean
      }
      this.$store.dispatch(this.urlList.queryPager, params).then((res) => {
        this.pagination.pageIndex = res.current
        this.pagination.pageSize = res.size
        this.total = res.total
        this.tableData = res.records
      }).catch(() => {
      })
    },
    /**
     * 切换每页显示的数量
     */
    handleSizeChange(pagination) {
      // 切换每页显示条数时，重新从第一页开始
      this.getPagerData(1, pagination.pageSize, this.searchForm)
    },
    /**
      * 切换页码
      */
    handleIndexChange(pagination) {
      this.getPagerData(pagination.pageIndex, pagination.pageSize, this.searchForm)
    },
    /**
      * 选中
      */
    handleSelectionChange(val) {
    },
    addRowUpdate(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        if (bean.userId) {
          // 修改
          this.$store.dispatch('user/update', bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisible = val[0]
            // 清空
            this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.addUserManagement.clearFields()
            // 修改成功
            this.$message({
              type: 'success',
              message: this.$t('common.editSuccess')
            })
            // 刷新数据
            this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
          }).catch(() => {
          })
        } else {
          // 新增
          this.$store.dispatch('user/save', bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisible = val[0]
            // 清空
            this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.addUserManagement.clearFields()
            // 新增成功
            this.$message({
              type: 'success',
              message: this.$t('common.addSuccess')
            })
            // 刷新数据
            this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
          }).catch(() => {
          })
        }
      } else {
        // 第一个参数是显不显示对话框
        this.addRowVisible = val[0]
        // 清空
        this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.addUserManagement.clearFields()
      }
    },
    addRowUpdateRole(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        // 修改角色信息
        this.$store.dispatch('roles/saveUserReRoles', { userId: bean.userId, roles: bean.roles }).then((res) => {
          // 第一个参数是显不显示对话框
          this.addRowVisibleRole = val[0]
          // 清空
          this.editRowRole = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
          this.$refs.addUserManagementRole.clearFields()
          // 修改成功
          this.$message({
            type: 'success',
            message: this.$t('common.editSuccess')
          })
        }).catch(() => {
        })
      } else {
        // 第一个参数是显不显示对话框
        this.addRowVisibleRole = val[0]
        // 清空
        this.editRowRole = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.addUserManagementRole.clearFields()
      }
    }
  }
}
</script>
