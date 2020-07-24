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

/**
* 校验只要是数字（包含正负整数，0以及正负浮点数）就返回true
**/

export function isNumber(val) {
  var regPos = /^\d+(\.\d+)?$/ // 非负浮点数
  var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/ // 负浮点数
  if (regPos.test(val) && regNeg.test(val)) {
    return true
  } else {
    return false
  }
}

/**
* 校验正负正数就返回true
**/

export function isIntNum(val) {
  var regPos = /^\d+$/ // 非负整数
  var regNeg = /^\-[1-9][0-9]*$/ // 负整数
  if (regPos.test(val) || regNeg.test(val)) {
    return true
  } else {
    return false
  }
}

/**
 * 格式化为百分比
 * @param {*}} val
 */
export function formatPercent(val) {
  return (Math.round(val * 10000) / 100).toFixed(2) + '%'
}

/**
 * 格式化为千分比
 * @param {*}} val
 */
export function formatThousandth(val) {
  return (Math.round(val * 100000) / 100).toFixed(2) + '‰'
}

/**
 * 小数转换为分数（或者比例）  会出现1/3 为3333/10000
 */
export function decimalConvert(d, intervalChar) {
  var has_string = d.toString().search(/[a-zA-Z]/)

  if (has_string !== -1) return '不合法的输入'
  if (d === parseInt(d)) return d + intervalChar + '1'

  var t = d.toString().includes('.') ? d.toString().replace(/\d+[.]/, '') : 0
  var b = Math.pow(10, t.toString().replace('-', '').length)

  if (d >= 1) t = +t + (Math.floor(d) * b)

  else if (d <= -1) t = +t + (Math.ceil(d) * b)

  var divisor = (function f(a, b) {
    return b ? f(b, a % b) : a
  })(t, b)

  var x = Math.abs(divisor)
  return (t / x) + intervalChar + (b / x)
}

/**
 * 小数转换为分数（或者比例）  不会出现小数比的情况
 */
export function appointment(a, b) {
  var e
  // 约分操作
  if (a === 0 || b === 1) return // 如果分子是0或分母是1就不用约分了
  e = gcd(a, b)
  a /= e
  b /= e
  return a + ':' + b
}

function gcd(a, b) {
  // 欧几里德算法
  return b === 0 ? a : gcd(b, a % b)
}

/**
 * 小数转换为分数（或者比例）  小数比的情况  取其中最小的数为1，最大的数除以小的数，保留两位
 */
export function decimalPercentConvert(a, b) {
  if (a === 0 || b === 0) {
    return
  }
  if (b === 1) {
    return a + ':' + b
  }
  if (a === b) {
    return '1:1'
  }
  if (a > b) {
    const c = (a / b).toFixed(1)
    return c + ':1'
  } else {
    const c = (b / a).toFixed(1)
    return '1:' + c
  }
}
