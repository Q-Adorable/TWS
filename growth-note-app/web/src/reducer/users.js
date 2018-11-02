export default (state = [], action) => {
  switch (action.type) {
    case 'REFRESH_CONTACT_USERS':
      return action.users
    default:
      return state
  }
}
