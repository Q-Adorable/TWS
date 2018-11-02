import HTTP_CODE from '../constant/http-code'
import * as request from '../constant/fetch-request'
import { message } from 'antd'

export const refreshAssignmentBasicQuizzes = quizzes => {
  return {
    type: 'ASSIGNMENT_BASIC_QUIZZES',
    quizzes
  }
}

export const refreshBasicQuizzesReview = review => {
  return {
    type: 'ASSIGNMENT_REVIEW',
    review
  }
}

export const getMyStudentAssignment = (assignmentId, studentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/students/${studentId}/quizzes`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentBasicQuizzes(res.body))
      }
    })()
  }
}

export const getBasicQuizAssignment = (assignmentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentBasicQuizzes(res.body))
      }
    })()
  }
}

export const tutorGetStudentReviewQuiz = (studentId, assignmentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/students/${studentId}/assignments/${assignmentId}/quizzes/-1/review`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshBasicQuizzesReview(res.body))
      }
    })()
  }
}

export const getReviewQuiz = (assignmentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes/-1/review`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshBasicQuizzesReview(res.body))
      }
    })()
  }
}
export const submitBasicQuizAnswer = (answers, assignmentId) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`../api/v2/assignments/${assignmentId}/answers`, answers)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('提交成功')
        dispatch(getBasicQuizAssignment(assignmentId))
        dispatch(getReviewQuiz(assignmentId))
      }
    })()
  }
}
export const submitSupplementary = (id, supplement, assignmentId) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`../api/v2/review/${id}/supplement`, {supplement})
      if (res.status === HTTP_CODE.CREATED) {
        message.success('发送成功')
        dispatch(getReviewQuiz(assignmentId))
      }
    })()
  }
}
