import HTTP_CODE from '../constant/http-code'
import * as request from '../constant/fetch-request'
import { message } from 'antd'
import { studentGetReviewQuiz } from './reviewQuiz'

export const refreshAssignmentQuiz = assignmentQuiz => {
  return {
    type: 'ASSIGNMENT_QUIZ',
    quiz: assignmentQuiz
  }
}

export const refreshAssignmentQuizzes = assignmentQuizzes => {
  return {
    type: 'ASSIGNMENT_QUIZZES',
    quizzes: assignmentQuizzes
  }
}
export const tutorFollowStudent = myStudent =>{
  return {
    type: 'FOLLOW_STUDENT_PROGRAM',
    myStudent: myStudent
  }
}

export const submitAnswer = (userAnswer, taskId, assignmentId, quizId, callback) => {
  const data = {
    userAnswer,
    type: 'SUBJECTIVE_QUIZ'
  }
  return dispatch => {
    (async () => {
      const res = await request.post(`../api/v2/subjectQuizzes/tasks/${taskId}/assignments/${assignmentId}/quizzes/${quizId}/submission`, data)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('提交成功')
        callback()
        dispatch(getAssignmentQuiz(assignmentId, quizId))
        dispatch(studentGetReviewQuiz(assignmentId, quizId))
      }
    })()
  }
}

export const getStudentAssignmentQuiz = (assignmentId, quizId, studentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/students/${studentId}/assignments/${assignmentId}/quizzes/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentQuiz(res.body))
      }
    })()
  }
}

export const getAssignmentQuiz = (assignmentId, quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentQuiz(res.body))
      }
    })()
  }
}

export const getAssignmentQuizzes = (assignmentId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/v2/assignments/${assignmentId}/quizzes`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshAssignmentQuizzes(res.body))
      }
    })()
  }
}
export const getTutorFollowStudent = (programId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`../api/myStudents/programs/${programId}`)
      if (res.status === HTTP_CODE.OK) {
        console.log('res.body',res.body)
        dispatch(tutorFollowStudent(res.body))
      }
    })()
  }
}
