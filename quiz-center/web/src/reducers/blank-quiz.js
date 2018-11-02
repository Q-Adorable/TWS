const init = {
  content: [],
  totalElements: 0
}

export default (state = {quizzes: init, quiz: null}, action) => {
  let quizzes = state.quizzes
  let quiz = null

  switch (action.type) {
    case 'GET_ALL_BLANK_QUIZZES':
      return Object.assign({}, state, {quizzes: action.data})
    // case 'ADD_BLANK_QUIZ':
    // quizzes.list.push(Object.assign({}, action.quiz, {id}))
    // id++
    //
    // return Object.assign({}, state, {quizzes: {...quizzes}})
    case 'GET_BLANK_QUIZ':
      return Object.assign({}, state, {quiz: action.data})
    case 'EDIT_BLANK_QUIZ':
      quiz = quizzes.list.find(quiz => quiz.id === action.quiz.id)
      const index = quizzes.list.indexOf(quiz)

      quizzes.list[index] = action.quiz

      return Object.assign({}, state, {quizzes: {...quizzes}})
    default:
      return state
  }
}
