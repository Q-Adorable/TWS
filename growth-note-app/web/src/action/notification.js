import * as request from '../constant/fetchRequest'
import HTTP_CODE from '../constant/httpCode'
import * as types from '../constant/action-types'

export const refreshNotificationUnRead = notifications => ({
  type: types.REFRESH_NOTIFICATIONS,
  notifications
})

export const getNotificationUnRead = () => {
  return dispatch => {
    ;(async () => {
      const res = await request.get('./api/notifications')
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshNotificationUnRead(res.body))
      }
    })()
  }
}

export const updateUnReadToReadById = (notificationId, callback) => {
  return dispatch => {
    ;(async () => {
      const res = await request.update('./api/notifications/' + notificationId)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        callback()
      }
    })()
  }
}
export const addNotification = data => {
  return dispatch => {
    ;(async () => {
      await request.post('./api/notifications/', data)
    })()
  }
}
