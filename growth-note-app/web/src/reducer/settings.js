const genSetting = data => {
  const { appContextPath } = data
  const userCenterHomeUrl = `${appContextPath}/home/index.html`
  const logoutUrl = `${appContextPath}/cas/logout`
  const notificationMoreUrl = `${appContextPath}/notification-center/index.html`
  return Object.assign(
    {},
    {
      userCenterHomeUrl,
      logoutUrl,
      notificationMoreUrl
    },
    data
  )
}

export default (state = {}, action) => {
  switch (action.type) {
    case 'GET_SETTINGS':
      return genSetting(action.data)
    default:
      return state
  }
}
