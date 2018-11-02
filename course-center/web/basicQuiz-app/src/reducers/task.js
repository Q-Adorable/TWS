const taskInfo = {
  definitions: [],
  sectionList: [],
  task: {}
}
export default (state = taskInfo, action) => {
  switch (action.type) {
    case 'REFRESH_TASK':
      return Object.assign({}, state, {task: action.taskInfo})
    default:
      return state
  }
}
