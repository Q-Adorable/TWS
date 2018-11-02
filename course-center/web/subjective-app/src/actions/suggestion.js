import HTTP_CODE from '../constant/http-code'
import * as request from '../constant/fetch-request'
import {message} from 'antd'

export const refreshQuizSuggestions = quizSuggestions => {
  return {
    type: 'REFRESH_QUIZ_SUGGESTION',
    quizSuggestions
  }
}

export const submitSuggestion = (toUserId, assignmentId, quizId, studentId, content, parentId) => {
  const data = {
    toUserId,
    assignmentId,
    content,
    quizId,
    parentId
  }
  return dispatch => {
    (async () => {
      const res = await request.post(`../api/v2/suggestions`, data)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('评价成功')
        dispatch(tutorGetQuizSuggestions(assignmentId, quizId, studentId))
      }
    })()
  }
}

export const submitReploySuggestion = (toUserId, assignmentId, quizId, content, parentId, isTutor) => {
  const data = {
    toUserId,
    assignmentId,
    content,
    quizId,
    parentId
  }
  return dispatch => {
    (async () => {
      const res = await request.post(`../api/v2/suggestions`, data)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('评价成功')
        if(isTutor){
          dispatch(tutorGetQuizSuggestions(assignmentId, quizId, toUserId))
        }else{
          dispatch(studentGetQuizSuggestions(assignmentId, quizId))
        }
      }
    })()
  }
}

export const studentGetQuizSuggestions = (assignmentId, quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes/${quizId}/suggestions`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshQuizSuggestions(res.body))
      }
    })()
  }
}

export const tutorGetQuizSuggestions = (assignmentId, quizId, studentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/students/${studentId}/assignments/${assignmentId}/quizzes/${quizId}/suggestions`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshQuizSuggestions(res.body))
      }
    })()
  }
}
export const modifyComment = (commentId, content, assignmentId, quizId, studentId, isTutor) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`../api/v2/suggestions/${commentId}`, {content})
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('修改成功')
        if(isTutor){
          dispatch(tutorGetQuizSuggestions(assignmentId, quizId, studentId))
        }else{
          dispatch(studentGetQuizSuggestions(assignmentId, quizId))
        }
      }
    })()
  }
}

export const deleteComment = (commentId, assignmentId, quizId, studentId, isTutor) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`../api/v2/suggestions/${commentId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        if(isTutor){
          dispatch(tutorGetQuizSuggestions(assignmentId, quizId, studentId))
        }else{
          dispatch(studentGetQuizSuggestions(assignmentId, quizId))
        }
      }
    })()
  }
}
