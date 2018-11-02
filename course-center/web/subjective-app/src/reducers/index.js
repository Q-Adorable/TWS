import {combineReducers} from 'redux'
import taskInfo from './task'
import reviewQuizAndSuggestions from './assignment-review-quiz-comment'
import assignmentQuizzesData from './assignment-quizzes-data'
import user from './user'
import settings from './settings'
import notifications from './notifications'

export default combineReducers({
  taskInfo,
  reviewQuizAndSuggestions,
  assignmentQuizzesData,
  user,
  settings,
  notifications
})
