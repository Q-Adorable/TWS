export default (state = {}, action) => {
  switch (action.type) {
    case 'REFRESH_PRACTISE_DIARY_COMMENT':
      return action.practiseDiaryAndComments
    default:
      return state
  }
}
