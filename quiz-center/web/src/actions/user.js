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
      const res = await request.get('./api/users/' + 21)
      if (res.status === HTTP_CODE.OK) {
        dispatch(getUser(res.body))
        dispatch(getNotificationUnRead(res.body.id))
      }
    })()
  }
}

export const searchUser = (value) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/users?userName=${value}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'SEARCH_USERS',
          data: res.body
        })
      }
    })()
  }
}
