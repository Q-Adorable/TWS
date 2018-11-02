export default (state = [], action) => {
  switch (action.type) {
    case 'SEARCH_TAGS':
      return [...action.data]
    default:
      return state
  }
}
