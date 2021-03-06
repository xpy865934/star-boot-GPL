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
 * ??????????????????????????????
 * @param url
 * @param tid
 */
export function getFileUrl(url, fileId) {
  return (
    process.env.VUE_APP_BASE_API +
           url +
           '?fileId=' +
           fileId +
           '&token=' +
           getToken() +
           '&client=client:' +
           getCurrentBrowser() +
           '&os=os:' +
           getOs()
  )
}

/**
 * ??????????????????
 * @param url
 * @param tid
 */
export function getExportUrl(url, params) {
  return (
    process.env.VUE_APP_BASE_API +
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
 * ?????????????????????????????????key?????????  ????????????????????????text ??? value
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
* ?????????????????????????????????????????????0?????????????????????????????????true
**/

export function isNumber(val) {
  var regPos = /^\d+(\.\d+)?$/ // ???????????????
  var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/ // ????????????
  if (regPos.test(val) && regNeg.test(val)) {
    return true
  } else {
    return false
  }
}

/**
* ???????????????????????????true
**/

export function isIntNum(val) {
  var regPos = /^\d+$/ // ????????????
  var regNeg = /^\-[1-9][0-9]*$/ // ?????????
  if (regPos.test(val) || regNeg.test(val)) {
    return true
  } else {
    return false
  }
}

/**
 * ?????????????????????
 * @param {*}} val
 */
export function formatPercent(val) {
  return (Math.round(val * 10000) / 100).toFixed(2) + '%'
}

/**
 * ?????????????????????
 * @param {*}} val
 */
export function formatThousandth(val) {
  return (Math.round(val * 100000) / 100).toFixed(2) + '???'
}

/**
 * ???????????????????????????????????????  ?????????1/3 ???3333/10000
 */
export function decimalConvert(d, intervalChar) {
  var has_string = d.toString().search(/[a-zA-Z]/)

  if (has_string !== -1) return '??????????????????'
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
 * ???????????????????????????????????????  ??????????????????????????????
 */
export function appointment(a, b) {
  var e
  // ????????????
  if (a === 0 || b === 1) return // ???????????????0????????????1??????????????????
  e = gcd(a, b)
  a /= e
  b /= e
  return a + ':' + b
}

function gcd(a, b) {
  // ??????????????????
  return b === 0 ? a : gcd(b, a % b)
}

/**
 * ???????????????????????????????????????  ??????????????????  ????????????????????????1?????????????????????????????????????????????
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

export function translateFirstDict(firstDict, value) {
  let result = ''
  for (let i = 0; i < firstDict.length; i++) {
    if (firstDict[i].firstDictCode === value) {
      result = firstDict[i].firstDictName
      break
    }
  }
  return result
}

export function translateUserName(userList, value) {
  let result = ''
  for (let i = 0; i < userList.length; i++) {
    if (userList[i].userId === value) {
      result = userList[i].userName
      break
    }
  }
  return result
}

export function uuid() {
  var s = []
  var hexDigits = '0123456789abcdef'
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
  }
  s[14] = '4' // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1) // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = '-'

  var uuid = s.join('')
  return uuid
}
