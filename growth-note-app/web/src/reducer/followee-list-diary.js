export default (state = [], action) => {
  switch (action.type) {
    case 'REFRESH_PRACTISE_CONTACTS':
      return action.followeeListAndDiaries
    default:
      return state
  }
}
