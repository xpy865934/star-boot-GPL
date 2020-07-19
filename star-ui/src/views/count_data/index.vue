<template>
  <div class="app-container">
    <!-- 顶部搜索条件 -->
    <el-collapse v-model="activeNames">
      <el-collapse-item title="搜索条件" name="1">
        <el-form
          ref="searchForm"
          :model="searchForm"
          :size="this.$config.formSize"
          :inline="true"
          :label-position="this.$config.formLabelPosition"
          :label-width="this.$config.formLabelWidth"
        >
          <el-form-item
            label="上报日期"
            prop="contractCode"
          >
            <el-input
              v-model="searchForm.contractCode"
              placeholder=""
              :size="this.$config.formSize"
            />
          </el-form-item>
        </el-form>
        <el-row class="search-btn">
          <el-button type="primary" :size="this.$config.buttonSize" @click="ok">查询</el-button>
          <el-button type="primary" :size="this.$config.buttonSize" @click="reset">重置</el-button>
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
  data() {
    return {
      activeNames: ['1'],
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
          // 上报日期
          prop: 'sbsj',
          label: '上报日期',
          align: 'center'
        },
        {
          // 上报人
          prop: 'userName',
          label: '上报人',
          align: 'center'
        }
      ],
      // 操作列
      operates: {
        width: 300,
        fixed: 'right',
        list: [
          {
            // 查看
            label: '查看',
            type: 'primary',
            show: true,
            icon: 'el-icon-view',
            method: (index, row) => {
              // 查看
              this.editRow = row
              this.addRowVisible = true
              this.addRowDisabled = true
            }
          }
        ]
      }
    }
  },
  mounted() {
    // 获取表格数据
    this.getPagerData(
      this.pagination.pageIndex,
      this.pagination.pageSize,
      this.searchForm
    )
  },
  methods: {
    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.$message({
        message: 'cancel!',
        type: 'warning'
      })
    },
    ok() {
      this.getPagerData(
        this.pagination.pageIndex,
        this.pagination.pageSize,
        this.searchForm
      )
    },
    reset() {
      // 需要将表单searchForm置为空，否则会出现，点击重置之后部分其他输入框无法输入，输入之前的输入框是，值又显示出来  参考：https://blog.csdn.net/nidunlove/article/details/100975379
      this.searchForm = {}
      this.$refs['searchForm'].resetFields()
    },
    /**
     * 获取表格分页数据
     */
    getPagerData(current, size, bean) {
      const params = {
        current: current,
        size: size,
        bean: bean
      }
      this.$store
        .dispatch('uploadData/queryPager', params)
        .then(res => {
          this.pagination.pageIndex = res.current
          this.pagination.pageSize = res.size
          this.total = res.total
          this.tableData = res.records
        })
        .catch(() => {})
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
      this.getPagerData(
        pagination.pageIndex,
        pagination.pageSize,
        this.searchForm
      )
    },
    /**
     * 选中
     */
    handleSelectionChange(val) {}
  }
}
</script>

<style scoped>
.search-btn{
  text-align: center
}
</style>

