<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <!-- 顶部搜索条件 -->
        <el-collapse v-model="sjzdActiveNames">
          <el-collapse-item :title="$t('common.searchCondition')" name="1">
            <el-form ref="sjzdSearchForm" :model="sjzdSearchForm" :size="formSize" :inline="true" :label-position="formLabelPosition" :label-width="formLabelWidth">
              <el-form-item :label="$t('dict.dictCode')" prop="dictCode">
                <el-input v-model="sjzdSearchForm.dictCode" :placeholder="$t('common.pleaseInput')+$t('dict.dictCode')" :size="formSize" />
              </el-form-item>
              <el-form-item :label="$t('dict.dictName')" prop="dictName">
                <el-input v-model="sjzdSearchForm.dictName" :placeholder="$t('common.pleaseInput')+$t('dict.dictName')" :size="formSize" />
              </el-form-item>
            </el-form><el-row class="search-btn">
              <el-button :type="searchButton" :size="buttonSize" @click="sjzdOk">{{ $t('common.search') }}</el-button>
              <el-button :type="resetButton" :size="buttonSize" @click="sjzdReset">{{ $t('common.reset') }}</el-button>
            </el-row>
          </el-collapse-item>
          <el-row class="collapse-next-row">
            <el-button :type="addButton" :size="buttonSize" @click="sjzdAdd">{{ $t('common.newAdd') }}</el-button>
          </el-row>
        </el-collapse>
        <el-card shadow="always">
          <x-table
            id="sjzdXTable"
            :total="sjzdTotal"
            :options="sjzdOptions"
            :pagination="sjzdPagination"
            :columns="sjzdColumns"
            :operates="sjzdOperates"
            :list="sjzdTableData"
            @handleSizeChange="sjzdHandleSizeChange"
            @handleIndexChange="sjzdHandleIndexChange"
            @handleSelectionChange="sjzdHandleSelectionChange"
            @handleRowClick="sjzdHandleRowClick"
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <!-- 顶部搜索条件 -->
        <el-collapse v-model="firstDictActiveNames">
          <el-collapse-item :title="$t('common.searchCondition')" name="1">
            <el-form ref="firstDictSearchForm" :model="firstDictSearchForm" :size="formSize" :inline="true" :label-position="formLabelPosition" :label-width="formLabelWidth">
              <el-form-item :label="$t('dict.dictCode')" prop="firstDictCode">
                <el-input v-model="firstDictSearchForm.firstDictCode" :placeholder="$t('common.pleaseInput')+$t('dict.dictCode')" :size="formSize" />
              </el-form-item>
              <el-form-item :label="$t('dict.dictName')" prop="firstDictName">
                <el-input v-model="firstDictSearchForm.firstDictName" :placeholder="$t('common.pleaseInput')+$t('dict.dictName')" :size="formSize" />
              </el-form-item>
            </el-form><el-row class="search-btn">
              <el-button :type="searchButton" :size="buttonSize" @click="firstDictOk">{{ $t('common.search') }}</el-button>
              <el-button :type="resetButton" :size="buttonSize" @click="firstDictReset">{{ $t('common.reset') }}</el-button>
            </el-row>
          </el-collapse-item>
          <el-row class="collapse-next-row">
            <el-button :type="addButton" :size="buttonSize" @click="firstDictAdd">{{ $t('common.newAdd') }}</el-button>
          </el-row>
        </el-collapse>
        <el-card shadow="always">
          <x-table id="firstDictXTable" :total="firstDictTotal" :options="firstDictOptions" :pagination="firstDictPagination" :columns="firstDictColumns" :operates="firstDictOperates" :list="firstDictTableData" @handleSizeChange="firstDictHandleSizeChange" @handleIndexChange="firstDictHandleIndexChange" @handleSelectionChange="firstDictHandleSelectionChange" />
        </el-card>
      </el-col>
    </el-row>
    <sjzd-window ref="sjzdWindow" :row="sjzdEditRow" :add-row-visible="sjzdAddRowVisible" :disabled="sjzdAddRowDisabled" @update="sjzdAddRowUpdate(arguments)" />
    <first-dict-window ref="firstDictWindow" :row="firstDictEditRow" :add-row-visible="firstDictAddRowVisible" :disabled="firstDictAddRowDisabled" @update="firstDictAddRowUpdate(arguments)" />
  </div>
</template>

<script>
import sjzdWindow from './sjzdWindow'
import firstDictWindow from './firstDictWindow'
import { mapState } from 'vuex'
export default {
  name: 'Dict',
  components: { sjzdWindow, firstDictWindow },
  data() {
    return {
      urlList: {
        queryDictPager: 'dict/queryPager',
        saveDict: 'dict/save',
        updateDict: 'dict/update',
        queryFirstDictPager: 'dict/queryFirstDictPager',
        saveFirstDict: 'dict/saveFirstDict',
        updateFirstDict: 'dict/updateFirstDict'
      },
      currentSelectRow: {},
      // 数据字典相关
      sjzdActiveNames: ['1'],
      sjzdSearchForm: {},
      // 表格数据
      sjzdTableData: [],
      // table 的参数
      sjzdOptions: {
        stripe: true, // 是否为斑马纹 table
        loading: false, // 是否添加表格loading加载动画
        highlightCurrentRow: true, // 是否支持当前行高亮显示
        mutiSelect: false, // 是否支持列表项选中功能
        showIndex: true // 是否显示序号列
      },
      // 分页
      sjzdTotal: 0,
      // 分页参数
      sjzdPagination: {
        pageIndex: 1,
        pageSize: 10
      },

      // 表格列
      sjzdColumns: [
        {
          // dictCode
          prop: 'dictCode',
          label: this.$t('dict.dictCode'),
          align: 'center',
          minWidth: 80
        },
        {
          // dictName
          prop: 'dictName',
          label: this.$t('dict.dictName'),
          align: 'center',
          minWidth: 80
        }
      ],
      // 操作列
      sjzdOperates: {
        width: 300,
        fixed: 'right',
        list: [
          {
            // 查看
            label: this.$t('common.view'),
            type: 'primary',
            show: true,
            icon: 'el-icon-view',
            method: (index, row) => {
              // 查看
              this.sjzdEditRow = row
              this.sjzdAddRowVisible = true
              this.sjzdAddRowDisabled = true
            }
          },
          {
            // 编辑
            label: this.$t('common.edit'),
            type: 'primary',
            show: true,
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 编辑
              this.sjzdEditRow = row
              this.sjzdAddRowVisible = true
              this.sjzdAddRowDisabled = false
            }
          }
        ]
      },

      // 一级代码相关
      firstDictActiveNames: ['1'],
      firstDictSearchForm: {},
      // 表格数据
      firstDictTableData: [],
      // table 的参数
      firstDictOptions: {
        stripe: true, // 是否为斑马纹 table
        loading: false, // 是否添加表格loading加载动画
        highlightCurrentRow: true, // 是否支持当前行高亮显示
        mutiSelect: true, // 是否支持列表项选中功能
        showIndex: true // 是否显示序号列
      },
      // 分页
      firstDictTotal: 0,
      // 分页参数
      firstDictPagination: {
        pageIndex: 1,
        pageSize: 10
      },

      // 表格列
      firstDictColumns: [
        {
          // firstDictCode
          prop: 'firstDictCode',
          label: this.$t('dict.dictCode'),
          align: 'center',
          minWidth: 80
        },
        {
          // firstDictName
          prop: 'firstDictName',
          label: this.$t('dict.dictName'),
          align: 'center',
          minWidth: 80
        }
      ],
      // 操作列
      firstDictOperates: {
        width: 300,
        fixed: 'right',
        list: [
          {
            // 查看
            label: this.$t('common.view'),
            type: 'primary',
            show: true,
            icon: 'el-icon-view',
            method: (index, row) => {
              // 查看
              this.firstDictEditRow = row
              this.firstDictEditRow.dictId = this.currentSelectRow.dictId
              this.firstDictEditRow.dictCode = this.currentSelectRow.dictCode
              this.firstDictEditRow.dictName = this.currentSelectRow.dictName
              this.firstDictAddRowVisible = true
              this.firstDictAddRowDisabled = true
            }
          },
          {
            // 编辑
            label: this.$t('common.edit'),
            type: 'primary',
            show: true,
            icon: 'el-icon-edit',
            method: (index, row) => {
              // 编辑
              this.firstDictEditRow = row
              this.firstDictEditRow.dictId = this.currentSelectRow.dictId
              this.firstDictEditRow.dictCode = this.currentSelectRow.dictCode
              this.firstDictEditRow.dictName = this.currentSelectRow.dictName
              this.firstDictAddRowVisible = true
              this.firstDictAddRowDisabled = false
            }
          }
        ]
      },

      // 公共配置
      formSize: this.$config.formSize,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      buttonSize: this.$config.buttonSize,
      searchButton: this.$config.searchButton,
      resetButton: this.$config.resetButton,
      addButton: this.$config.addButton,

      // 数据字典新增
      // 用户控制新增修改
      sjzdEditRow: {},
      // 显示新增修改对话框
      sjzdAddRowVisible: false,
      // 控制表单是否可以操作
      sjzdAddRowDisabled: false,

      // 一级代码新增
      // 用户控制新增修改
      firstDictEditRow: {},
      // 显示新增修改对话框
      firstDictAddRowVisible: false,
      // 控制表单是否可以操作
      firstDictAddRowDisabled: false
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
    this.getSjzdPagerData(this.sjzdPagination.pageIndex, this.sjzdPagination.pageSize, this.sjzdSearchForm)
  },
  created() {
  },
  destroyed() {
  },
  methods: {
    /**
* 查询
*/
    sjzdOk() {
      this.getSjzdPagerData(this.sjzdPagination.pageIndex, this.sjzdPagination.pageSize, this.sjzdSearchForm)
    },
    /**
* 重置
*/
    sjzdReset() {
      // 需要将表单searchForm置为空，否则会出现，点击重置之后部分其他输入框无法输入，输入之前的输入框是，值又显示出来  参考：https://blog.csdn.net/nidunlove/article/details/100975379
      this.sjzdSearchForm = {}
      this.$refs['sjzdSearchForm'].resetFields()
    },
    /**
* 添加
*/
    sjzdAdd() {
      this.sjzdAddRowVisible = true
      this.sjzdAddRowDisabled = false
    },
    /**
* 切换每页显示的数量
*/
    sjzdHandleSizeChange(pagination) {
      // 切换每页显示条数时，重新从第一页开始
      this.getSjzdPagerData(1, pagination.pageSize, this.sjzdSearchForm)
    },
    /**
* 切换页码
*/
    sjzdHandleIndexChange(pagination) {
      this.getSjzdPagerData(pagination.pageIndex, pagination.pageSize, this.sjzdSearchForm)
    },
    /**
* 选中
*/
    sjzdHandleSelectionChange(val) {
    },
    /**
* 点击
*/
    sjzdHandleRowClick(row, column, event) {
      this.currentSelectRow = row
      this.$set(this.firstDictSearchForm, 'firstDictCode', '')
      this.$set(this.firstDictSearchForm, 'firstDictName', '')
      this.$set(this.firstDictSearchForm, 'dictId', row.dictId)
      this.getFirstDictPagerData(this.firstDictPagination.pageIndex, this.firstDictPagination.pageSize, this.firstDictSearchForm)
    },
    /**
* 查询
*/
    firstDictOk() {
      if (this.$isEmpty(this.currentSelectRow.dictId)) {
        this.$message({
          type: 'error',
          message: '请选择左侧的父级数据字典'
        })
        return
      }
      this.firstDictSearchForm.dictId = this.currentSelectRow.dictId
      this.getFirstDictPagerData(this.firstDictPagination.pageIndex, this.firstDictPagination.pageSize, this.firstDictSearchForm)
    },
    /**
* 重置
*/
    firstDictReset() {
      // 需要将表单searchForm置为空，否则会出现，点击重置之后部分其他输入框无法输入，输入之前的输入框是，值又显示出来  参考：https://blog.csdn.net/nidunlove/article/details/100975379
      this.firstDictSearchForm = {}
      this.$refs['firstDictSearchForm'].resetFields()
    },
    /**
* 添加
*/
    firstDictAdd() {
      if (this.$isEmpty(this.currentSelectRow.dictId)) {
        this.$message({
          type: 'error',
          message: '请选择左侧的父级数据字典'
        })
        return
      }
      this.firstDictEditRow = {
        dictId: this.currentSelectRow.dictId,
        dictCode: this.currentSelectRow.dictCode,
        dictName: this.currentSelectRow.dictName
      }
      this.firstDictAddRowVisible = true
      this.firstDictAddRowDisabled = false
    },
    /**
* 切换每页显示的数量
*/
    firstDictHandleSizeChange(pagination) {
      // 切换每页显示条数时，重新从第一页开始
      this.getFirstDictPagerData(1, pagination.pageSize, this.firstDictSearchForm)
    },
    /**
* 切换页码
*/
    firstDictHandleIndexChange(pagination) {
      this.getFirstDictPagerData(pagination.pageIndex, pagination.pageSize, this.firstDictSearchForm)
    },
    /**
* 选中
*/
    firstDictHandleSelectionChange(val) {
    },
    sjzdAddRowUpdate(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        if (bean.dictId) {
          // 修改
          this.$store.dispatch(this.urlList.updateDict, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.sjzdAddRowVisible = val[0]
            // 清空
            this.sjzdEditRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.sjzdWindow.clearFields()
            // 修改成功
            this.$message({
              type: 'success',
              message: this.$t('common.editSuccess')
            })
            // 刷新数据
            this.getSjzdPagerData(this.sjzdPagination.pageIndex, this.sjzdPagination.pageSize, this.sjzdSearchForm)
          }).catch(() => {
          })
        } else {
          // 新增
          this.$store.dispatch(this.urlList.saveDict, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.sjzdAddRowVisible = val[0]
            // 清空
            this.sjzdEditRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.sjzdWindow.clearFields()
            // 新增成功
            this.$message({
              type: 'success',
              message: this.$t('common.addSuccess')
            })
            // 刷新数据
            this.getSjzdPagerData(this.sjzdPagination.pageIndex, this.sjzdPagination.pageSize, this.sjzdSearchForm)
          }).catch((err) => {
            console.log(err)
          })
        }
      } else {
        // 第一个参数是显不显示对话框
        this.sjzdAddRowVisible = val[0]
        // 清空
        this.sjzdEditRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.sjzdWindow.clearFields()
      }
    },
    /**
* 获取表格分页数据
*/
    getSjzdPagerData(current, size, bean) {
      const params = {
        'current': current,
        'size': size,
        'bean': bean
      }
      this.$store.dispatch(this.urlList['queryDictPager'], params).then((res) => {
        this.sjzdPagination.pageIndex = res.current
        this.sjzdPagination.pageSize = res.size
        this.sjzdTotal = res.total
        this.sjzdTableData = res.records
      }).catch(() => {
      })
    },
    /**
* 获取表格分页数据
*/
    getFirstDictPagerData(current, size, bean) {
      const params = {
        'current': current,
        'size': size,
        'bean': bean
      }
      this.$store.dispatch(this.urlList['queryFirstDictPager'], params).then((res) => {
        this.firstDictPagination.pageIndex = res.current
        this.firstDictPagination.pageSize = res.size
        this.firstDictTotal = res.total
        this.firstDictTableData = res.records
      }).catch(() => {
      })
    },
    firstDictAddRowUpdate(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        if (bean.firstDictId) {
          // 修改
          this.$store.dispatch(this.urlList.updateFirstDict, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.firstDictAddRowVisible = val[0]
            // 清空
            this.firstDictEditRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.firstDictWindow.clearFields()
            // 修改成功
            this.$message({
              type: 'success',
              message: this.$t('common.editSuccess')
            })
            // 刷新数据
            this.getFirstDictPagerData(this.firstDictPagination.pageIndex, this.firstDictPagination.pageSize, this.firstDictSearchForm)
          }).catch(() => {
          })
        } else {
          // 新增
          this.$store.dispatch(this.urlList.saveFirstDict, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.firstDictAddRowVisible = val[0]
            // 清空
            this.firstDictEditRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.firstDictWindow.clearFields()
            // 新增成功
            this.$message({
              type: 'success',
              message: this.$t('common.addSuccess')
            })
            // 刷新数据
            this.getFirstDictPagerData(this.firstDictPagination.pageIndex, this.firstDictPagination.pageSize, this.firstDictSearchForm)
          }).catch((err) => {
            console.log(err)
          })
        }
      } else {
        // 第一个参数是显不显示对话框
        this.firstDictAddRowVisible = val[0]
        // 清空
        this.firstDictEditRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.firstDictWindow.clearFields()
      }
    }
  }
}
</script>
