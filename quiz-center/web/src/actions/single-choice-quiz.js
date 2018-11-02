import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import { message } from 'antd'

export const addQuiz = (quiz, callback) => {
  quiz = Object.assign({}, quiz, {type: 'SINGLE_CHOICE'})
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/basicQuizzes`, quiz)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('新增单选题成功')
        callback()
      }
    })()
  }
}

export const getQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/basicQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_SINGLE_CHOICE_QUIZ',
          data: res.body
        })
      }
    })()
  }
}

export const editQuiz = (quiz, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`./api/v3/basicQuizzes/${quiz.id}`, quiz)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('修改单选题成功')
        callback()
      }
    })()
  }
}

export const getQuizzes = (page = 1, searchType = '', searchContent = '') => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/basicQuizzes?type=SINGLE_CHOICE&page=${page}&${searchType}=${searchContent}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_SINGLE_CHOICE_QUIZZES',
          data: res.body
        })
      }
    })()
  }
}

export const deleteQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/basicQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('删除成功')
        dispatch(getQuizzes())
      }
    })()
  }
}
