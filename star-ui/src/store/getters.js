const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,

  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  name: state => state.user.name,
  userCode: state => state.user.userCode,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes
}
export default getters
