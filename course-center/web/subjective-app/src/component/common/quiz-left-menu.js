import React, { Component } from 'react'
import { Menu } from 'antd'
import { connect } from 'react-redux'
import '../../less/index.less'
import * as actions from '../../actions/assignment'
import { withRouter } from 'react-router-dom'

class QuizLeftMenu extends Component {
  constructor (props) {
    super(props)
    this.state = {
      assignmentId: -1,
      programId: -1,
      taskId: -1,
      quizId: -1
    }
  }

  componentDidMount () {
    this.setState({
      assignmentId: this.props.match.params.assignmentId,
      programId: this.props.match.params.programId,
      taskId: this.props.match.params.taskId,
      quizId: this.props.match.params.quizId
    }, () => {
      this.props.getAssignmentQuizzes(this.state.assignmentId)
    })
  }

  handleClick (e) {
    this.setState({quizId: parseInt(e.key)})
    this.props.clickQuiz(parseInt(e.key))
  }

  render () {
    const {assignmentQuizzesData} = this.props
    const quizzes = assignmentQuizzesData.quizzes
    return (
      <Menu selectedKeys={[`${this.state.quizId}`]} onClick={this.handleClick.bind(this)}
        mode='inline'
        style={{
          width: 240
        }}
      >{
        quizzes.map((quiz, index) =>
          <Menu.Item key={quiz.id}>
            第{index + 1}题
          </Menu.Item>)
      }
      </Menu>
    )
  }
}

const mapStateToProps = ({assignmentQuizzesData}) => ({
  assignmentQuizzesData
})

const mapDispatchToProps = dispatch => ({
  getAssignmentQuizzes: (assignmentId) => dispatch(actions.getAssignmentQuizzes(assignmentId))
})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(QuizLeftMenu))
