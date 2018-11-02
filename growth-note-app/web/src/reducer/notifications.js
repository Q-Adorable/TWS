export default (state = [], action) => {
  switch (action.type) {
    case 'REFRESH_NOTIFICATIONS':
      return action.notifications
    default:
      return state
  }
}
