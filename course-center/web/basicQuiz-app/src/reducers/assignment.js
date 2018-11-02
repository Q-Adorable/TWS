export default (state = {basicQuizzes: [], reviewQuiz: {}, student: {}}, action) => {
  switch (action.type) {
    case 'ASSIGNMENT_BASIC_QUIZZES':
      return Object.assign({}, state, {basicQuizzes: action.quizzes})
    case 'ASSIGNMENT_REVIEW':
      return Object.assign({}, state, {reviewQuiz: action.review})
    case 'GET_STUDENT':
      return Object.assign({}, state, {student: action.student})
    default:
      return state
  }
}
