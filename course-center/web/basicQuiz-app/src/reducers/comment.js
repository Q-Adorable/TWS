export default (state = [], action) => {
  switch (action.type) {
    case 'REFRESH_COMMENTS':
      return action.comments
    default:
      return state
  }
}
