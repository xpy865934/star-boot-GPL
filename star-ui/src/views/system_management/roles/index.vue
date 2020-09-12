<template>
  <div class="app-container">
    <!-- 顶部搜索条件 -->
    <el-collapse v-model="activeNames">
      <el-collapse-item :title="$t('common.searchCondition')" name="1">
        <el-form ref="rolesSearchForm" :model="searchForm" :size="formSize" :inline="true" :label-position="formLabelPosition" :label-width="formLabelWidth">
          <el-form-item :label="$t('roles.roleCode')" prop="roleCode">
            <el-input v-model="searchForm.roleCode" :placeholder="$t('common.pleaseInput')+$t('roles.roleCode')" :size="formSize" />
          </el-form-item>
          <el-form-item :label="$t('roles.roleName')" prop="roleName">
            <el-input v-model="searchForm.roleName" :placeholder="$t('common.pleaseInput')+$t('roles.roleName')" :size="formSize" />
          </el-form-item>
        </el-form><el-row class="search-btn">
          <el-button :type="searchButton" :size="buttonSize" @click="ok">{{ $t('common.search') }}</el-button>
          <el-button :type="resetButton" :size="buttonSize" @click="reset">{{ $t('common.reset') }}</el-button>
        </el-row>
      </el-collapse-item>
    </el-collapse>
    <el-row class="collapse-next-row">
      <el-button :type="addButton" :size="buttonSize" @click="add">{{ $t('common.newAdd') }}</el-button>
    </el-row>
    <x-table id="xTable" :total="total" :options="options" :pagination="pagination" :columns="columns" :operates="operates" :list="tableData" @handleSizeChange="handleSizeChange" @handleIndexChange="handleIndexChange" @handleSelectionChange="handleSelectionChange" />
    <role-window ref="roleWindow" :row="editRow" :add-row-visible="addRowVisible" :disabled="addRowDisabled" @update="addRowUpdate(arguments)" />
    <roleResourcesWindow ref="roleResourcesWindow" :row="editRowResources" :add-row-visible="addRowVisibleResources" @update="addRowUpdateResources(arguments)" />
  </div>
</template>

<script>
import roleWindow from './window'
import roleResourcesWindow from './roleResourcesWindow'
import { mapState } from 'vuex'
export default {
  name: 'Roles',
  components: { roleWindow, roleResourcesWindow },
  data() {
    return {
      urlList: {
        queryPager: 'roles/queryPager',
        save: 'roles/save',
        update: 'roles/update'
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

      // 资源相关
      editRowResources: {},
      addRowVisibleResources: false,

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
          // companyName
          prop: 'companyName',
          label: this.$t('roles.companyName'),
          align: 'center',
          minWidth: 80
        },
        {
          // roleCode
          prop: 'roleCode',
          label: this.$t('roles.roleCode'),
          align: 'center',
          minWidth: 80
        },
        {
          // roleName
          prop: 'roleName',
          label: this.$t('roles.roleName'),
          align: 'center',
          minWidth: 80
        }
      ],
      // 操作列
      operates: {
        width: 300,
        fixed: 'right',
        list: [
          {
            // 编辑
            label: this.$t('common.edit'),
            type: 'primary',
            show: true,
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 编辑
              this.editRow = row
              this.addRowVisible = true
              this.addRowDisabled = false
            }
          },
          {
            // 资源
            label: this.$t('roles.resources'),
            type: 'primary',
            show: true,
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 编辑
              this.editRowResources = row
              this.addRowVisibleResources = true
            }
          }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      user: (state) => state.user
    })
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
      this.$refs['rolesSearchForm'].resetFields()
    },
    /**
* 添加
*/
    add() {
      this.editRow = {
        companyId: this.user.companyId
      }
      this.addRowVisible = true
      this.addRowDisabled = false
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
      this.$store.dispatch(this.urlList['queryPager'], params).then((res) => {
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
        if (bean.roleId) {
          // 修改
          this.$store.dispatch(this.urlList.update, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisible = val[0]
            // 清空
            this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.roleWindow.clearFields()
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
          this.$store.dispatch(this.urlList.save, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisible = val[0]
            // 清空
            this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.roleWindow.clearFields()
            // 新增成功
            this.$message({
              type: 'success',
              message: this.$t('common.addSuccess')
            })
            // 刷新数据
            this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
          }).catch((err) => {
            console.log(err)
          })
        }
      } else {
        // 第一个参数是显不显示对话框
        this.addRowVisible = val[0]
        // 清空
        this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.addRoleManagement.clearFields()
      }
    },
    addRowUpdateResources(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        if (val[2]) {
          // 自己的处理逻辑
          const checked = val[1]
          const roleId = val[2]
          // 修改角色资源信息
          this.$store.dispatch('roles/updateRoleResources', { roleId: roleId, checked: checked }).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisibleResources = val[0]
            // 清空
            this.editRowResources = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.roleResourcesWindow.clearFields()
            // 修改成功
            this.$message({
              type: 'success',
              message: this.$t('common.editSuccess')
            })
          }).catch(() => {
          })
        }
      } else {
        // 第一个参数是显不显示对话框
        this.addRowVisibleResources = val[0]
        // 清空
        this.editRowResources = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.roleResourcesWindow.clearFields()
      }
    }
  }
}
</script>
