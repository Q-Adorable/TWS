import React, {Component} from 'react'
import {withRouter} from 'react-router-dom'
import {connect} from 'react-redux'
import UrlPattern from 'url-pattern'
import * as assignment from '../actions/assignment'
import BasicBlankQuiz from './common/basic-blank-quiz'
import SingleChoiceQuiz from './common/single-choice-quiz'
import MultipleChoiceQuiz from './common/multiple-choice-quiz'

// TOdo: need to delete
class BasicQuizAnswerPageBody extends Component {
  componentDidMount () {
    const pattern = new UrlPattern('/program/:programId/task/:taskId/assignment/:assignmentId/basic-quiz-answer')
    const urlParams = pattern.match(this.props.location.pathname) || {}
    this.props.getBasicQuizAssignment(urlParams.assignmentId)
  }

  render () {
    let assignment = this.props.assignment
    const {basicQuizzes = []} = assignment
    const quizMap = {
      'SINGLE_CHOICE': SingleChoiceQuiz,
      'MULTIPLE_CHOICE': MultipleChoiceQuiz,
      'BASIC_BLANK_QUIZ': BasicBlankQuiz
    }

    return <div>
      <div className='margin-t-3'>
        <h3>作业内容</h3>
        <div className='margin-t-2 mark-down-wrap'>
          <div style={{background: 'white'}} />
          {
                    basicQuizzes.map((quiz, index) => {
                      const QuizComponent = quizMap[quiz.type]
                      return <QuizComponent key={index} preview quiz={quiz} />
                    })
          }
        </div>
      </div>
    </div>
  }
}
const mapStateToProps = state => ({
  assignment: state.assignment
})
const mapDispatchToProps = dispatch => ({
  getBasicQuizAssignment: (assignmentId) => dispatch(assignment.getBasicQuizAssignment(assignmentId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(BasicQuizAnswerPageBody))
