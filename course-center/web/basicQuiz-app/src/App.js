import React, { Component } from 'react'
import TwsLayout from './component/tws-layout/index'
import { connect } from 'react-redux'
import {
    HashRouter as Router,
    Route
} from 'react-router-dom'

import studentAssignmentPageBody from './component/student-assignment-page-body'
import TutorAssignmentPageBody from './component/tutor-assignment-page-body'
import BasicQuizAnswerPageBody from './component/basic-quiz-answer-page-body'

class App extends Component {
  render () {
    return (
      <Router>
        <TwsLayout>
          <div>
            <Route exact path='/' component={studentAssignmentPageBody} />
            <Route path='/student/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId' component={studentAssignmentPageBody} />
            <Route path='/tutor/program/:programId/task/:taskId/student/:studentId/assignment/:assignmentId/quiz/:quizId' component={TutorAssignmentPageBody} />
            <Route path='/tutor/program/:programId/task/:taskId/student/:studentId/divider/assignment/:assignmentId/quiz/:quizId' component={TutorAssignmentPageBody} />
            <Route path='/program/:programId/task/:taskId/assignment/:assignmentId/basic-quiz-answer' component={BasicQuizAnswerPageBody} />
          </div>
        </TwsLayout>
      </Router>
    )
  }
}

const mapStateToProps = ({lang}) => ({lang})

export default connect(mapStateToProps)(App)
