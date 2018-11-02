import HTTP_CODE from '../constant/http-code'
import * as request from '../constant/fetch-request'

export const refreshTaskInfo = taskInfo => ({
  type: 'REFRESH_TASK',
  taskInfo
})

export const getTaskInfo = (taskId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/tasks/${taskId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshTaskInfo(res.body))
      }
    })()
  }
}
