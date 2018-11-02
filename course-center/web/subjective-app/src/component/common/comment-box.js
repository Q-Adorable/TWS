import React, {Component} from 'react'
import {Button, Col, Divider, Icon, Popconfirm} from 'antd'
import {connect} from 'react-redux'
import moment from 'moment'
import '../../less/index.less'
import {withRouter} from 'react-router-dom'
import * as actions from '../../actions/suggestion'
import {TwsReactMarkdownEditor, TwsReactMarkdownPreview} from 'tws-antd'
import {getUploadUrl} from '../../constant/upload-url'
import {TUTOR_COMMENT_ASSIGNMENT, STUDENT_COMMENT_ASSIGNMENT} from '../../constant/notification-status'
import {addNotification} from '../../actions/notification'

const NOT_EXIT_ID = -1

class CommentBox extends Component {
  constructor (props) {
    super(props)
    this.state = {
      showSuggestionInput: false,
      content: '',
      currentEditCommentId: NOT_EXIT_ID,
      replyContent: '',
      currentReplyId: NOT_EXIT_ID
    }
  }

  showSuggestionInput () {
    this.setState({
      showSuggestionInput: !this.state.showSuggestionInput
    })
  }
  commentNotification (tutorId) {
    const {programId, taskId, assignmentId, quizId, studentId, isTutor} = this.props
    const notification = {
      type: isTutor ? TUTOR_COMMENT_ASSIGNMENT : STUDENT_COMMENT_ASSIGNMENT,
      appType: 'subjectiveQuiz',
      programId,
      taskId,
      assignmentId,
      quizId,
      studentId,
      receiverId: isTutor ? studentId : tutorId
    }
    this.props.addNotification(notification)
  }

  submitComment () {
    const { assignmentId, quizId, studentId } = this.props
    const toUserId = studentId
    this.props.submitSectionSuggestion(toUserId, assignmentId, quizId, studentId, this.state.content)
    this.commentNotification()
    this.setState({
      showSuggestionInput: false,
      content: ''
    })
  }

  submitReplyComment (fromUserId, parentId) {
    const toUserId = fromUserId
    const { assignmentId, quizId, isTutor} = this.props
    this.props.submitReplySuggestion(toUserId, assignmentId, quizId, this.state.replyContent, parentId, isTutor)
    this.commentNotification(toUserId)
    this.setState({
      currentReplyId: NOT_EXIT_ID,
      replyContent: ''
    })
  }

  showEditInput (id, suggestionContent) {
    const { currentEditCommentId, content } = this.state
    const { assignmentId, studentId, quizId, isTutor } = this.props
    if (id !== currentEditCommentId) {
    } else {
      this.props.onModifyComment(id, content, assignmentId, quizId, studentId, isTutor)

      suggestionContent = ''
      id = NOT_EXIT_ID
    }

    this.setState({
      currentEditCommentId: id,
      content: suggestionContent
    })
  }

  deleteComment (id) {
    this.props.onDeleteComment(id, this.props.assignmentId, this.props.quizId, this.props.studentId, this.props.isTutor)
  }

  cancelEditInput () {
    this.setState({
      currentEditCommentId: NOT_EXIT_ID,
      content: ''
    })
  }

  cancelReplyInput () {
    this.setState({
      currentReplyId: NOT_EXIT_ID,
      replyContent: ''
    })
  }

  getBothNames (suggestion) {
    const fromUserName = suggestion.fromUser.username
    const toUserName = suggestion.toUser.username
    if (fromUserName === toUserName) {
      return `[${fromUserName}]()：\n`
    }
    return `[${fromUserName}]() 回复  [${toUserName}]()：\n`
  }

  isMyComment (suggestion) {
    const user = this.props.user
    return user.id === suggestion.fromUserId
  }

  displayAndEdit (suggestion) {
    const {settings} = this.props
    return this.state.currentEditCommentId !== suggestion.id
      ? <TwsReactMarkdownPreview source={this.getBothNames(suggestion) + suggestion.content} />
      : <div>
        <TwsReactMarkdownEditor value={suggestion.content}
          action={getUploadUrl(settings.appContextPath)}
          onChange={commentContent => this.setState({ content: commentContent })} />
        <Button className='margin-t-2' type="primary"
          onClick={this.showEditInput.bind(this, suggestion.id)}>确定</Button>
        <Button className='margin-t-2 margin-l-2'
          onClick={this.cancelEditInput.bind(this, suggestion.id)}>取消</Button>
      </div>
  }
  oneSuggestion (suggestion, parentId) {
    return <div>
      {
        this.displayAndEdit(suggestion)
      }
      <div className='comment-title-icon'>
        {
          this.suggestionOperationIcons(suggestion)
        }
      </div>
      <Divider dashed />
    </div>
  }
  suggestionOperationIcons (suggestion) {
    return this.isMyComment(suggestion)
      ? <div>
        {moment(suggestion.createTime).format('YYYY-MM-DD')}
        <Divider className='margin-l-1 margin-r-1' type='vertical' />
        <Icon className='margin-r-1' type='edit'
          onClick={this.showEditInput.bind(this, suggestion.id, suggestion.content)} />

        <Popconfirm
          title='确定删除'
          onConfirm={this.deleteComment.bind(this, suggestion.id)}
          okText='Yes'
          cancelText='No'>
          <Icon className=' margin-r-1' type='delete' />
        </Popconfirm>
      </div>
      : <div>
        {moment(suggestion.createTime).format('YYYY-MM-DD')}
        <Divider className='margin-l-1 margin-r-1' type='vertical' />

      </div>
  };

  operReplyModal (suggestion, parentId) {
    const {settings} = this.props
    const fromUserId = this.props.isTutor ? suggestion.toUserId : suggestion.fromUserId
    return this.state.currentReplyId === suggestion.id
      ? <div key={suggestion.id}>
        <TwsReactMarkdownEditor
          action={getUploadUrl(settings.appContextPath)}
          onChange={commentContent => this.setState({ replyContent: commentContent })} />
        <Button className='margin-t-2' type="primary"
          onClick={this.submitReplyComment.bind(this, fromUserId, parentId)}>确定</Button>
        <Button className='margin-t-2 margin-l-2'
          onClick={this.cancelReplyInput.bind(this, suggestion.id)}>取消</Button>
      </div>
      : ''
  }

  render () {
    let suggestions = this.props.suggestions || []
    const {settings} = this.props
    return (
      <div className='margin-t-3'>
        <h3>助教反馈</h3>
        <div className='margin-t-2'>

          {suggestions.length > 0 ? suggestions.map((suggestion, index) => {
            const parentId = suggestion.id
            return <div key={index} className='mark-down-wrap margin-b-2'>
              {this.oneSuggestion(suggestion, parentId)}
              <div className='child-suggestion margin-l-3'>
                {
                  suggestion.childSuggestions.map(item => this.oneSuggestion(item, parentId))
                }
                {
                  this.operReplyModal(suggestion, parentId)
                }
                &nbsp;
                <div className='comment-title-icon'>
                  <a href='javascript:'
                    onClick={() => this.setState({ currentReplyId: suggestion.id })}>回复</a>
                </div>
              </div>
            </div>
          }) : '暂无反馈'
          }
          {this.props.isTutor
          ? <div>
            <div className={this.state.showSuggestionInput ? 'hidden' : ''}>
              <Button onClick={this.showSuggestionInput.bind(this)}>新主题</Button>
            </div>
            <div className={this.state.showSuggestionInput ? 'margin-t-2' : 'hidden'}>
              <TwsReactMarkdownEditor value={this.state.content}
                action={getUploadUrl(settings.appContextPath)}
                onChange={commentContent => this.setState({ content: commentContent })} />
              <Col span={4}>
                <Button className='margin-t-2' onClick={this.submitComment.bind(this)}
                  type='primary'>提交新主题</Button>
              </Col>
            </div>
          </div>
            : ''
          }
        </div>

      </div>
    )
  }
}
const mapStateToProps = state => ({
  settings: state.settings
})
const mapDispatchToProps = dispatch => ({
  submitSectionSuggestion: (toUserId, assignmentId, quizId, studentId, content, parentId) => dispatch(actions.submitSuggestion(toUserId, assignmentId, quizId, studentId, content, parentId)),
  submitReplySuggestion: (toUserId, assignmentId, quizId, content, parentId, isTutor) => dispatch(actions.submitReploySuggestion(toUserId, assignmentId, quizId, content, parentId, isTutor)),
  addNotification: data => dispatch(addNotification(data))
})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(CommentBox))
