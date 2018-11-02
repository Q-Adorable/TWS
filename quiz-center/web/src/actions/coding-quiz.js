import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'

export const getQuizzes = (page = 1, searchType = '', searchContent = '') => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/homeworkQuizzes?page=${page}&${searchType}=${searchContent}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_CODING_QUIZZES',
          data: res.body
        })
      }
    })()
  }
}

export const addQuiz = (quiz) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/single-stack-programming-quiz`, quiz)
      if (res.status === HTTP_CODE.CREATED) {
        dispatch({
          type: 'ADD_CODING_QUIZ',
          data: res.body.id
        })
      }
    })()
  }
}

export const getQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/homeworkQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_CODING_QUIZ',
          data: res.body
        })
      }
    })()
  }
}

export const editQuiz = (quiz, id) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`./api/v3/homeworkQuizzes/${id}`, quiz)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch({
          type: 'EDIT_CODING_QUIZ',
          data: id
        })
      }
    })()
  }
}

export const getLogs = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/homeworkQuizzes/${quizId}/status`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_CODING_LOGS',
          code: res.body.status,
          logs: res.body.logs
        })
      }
    })()
  }
}

export const deleteQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/homeworkQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch(getQuizzes())
      }
    })()
  }
}
