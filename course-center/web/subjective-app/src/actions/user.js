import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import {getNotificationUnRead} from './notification'

export const getUser = user => ({
  type: 'GET_USER',
  user
})

export const initUser = () => {
  return dispatch => {
    (async () => {
      const res = await request.get('../api/users/' + 21)
      if (res.status === HTTP_CODE.OK) {
        dispatch(getNotificationUnRead())
        dispatch(getUser(res.body))
      }
    })()
  }
}

export const getMyFollowStudent = student => ({
  type: 'GET_MY_STUDENT',
  student
})

export const getStudent = (studentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get('../api/v2/students/' + studentId)
      if (res.status === HTTP_CODE.OK) {
        dispatch(getMyFollowStudent(res.body))
      }
    })()
  }
}
