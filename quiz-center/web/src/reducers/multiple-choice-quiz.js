/* eslint-disable no-mixed-spaces-and-tabs */
let id = 2

const init = {
  totalElements: 0,
  content: []
}

export default (state = {quizzes: init, quiz: {}}, action) => {
  const multipleChoiceQuizzes = state.quizzes
  let quiz = null

  switch (action.type) {
    case 'ADD_MULTIPLE_QUIZ':
      quiz = Object.assign({}, action.quiz, {id})
      multipleChoiceQuizzes.list.push(quiz)
      id++

      return Object.assign({}, state, {quizzes: {...multipleChoiceQuizzes}})
    case 'GET_MULTIPLE_QUIZ':
      return Object.assign({}, state, {quiz: {...action.data}})
    case 'GET_ALL_MULTIPLE_QUIZ':
      return Object.assign({}, state, {quizzes: {...action.data}})
    default:
      return state
  }
}
