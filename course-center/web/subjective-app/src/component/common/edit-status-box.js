import React, { Component } from 'react'
import '../../less/index.less'
import { withRouter } from 'react-router-dom'
import { TwsEditAssignmentStatus, TwsShowAssignmentStatus } from 'tws-antd'
import { message } from 'antd'
import { TUTOR_MODIFY_ASSIGNMENT_STATUS } from '../../constant/notification-status'

class EditStatusBox extends Component {
  constructor () {
    super()
    this.state = {
      grade: 0,
      excellence: 0,
      disable: false
    }
  }

  componentWillReceiveProps (nextProps) {
    const reviewQuiz = nextProps.reviewQuiz
    const grade = reviewQuiz.status ? reviewQuiz.grade : 0
    this.setState({grade})
  }

  updateReview () {
    const taskId = this.props.taskId
    const studentId = this.props.studentId
    const sectionId = this.props.sectionId
    const id = this.props.reviewQuiz.id || 0
    let {grade, status, excellence} = this.state
    grade = grade === '' ? this.props.reviewQuiz.grade : grade
    return {grade, status, excellence, id, taskId, sectionId, studentId}
  }

  disableEdit () {
    this.setState({disable: true})
  }

  enableEdit () {
    this.setState({disable: false})
  }

  submitGrade () {
    const {quizId, assignmentId, reviewQuiz, programId, studentId, taskId} = this.props
    const id = reviewQuiz.id
    const {excellence} = this.state
    const grade = this.state.grade||0
    const excellenceGradle = 85
    const status = grade > excellenceGradle ? '优秀' : '已完成'
    const data = {grade, excellence, status, id, assignmentId, studentId, quizId, taskId}
    const notification = { receiverId: studentId, programId, taskId, assignmentId, quizId, appType: 'subjectiveQuiz', type: TUTOR_MODIFY_ASSIGNMENT_STATUS}
    this.disableEdit()
    this.props.submitGrade(data, () => {
      this.enableEdit()
      message.success('成绩保存成功！')
      this.props.addNotification(notification)
    })
  }

  updateGrade (e) {
    const grade = e.target.value
    const pattern = /^([0-9]{1,2}|100)$/
    if (pattern.test(grade) || grade === '') {
      this.setState({grade})
    } else {
      message.warning('分数范围是 1-100 哦')
    }
  }

  getStatus (reviewQuiz) {
    if (reviewQuiz && reviewQuiz.status) {
      return reviewQuiz.status
    } else {
      return '未完成'
    }
  }

  render () {
    const reviewQuiz = this.props.reviewQuiz || {}
    const status = this.getStatus(reviewQuiz)
    let {grade} = this.state

    return (<div>
      {this.props.isTutor
       ? <TwsEditAssignmentStatus
         grade={grade}
         disabled={!reviewQuiz.studentId}
         onBlur={this.submitGrade.bind(this)}
         onChangeGrade={this.updateGrade.bind(this)}
         status={status} />
      : <TwsShowAssignmentStatus grade={reviewQuiz.grade ? reviewQuiz.grade.toString() : '未打分'} status={status} />
       }
    </div>
    )
  }
}

export default withRouter(EditStatusBox)
