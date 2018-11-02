export default (state = [], action) => {
  switch (action.type) {
    case 'REFRESH_CONTACT_USER_DIARY_COMMENTS':
      return action.followeeDiariesAndComments
    default:
      return state
  }
}
