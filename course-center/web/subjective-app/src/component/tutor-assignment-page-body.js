import React, { Component } from 'react'
import { Col, Icon, Row } from 'antd'
import { TwsBelongTask } from 'tws-antd'
import { connect } from 'react-redux'
import '../less/index.less'
import { withRouter } from 'react-router-dom'
import EditStatusBox from './common/edit-status-box'
import CommentBox from './common/comment-box'
import AssignmentContentAnswerBox from './common/assignment-content-answer-box'
import AssignmentSupplementaryBox from './common/assignment-supplementary-box'
import UrlPattern from 'url-pattern'
import * as taskActions from '../actions/task'
import * as assignmentActions from '../actions/assignment'
import * as suggestionActions from '../actions/suggestion'
import * as userActions from '../actions/user'
import * as reviewQuizActions from '../actions/reviewQuiz'
import StudentLeftMenu from './common/students-left-menu'
import { addNotification } from '../actions/notification'

class TutorAssignmentPageBody extends Component {
  constructor (props) {
    super(props)
    this.state = {
      sectionId: 0,
      studentId: 0,
      programId: 0,
      taskId: 0
    }
  }

  componentDidMount () {
    this.setState({
      taskId: this.props.match.params.taskId,
      programId: this.props.match.params.programId,
      studentId: this.props.match.params.studentId
    }, () => {
      this.props.getTaskInfo(this.state.taskId)
    })
    const {history, location} = this.props
    history.listen(this.update.bind(this))
    this.update(location)
  }

  update (location) {
    const pattern = new UrlPattern('/tutor/program/:programId/task/:taskId/student/:studentId/assignment/:assignmentId/quiz/:quizId')
    const urlParams = pattern.match(location.pathname.replace('divider/', '')) || {}
    this.setState({
      quizId: urlParams.quizId,
      assignmentId: urlParams.assignmentId
    }, () => {
      const {assignmentId, quizId, studentId} = this.state
      this.props.getStudentCurrentAssignmentQuiz(assignmentId, quizId, studentId)
      this.props.getReviewQuiz(studentId, assignmentId, quizId)
      this.props.getAssignmentSuggestion(assignmentId, quizId, studentId)
      this.props.getStudent(studentId)
    })
  }

  changeStudent (studentId) {
    const {programId, taskId, assignmentId, quizId} = this.state
    this.props.history.push(`/tutor/program/${programId}/task/${taskId}/student/${studentId}/assignment/${assignmentId}/quiz/${quizId}`)
    this.setState({studentId}, () => {
      const {history, location} = this.props
      history.listen(this.update.bind(this))
      this.update(location)
    })
  }

  getCompleteName (student) {
    const name = student.name || ''
    const userName = student.userName || ''

    return `${name}(@${userName})`
  }

  render () {
    const {taskInfo, assignmentQuizzesData, settings} = this.props
    const {programId, taskId, assignmentId, studentId, quizId} = this.state
    const quiz = assignmentQuizzesData.quiz
    const answer = quiz.userAnswer || '学员还没有提交作业, 快去催催吧'
    const {reviewQuiz, suggestions, student} = this.props.reviewQuizAndSuggestions
    const completeName = this.getCompleteName(student)
    const type = 'tutor'
    const supplement = reviewQuiz.supplement || '学员暂时没有补充内容'

    return (<div>
      <Row>
        <Col span={6}>
          <StudentLeftMenu
            clickQuiz={this.changeStudent.bind(this)}
            studentId={this.state.studentId}
            />
        </Col>
        <Col span={18}>
          <div className='bottom-line padding-b-2'>
            <Col span={5}>
              <h2>主观题</h2>
            </Col>
            <Row gutter={4}>
              <Col span={3}>
                <div>By {completeName}</div>
              </Col>
              <Col span={4} offset={3} />
              <EditStatusBox
                submitGrade={this.props.submitGrade}
                addNotification={this.props.addNotification}
                programId={this.state.programId}
                answer={answer} isDisabled={false} reviewQuiz={reviewQuiz} taskId={taskId}
                quizId={quizId} studentId={studentId} assignmentId={assignmentId} isTutor />
            </Row>
          </div>
          <TwsBelongTask title='所属任务卡'>
            <a
              href={settings.studentUrl + `/tutor/program/${this.state.programId}/task/${this.state.taskId}/student/${this.state.studentId}`}>
              <Icon type='arrow-left' />{taskInfo.title}
            </a>
          </TwsBelongTask>
          <AssignmentContentAnswerBox
            submitAnswer={this.props.submitAnswer}
            assignmentId={assignmentId}
            answer={answer}
            quiz={quiz}
            />
          <AssignmentSupplementaryBox
            reviewQuizId={reviewQuiz.id}
            supplement={supplement}
            userType={type}
            />
          <CommentBox
            isTutor
            programId={programId}
            user={this.props.user}
            taskId={taskId}
            assignmentId={assignmentId}
            studentId={studentId}
            quizId={quizId}
            suggestions={suggestions}
            onModifyComment={this.props.modifyComment}
            onDeleteComment={this.props.deleteComment}
            />
        </Col>
      </Row>
    </div>
    )
  }
}

const mapStateToProps = ({settings, user, taskInfo, assignmentQuizzesData, reviewQuizAndSuggestions}) => ({
  user, taskInfo, assignmentQuizzesData, reviewQuizAndSuggestions, settings
})

const mapDispatchToProps = dispatch => ({
  submitGrade: (data, changeStatus) => dispatch(reviewQuizActions.submitTutorReview(data, changeStatus)),
  modifyComment: (commentId, content, assignmentId, quizId, studentId, isTutor) => dispatch(suggestionActions.modifyComment(commentId, content, assignmentId, quizId, studentId, isTutor)),
  deleteComment: (commentId, assignmentId, quizId, studentId, isTutor) => dispatch(suggestionActions.deleteComment(commentId, assignmentId, quizId, studentId, isTutor)),
  getTaskInfo: (taskId) => dispatch(taskActions.getTaskInfo(taskId)),
  getAssignmentSuggestion: (assignmentId, quizId, studentId) => dispatch(suggestionActions.tutorGetQuizSuggestions(assignmentId, quizId, studentId)),
  getReviewQuiz: (studentId, assignmentId, quizId) => dispatch(reviewQuizActions.tutorGetStudentReviewQuiz(studentId, assignmentId, quizId)),
  getStudentCurrentAssignmentQuiz: (assignmentId, quizId, studentId) => dispatch(assignmentActions.getStudentAssignmentQuiz(assignmentId, quizId, studentId)),
  getStudent: (studentId) => dispatch(userActions.getStudent(studentId)),
  addNotification: (data) => dispatch(addNotification(data))
})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(TutorAssignmentPageBody))
