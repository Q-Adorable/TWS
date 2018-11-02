import React, { Component } from 'react'
import { Card, Button } from 'antd'
import { connect } from 'react-redux'
import * as excellentDiary from '../../action/excellent-diary'
import PractiseDiaryComment from '../common/practise-diary-comment'
import '../../style/App.css'
import DiaryContent from '../common/diary-content'
import * as NOTIFICATION_TYPE from '../../constant/notification-type'
import * as followActions from '../../action/follow'

class FolloweeDiary extends Component {
  constructor(props) {
    super(props)
    this.state = {
      visible: false,
      isShowCommentInput: false
    }
  }

  showCommentInput(isShowCommentInput) {
    this.setState({
      isShowCommentInput: isShowCommentInput
    })
  }

  addExcellentDiary(excellentDiaryId) {
    this.props.addExcellentDiary(excellentDiaryId)
  }
  onSearchChange() {
    const user = []
    user.push(this.props.contactDiaryAndComList.followeeInfo)
    this.props.getFollowUsers(user)
  }

  getDiaryType(practiseDiary) {
    if (practiseDiary.diaryType === 'diary') {
      return '日志'
    }
    if (practiseDiary.diaryType === 'goal') {
      return '目标'
    }
  }

  render() {
    const diaryAndCommentsList = this.props.contactDiaryAndComList.practiseDiaryAndComments || []
    const practiseDiaryAndComments = diaryAndCommentsList.find(practiseDiaryAndComment => practiseDiaryAndComment.practiseDiary.id === this.props.id)
    let practiseDiary = practiseDiaryAndComments.practiseDiary
    let followeeInfo = this.props.contactDiaryAndComList.followeeInfo
    let receiverIds = []
    receiverIds.push(followeeInfo.id)
    let diaryType = this.getDiaryType(practiseDiary)

    return (
      <div>
        <div className='practise-diary'>
          <Card title={`${followeeInfo.userName}的${diaryType}`}
            extra={`${(practiseDiary.date).split(' ')[0]}`}
            className='title-text-position' bordered noHovering>
            <DiaryContent content={practiseDiary.content} isShowAndCloseAllContent={this.props.isShowAndCloseAllContent} />
            <div className='practise-diary-operation-button-group'>
              <Button type='primary' size='small' ghost
                className='button-note'
                onClick={this.showCommentInput.bind(this, true)}>评论日志
              </Button>

              <Button type='primary' size='small' ghost
                className='button-note'
                onClick={this.addExcellentDiary.bind(this, practiseDiary.id)}>推荐优秀日志
              </Button>
            </div>
            <PractiseDiaryComment isShowCommentInput={this.state.isShowCommentInput}
              followeeId={this.props.followeeId}
              practiseDiaryComments={practiseDiaryAndComments.comments}
              showCommentInput={this.showCommentInput.bind(this)}
              id={practiseDiary.id}
              receiverIds={receiverIds}
              diaryDate={(practiseDiary.date).split(' ')[0]}
              type={NOTIFICATION_TYPE.GROWTH_NOTE_TUTOR_TO_STUDENT}
              onSearchChange={this.onSearchChange.bind(this)}
            />
          </Card>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  contactDiaryAndComList: state.contactUserDiariesAndComments
})
const mapDispatchToProps = dispatch => ({
  addExcellentDiary: (practiseDiaryId) => {
    dispatch(excellentDiary.addExcellentDiary(practiseDiaryId))
  },
  getFollowUsers: (user) => { dispatch(followActions.getFollowUsers(user)) }
})

export default connect(mapStateToProps, mapDispatchToProps)(FolloweeDiary)
