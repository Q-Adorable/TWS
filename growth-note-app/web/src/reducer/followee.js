export default (state = [], action) => {
  switch (action.type) {
    case 'REFRESH_CONTACT_USER':
      return action.followees
    default:
      return state
  }
}
