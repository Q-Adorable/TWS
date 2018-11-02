const init = {
  totalElements: 0,
  content: []
}
export default (state = {quizzes: init, quiz: null, isCreateSuccess: false}, action) => {
  const quizzes = state.quizzes

  switch (action.type) {
    case 'ADD_SUBJECTIVE_QUIZ':
      return Object.assign({}, state, {quizzes: {...quizzes}})
    case 'GET_SUBJECTIVE_QUIZ':
      return Object.assign({}, state, {quiz: action.data})
    // case 'EDIT_SUBJECTIVE_QUIZ':

    // quiz = quizzes.list.find(quiz => quiz.id === action.quiz.id)
    // const index = quizzes.list.indexOf(quiz)
    //
    // quizzes.list[index] = action.quiz
    // return Object.assign({}, state, {quizzes: {...quizzes}})
    case 'GET_SUBJECTIVE_QUIZZES':
      return Object.assign({}, state, {quizzes: action.data})
    default:
      return state
  }
}
