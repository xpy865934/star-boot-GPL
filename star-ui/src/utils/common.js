// get brower
import { getToken } from './auth'

export function getCurrentBrowser() {
  const ua = navigator.userAgent.toLocaleLowerCase()
  let browserType = null
  if (ua.match(/msie/) != null || ua.match(/trident/) != null) {
    browserType = 'IE'
  } else if (ua.match(/firefox/) != null) {
    browserType = 'firefox'
  } else if (ua.match(/ucbrowser/) != null) {
    browserType = 'UC'
  } else if (ua.match(/opera/) != null || ua.match(/opr/) != null) {
    browserType = 'opera'
  } else if (ua.match(/bidubrowser/) != null) {
    browserType = 'baidu'
  } else if (ua.match(/metasr/) != null) {
    browserType = 'sougou'
  } else if (
    ua.match(/tencenttraveler/) != null ||
    ua.match(/qqbrowse/) != null
  ) {
    browserType = 'QQ'
  } else if (ua.match(/maxthon/) != null) {
    browserType = 'maxthon'
  } else if (ua.match(/chrome/) != null) {
    var is360 = _mime('type', 'application/vnd.chromium.remoting-viewer')
    if (is360) {
      browserType = '360'
    } else {
      browserType = 'chrome'
    }
  } else if (ua.match(/safari/) != null) {
    browserType = 'Safari'
  } else {
    browserType = 'others'
  }
  return browserType
}

function _mime(option, value) {
  var mimeTypes = navigator.mimeTypes
  for (var mt in mimeTypes) {
    if (mimeTypes[mt][option] === value) {
      return true
    }
  }
  return false
}

// get os
export function getOs() {
  const sUserAgent = navigator.userAgent.toLocaleLowerCase()
  const platform = navigator.platform.toLocaleLowerCase()
  const isWin = platform === 'win32' || platform === 'windows'
  const isMac =
    platform === 'mac68k' ||
    platform === 'macppc' ||
    platform === 'macintosh' ||
    platform === 'macintel'
  if (isMac) return 'Mac'
  var isUnix = platform === 'x11' && !isWin && !isMac
  if (isUnix) return 'Unix'
  var isLinux = String(platform).indexOf('linux') > -1
  if (isLinux) return 'Linux'
  if (isWin) {
    var isWin2K =
      sUserAgent.indexOf('windows nt 5.0') > -1 ||
      sUserAgent.indexOf('windows 2000') > -1
    if (isWin2K) return 'Win2000'
    var isWinXP =
      sUserAgent.indexOf('windows nt 5.1') > -1 ||
      sUserAgent.indexOf('windows xp') > -1
    if (isWinXP) return 'WinXP'
    var isWin2003 =
      sUserAgent.indexOf('windows nt 5.2') > -1 ||
      sUserAgent.indexOf('windows 2003') > -1
    if (isWin2003) return 'Win2003'
    var isWinVista =
      sUserAgent.indexOf('windows nt 6.0') > -1 ||
      sUserAgent.indexOf('windows vista') > -1
    if (isWinVista) return 'WinVista'
    var isWin7 =
      sUserAgent.indexOf('windows nt 6.1') > -1 ||
      sUserAgent.indexOf('windows 7') > -1
    if (isWin7) return 'Win7'
    var isWin10 = sUserAgent.indexOf('windows nt 10') !== -1
    if (isWin10) return 'Win10'
  }
  if (sUserAgent.indexOf('android') > -1) return 'Android'
  if (sUserAgent.indexOf('iphone') > -1) return 'iPhone'
  if (sUserAgent.indexOf('symbianos') > -1) return 'SymbianOS'
  if (sUserAgent.indexOf('windows phone') > -1) return 'Windows Phone'
  if (sUserAgent.indexOf('ipad') > -1) return 'iPad'
  if (sUserAgent.indexOf('ipod') > -1) return 'iPod'
  return 'others'
}

/**
 * 封装文件请求访问路径
 * @param url
 * @param tid
 */
export function getFileUrl(url, tid) {
  return (
    process.env.BASE_API +
    url +
    '?tid=' +
    tid +
    '&token=' +
    getToken() +
    '&client=client:' +
    getCurrentBrowser() +
    '%20%20os:' +
    getOs()
  )
}

/**
 * 封装导出路径
 * @param url
 * @param tid
 */
export function getExportUrl(url, params) {
  return (
    process.env.BASE_API +
    url +
    '?params=' +
    encodeURIComponent(JSON.stringify(params)) +
    '&token=' +
    getToken() +
    '&client=client:' +
    getCurrentBrowser() +
    '  os:' +
    getOs()
  )
}

// const generateNo = function() {
//   const now = new Date()
//   const year = now.getFullYear()
//   let month = now.getMonth() + 1
//   let day = now.getDate()
//   let hour = now.getHours()
//   let minutes = now.getMinutes()
//   let seconds = now.getSeconds()
//   String(month).length < 2 ? (month = Number('0' + month)) : month
//   String(day).length < 2 ? (day = Number('0' + day)) : day
//   String(hour).length < 2 ? (hour = Number('0' + hour)) : hour
//   String(minutes).length < 2 ? (minutes = Number('0' + minutes)) : minutes
//   String(seconds).length < 2 ? (seconds = Number('0' + seconds)) : seconds
//   const yyyyMMddHHmmss = `${year}${month}${day}${hour}${minutes}${seconds}`
//   return yyyyMMddHHmmss + '-' + Math.random().toString(36).substr(2, 9)
// }

/**
 * 获取某个数组中第一个该key值对象  数组中对象格式为text 、 value
 * @param {Object} array
 * @param {Object} key
 */
export function getArrayObject(array, key) {
  var result = {}
  for (var i = 0; i < array.length; i++) {
    // eslint-disable-next-line eqeqeq
    if (array[i].tid == key) {
      // eslint-disable-line
      result = array[i]
      break
    }
  }
  return result
}

export function formatNullToStr(obj) {
  if (isEmpty(obj)) {
    return ''
  }
  return obj
}

export function isEmpty(obj) {
  return obj === null || obj === 'null' || obj === undefined || obj === 'undefined' || obj.length === 0
}
