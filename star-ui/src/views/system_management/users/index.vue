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
  </div>
</template>

<script>
export default {
  name: 'Users',
  data() {
    return {
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
