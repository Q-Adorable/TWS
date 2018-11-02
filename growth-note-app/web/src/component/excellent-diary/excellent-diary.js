import React, { Component } from 'react'
import { Button, Card } from 'antd'
import { connect } from 'react-redux'
import * as actions from '../../action/excellent-diary'
import DiaryContent from '../common/diary-content'
import PractiseDiaryComment from '../common/practise-diary-comment'
import constant from '../../constant/constant'
import * as NOTIFICATION_TYPE from '../../constant/notification-type'
import * as userActions from '../../action/user'

class ExcellentPractiseDiary extends Component {
  constructor (props) {
    super(props)
    this.state = {
      isShowCommentInput: false,
      page: 1,
      pageSize: constant.PAGE_SIZE
    }
  }

  showCommentInput (isShowCommentInput) {
    this.setState({
      isShowCommentInput: isShowCommentInput
    })
  }

  cancelExcellentDiary (diaryId) {
    this.props.cancelExcellentDiary(diaryId)
  }

  getCompleteName (userInfo) {
    const name = userInfo.name || ''
    const userName = userInfo.username || ''

    return `${name}(@${userName})`
  }
  onSearchChange (username) {
    this.props.getUserLikeUsername(username)
  }
  render () {
    const excellentDiariesAndComments = this.props.excellentDiariesInfo || []

    const practiseDiaryAndComments = excellentDiariesAndComments.find(practiseDiaryAndComment => practiseDiaryAndComment.excellentDiary.id === this.props.id)
    let practiseDiary = practiseDiaryAndComments.excellentDiary
    let isAssistant = this.props.user.roles.includes(constant.ASSISTANT_ROLE)
    let diaryAuthorInfo = practiseDiaryAndComments.diaryAuthorInfo
    const completeName = this.getCompleteName(diaryAuthorInfo)
    let receiverIds = []
    receiverIds.push(diaryAuthorInfo.id)
    return (
      <div className='practise-diary'>
        <Card title={completeName}
          extra={(practiseDiary.date).split(' ')[0]}
          className='title-text-position' bordered noHovering>
          <DiaryContent content={practiseDiary.content}
            isShowAndCloseAllContent={this.props.isShowAndCloseAllContent} />
          <div className='practise-diary-operation-button-group'>

            <Button type='primary' size='small' ghost className='button-note'
              onClick={this.showCommentInput.bind(this, true)}>评论日志
                        </Button>

            <Button type='primary' size='small' ghost className={isAssistant ? 'button-note' : 'hidden'}
              onClick={this.cancelExcellentDiary.bind(this, practiseDiary.id)}>取消优秀日志
                        </Button>
          </div>
          <PractiseDiaryComment isShowCommentInput={this.state.isShowCommentInput}
            practiseDiaryComments={practiseDiaryAndComments.comments}
            showCommentInput={this.showCommentInput.bind(this)}
            id={practiseDiary.id}
            receiverIds={receiverIds}
            name={completeName}
            diaryDate={(practiseDiary.date).split(' ')[0]}
            type={NOTIFICATION_TYPE.GROWTH_EXCELLENT_NOTE}
            onSearchChange={this.onSearchChange.bind(this)}
                    />
        </Card>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  excellentDiariesInfo: state.excellentDiariesInfo.excellentDiariesAndComments,
  user: state.user,
  followTutors: state.followTutors
})
const mapDispatchToProps = dispatch => ({
  cancelExcellentDiary: (excellentDiaryId) => dispatch(actions.cancelExcellentDiary(excellentDiaryId)),
  getUserLikeUsername: (username) => { dispatch(userActions.getUserLikeUsername(username)) }
})

export default connect(mapStateToProps, mapDispatchToProps)(ExcellentPractiseDiary)
