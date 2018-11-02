import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'

export const refreshNotificationUnRead = notifications => ({
  type: 'GET_NOTIFICATION_NO_READ',
  notifications
})

export const getNotificationUnRead = () => {
  return dispatch => {
    (async () => {
      const res = await request.get('../api/notifications')
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshNotificationUnRead(res.body))
      }
    })()
  }
}

export const updateUnReadToReadById = (notificationId, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.update('../api/notifications/' + notificationId)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        callback()
      }
    })()
  }
}

export const addNotification = (data) => {
  return dispatch => {
    (async () => {
      await request.post('../api/notifications/', data)
    })()
  }
}
