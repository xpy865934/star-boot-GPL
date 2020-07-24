const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,

  // tagsView
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,

  // user
  userCode: state => state.user.userCode,
  userName: state => state.user.userName,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes
}
export default getters
