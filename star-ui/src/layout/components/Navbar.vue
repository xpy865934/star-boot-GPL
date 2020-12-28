<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <span style="display:block;">{{ userName }} - {{ userCode }}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <el-dropdown-item divided>
            <!-- <el-badge :value="msgUnReadCount"> -->
            <span style="display:block;" @click="readMsg">{{ $t('navbar.message') }}</span>
            <!-- </el-badge> -->
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span style="display:block;" @click="changePassword">{{ $t('navbar.changePassword') }}</span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="logout">
            <span style="display:block;">{{ this.$t('common.logout') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 审核 -->
    <dialog-window
      ref="changePasswordWindow"
      current-window="changePasswordWindow"
      :row="editRow"
      :add-row-visible="addRowVisible"
      :disabled="addRowDisabled"
      :action="action"
      :width="dialogWidth.small"
      :body-scroll="false"
      :is-flow="false"
      @update="addRowUpdate(arguments)"
    />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import dialogWindow from '../../views/common/dialogWindow'
import { Notification } from 'element-ui'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    dialogWindow
  },
  data() {
    return {
      // 用户控制新增修改
      editRow: {},
      // 显示新增修改对话框
      addRowVisible: false,
      // 控制表单是否可以操作
      addRowDisabled: false,
      action: '',
      dialogWidth: this.$config.dialogWidth
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'userName',
      'userCode',
      'msgUnReadCount',
      'userId'
    ])
  },
  mounted() {
    this.openWebsocket()
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    /**
     * 显示消息框
     */
    readMsg() {
      this.$router.push({ path: '/message' })
    },
    changePassword() {
      this.editRow = {}
      this.action = 'changePassword'
      this.addRowVisible = true
      this.addRowDisabled = false
    },
    addRowUpdate(value) {
      // 将值拷贝一份，否则下方的更新可能会影响值
      const val = JSON.parse(JSON.stringify(value))
      // 第二个参数是传出来的值
      if (val[1]) {
        // 自己的处理逻辑
        const bean = val[1]
        // 修改
        this.$store.dispatch('user/changePassword', bean).then((res) => {
          // 第一个参数是显不显示对话框
          this.addRowVisible = val[0]
          // 清空
          this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
          this.$refs.changePasswordWindow.clearFields()
          // 修改成功
          this.$message({
            type: 'success',
            message: this.$t('common.changePasswordSuccess')
          })
          this.logout()
        }).catch(() => {
        })
      } else {
        // 第一个参数是显不显示对话框
        this.addRowVisible = val[0]
        // 清空
        this.editRow = {} // 需要置空，否则会导致页面清空，但是变量里面的数据还在
        this.$refs.changePasswordWindow.clearFields()
      }
    },
    openWebsocket() {
      var socket = null
      if (typeof (WebSocket) === 'undefined') {
        console.log('您的浏览器不支持WebSocket')
      } else {
        console.log('您的浏览器支持WebSocket')
        var url = process.env.VUE_APP_BASE_API.substring(5)
        socket = new WebSocket('ws:' + url + '/websocket/' + this.userId)
        // 打开
        socket.onopen = function() {
          console.log('已打开')
          socket.send('来自客户端的消息：' + new Date())
        }
        // 获的消息
        socket.onmessage = function(res) {
          console.log(res)
          const msg = JSON.parse(res.data)
          Notification({
            title: msg.title,
            message: msg.sysMessage,
            type: 'success'
          })
        }
        // 关闭
        socket.onclose = function() {
          console.log('socket已关闭')
        }
        // 发生错误
        socket.onerror = function() {
          console.log('socket发生错误')
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
