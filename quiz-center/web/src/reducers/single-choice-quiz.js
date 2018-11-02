const init = {
  totalElements: 0,
  content: []
}

export default (state = {quizzes: init, quiz: {}}, action) => {
  const quizzes = state.quizzes

  switch (action.type) {
    case 'ADD_SINGLE_CHOICE_QUIZ':
      return Object.assign({}, state, {quizzes: [...quizzes]})
    case 'GET_SINGLE_CHOICE_QUIZ':
      return Object.assign({}, state, {quiz: {...action.data}})
    case 'GET_SINGLE_CHOICE_QUIZZES':
      return Object.assign({}, state, {quizzes: {...action.data}})

    default:
      return state
  }
}
