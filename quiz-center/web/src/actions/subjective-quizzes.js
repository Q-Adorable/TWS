import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import { message } from 'antd'

export const addQuiz = (quiz, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/subjectiveQuizzes`, quiz)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('新增主观题成功')
        callback()
        dispatch({
          type: 'ADD_SUBJECTIVE_QUIZ',
          data: true
        })
      }
    })()
  }
}

export const getQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/subjectiveQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_SUBJECTIVE_QUIZ',
          data: res.body
        })
      }
    })()
  }
}

export const editQuiz = (quiz, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`./api/v3/subjectiveQuizzes/${quiz.id}`, quiz)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('更新成功')
        callback()
        dispatch(getQuizzes())
      }
    })()
  }
}

export const deleteQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/subjectiveQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('删除成功')
        dispatch(getQuizzes())
      }
    })()
  }
}
export const getQuizzes = (page = 1, pageSize = 10, searchType = '', searchContent = '') => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/subjectiveQuizzes?page=${page}&pageSize=${pageSize}&${searchType}=${searchContent}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_SUBJECTIVE_QUIZZES',
          data: res.body
        })
      }
    })()
  }
}
