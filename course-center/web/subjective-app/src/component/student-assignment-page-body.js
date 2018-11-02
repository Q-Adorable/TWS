import React, {Component} from 'react'
import {Button, Col, Icon, Row, Tooltip} from 'antd'
import {TwsBelongTask} from 'tws-antd'
import {connect} from 'react-redux'
import '../less/index.less'
import {withRouter} from 'react-router-dom'
import EditStatusBox from './common/edit-status-box'
import AssignmentContentAnswerBox from './common/assignment-content-answer-box'
import AssignmentSupplementaryBox from './common/assignment-supplementary-box'
import UrlPattern from 'url-pattern'
import * as taskActions from '../actions/task'
import * as actions from '../actions/assignment'
import * as reviewQuizActions from '../actions/reviewQuiz'
import * as suggestionActions from '../actions/suggestion'
import CommentBox from './common/comment-box'
import {addNotification} from '../actions/notification'
class StudentAssignmentPageBody extends Component {
  constructor (props) {
    super(props)
    this.state = {
      assignmentId: 1,
      programId: 1,
      taskId: 1,
      quizId: -1
    }
  }

  componentDidMount () {
    this.setState({
      taskId: this.props.match.params.taskId,
      programId: this.props.match.params.programId,
    }, () => {
      this.props.getTaskInfo(this.state.taskId)
    })
    const {history, location} = this.props
    history.listen(this.update.bind(this))
    this.update(location)
  }

  update (location) {
    const pattern = new UrlPattern('/student/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId')
    const urlParams = pattern.match(location.pathname) || {}
    this.setState({
      quizId: urlParams.quizId,
      assignmentId: urlParams.assignmentId
    }, () => {
      const {assignmentId, quizId} = this.state
      this.props.getCurrentAssignmentQuiz(assignmentId, quizId)
      this.props.getReviewQuiz(assignmentId, quizId)
      this.props.getAssignmentSuggestions(assignmentId, quizId)
    })
  }

  goExcellentAssignmentList () {
    const {programId, taskId, assignmentId, quizId} = this.state
    window.location.href = this.props.settings.studentUrl + `/program/${programId}/task/${taskId}/assignment/${assignmentId}/quiz/${quizId}/excellent-quiz-list`
  }

  changeQuiz (quizId) {
    const {programId, taskId, assignmentId} = this.state
    this.props.history.push(`/student/program/${programId}/task/${taskId}/assignment/${assignmentId}/quiz/${quizId}`)
  }

  render () {
    const {taskInfo, assignmentQuizzesData, settings} = this.props
    const quiz = assignmentQuizzesData.quiz
    const answer = quiz.userAnswer || ''
    const {reviewQuiz, suggestions} = this.props.reviewQuizAndSuggestions
    return (<div>
      <Row>
        <Col span={3} />
        <Col span={18}>
          <div className='bottom-line padding-b-2'>
            <Row gutter={4}>
              <Col span={5}>
                <h2>主观题</h2>
              </Col>
              <Col span={4} />
              <Col span={3} offset={3}>
                {reviewQuiz && (reviewQuiz.status === '已完成' || reviewQuiz.status === '优秀')
                                        ? <Button onClick={this.goExcellentAssignmentList.bind(this)}><Icon
                                          type='download' />优秀作业</Button>
                                        : <Tooltip placement='top' title='' overlay='完成作业才能看呦'>
                                          <Button disabled='true'><Icon type='download' />优秀作业</Button>
                                        </Tooltip>}
              </Col>
              <EditStatusBox reviewQuiz={reviewQuiz} />
            </Row>
          </div>

          <TwsBelongTask title='所属任务卡'>
            <a
              href={settings.studentUrl + '/student/program/' + this.state.programId + '/task/' + this.state.taskId}>
              <Icon type='arrow-left' />{taskInfo.title}
            </a>
          </TwsBelongTask>
          <AssignmentContentAnswerBox
            submitAnswer={this.props.submitAnswer}
            assignmentId={this.state.assignmentId}
            taskId={this.state.taskId}
            programId={this.state.programId}
            addNotification={this.props.addNotification}
            answer={answer}
            quiz={quiz} />
          {reviewQuiz.id
              ? <AssignmentSupplementaryBox
                submitSupplementary={this.props.submitSupplementary}
                reviewQuizId={reviewQuiz.id}
                supplement={reviewQuiz.supplement} /> : ''}

          <CommentBox
            onModifyComment={this.props.modifyComment}
            user={this.props.user}
            programId={this.state.programId}
            taskId={this.state.taskId}
            assignmentId={this.state.assignmentId}
            studentId={this.props.user.id}
            quizId={this.state.quizId}
            onDeleteComment={this.props.deleteComment}
            suggestions={suggestions} />
        </Col>
        <Col span={3} />
      </Row>
    </div>
    )
  }
}

const mapStateToProps = ({settings, user, taskInfo, assignmentQuizzesData, reviewQuizAndSuggestions}) => ({
  user, taskInfo, assignmentQuizzesData, reviewQuizAndSuggestions, settings
})

const mapDispatchToProps = dispatch => ({
  getTaskInfo: (taskId) => dispatch(taskActions.getTaskInfo(taskId)),
  modifyComment: (commentId, content, assignmentId, quizId, studentId) => dispatch(suggestionActions.modifyComment(commentId, content, assignmentId, quizId, studentId)),
  getReviewQuiz: (assignmentId, quizId) => dispatch(reviewQuizActions.studentGetReviewQuiz(assignmentId, quizId)),
  getAssignmentSuggestions: (assignmentId, quizId) => dispatch(suggestionActions.studentGetQuizSuggestions(assignmentId, quizId)),
  submitAnswer: (userAnswer, taskId, assignmentId, quizId, callback) => dispatch(actions.submitAnswer(userAnswer, taskId, assignmentId, quizId, callback)),
  getCurrentAssignmentQuiz: (assignmentId, quizId) => dispatch(actions.getAssignmentQuiz(assignmentId, quizId)),
  deleteComment: (commentId, assignmentId, quizId, studentId, isTutor) => dispatch(suggestionActions.deleteComment(commentId, assignmentId, quizId, studentId, isTutor)),
  submitSupplementary: (id, supplement) => dispatch(reviewQuizActions.submitSupplementary(id, supplement)),
  addNotification: (data) => dispatch(addNotification(data))

})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(StudentAssignmentPageBody))
