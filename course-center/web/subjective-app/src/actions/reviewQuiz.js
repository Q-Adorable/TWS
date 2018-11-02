import HTTP_CODE from '../constant/http-code'
import * as request from '../constant/fetch-request'
import {message} from 'antd/lib/index'
export const refreshAssignmentQuizReview = reviewQuiz => {
  return {
    type: 'QUIZ_REVIEW',
    reviewQuiz
  }
}

export const submitTutorReview = (reviewQuiz, changeStatus) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`../api/v2/review`, reviewQuiz)
      if (res.status === HTTP_CODE.CREATED) {
        changeStatus()
        dispatch(getReviewQuiz(res.body.id))
      }
    })()
  }
}
export const getReviewQuiz = (reviewId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/review/${reviewId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentQuizReview(res.body))
      }
    })()
  }
}

export const tutorGetStudentReviewQuiz = (studentId, assignmentId, quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/students/${studentId}/assignments/${assignmentId}/quizzes/${quizId}/review`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentQuizReview(res.body))
      }
    })()
  }
}

export const studentGetReviewQuiz = (assignmentId, quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes/${quizId}/review`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentQuizReview(res.body))
      }
    })()
  }
}

export const submitSupplementary = (id, supplement) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`../api/v2/review/${id}/supplement`, {supplement})
      if (res.status === HTTP_CODE.CREATED) {
        message.success('发送成功')
        dispatch(getReviewQuiz(id))
      }
    })()
  }
}
