import React, { Component } from 'react'
import TwsLayout from './component/tws-layout/index'
import { connect } from 'react-redux'
import StudentAssignmentPageBody from './component/student-assignment-page-body'
import TutorAssignmentPageBody from './component/tutor-assignment-page-body'
import ExcellentAssignmentPageBody from './component/excellent-assignment-page-body'

import {
    HashRouter as Router,
    Route
} from 'react-router-dom'

class App extends Component {
  render () {
    return (
      <Router>
        <TwsLayout>
          <div>
            <Route path='/student/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId' component={StudentAssignmentPageBody} />
            <Route path='/tutor/program/:programId/task/:taskId/student/:studentId/assignment/:assignmentId/quiz/:quizId' component={TutorAssignmentPageBody} />
            <Route path='/tutor/program/:programId/task/:taskId/student/:studentId/divider/assignment/:assignmentId' component={TutorAssignmentPageBody} />
            <Route path='/student/:studentId/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId/excellent-quizzes' component={ExcellentAssignmentPageBody} />
          </div>
        </TwsLayout>
      </Router>
    )
  }
}

const mapStateToProps = ({lang}) => ({lang})

export default connect(mapStateToProps)(App)
