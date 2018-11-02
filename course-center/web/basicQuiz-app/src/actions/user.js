import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import {getNotificationUnRead} from "./notification";

export const getUser = user => ({
  type: 'GET_USER',
  user
})

export const getMyStudent = student => ({
  type: 'GET_STUDENT',
  student
})

export const initUser = () => {
  return dispatch => {
    (async () => {
      const res = await request.get('../api/users/' + 26)
      if (res.status === HTTP_CODE.OK) {
        dispatch(getNotificationUnRead())
        dispatch(getUser(res.body))
      }
    })()
  }
}

export const getCurrentStudent = (studentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/students/${studentId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(getMyStudent(res.body))
      }
    })()
  }
}
