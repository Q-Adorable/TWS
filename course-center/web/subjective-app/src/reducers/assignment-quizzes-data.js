export default (state = {quizzes: [], quiz: {}, myStudent: []}, action) => {
  switch (action.type) {
    case 'SECTION_QUIZZESS':
      return action.sectionQuizzess

    case 'ASSIGNMENT_QUIZ':
      return Object.assign({}, state, {quiz: action.quiz})

    case 'ASSIGNMENT_QUIZZES':
      return Object.assign({}, state, {quizzes: action.quizzes})

    case 'FOLLOW_STUDENT_PROGRAM':
      return Object.assign({}, state, {myStudent: action.myStudent})
    default:
      return state
  }
}
