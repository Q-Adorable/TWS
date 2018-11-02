import { combineReducers } from 'redux'
import userInfo from './user'
import quizzes from './quizzes'
import multipleChoice from './multiple-choice-quiz'
import subjective from './subjective-quiz'
import singleChoice from './single-choice-quiz'
import blank from './blank-quiz'
import logicQuizData from './logic-quiz'
import coding from './coding-quiz'
import onlineCoding from './online-coding-quiz'
import onlineLanguage from './online-language-quiz'
import tagData from './tag'
import stackData from './stack'
import quizGroupData from './quiz-group'
import searchTags from './search-tags'
import settings from './settings'
import notifications from './notifications'

export default combineReducers({
  userInfo,
  multipleChoice,
  notifications,
  quizzes,
  subjective,
  singleChoice,
  blank,
  tagData,
  logicQuizData,
  coding,
  quizGroupData,
  settings,
  onlineCoding,
  onlineLanguage,
  stackData,
  searchTags
})
