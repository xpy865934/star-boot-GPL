<template>
  <div class="login-container">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="login-form" auto-complete="on" label-position="left" :size="$config.formSize">

      <div class="title-container">
        <h3 class="title">注册</h3>
      </div>

      <el-form-item prop="userName">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="userName"
          v-model="registerForm.userName"
          placeholder="医院名称(中文)"
          name="userName"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="userCode">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="userCode"
          v-model="registerForm.userCode"
          placeholder="用户名(医生姓名)"
          name="userCode"
          type="text"
          tabindex="2"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="tel">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="tel"
          v-model.number="registerForm.tel"
          placeholder="联系方式"
          name="tel"
          tabindex="3"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="registerForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="4"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item prop="passwordConfirm">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="passwordConfirm"
          v-model="registerForm.passwordConfirm"
          :type="passwordType"
          placeholder="确认密码"
          name="passwordConfirm"
          tabindex="5"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-row type="flex" justify="center">
        <el-col :span="6"><el-button :loading="loading" type="primary" :size="$config.buttonSize" @click.native.prevent="handleRegister">注册</el-button></el-col>
        <el-col :span="6" />
        <el-col :span="6"><el-button type="primary" plain :size="$config.buttonSize" @click.native.prevent="handleLogin">去登录</el-button></el-col>
      </el-row>

    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    /**
     判断确认密码是否一致
     */
    const validateConfirmPassword = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('确认密码不能为空'))
      } else {
        if (value === this.registerForm.password) {
          callback()
        } else {
          callback(new Error('密码不一致'))
        }
      }
    }
    return {
      registerForm: {
        userName: '',
        userCode: '',
        password: '',
        passwordConfirm: ''
      },
      registerRules: {
        userName: [{ required: true, trigger: 'blur', message: '医院名称(中文)不能为空' }],
        tel: [{ type: 'number', required: true, trigger: 'blur', message: '联系方式不能为空' }],
        userCode: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        passwordConfirm: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
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
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$router.push({ path: '/login' })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$set(this.registerForm, 'companyCode', this.$config.defaultCompanyCode)
          this.$set(this.registerForm, 'departmentCode', this.$config.defaultDepartmentCode)
          this.$store.dispatch('user/register', this.registerForm).then(() => {
            this.$router.push({ path: '/login' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 14px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
