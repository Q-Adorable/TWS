
export default (state = {logicQuiz: {}, logicQuizzes: {}}, action) => {
  switch (action.type) {
    case 'GET_LOGIC_QUIZZES':
      return Object.assign({}, state, {logicQuizzes: action.data})
    default:
      return state
  }
}
