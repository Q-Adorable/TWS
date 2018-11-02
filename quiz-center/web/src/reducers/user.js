export default (state = {user: {}, users: []}, action) => {
  switch (action.type) {
    case 'GET_USER':
      return Object.assign({}, state, {user: {...action.user}})
    case 'SEARCH_USERS':
      return Object.assign({}, state, {users: [...action.data]})
    default:
      return state
  }
}
