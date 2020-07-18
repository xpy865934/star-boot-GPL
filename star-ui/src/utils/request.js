import axios from 'axios'
import { Message, Loading } from 'element-ui'
import store from '@/store'
import router from '@/router'
import config from '@/config' // config
import { getToken } from '@/utils/auth'
import { getCurrentBrowser, getOs } from './common'

const request = {}

let loading // 定义loading变量

function startLoading() {
  // 使用Element loading-start 方法
  loading = Loading.service({
    lock: true,
    text: '数据加载中……',
    background: 'rgba(0, 0, 0, 0.3)'
  })
}
function endLoading() {
  // 使用Element loading-close 方法
  loading.close()
}

// 那么 showFullScreenLoading() tryHideFullScreenLoading() 要干的事儿就是将同一时刻的请求合并。
// 声明一个变量 needLoadingRequestCount，每次调用showFullScreenLoading方法 needLoadingRequestCount + 1。
// 调用tryHideFullScreenLoading()方法，needLoadingRequestCount - 1。needLoadingRequestCount为 0 时，结束 loading。
let needLoadingRequestCount = 0
export function showFullScreenLoading() {
  if (needLoadingRequestCount === 0) {
    startLoading()
  }
  needLoadingRequestCount++
}

export function tryHideFullScreenLoading() {
  if (needLoadingRequestCount <= 0) return
  needLoadingRequestCount--
  if (needLoadingRequestCount === 0) {
    endLoading()
  }
}

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // Do something before request is sent
    if (store.getters.token) {
      // 让每个请求携带token-- ['token']为自定义key 请根据实际情况自行修改
      config.headers['token'] = getToken()
      // 带上请求的浏览器和操作系统
      config.headers['client'] = 'client:' + getCurrentBrowser()
      config.headers['os'] = 'os:' + getOs()
    }
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过 xmlhttprequest 来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  response => {
    const res = response.data
    if (res.code === 10005) {
      Message({
        type: 'error',
        message: res.msg,
        duration: config.messageDuration
      })
      store.dispatch('LogOut').then(() => {
        router.push({ path: '/login' })
        // location.reload() // 为了重新实例化vue-router对象 避免bug
      })
      return Promise.reject('error')
    }
    if (res.code === 200) {
      // Message({
      //   type: 'success',
      //   message: res.msg,
      //   duration: config.messageDuration
      // })
      return res
    } else {
      // 用户无权限  10004
      // 用户名或者密码不正确 10000
      // 账户已被禁用 40001
      // 保存失败 40003
      // 修改失败 40004
      Message({
        type: 'error',
        message: res.msg,
        duration: config.messageDuration
      })
      return Promise.reject('error')
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: '网络错误，请联系系统管理员',
      type: 'error',
      duration: config.messageDuration
    })
    return Promise.reject(error)
  }
)

/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */
request.get = function get(url, params = {}, showLoading = false) {
  return new Promise((resolve, reject) => {
    showLoading && showFullScreenLoading() // 显示等待框

    service
      .get(url, {
        params: params
      })
      .then(response => {
        showLoading && tryHideFullScreenLoading() // 隐藏等待框

        resolve(response)
      })
      .catch(err => {
        showLoading && tryHideFullScreenLoading() // 隐藏等待框

        reject(err)
      })
  })
}

/**
 * 封装post请求
 * @param url
 * @param params
 * @returns {Promise}
 */

request.post = function post(url, params = {}, showLoading = false) {
  return new Promise((resolve, reject) => {
    showLoading && showFullScreenLoading() // 显示等待框

    service.post(url, params).then(
      response => {
        showLoading && tryHideFullScreenLoading() // 隐藏等待框

        resolve(response)
      },
      err => {
        showLoading && tryHideFullScreenLoading() // 隐藏等待框

        reject(err)
      }
    )
  })
}

/**
 * 封装patch请求
 * @param url
 * @param data
 * @returns {Promise}
 */

request.patch = function patch(url, data = {}) {
  return new Promise((resolve, reject) => {
    service.patch(url, data).then(
      response => {
        resolve(response)
      },
      err => {
        reject(err)
      }
    )
  })
}

/**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */

request.put = function put(url, data = {}) {
  return new Promise((resolve, reject) => {
    service.put(url, data).then(
      response => {
        resolve(response)
      },
      err => {
        reject(err)
      }
    )
  })
}
export default request
