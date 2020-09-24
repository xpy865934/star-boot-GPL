<template>
  <div class="app-container">
    <!-- 顶部搜索条件 -->
    <el-collapse v-model="activeNames">
      <el-collapse-item :title="$t('common.searchCondition')" name="1">
        <el-form ref="syslogSearchForm" :model="searchForm" :size="formSize" :inline="true" :label-position="formLabelPosition" :label-width="formLabelWidth">
          <el-form-item :label="$t('syslog.userCode')" prop="userCode">
            <el-input v-model="searchForm.userCode" :placeholder="$t('common.pleaseInput')+$t('syslog.userCode')" :size="formSize" />
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
  </div>
</template>

<script>
export default {
  name: 'SysLog',
  data() {
    return {
      urlList: {
        queryPager: 'syslog/queryPager'
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
          // userCode
          prop: 'userCode',
          label: this.$t('syslog.userCode'),
          align: 'center',
          minWidth: 80
        },
        {
          // methodDesc
          prop: 'methodDesc',
          label: this.$t('syslog.methodDesc'),
          align: 'center',
          minWidth: 80
        },
        {
          // requireMethod
          prop: 'requireMethod',
          label: this.$t('syslog.requireMethod'),
          align: 'center',
          minWidth: 80
        },
        {
          // ip
          prop: 'ip',
          label: this.$t('syslog.ip'),
          align: 'center',
          minWidth: 80
        },
        {
          // client
          prop: 'client',
          label: this.$t('syslog.client'),
          align: 'center',
          minWidth: 80
        },
        {
          // os
          prop: 'client',
          label: this.$t('syslog.os'),
          align: 'center',
          minWidth: 80
        },
        {
          // params
          prop: 'client',
          label: this.$t('syslog.params'),
          align: 'center',
          minWidth: 80
        }
      ],
      // 操作列
      operates: {
        width: 0,
        list: [
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
      this.$refs['syslogSearchForm'].resetFields()
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
    }
  }
}
</script>
