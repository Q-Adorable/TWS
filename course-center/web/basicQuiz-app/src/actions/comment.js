import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import {message} from 'antd'

export const refreshSectionComments = comments => ({
  type: 'REFRESH_COMMENTS',
  comments
})

export const getSectionComments = (assignmentId, studentId, quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/students/${studentId}/assignments/${assignmentId}/quizzes/${quizId}/suggestions`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshSectionComments(res.body))
      }
    })()
  }
}

export const studentGetSectionComments = (assignmentId, quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes/${quizId}/suggestions`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshSectionComments(res.body))
      }
    })()
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
        dispatch(getSectionComments(assignmentId, studentId, quizId))
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
          dispatch(getSectionComments(assignmentId, toUserId, quizId))
        }else{
          dispatch(studentGetSectionComments(assignmentId, quizId))
        }
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
          dispatch(getSectionComments(assignmentId, studentId, quizId))
        }else{
          dispatch(studentGetSectionComments(assignmentId, quizId))
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
          dispatch(getSectionComments(assignmentId, studentId, quizId))
        }else{
          dispatch(studentGetSectionComments(assignmentId, quizId))
        }
      }
    })()
  }
}
