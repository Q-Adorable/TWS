import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import * as callback from '../components/online-language-quizzes/add-or-edit-online-language-quiz'
export const getQuizzes = (page = 1, searchType = '', searchContent = '') => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/onlineLanguageQuizzes?page=${page}&${searchType}=${searchContent}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ONLINE_LANGUAGE_QUIZZES',
          data: res.body
        })
      }
    })()
  }
}

export const addQuiz = (quiz) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/onlineLanguageQuizzes`, quiz)
      if (res.status === HTTP_CODE.CREATED) {
        dispatch({
          type: 'ADD_ONLINE_LANGUAGE_QUIZ',
          code: res.body.status,
          data: res.body.id
        })
      }
    })()
  }
}

export const getQuiz = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/onlineLanguageQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ONLINE_LANGUAGE_QUIZ',
          data: res.body
        })
      }
    })()
  }
}

export const editQuiz = (quiz, id) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`./api/v3/onlineLanguageQuizzes/${id}`, quiz)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch({
          type: 'EDIT_ONLINE_LANGUAGE_QUIZ',
          data: id
        })

      }
    })()
  }
}

export const getLogs = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/onlineLanguageQuizzes/${quizId}/status`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ONLINE_LANGUAGE_LOGS',
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
      const res = await request.del(`./api/v3/onlineLanguageQuizzes/${quizId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch(getQuizzes())
      }
    })()
  }
}

export const getLanguageTemplate = (language,callback) =>{
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/onlineLanguageQuizzes/template/${language}`)
      if(res.status === HTTP_CODE.OK){
        dispatch({
          type: 'GET_ONLINE_LANGUAGE_TEMPLATE',
          data: res.body
        })
        callback()
      }
    })()
  }
}

export const getAllLanguages = (callback) =>{
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/onlineLanguageQuizzes/languages`)
      if(res.status === HTTP_CODE.OK){
        dispatch({
          type: 'GET_ALL_LANGUAGE',
          data: res.body
        })
        callback()
      }
    })()
  }
}
