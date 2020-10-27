<!--region 封装的分页 table-->
<template>
  <div class="table">
    <el-table
      id="xTable"
      ref="mutipleTable"
      v-loading.xTable="options.loading"
      :data="list"
      :max-height="height"
      :stripe="options.stripe"
      :border="options.border"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
    >
      <!--region 选择框-->
      <el-table-column v-if="options.mutiSelect" type="selection" style="width: 55px;" />
      <!--endregion-->
      <!--region 序号列-->
      <el-table-column
        v-if="options.showIndex"
        type="index"
        label="序号"
        :align="columns[0].align"
        width="55px;"
      />
      <!--endregion-->
      <!--region 数据列-->
      <!-- 流程列 -->
      <template v-if="options.isFlow">
        <template v-for="(column, index) in flowColumns">
          <el-table-column
            :key="index"
            :prop="column.prop"
            :label="column.label"
            :align="column.align"
            :show-overflow-tooltip="column.showOverflowTooltip === undefined ? true : column.showOverflowTooltip"
            :width="column.width"
            :min-width="column.minWidth"
          >
            <template slot-scope="scope">
              <template v-if="!column.render">
                <template v-if="column.formatter">
                  <span>{{ column.formatter(scope.row, column) }}</span>
                </template>
                <template v-else>
                  <span>{{ scope.row[column.prop] }}</span>
                </template>
              </template>
              <template v-else>
                <expand-dom :column="column" :row="scope.row" :render="column.render" :index="index" />
              </template>
            </template>
          </el-table-column>
        </template>
      </template>
      <template v-for="(column, index) in columns">
        <el-table-column
          :key="index + 1"
          :prop="column.prop"
          :label="column.label"
          :align="column.align"
          :show-overflow-tooltip="column.showOverflowTooltip === undefined ? true : column.showOverflowTooltip"
          :width="column.width"
          :min-width="column.minWidth"
        >
          <template slot-scope="scope">
            <template v-if="!column.render">
              <template v-if="column.formatter">
                <span>{{ column.formatter(scope.row, column) }}</span>
              </template>
              <template v-else>
                <span>{{ scope.row[column.prop] }}</span>
              </template>
            </template>
            <template v-else>
              <expand-dom :column="column" :row="scope.row" :render="column.render" :index="index" />
            </template>
          </template>
        </el-table-column>
      </template>
      <!--endregion-->
      <!--region 按钮操作组-->
      <el-table-column
        v-if="operates.list.length > 0"
        ref="fixedColumn"
        label="操作"
        align="center"
        :width="operates.width"
        :fixed="operates.fixed"
      >
        <template slot-scope="scope">
          <div class="operate-group">
            <template v-for="(btn, key) in operates.list">
              <div v-if="btn.showFun && btn.showFun(key,scope.row)" :key="key" class="item">
                <el-button
                  v-if="btn.label"
                  :type="btn.type"
                  :icon="btn.icon"
                  :disabled="btn.disabled"
                  :plain="btn.plain"
                  :size="buttonSize"
                  @click.native.prevent="btn.method(key,scope.row)"
                >{{ btn.label }}</el-button>
                <el-button
                  v-else
                  :type="btn.type"
                  :icon="btn.icon"
                  :disabled="btn.disabled"
                  :plain="btn.plain"
                  :size="buttonSize"
                  @click.native.prevent="btn.method(key,scope.row)"
                />
              </div>
              <div v-else-if="btn.show" :key="key" class="item">
                <el-button
                  v-if="btn.label"
                  :type="btn.type"
                  :icon="btn.icon"
                  :disabled="btn.disabled"
                  :plain="btn.plain"
                  :size="buttonSize"
                  @click.native.prevent="btn.method(key,scope.row)"
                >{{ btn.label }}</el-button>
                <el-button
                  v-else
                  :type="btn.type"
                  :icon="btn.icon"
                  :disabled="btn.disabled"
                  :plain="btn.plain"
                  :size="buttonSize"
                  @click.native.prevent="btn.method(key,scope.row)"
                />
              </div>
            </template>
          </div>
        </template>
      </el-table-column>
      <!--endregion-->
    </el-table>
    <div style="height:12px;" />
    <!--region 分页-->
    <div style="text-align:center">
      <el-pagination
        v-if="pagination"
        :page-size="tableCurrentPagination.pageSize"
        :page-sizes="tableCurrentPagination.pageArray"
        :current-page="tableCurrentPagination.pageIndex"
        layout="total,sizes, prev, pager, next,jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleIndexChange"
      />
    </div>
    <!--endregion-->
  </div>
</template>
<!--endregion-->
<script>
const _pageArray = [10, 20, 30, 40] // 每页展示条数的控制集合
export default {
  name: 'XTable',
  components: {
    expandDom: {
      functional: true,
      props: {
        row: Object,
        render: Function,
        index: Number,
        column: {
          type: Object,
          default: null
        }
      },
      render: (h, ctx) => {
        const params = {
          row: ctx.props.row,
          index: ctx.props.index
        }
        if (ctx.props.column) params.column = ctx.props.column
        return ctx.props.render(h, params)
      }
    }
  },
  props: {
    list: {
      type: Array,
      default: function() {
        return []
      }
    },
    // 数据列表
    columns: {
      type: Array,
      default: function() {
        return []
      } // 需要展示的列 === prop：列数据对应的属性，label：列名，align：对齐方式（left, center, right），width：列宽
    },
    operates: {
      type: Object,
      default: function() {
        return {}
      } // width:按钮列宽，fixed：是否固定（left,right）,按钮集合 === label: 文本，type :类型（primary / success / warning / danger / info / text），show：是否显示，icon：按钮图标，plain：是否朴素按钮，disabled：是否禁用，method：回调方法
    },
    total: {
      type: Number,
      default: 0
    }, // 总数
    pagination: {
      type: Object,
      default: null // 分页参数 === pageSize:每页展示的条数，pageIndex:当前页，pageArray: 每页展示条数的控制集合，默认 _page_array
    },
    otherHeight: {
      type: Number,
      default: 160
    }, // 计算表格的高度
    options: {
      type: Object,
      default: () => ({
        stripe: true, // 是否为斑马纹 table
        loading: false, // 是否添加表格loading加载动画
        highlightCurrentRow: true, // 是否支持当前行高亮显示
        mutiSelect: false, // 是否支持列表项选中功能
        showIndex: true, // 是否显示序号列
        border: false, // 是否显示边框
        isFlow: false
      })
    } // table 表格的控制参数
  },
  data() {
    return {
      pageIndex: 1,
      tableCurrentPagination: {},
      buttonSize: this.$config.buttonSize,
      multipleSelection: [], // 多行选中
      // 流程
      flowColumns: [
        {
          // TASK_NAMES
          prop: 'taskNames',
          label: this.$t('common.taskNames'),
          align: 'center',
          minWidth: 50
        }
      ]
    }
  },
  computed: {
    // 计算table高度
    height() {
      // return this.$utils.Common.getWidthHeight().height - this.otherHeight
      return '100%'
    }
  },
  created() {},
  mounted() {
    if (this.pagination && !this.pagination.pageSizes) {
      this.pagination.pageArray = _pageArray // 每页展示条数控制
    }
    this.tableCurrentPagination = this.pagination || {
      pageSize: this.total,
      pageIndex: 1
    } // 判断是否需要分页
  },
  methods: {
    // 切换每页显示的数量
    handleSizeChange(size) {
      if (this.pagination) {
        this.tableCurrentPagination = {
          pageIndex: 1,
          pageSize: size
        }
        this.$emit('handleSizeChange', this.tableCurrentPagination)
      }
    },
    // 切换页码
    handleIndexChange(currnet) {
      if (this.pagination) {
        this.tableCurrentPagination.pageIndex = currnet
        this.$emit('handleIndexChange', this.tableCurrentPagination)
      }
    },
    // 多行选中
    handleSelectionChange(val) {
      this.multipleSelection = val
      this.$emit('handleSelectionChange', val)
    },
    // 行点击
    handleRowClick(row, column, event) {
      this.$emit('handleRowClick', row, column, event)
    }
  }
}
</script>
<style lang="less" rel="stylesheet/less">
.table {
  height: 100%;
  .el-pagination {
    float: right;
    margin: 20px;
  }
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
  .el-table-column--selection .cell {
    padding: 0;
    text-align: center;
  }
  // 调整行
  .el-table__row td{
    padding: 8px 0;
  }
  .el-table__fixed-right {
    bottom: 0 !important;
    right: 6px !important;
    z-index: 1004;
  }
  .operate-group {
    display: flex;
    flex-wrap: wrap;
    .item {
      // margin-top: 4px;
      // margin-bottom: 4px;
      display: block;
      flex: 0 0 20%;
    }
  }
  .filter-data {
    top: e("calc((100% - 100px) / 3)");
    background-color: rgba(0, 0, 0, 0.7);
  }
  .table-action {
    top: e("calc((100% - 100px) / 2)");
    background-color: rgba(0, 0, 0, 0.7);
  }
  .fix-right {
    position: absolute;
    right: 0;
    height: 100px;
    color: #ffffff;
    width: 30px;
    display: block;
    z-index: 1005;
    writing-mode: vertical-rl;
    text-align: center;
    line-height: 28px;
    border-bottom-left-radius: 6px;
    border-top-left-radius: 6px;
    cursor: pointer;
  }
}
</style>
