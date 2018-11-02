export default (state = {tags: {}, tag: null}, action) => {
  const tags = state.tags
  // let tag = {}

  switch (action.type) {
    case 'ADD_TAG':
      return Object.assign({}, state, {tags: [...tags]})
    case 'GET_TAG':
      return Object.assign({}, state, {tag: action.data})
    case 'EDIT_TAG':
      // tag = tags.find(tag => tag.id === action.tag.id)
      //
      // const index = tags.indexOf(tag)
      // tags[index] = action.tag

      return Object.assign({}, state, {tags: [...tags]})
    case 'GET_ALL_TAGS':
      return Object.assign({}, state, {tags: action.data})
    default:
      return state
  }
}
