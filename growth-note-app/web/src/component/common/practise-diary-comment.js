import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Button, Card, Form, Icon, Mention, Row } from 'antd'
import * as actions from '../../action/comment'
import { addNotification } from '../../action/notification'

import 'antd/dist/antd.css'
import '../../style/practise-diary-comment.less'
import formItemLayOut from '../../constant/formItemLayout'

const FormItem = Form.Item
const {toString} = Mention
const Nav = Mention.Nav

class PractiseDiaryComment extends Component {
  constructor (props) {
    super(props)
    this.state = {
      comments: '',
      isShowComments: false,
      filtered: [],
      receiverIds: this.props.receiverIds || []
    }
  }

  handleInputChangeComments (editorState) {
    this.setState({
      comments: toString(editorState)
    })
  }

  submitComments (id) {
    let followId = -1
    const { comments, receiverIds } = this.state
    const { followeeId, diaryDate, type, name } = this.props
    if (followeeId) {
      followId = followeeId
    }
    if (comments) {
      this.props.submitComments(followId, id, comments, () => {
        this.notificationData(receiverIds, type, diaryDate, name)
      })
    }
    this.setState({
      comments: ''
    }, () => {
      this.props.showCommentInput(false)
      this.setState({
        isShowComments: true
      })
    })
  }
  notificationData (receiverIds, type, diaryDate, name) {
    this.props.addNotification({
      receiverId: receiverIds.join(','),
      type,
      appType: 'practise-diary',
      diaryDate,
      name
    })
  }
  cancelComments () {
    this.props.showCommentInput(false)
  }

  changeCommentSate () {
    this.setState({
      isShowComments: !this.state.isShowComments
    })
  }
  onSelect (suggestion, data) {
    const { receiverIds } = this.state
    if (!receiverIds.includes(data)) {
      receiverIds.push(data)
      this.setState({receiverIds})
    }
  }
  onSearchChange (value) {
    this.props.onSearchChange(value)
  }

  render () {
    const { isShowComments } = this.state
    let practiseDiaryComments = this.props.practiseDiaryComments || []
    const { users } = this.props
    const suggestions = users.map(suggestion => (
      <Nav
        value={suggestion.userName || suggestion.username}
        data={suggestion.id}
      >
        <span>{suggestion.userName || suggestion.username}</span>
      </Nav>
    ))
    return (
      <div className='practise-diary-comment'>
        <Card bordered={false} noHovering>
          <FormItem {...formItemLayOut} label='评论'
            className={this.props.isShowCommentInput ? '' : 'hidden'}>
            <Mention
              style={{width: '100%', height: 100}}
              rows={3} placeholder='@TA 发表你的看法'
              onChange={this.handleInputChangeComments.bind(this)}
              onSelect={this.onSelect.bind(this)}
              onSearchChange={this.onSearchChange.bind(this)}
              suggestions={suggestions}
              multiLines
            />
            <Row>
              <Button type='primary' size='small' ghost className='button-note'
                onClick={this.submitComments.bind(this, this.props.id)}>提交
              </Button>
              <Button size='small' className='button-note'
                onClick={this.cancelComments.bind(this)}>
                取消
              </Button>
            </Row>
          </FormItem>

          <div className={practiseDiaryComments.length === 0 ? 'hidden' : ''}>
            <div onClick={this.changeCommentSate.bind(this)}
              className='comment-tip'>
              {isShowComments
                ? <div>收起评论<Icon type='caret-down' /></div>
                : <div>{practiseDiaryComments.length}条评论<Icon type='caret-right' /></div>
              }
            </div>
            <div className={isShowComments ? '' : 'hidden'}>
              {
                practiseDiaryComments.map((practiseDiaryComment, index) => {
                  let commentAuthor = practiseDiaryComment.commentAuthorInfo
                  return <div key={index} className='comment'>
                    {commentAuthor.name + '(@' + commentAuthor.username + ')说: ' + practiseDiaryComment.comment.commentContent}
                  </div>
                })
              }
            </div>
          </div>

        </Card>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  user: state.user,
  users: state.users
})
const mapDispatchToProps = dispatch => ({
  submitComments: (followeeId, id, comments, callback) => {
    dispatch(actions.submitComments(followeeId, id, comments, callback))
  },
  addNotification: (data) => {
    dispatch(addNotification(data))
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(PractiseDiaryComment)
