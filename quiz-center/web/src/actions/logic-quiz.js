import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import { message } from 'antd'

export const getLogicQuizzes = (page = 1, searchType = '', searchContent = '') => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/logicQuizzes?page=${page}&${searchType}=${searchContent}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_LOGIC_QUIZZES',
          data: res.body
        })
      }
    })()
  }
}

export const addLogicQuiz = (quiz, callback) => {
  console.log(callback)
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/logicQuizzes`, quiz)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('批量创建逻辑题成功')
        callback()
      }
    })()
  }
}

export const deleteQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/logicQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('删除成功')
        dispatch(getLogicQuizzes())
      }
    })()
  }
}
