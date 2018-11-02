import React, {Component} from 'react'
import {Button, Col, Icon, Popconfirm, Row} from 'antd'
import {TwsBelongTask} from 'tws-antd'
import {connect} from 'react-redux'
import '../less/index.less'
import {withRouter} from 'react-router-dom'
import MultipleChoiceQuiz from './common/multiple-choice-quiz'
import SingleChoiceQuiz from './common/single-choice-quiz'
import BasicBlankQuiz from './common/basic-blank-quiz'
import EditStatusBox from './common/edit-status-box'
import * as taskActions from '../actions/task'
import * as assignment from '../actions/assignment'
import {studentGetSectionComments} from '../actions/comment'
import CommentBox from './common/comment-box'
import AssignmentSupplementaryBox from './common/assignment-supplementary-box'

class AssignmentPageBody extends Component {
  constructor (props) {
    super(props)
    this.state = {
      assignmentId: 1,
      programId: 1,
      taskId: 1,
      answers: [],
      disable: false,
      popconfirmMessage: '是否要提交,还有未完成的题哦！'
    }
  }

  componentDidMount () {
    this.setState({
      assignmentId: this.props.match.params.assignmentId,
      taskId: this.props.match.params.taskId,
      programId: this.props.match.params.programId
    }, () => {
      const {assignmentId, taskId} = this.state
      this.props.getTaskInfo(taskId)
      this.props.getBasicQuizAssignment(assignmentId)
      this.props.getReviewQuiz(assignmentId)
      this.props.getComments(assignmentId, assignmentId)
    })
  }

  modifyAnswer (quiz, answer) {
    const {answers} = this.state
    const index = answers.findIndex(item => item.quizId === quiz.id)
    if (index !== -1) {
      answers[index].userAnswer = answer
    } else {
      answers.push({quizId: quiz.id, userAnswer: answer})
    }

    let popconfirmMessage = this.updatePoconfirmMessage()
    this.setState({
      answers,
      popconfirmMessage
    })
  }
  updatePoconfirmMessage () {
    let popconfirmMessage = '是否要提交'
    if (this.state.answers.length !== this.props.assignment.basicQuizzes.length) {
      popconfirmMessage += ',还有未完成的题哦！'
    }
    return popconfirmMessage
  }

  submitBasicQuiz () {
    this.props.submitBasicQuizAnswer(this.state.answers, this.state.assignmentId)
  }

  render () {
    const {assignment, comments, user} = this.props
    const {basicQuizzes, reviewQuiz} = assignment
    const {task} = this.props.taskInfo
    const {studentHomeUrl} = this.props.settings
    const {programId, taskId, assignmentId} = this.state

    const quizMap = {
      'SINGLE_CHOICE': SingleChoiceQuiz,
      'MULTIPLE_CHOICE': MultipleChoiceQuiz,
      'BASIC_BLANK_QUIZ': BasicBlankQuiz
    }

    return <div>
      <div className='bottom-line padding-b-2'>
        <Row gutter={4}>
          <Col span={5}>
            <h2>{task.title}</h2>
          </Col>
          <Col span={4} />
          <EditStatusBox reviewQuiz={reviewQuiz} disable />
        </Row>
      </div>

      <TwsBelongTask title='所属任务卡'>
        <a href={`${studentHomeUrl}/student/program/` + this.state.programId + '/task/' + this.state.taskId}><Icon type='arrow-left' />{task.title}</a>
      </TwsBelongTask>

      <div className='margin-t-3'>
        <h3>作业内容</h3>
        <div className='margin-t-2 mark-down-wrap'>
          <div style={{background: 'white'}} />
          {
              basicQuizzes.map((quiz, index) => {
                const QuizComponent = quizMap[quiz.type]
                return <QuizComponent key={index} index={index} modifyAnswer={this.modifyAnswer.bind(this)} quiz={quiz} />
              })
            }
          {
              reviewQuiz.id
                  ? ''
                  : <Popconfirm title={this.state.popconfirmMessage} onConfirm={this.submitBasicQuiz.bind(this, this.state.answers)} okText='Yes' cancelText='No'>
                    <Button className='margin-t-2 margin-b-3' type='primary' >提交</Button>
                  </Popconfirm>

           }
        </div>
      </div>
      {reviewQuiz.id
            ? <AssignmentSupplementaryBox
              submitSupplementary={this.props.submitSupplementary}
              reviewQuizId={reviewQuiz.id}
              assignmentId={this.state.assignmentId}
              supplement={reviewQuiz.supplement} /> : ''}

      <CommentBox
        user={user}
        studentId={user.id}
        programId={programId}
        taskId={taskId}
        suggestions={comments}
        quizId={assignmentId}
        assignmentId={assignmentId} />
    </div>
  }
}

const mapStateToProps = state => ({
  taskInfo: state.taskInfo,
  assignment: state.assignment,
  comments: state.comments,
  user: state.user,
  settings: state.settings
})
const mapDispatchToProps = dispatch => ({
  getTaskInfo: (taskId) => dispatch(taskActions.getTaskInfo(taskId)),
  getBasicQuizAssignment: (assignmentId) => dispatch(assignment.getBasicQuizAssignment(assignmentId)),
  getReviewQuiz: (assignmentId) => dispatch(assignment.getReviewQuiz(assignmentId)),
  submitBasicQuizAnswer: (answers, assignmentId) => dispatch(assignment.submitBasicQuizAnswer(answers, assignmentId)),
  submitSupplementary: (id, supplement, assignmentId) => dispatch(assignment.submitSupplementary(id, supplement, assignmentId)),
  getComments: (assignmentId, quizId) => dispatch(studentGetSectionComments(assignmentId, quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(AssignmentPageBody))
