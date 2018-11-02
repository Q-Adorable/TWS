import {combineReducers} from 'redux'
import taskInfo from './task'
import assignment from './assignment'
import user from './user'
import comments from './comment'
import settings from './settings'
import notifications from './notifications'

export default combineReducers({
  taskInfo,
  user,
  assignment,
  comments,
  settings,
  notifications
})
