<template>
  <div class="app-container table">
    <el-button type="primary" size="mini" @click="add">新增</el-button>
    <el-table
      ref="table"
      :data="tableData"
      row-key="resourcesId"
      :stripe="true"
      :highlight-current-row="true"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      @selection-change="handleSelectionChange"
      @row-click="rowClick"
      @select="handleSelection"
    >
      <el-table-column type="selection" style="width: 55px;" />
      <el-table-column label="菜单编号" width="200" align="center">
        <template slot-scope="scope">{{ scope.row.resourcesNum }}</template>
      </el-table-column>
      <el-table-column label="菜单名称" width="200" align="center">
        <template slot-scope="scope">{{ scope.row.resourcesName }}</template>
      </el-table-column>
      <el-table-column label="上级菜单" width="200" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.parentResourcesNum==null?'':`${scope.row.parentResourcesName}(${ scope.row.parentResourcesNum})` }}</template>
      </el-table-column>
      <el-table-column label="图标" width="100" align="center" />
      <el-table-column label="类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.resourcesType==1" key="菜单" type="">菜单</el-tag>
          <el-tag v-else key="按钮" type="success">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="排序号" width="100" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.PXH }}</template>
      </el-table-column>
      <el-table-column label="是否启用" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-if="scope.row.resourcesType==1"
            v-model="scope.row.used"
            :active-value="1"
            :inactive-value="0"
            @change="onEnable($event,scope.row )"
          />
          <el-switch
            v-else
            v-model="scope.row.used"
            :active-value="1"
            :inactive-value="0"
            disabled
            @change="onEnable($event,scope.row )"
          />
        </template>
      </el-table-column>
      <el-table-column label="资源代码" align="center">
        <template
          slot-scope="scope"
        >{{ scope.row.resourcesCode }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template
          slot-scope="scope"
        >
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            circle
            @click.native.prevent="modify(scope.row)"
          />
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            circle
            @click.native.prevent="deleteData(scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>
    <resources-window ref="resourcesWindow" :row="editRow" :add-row-visible="addRowVisible" :disabled="addRowDisabled" @update="addRowUpdate(arguments)" />
  </div>
</template>

<script>
import resourcesWindow from './window'
import { mapState } from 'vuex'
export default {
  name: 'Resources',
  components: { resourcesWindow },
  data() {
    return {
      // 表格数据
      tableData: [],
      urlList: {
        queryList: 'resources/queryList',
        updateUsed: 'resources/updateUsed',
        deleteById: 'resources/deleteById',
        update: 'resources/update',
        save: 'resources/save'
      },
      selects: [],
      // 用户控制新增修改
      editRow: {},
      // 显示新增修改对话框
      addRowVisible: false,
      // 控制表单是否可以操作
      addRowDisabled: false
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
    this.getPagerData()
  },
  created() {

  },
  destroyed() {

  },
  methods: {
    /**
      * 点击选择框事件
      */
    handleSelection(selection, row) {
      let isExist = false
      for (let i = 0; i < selection.length; i++) {
        if (row.resourcesId === selection[i].resourcesId) {
          isExist = true
          break
        }
      }
      if (isExist) {
        this.$refs.table.clearSelection()
        this.$refs.table.toggleRowSelection(row)
      } else {
        this.$refs.table.clearSelection()
        this.$refs.table.toggleRowSelection(row, false)
      }
    },
    handleSelectionChange(selection) {
      // 此处如果点复选框，再次点其他行复选框会导致selection里面有两条数据，暂时是取最后一条数据
      this.selects = []
      if (selection && selection.length > 0) {
        this.selects.push(selection[selection.length - 1])
      }
    },
    /**
      * 行点击事件
      */
    rowClick(row, column, event) {
      this.$refs.table.clearSelection()
      this.$refs.table.toggleRowSelection(row)
    },
    onEnable(event, row) {
      this.$store.dispatch(this.urlList.updateUsed, row).then((res) => {
        this.getPagerData()
      }).catch(() => {
      })
    },
    add() {
      // 获取选中的
      if (this.selects && this.selects.length > 0) {
        // 如果是按钮，按钮不可以再次添加
        if (this.selects[0].resourcesType === 1) {
          this.editRow = {
            parentResourcesNum: this.selects[0].resourcesNum,
            companyId: this.user.companyId
          }
        } else {
          this.$message({
            type: 'error',
            message: '按钮菜单不可再次增加子菜单'
          })
          return
        }
      } else {
        this.editRow = {
          companyId: this.user.companyId
        }
      }
      this.addRowVisible = true
      this.addRowDisabled = false
    },
    modify(row) {
      this.editRow = row
      this.addRowVisible = true
      this.addRowDisabled = false
    },
    /**
     * 获取表格分页数据
     */
    getPagerData(current, size, bean) {
      const params = {}
      this.$store.dispatch(this.urlList.queryList, params).then((res) => {
        this.tableData = res
      }).catch(() => {
      })
    },
    /**
     * 删除资源数据
     */
    deleteData(row) {
      this.$confirm('删除后下级数据会一并删除，确认删除该资源数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch(this.urlList.deleteById, row.resourcesId).then((res) => {
          this.getPagerData()
        }).catch(() => {
        })
      }).catch(() => {
      })
    },
    addRowUpdate(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        if (bean.resourcesId) {
          // 修改
          this.$store.dispatch(this.urlList.update, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisible = val[0]
            // 清空
            this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.resourcesWindow.clearFields()
            // 修改成功
            this.$message({
              type: 'success',
              message: this.$t('common.editSuccess')
            })
            // 刷新数据
            this.getPagerData()
          }).catch(() => {
          })
        } else {
          // 新增
          this.$store.dispatch(this.urlList.save, bean).then((res) => {
            // 第一个参数是显不显示对话框
            this.addRowVisible = val[0]
            // 清空
            this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
            this.$refs.resourcesWindow.clearFields()
            // 新增成功
            this.$message({
              type: 'success',
              message: this.$t('common.addSuccess')
            })
            // 刷新数据
            this.getPagerData()
          }).catch((err) => {
            console.log(err)
          })
        }
      } else {
        // 第一个参数是显不显示对话框
        this.addRowVisible = val[0]
        // 清空
        this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.resourcesWindow.clearFields()
      }
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
