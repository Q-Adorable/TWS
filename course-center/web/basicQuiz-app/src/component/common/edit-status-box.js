import React, { Component } from 'react'
import '../../less/index.less'
import { withRouter } from 'react-router-dom'
import {TwsEditAssignmentStatus, TwsShowAssignmentStatus} from 'tws-antd'
import {message} from 'antd'

const GRADE_UNDEFINE = {
  text: '未打分',
  count: -1
}

class EditStatusBox extends Component {
  constructor (props) {
    super(props)
    this.state = {
      grade: 0,
      status: 'w',
      excellence: 0,
      disable: false

    }
  }

  updateReview () {
    const taskId = this.props.taskId
    const studentId = this.props.studentId
    const sectionId = this.props.sectionId
    const id = this.props.reviewQuiz.id || 0
    let {grade, status, excellence} = this.state

    return {grade, status, excellence, id, taskId, sectionId, studentId}
  }

  componentWillReceiveProps (nextProps) {
    const reviewQuiz = nextProps.reviewQuiz || {}
    const grade = reviewQuiz.grade
    this.setState({
      grade: !grade && grade !== 0 ? GRADE_UNDEFINE.count : grade
    })
  }

  disableEdit () {
    this.setState({disable: true})
  }

  enableEdit () {
    this.setState({disable: false})
  }

  submitGrade () {
    const review = this.updateReview()
    this.disableEdit()

    this.props.submitTutorReviewGrade(review, () => {
      this.enableEdit()
      message.success('成绩保存成功！')
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

  render () {
    const reviewQuiz = this.props.reviewQuiz
    const status = reviewQuiz.status ? reviewQuiz.status : '未提交'
    const tutorGrade = this.state.grade === GRADE_UNDEFINE.count ? 0 : this.state.grade
    const studentGrade = this.state.grade === GRADE_UNDEFINE.count ? GRADE_UNDEFINE.text : this.state.grade

    return (<div>
      {this.props.isTutor
        ? <TwsEditAssignmentStatus
          grade={tutorGrade.toString()}
          onBlur={this.submitGrade.bind(this)}
          onChangeGrade={this.updateGrade.bind(this)}
          status={status}
          disabled={this.props.disable} />
        : <TwsShowAssignmentStatus grade={studentGrade.toString()} status={status} />
      }
    </div>
    )
  }
}

export default withRouter(EditStatusBox)
