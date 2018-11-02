import { combineReducers } from 'redux'

import practiseDiariesInfo from './practise-diary'
import diariesAndContactInfoList from './followee-list-diary'
import excellentDiariesInfo from './excellent-diary'
import contactUsers from './followee'
import contactUserDiariesAndComments from './followee-diary-comment'
import user from './user'
import settings from './settings'
import notifications from './notifications'
import users from './users'

export default combineReducers({
  user,
  practiseDiariesInfo,
  diariesAndContactInfoList,
  excellentDiariesInfo,
  contactUsers,
  contactUserDiariesAndComments,
  settings,
  notifications,
  users
})
