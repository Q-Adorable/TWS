import * as request from '../constant/fetchRequest'
import * as types from '../constant/action-types'
import HTTP_CODE from '../constant/httpCode'
import { getNotificationUnRead } from './notification'

export const initUser = () => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(`./api/user`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(getNotificationUnRead())
        dispatch({
          type: types.INIT_USER,
          user: res.body
        })
      }
    })()
  }
}
export const getUserLikeUsername = username => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(`./api/users?username=` + username)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: types.REFRESH_CONTACT_USERS,
          users: res.body
        })
      }
    })()
  }
}
