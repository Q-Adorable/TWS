const init = {
  definitions: [],
  assignments: [],
  task: {}
}
export default (state = init, action) => {
  switch (action.type) {
    case 'REFRESH_TASK':
      return action.taskInfo
    default:
      return state
  }
}
