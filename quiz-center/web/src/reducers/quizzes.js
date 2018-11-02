const init = {
  totalElements: 10,
  content: []}

export default (state = init, action) => {
  switch (action.type) {
    case 'GET_ALL_QUIZZES':
      return Object.assign({}, state, {...action.data})
    default:
      return state
  }
}
