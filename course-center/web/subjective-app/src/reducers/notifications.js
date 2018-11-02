export default (state = [], action) => {
  switch (action.type) {
    case 'GET_NOTIFICATION_NO_READ':
      return action.notifications
    default:
      return state
  }
}
