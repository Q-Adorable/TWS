import React, { Component } from 'react'
import { Col, Row, Icon } from 'antd'
import { TwsBelongTask } from 'tws-antd'
import { connect } from 'react-redux'
import '../less/index.less'
import { withRouter } from 'react-router-dom'
import MultipleChoiceQuiz from './common/multiple-choice-quiz'
import SingleChoiceQuiz from './common/single-choice-quiz'
import BasicBlankQuiz from './common/basic-blank-quiz'
import EditStatusBox from './common/edit-status-box'
import UrlPattern from 'url-pattern'
import * as taskActions from '../actions/task'
import * as assignment from '../actions/assignment'
import * as userActions from '../actions/user'
import AssignmentSupplementaryBox from './common/assignment-supplementary-box'
import {getSectionComments} from '../actions/comment'
import CommentBox from './common/comment-box'

class AssignmentPageBody extends Component {
  constructor (props) {
    super(props)
    this.state = {
      sectionId: 0,
      programId: 0,
      studentId: 0,
      taskId: 0,
      assignmentId: 0
    }
  }

  componentDidMount () {
    const pattern = new UrlPattern('/tutor/program/:programId/task/:taskId/student/:studentId/assignment/:assignmentId/quiz/:quizId')
    const urlParams = pattern.match(this.props.location.pathname.replace('divider/', '')) || {}
    this.setState({
      assignmentId: urlParams.assignmentId,
      studentId: urlParams.studentId,
      taskId: urlParams.taskId,
      programId: urlParams.programId
    }, () => {
      const { taskId, assignmentId, studentId } = this.state
      this.props.getTaskInfo(taskId)
      this.props.getBasicQuizAssignment(assignmentId, studentId)
      this.props.tutorGetStudentReviewQuiz(studentId, assignmentId)
      this.props.getStudent(studentId)
      const quizId = assignmentId
      this.props.getComments(assignmentId, studentId, quizId)
    })
  }
  getCompleteName (student) {
    const name = student.name || ''
    const userName = student.userName || ''

    return `${name}(@${userName})`
  }

  render () {
    const {suggestions, assignment} = this.props
    const { basicQuizzes, reviewQuiz, student } = assignment
    const { task } = this.props.taskInfo
    const { studentHomeUrl } = this.props.settings
    const assignments = task.assignments || []
    const currentAssignment = assignments.find(assignment => assignment.id === parseInt(this.state.assignmentId))
    const completeName = this.getCompleteName(student)
    const type = 'tutor'
    const supplement = reviewQuiz.supplement || '学员暂时没有补充内容'
    const {studentId, assignmentId} = this.state
    const quizMap = {
      'SINGLE_CHOICE': SingleChoiceQuiz,
      'MULTIPLE_CHOICE': MultipleChoiceQuiz,
      'BASIC_BLANK_QUIZ': BasicBlankQuiz
    }
    return <div>
      <div className='bottom-line padding-b-2'>
        <Row gutter={4}>
          <Col span={5}>
            <h2>{currentAssignment ? currentAssignment.title : ''}</h2>
          </Col>
          <Col span={4}>
            <div>by {completeName}</div>
          </Col>

          <EditStatusBox reviewQuiz={reviewQuiz} disable />
        </Row>
      </div>

      <TwsBelongTask title={'所属任务卡'}>
        <a
          href={studentHomeUrl + `/tutor/program/${this.state.programId}/task/${this.state.taskId}/student/${this.state.studentId}`}>
          <Icon type='arrow-left' />{task.title}
        </a>
      </TwsBelongTask>

      <div className='margin-t-3'>
        <h3>作业内容</h3>
        <div className='margin-t-2 mark-down-wrap'>
          <div style={{ background: 'white' }} />
          {
            basicQuizzes.map((quiz, index) => {
              const QuizComponent = quizMap[quiz.type]
              return <QuizComponent key={index} index={index} quiz={quiz} isTutor />
            })
          }
        </div>
      </div>
      <AssignmentSupplementaryBox
        reviewQuizId={reviewQuiz.id}
        supplement={supplement}
        userType={type}
        />
      <CommentBox
        suggestions={suggestions}
        isTutor
        user={this.props.user}
        programId={this.state.programId}
        taskId={this.state.taskId}
        assignmentId={assignmentId}
        studentId={studentId}
        quizId={assignmentId}

      />
    </div>
  }
}

const mapStateToProps = ({ user, taskInfo, assignment, comments, settings }) => ({
  user,
  taskInfo,
  assignment,
  settings,
  suggestions: comments
})
const mapDispatchToProps = dispatch => ({
  getTaskInfo: (taskId) => dispatch(taskActions.getTaskInfo(taskId)),
  getBasicQuizAssignment: (assignmentId, studentId) => dispatch(assignment.getMyStudentAssignment(assignmentId, studentId)),
  tutorGetStudentReviewQuiz: (studentId, assignmentId) => dispatch(assignment.tutorGetStudentReviewQuiz(studentId, assignmentId)),
  getStudent: (studentId) => dispatch(userActions.getCurrentStudent(studentId)),
  getComments: (assignmentId, studentId, quizId) => dispatch(getSectionComments(assignmentId, studentId, quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(AssignmentPageBody))
