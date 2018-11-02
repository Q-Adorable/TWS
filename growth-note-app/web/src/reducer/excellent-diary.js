export default (state = {}, action) => {
  switch (action.type) {
    case 'REFRESH_EXCELLENT_DIARY':
      return action.excellentDiaries
    default:
      return state
  }
}
