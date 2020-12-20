const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,

  // tagsView
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,

  // user
  userId: state => state.user.userId,
  userCode: state => state.user.userCode,
  userName: state => state.user.userName,
  permissions: state => state.user.permissions,
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  msgUnReadCount: state => state.user.msgUnReadCount,

  // file
  uploadToken: state => state.user.uploadToken
}
export default getters
