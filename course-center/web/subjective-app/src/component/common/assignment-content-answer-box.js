import React, { Component } from 'react'
import { Button, message } from 'antd'
import '../../less/index.less'
import {connect} from 'react-redux'
import { withRouter } from 'react-router-dom'
import { TwsReactMarkdownPreview, TwsReactMarkdownEditor } from 'tws-antd'
import {getUploadUrl} from '../../constant/upload-url'
import { STUDENT_MODIFY_ASSIGNMENT_STATUS } from '../../constant/notification-status'
class AssignmentContentAndAnswerBox extends Component {
  constructor (props) {
    super(props)
    this.state = {
      assignmentAnswer: ''
    }
  }

  submitAnswer (quizId) {
    if (this.state.assignmentAnswer.trim() === '') {
      message.warning('亲，答案不能为空哦')
    } else {
      const { taskId, assignmentId, programId } = this.props
        const notification = { programId, taskId, assignmentId, quizId, appType: 'subjectiveQuiz', type: STUDENT_MODIFY_ASSIGNMENT_STATUS}
      this.props.submitAnswer(this.state.assignmentAnswer, this.props.taskId, this.props.assignmentId, quizId, ()=>{
        this.props.addNotification(notification)
      })
    }
  }

  render () {
    const quiz = this.props.quiz || {}
    const {answer, settings} = this.props

    return (<div>
      <div className='margin-t-3'>
        <h3>作业内容</h3>
        <div className='margin-t-2 mark-down-wrap'>
          <TwsReactMarkdownPreview source={quiz.description} />
        </div>
      </div>
      {answer
        ? <div className='margin-t-3'>
          <h3>作业答案</h3>
          <div className='margin-t-2 mark-down-wrap'>
            <TwsReactMarkdownPreview source={answer} />
          </div>
        </div>
        : <div className='margin-t-3'>
          <h3>提交答案</h3>
          <TwsReactMarkdownEditor
            action={getUploadUrl(settings.appContextPath)}
            value=''
            onChange={assignmentAnswer => this.setState({ assignmentAnswer })} />
          <Button className='margin-t-2' onClick={this.submitAnswer.bind(this, quiz.id)} type='primary'>提交</Button>
        </div>
      }

    </div>
    )
  }
}
const mapStateToProps = state => ({
  settings: state.settings
})
export default withRouter(connect(mapStateToProps, null)(AssignmentContentAndAnswerBox))
