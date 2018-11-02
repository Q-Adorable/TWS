const init = {
  reviewQuiz: {},
  suggestions: [],
  student: {}
}
export default (state = init, action) => {
  switch (action.type) {
    case 'QUIZ_REVIEW':
      return Object.assign({}, state, {reviewQuiz: action.reviewQuiz})
    case 'REFRESH_QUIZ_SUGGESTION':
      return Object.assign({}, state, {suggestions: action.quizSuggestions})
    case 'GET_MY_STUDENT':
      return Object.assign({}, state, {student: action.student})
    default:
      return state
  }
}
