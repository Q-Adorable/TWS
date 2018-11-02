import * as request from '../constant/fetchRequest'
import * as types from '../constant/action-types'
import HTTP_CODE from '../constant/httpCode'
export const getUser = data => ({
  type: types.GET_SETTINGS,
  data
})

export const initSettings = () => {
  return dispatch => {
    ;(async () => {
      const res = await request.get('./api/settings')
      if (res.status === HTTP_CODE.OK) {
        dispatch(getUser(res.body))
      }
    })()
  }
}
