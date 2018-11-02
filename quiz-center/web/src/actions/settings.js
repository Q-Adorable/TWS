import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'

export const getSettings = data => ({
  type: 'GET_SETTINGS',
  data
})

export const initSettings = () => {
  return dispatch => {
    (async () => {
      const res = await request.get('./api/settings')
      if (res.status === HTTP_CODE.OK) {
        dispatch(getSettings(res.body))
      }
    })()
  }
}
