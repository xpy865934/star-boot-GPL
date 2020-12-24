<template>
  <div class="app-container">
    <!-- 顶部搜索条件 -->
    <el-collapse v-model="activeNames">
      <el-collapse-item :title="$t('common.searchCondition')" name="1">
        <el-form
          ref="searchForm"
          :model="searchForm"
          :size="formSize"
          :inline="true"
          :label-position="formLabelPosition"
          :label-width="formLabelWidth"
        >
          <el-form-item :label="$t('message.status')" prop="messageRead">
            <el-select
              v-model="searchForm.messageRead"
              filterable
              :placeholder="
                $t('common.pleaseSelect') +
                  $t('message.messageRead')
              "
              :size="formSize"
              :style="{ width: formFieldWidth }"
            >
              <el-option
                :key="0"
                label="未读"
                :value="0"
              />
              <el-option
                :key="1"
                label="已读"
                :value="1"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="$t('message.title')"
            prop="title"
          >
            <el-input
              v-model="searchForm.title"
              :placeholder="
                $t('common.pleaseInput') +
                  $t('message.title')
              "
              :size="formSize"
            />
          </el-form-item>
          <el-form-item
            :label="$t('message.sysMessage')"
            prop="sysMessage"
          >
            <el-input
              v-model="searchForm.sysMessage"
              :placeholder="
                $t('common.pleaseInput') + $t('message.sysMessage')
              "
              :size="formSize"
            />
          </el-form-item>
        </el-form>
        <el-row class="search-btn">
          <el-button :type="searchButton" :size="buttonSize" @click="ok">{{
            $t("common.search")
          }}</el-button>
          <el-button :type="resetButton" :size="buttonSize" @click="reset">{{
            $t("common.reset")
          }}</el-button>
          <el-button :type="searchButton" :size="buttonSize" @click="oneRead">{{
            $t("message.oneRead")
          }}</el-button>
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
import { mapState } from 'vuex'
export default {
  name: 'Message',
  components: {},
  data() {
    return {
      // 控制是否显示顶部搜索
      activeNames: ['1'],
      // 搜索form
      searchForm: {
        messageRead: 0
      },
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
      formFieldWidth: this.$config.formFieldWidth,
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
          // titile
          prop: 'title',
          label: this.$t('message.title'),
          align: 'center',
          minWidth: 80
        },
        {
          // secondTitle
          prop: 'secondTitle',
          label: this.$t('message.secondTitle'),
          align: 'center',
          minWidth: 80
        },
        {
          // fromId
          prop: 'fromId',
          label: this.$t('message.fromId'),
          align: 'center',
          minWidth: 60,
          render: (h, params) => {
            const _this = this
            return h('span', _this.$translateUserName(_this.userList, params.row.fromId))
          }
        },
        {
          // MEMBER_ID
          prop: 'memberId',
          label: this.$t('message.memberId'),
          align: 'center',
          minWidth: 60,
          render: (h, params) => {
            const _this = this
            return h('span', _this.$translateUserName(_this.userList, params.row.memberId))
          }
        },
        {
          // SYS_MESSAGE
          prop: 'sysMessage',
          label: this.$t('message.sysMessage'),
          align: 'center',
          minWidth: 80
        },
        {
          // 是否已读
          prop: 'messageRead',
          label: this.$t('message.status'),
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            return h('el-tag', {
              props: { type: params.row.messageRead === 1 ? 'success' : 'danger' } // 组件的props
            }, params.row.messageRead === 1 ? '已读' : '未读')
          }
        },
        {
          // 读取时间 READ_TIME
          prop: 'readTime',
          label: this.$t('message.readTime'),
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
            // 已读
            label: this.$t('message.messageRead'),
            type: 'primary',
            showFun: (index, row) => {
              return row.messageRead === 0
            },
            icon: 'el-icon-view',
            method: (index, row) => {
              // 已读
              this.$store.dispatch('message/upadteUserMessageRead', row).then(data => {
                this.$message({
                  type: 'success',
                  message: this.$t('common.success')
                })
                // 刷新数据
                this.getPagerData(this.pagination.pageIndex, this.pagination.pageSize, this.searchForm)
              }).catch(() => {
              })
            }
          }]
      }
    }
  },
  computed: {
    ...mapState({
      firstDict: (state) => state.app.firstDict,
      userList: (state) => {
        // 过滤userList里面userId重复
        const map = new Map()
        for (const item of state.app.userList) {
          if (!map.has(item.userId)) {
            map.set(item.userId, item)
          }
        }
        return [...map.values()]
      }
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
    this.getPagerData(
      this.pagination.pageIndex,
      this.pagination.pageSize,
      this.searchForm
    )
  },
  created() {},
  destroyed() {},
  methods: {
    /**
     * 查询
     */
    ok() {
      this.getPagerData(
        this.pagination.pageIndex,
        this.pagination.pageSize,
        this.searchForm
      )
    },
    /**
     * 重置
     */
    reset() {
      // 需要将表单searchForm置为空，否则会出现，点击重置之后部分其他输入框无法输入，输入之前的输入框是，值又显示出来  参考：https://blog.csdn.net/nidunlove/article/details/100975379
      this.searchForm = {
        messageRead: 0
      }
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
        .dispatch('message/getUserMessagePager', params)
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
    handleSelectionChange(val) {},
    /**
      * 一键已读
     */
    oneRead() {
      this.$store
        .dispatch('message/updateAllRead', {})
        .then(res => {
          this.$message({
            type: 'success',
            message: this.$t('common.success')
          })
          this.getPagerData(
            this.pagination.pageIndex,
            this.pagination.pageSize,
            this.searchForm
          )
        })
        .catch(() => {})
    }
  }
}
</script>
