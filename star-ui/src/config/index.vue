<script type="text/javascript">

// 自定义报表路径
const UREPORT_BASE_API = 'http://119.45.149.192:8006/ureport'

// 表单大小
const formSize = 'mini'
// 按钮大小
const buttonSize = 'mini'
// form 表单中的控件宽度
const formFieldWidth = '100%'
// form 表单中的label宽度
const formLabelWidth = '85px'
// form 表单中的位置
const formLabelPosition = 'right'
// 搜索按钮样式
const searchButton = 'primary'
// 重置按钮样式
const resetButton = ''
// 新增按钮样式
const addButton = 'primary'
// 保存按钮样式
const saveButton = 'primary'
// 删除按钮样式
const deletedButton = 'primary'
// 弹出框的默认宽度
const dialogWidth = {
  large: '70%',
  middle: '50%',
  small: '30%'
}
// esc关闭模态框
const dialogEsc = true
// 点击关闭模态框
const dialogClick = false
// 表格图标显示
const tableIcon = true
// 表格文字显示
const tableText = true
// message 关闭时间
const messageDuration = 1500

// 默认一家公司时的公司代码和部门代码
const defaultCompanyCode = '1000'
const defaultDepartmentCode = '1000-1'

// 分页默认属性
const pager = { page: '1', rows: 20, total: 0 }

// 流程状态常量
const PROCESS_STATE = {
  CREATE: '1',
  APPROVING: '2',
  COMPLETE: '3'
}

export default {
  formSize,
  buttonSize,
  formFieldWidth,
  formLabelWidth,
  formLabelPosition,
  searchButton,
  resetButton,
  addButton,
  saveButton,
  deletedButton,
  dialogWidth,
  dialogEsc,
  pager,
  dialogClick,
  messageDuration,
  UREPORT_BASE_API,
  defaultCompanyCode,
  defaultDepartmentCode,
  PROCESS_STATE,
  tableIcon,
  tableText
}
</script>
