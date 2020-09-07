<template>
  <div class="app-container table">
    <el-table
      ref="table"
      :data="items"
      row-key="id"
      :stripe="true"
      :highlight-current-row="true"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      @row-click="rowClick"
    >
      <el-table-column type="selection" style="width: 55px;" />
      <el-table-column label="菜单编号" width="200" align="center">
        <template slot-scope="scope">{{ scope.row.bmwh1.name }} ({{ scope.row.bmwh1.code }})</template>
      </el-table-column>
      <el-table-column label="菜单名称" width="200" align="center">
        <template slot-scope="scope">{{ scope.row.bmwh1.updatedAt }}</template>
      </el-table-column>
      <el-table-column label="上级菜单" width="200" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.bmwh1.pid==null?'':`${scope.row.bmwh2.name}(${ scope.row.bmwh2.code})` }}</template>
      </el-table-column>
      <el-table-column label="图标" width="100" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.bmwh1.pid==null?'':`${scope.row.bmwh2.name}(${ scope.row.bmwh2.code})` }}</template>
      </el-table-column>
      <el-table-column label="类型" width="100" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.bmwh1.pid==null?'':`${scope.row.bmwh2.name}(${ scope.row.bmwh2.code})` }}</template>
      </el-table-column>
      <el-table-column label="排序号" width="100" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.bmwh1.pid==null?'':`${scope.row.bmwh2.name}(${ scope.row.bmwh2.code})` }}</template>
      </el-table-column>
      <el-table-column label="是否启用" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.bmwh1.enable"
            @change="onenable($event,scope.row.bmwh1 )"
          />
        </template>
      </el-table-column>
      <el-table-column label="授权标识" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.bmwh1.pid==null?'':`${scope.row.bmwh2.name}(${ scope.row.bmwh2.code})` }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Resources',
  data() {
    return {
      items: [{
        id: '1',
        bmwh1: {
          name: '1',
          code: '001',
          updatedAt: '2020-08-19'
        },
        children: [{
          id: '2',
          bmwh1: {
            name: '2',
            code: '002',
            updatedAt: '2020-08-19'
          }
        }]
      }],

      urlList: {
        queryPager: 'user/queryPager'
      },
      // 控制是否显示顶部搜索
      activeNames: ['1'],
      // 搜索form
      searchForm: {},
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
            // 编辑
            label: this.$t('common.edit'),
            type: 'primary',
            show: false,
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 编辑
              this.editRow = row
              this.addRowVisible = true
              this.addRowDisabled = false
            }
          },
          {
            // 删除
            label: this.$t('common.delete'),
            type: 'danger',
            showFun: (index, row) => {
              if (this.$access('usersDeleted')) {
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
    rowClick(row, column, event) {
      this.$refs.table.clearSelection()
      this.$refs.table.toggleRowSelection(row)
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
    }
  }
}
</script>
<style lang="less" rel="stylesheet/less">
.table {
  height: 100%;
  .el-table__header-wrapper,
  .el-table__fixed-header-wrapper {
    thead {
      tr {
        th {
          color: #333333;
        }
      }
    }
  }
}
</style>
