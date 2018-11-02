const genSetting = (data) => {
  const { appContextPath } = data
  const userCenterHomeUrl = `${appContextPath}/home/index.html`
  const logoutUrl = `${appContextPath}/cas/logout`
  const notificationMoreUrl = `${appContextPath}/notification-center/index.html`
  const introduceUrl = `${appContextPath}/program-center/instructor/index.html#`
  const studentUrl = `${appContextPath}/program-center/student/index.html#`
  const studentHomeUrl = `${appContextPath}/program-center/student/index.html#`

  return Object.assign({}, {
    userCenterHomeUrl,
    logoutUrl,
    notificationMoreUrl,
    introduceUrl,
    studentUrl,
    studentHomeUrl
  }, data)
}

export default (state = {}, action) => {
  switch (action.type) {
    case 'GET_SETTINGS':
      return genSetting(action.data)
    default:
      return state
  }
}
