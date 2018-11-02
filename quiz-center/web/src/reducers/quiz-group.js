const init = {
  totalElements: 0,
  content: []
}
export default (state = {quizGroups: init, allMyQuizGroups: [], quizGroup: null, users: []}, action) => {

  switch (action.type) {
    case 'ADD_QUIZ_GROUP':
    case  'GET_QUIZ_GROUPS':
      return Object.assign({}, state, {quizGroups: action.data})
    case  'GET_QUIZ_GROUP':
      return Object.assign({}, state, {quizGroup: action.data})
    case 'GET_GROUP_USERS':
      return Object.assign({}, state, {users: action.data})
    case 'GET_ALL_QUIZ_GROUPS':
      return Object.assign({}, state, {allMyQuizGroups: action.data})
    default:
      return state
  }
}
