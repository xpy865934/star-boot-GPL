<template>
  <el-dialog class="xpy_dialog" :title="title" :visible="addRowVisible" :width="dialogWidth.small" :close-on-press-escape="dialogEsc" :close-on-click-modal="dialogClick" :before-close="beforClose">
    <el-form ref="roleResourcesForm" :rules="rules" :model="form" :label-position="formLabelPosition" :label-width="formLabelWidth" :disabled="disabled">
      <el-row>
        <el-col :span="6">
          <el-tree
            ref="resourcesTree"
            :data="resourcesData"
            show-checkbox
            default-expand-all
            :expand-on-click-node="false"
            node-key="resourcesId"
            highlight-current
            :props="defaultProps"
          />
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!disabled" slot="footer">
      <el-button @click="cancle">取 消</el-button>
      <el-button type="primary" @click="ok">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'RoleResourcesAdd',
  props: {
    row: {
      type: Object,
      default: function() {
        return {}
      }
    },
    addRowVisible: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    disabled: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    return {
      splice: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      resourcesData: [],
      // 表单form
      form: {},
      // 公共配置
      formSize: this.$config.formSize,
      buttonSize: this.$config.buttonSize,
      formFieldWidth: this.$config.formFieldWidth,
      formLabelWidth: this.$config.formLabelWidth,
      formLabelPosition: this.$config.formLabelPosition,
      dialogEsc: this.$config.dialogEsc,
      dialogClick: this.$config.dialogClick,
      dialogWidth: this.$config.dialogWidth,
      companyTid: '',
      rules: { }
    }
  },
  computed: {
    // 标题
    title: function() {
      return '资源设置'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    },
    row(v) {
      this.form = JSON.parse(JSON.stringify(v))
      if (this.form.roleId) {
        this.getResourcesTree(this.form.roleId)
      }
    }
  },
  mounted() {
  },
  created() {},
  destroyed() {},
  methods: {
    clearFields() {
      this.$refs['roleResourcesForm'].resetFields()
      // 设置选中的资源
      this.$refs.resourcesTree.setCheckedKeys([])
      this.resourcesData = []
    },
    cancle() {
      this.$emit('update', false)
    },
    ok() {
      let checked = this.$refs.resourcesTree.getCheckedKeys()
      let halfKeys = this.$refs.resourcesTree.getHalfCheckedKeys()
      if (checked.length === 0) {
        checked = []
      }
      if (halfKeys.length === 0) {
        halfKeys = []
      }
      for (let i = 0; i < halfKeys.length; i++) {
        checked.push(halfKeys[i])
      }
      this.$emit('update', false, checked, this.form.roleId)
    },
    /**
* 关闭之前的调用，需要更新父组件，进行关闭模态框
*/
    beforClose() {
      this.$emit('update', false)
    },
    getResourcesTree(roleId) {
      this.$store.dispatch('resources/queryList', {}).then((data) => {
        this.resourcesData = data
        this.getSelectedResources(roleId)
      }).catch(() => {
      })
    },
    getSelectedResources(roleId) {
      this.$store.dispatch('roles/getResourcesByRoleTid', { roleId: roleId }).then((data) => {
        // 获取选中的所有id
        const selected = []
        for (let i = 0; i < data.length; i++) {
          selected.push(data[i].resourcesId)
        }

        // 方法一
        selected.forEach((value) => {
        // 1. 勾选节点的 key 或者 data 2. boolean 类型，节点是否选中 3. boolean 类型，是否设置子节点 ，默认为 false）如果全部选中setCheckedKeys
          this.$refs.resourcesTree.setChecked(value, true, false)
        })

        // 方法二   getParent递归会导致页面加载很慢
        // 获取到所有的父级
        // this.splice = []
        // this.getParent(selected, this.resourcesData)
        // 删除父级并赋值给已选择的数据
        // const checked = this.spliceParent(this.splice, selected)
        // 设置选中的资源
        // this.$refs.resourcesTree.setCheckedKeys(checked)
      }).catch(() => {
      })
    },
    // 递归获取到所有的为父级的ID
    getParent(selected, resources) {
      selected.forEach((d, i) => {
        resources.forEach((item, index) => {
          if (item.tid === d && item.children && item.children.length) {
            if (!this.splice.includes(d)) {
              this.splice.push(d)
            }
          }
          if (item.children) {
            this.getParent(selected, item.children)
          }
        })
      })
    },
    // 删除父级项
    spliceParent(del, src) {
      del.forEach((item, index) => {
        const i = src.indexOf(item)
        src.splice(i, 1)
      })
      return src
    }
  }
}
</script>
