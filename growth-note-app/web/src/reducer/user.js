import * as types from '../constant/action-types'

export default (state = {}, action) => {
  switch (action.type) {
    case types.INIT_USER:
      return action.user
    default:
      return state
  }
}
