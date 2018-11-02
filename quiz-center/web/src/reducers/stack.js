const init = {
  totalElements: 0,
  content: []
}

export default (state = {stacks: init, stack: {id: -1}, status: false, logs: ''}, action) => {
  switch (action.type) {
    case 'ADD_STACK':
      const stack = Object.assign({}, state.stack, {id: action.data.id})
      return Object.assign({}, state, {status: action.data.status, stack})
    case 'GET_ALL_STACKS':
      const stacks = state.stacks
      const newStacks = Object.assign({}, stacks, {content: [...action.data]})

      return Object.assign({}, state, {stacks: {...newStacks}})

    case 'GET_ALL_STACKS_PAGE':
      return Object.assign({}, state, {stacks: {...action.data}})

    case 'GET_STACK_LOGS':
      return Object.assign({}, state, {logs: action.data})

    case 'DELETE_STACK':

      let status = Object.assign({}, {status: {...action.data}})
      return Object.assign({}, state, {...status})
    default:
      return state
  }
}
