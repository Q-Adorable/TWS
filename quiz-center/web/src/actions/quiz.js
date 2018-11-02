import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'

export const getQuizzes = (page = 1, searchType = '', searchContent = '') => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/quizzes?page=${page}&${searchType}=${searchContent}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ALL_QUIZZES',
          data: res.body
        })
      }
    })()
  }
}
