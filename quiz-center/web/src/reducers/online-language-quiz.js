const init = {
  totalElements: 0,
  content: []
}

const initStatus = {
  code: 1, logs: ''
}

export default (state = {quizzes: init, quiz: {}, id: 0, status: initStatus}, action) => {

  switch (action.type) {
    case 'ADD_ONLINE_LANGUAGE_QUIZ':
      let newStatus = state.status

      newStatus = Object.assign({}, state.status, {code: action.code})

      return Object.assign({}, state, {id: action.data, status: {...newStatus}})

    case 'EDIT_ONLINE_LANGUAGE_QUIZ':
      return Object.assign({}, state, {id: action.data, status: {...initStatus}})

    case 'GET_ONLINE_LANGUAGE_QUIZ':
      return Object.assign({}, state, {quiz: {...action.data}})

    case 'GET_ONLINE_LANGUAGE_QUIZZES':
      return Object.assign({}, state, {quizzes: {...action.data}})

    case 'GET_ONLINE_LANGUAGE_TEMPLATE':
      return Object.assign({},state, {initCode: {...action.data}})

    case 'GET_ALL_LANGUAGE':
      console.log("Object",Object.assign({},state, {languages: [...action.data]}))
      return Object.assign({},state, {languages: [...action.data]})
    default:
      return state
  }
}
