import React, { Component } from 'react'
import {Col, Row, Icon} from 'antd'
import { connect } from 'react-redux'
import '../less/index.less'
import { withRouter } from 'react-router-dom'
import EditStatusBox from './common/edit-status-box'
import AssignmentContentAnswerBox from './common/assignment-content-answer-box'
import * as taskActions from '../actions/task'
import * as assignmentActions from '../actions/assignment'
import * as suggestionActions from '../actions/suggestion'
import * as reviewQuizActions from '../actions/reviewQuiz'
import CommentBox from './common/comment-box'

class ExcellentAssignmentPageBody extends Component {
  constructor (props) {
    super(props)
    this.state = {
      assignmentId: 1,
      programId: 1,
      taskId: 1,
      studentId: 1,
      quizId: 1
    }
  }

  componentDidMount () {
    this.setState({
      assignmentId: this.props.match.params.assignmentId,
      programId: this.props.match.params.programId,
      taskId: this.props.match.params.taskId,
      studentId: this.props.match.params.studentId,
      quizId: this.props.match.params.quizId
    }, () => {
      const {taskId, assignmentId, studentId, quizId} = this.state
      this.props.getTaskInfo(taskId)
      this.props.getCurrentAssignmentQuiz(assignmentId, quizId, studentId)
      this.props.getReviewQuiz(studentId, assignmentId, quizId)
      this.props.getAssignmentSuggestion(assignmentId, quizId, studentId)
    })
  }

  render () {
    const {taskInfo, assignmentQuizzesData} = this.props
    const quiz = assignmentQuizzesData.quiz
    const answer = quiz.userAnswer || '学员还没有提交作业'
    const {reviewQuiz, suggestions} = this.props.reviewQuizAndSuggestions

    return (<div>
      <div className='bottom-line padding-b-2'>
        <Row gutter={4}>
          <Col span={5}>
            <h2>{taskInfo.title}</h2>
          </Col>
          <Col span={4} />
          <Col span={3} offset={3} />
          <EditStatusBox reviewQuiz={reviewQuiz} />
        </Row>
      </div>
      <div className='margin-t-2'>
        <a onClick={() => window.history.back()}><Icon type='arrow-left' />返回优秀作业</a >
      </div>
      <AssignmentContentAnswerBox
        assignmentId={this.state.assignmentId}
        answer={answer}
        quiz={quiz}
        />
      <CommentBox
        assignmentId={this.state.assignmentId}
        studentId={this.state.studentId}
        quizId={this.state.quizId}
        suggestions={suggestions}
        />
    </div>
    )
  }
}

const mapStateToProps = ({taskInfo, assignmentQuizzesData, reviewQuizAndSuggestions}) => ({
  taskInfo, assignmentQuizzesData, reviewQuizAndSuggestions
})

const mapDispatchToProps = dispatch => ({
  getTaskInfo: (taskId) => dispatch(taskActions.getTaskInfo(taskId)),
  getReviewQuiz: (studentId, assignmentId, quizId) => dispatch(reviewQuizActions.tutorGetStudentReviewQuiz(studentId, assignmentId, quizId)),
  getAssignmentSuggestion: (assignmentId, quizId, studentId) => dispatch(suggestionActions.tutorGetQuizSuggestions(assignmentId, quizId, studentId)),
  getCurrentAssignmentQuiz: (assignmentId, quizId, studentId) => dispatch(assignmentActions.getStudentAssignmentQuiz(assignmentId, quizId, studentId))
})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(ExcellentAssignmentPageBody))
