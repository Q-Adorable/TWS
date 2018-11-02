import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Button, Card, Icon, message, Popconfirm } from 'antd'
import 'antd/dist/antd.css'
import '../../style/practise-diary.less'

import PractiseDiaryEditorBody from './practise-diary-editor-body'
import PractiseDiaryComment from '../common/practise-diary-comment'
import * as actions from '../../action/practise-diary'
import DiaryContent from '../common/diary-content'
import * as NOTIFICATION_TYPE from '../../constant/notification-type'

class PractiseDiary extends Component {
  constructor(props) {
    super(props)
    this.state = {
      visible: false,
      isShowCommentInput: false
    }
  }
  deletePractiseDiary(id, page, pageSize) {
    this.props.deletePractiseDiary(id, page, pageSize)
    message.success('删除成功')
  }

  setModalVisible(visible) {
    this.setState({ visible })
  }

  submitPractiseDiary(id, practiseDiary) {
    this.setModalVisible(false)
    this.props.updatePractiseDiary(id, practiseDiary)
  }

  showCommentInput(isShowCommentInput) {
    this.setState({
      isShowCommentInput: isShowCommentInput
    })
  }
  onSearchChange() { }
  render() {
    const practiseDiaryAndComments = this.props.practiseDiaryAndComments.find(practiseDiaryAndComment => practiseDiaryAndComment.practiseDiary.id === this.props.id)
    let practiseDiary = practiseDiaryAndComments.practiseDiary
    if (practiseDiary.diaryType === 'diary') {
      practiseDiary.diaryType = '日志'
    }
    if (practiseDiary.diaryType === 'goal') {
      practiseDiary.diaryType = '目标'
    }
    return (
      <div className='practise-diary'>
        <div className={this.state.visible ? 'hidden' : ''}>
          <Card className='title-text-position' title={`${(practiseDiary.date).split(' ')[0]} 的${practiseDiary.diaryType}`}
            bordered noHovering
            extra={
              <Popconfirm
                title='确定删除吗？'
                onConfirm={this.deletePractiseDiary.bind(this, practiseDiary.id, this.props.page, this.props.pageSize)}
                okText='确定' cancelText='取消'>

                <Icon type='delete' />
              </Popconfirm>
            }

          >
            <DiaryContent content={practiseDiary.content} />

            <div className='practise-diary-operation-button-group'>

              <Button type='primary' size='small' ghost className='button-note'
                onClick={this.setModalVisible.bind(this, true)}>修改日志
                            </Button>

              <Button type='primary' size='small' ghost className='button-note'
                onClick={this.showCommentInput.bind(this, true)}>评论日志
                            </Button>
            </div>
            <div>
              <PractiseDiaryComment isShowCommentInput={this.state.isShowCommentInput}
                practiseDiaryComments={practiseDiaryAndComments.comments}
                showCommentInput={this.showCommentInput.bind(this)}
                id={practiseDiary.id}
                diaryDate={(practiseDiary.date).split(' ')[0]}
                type={NOTIFICATION_TYPE.GROWTH_NOTE_STUDENT_TO_TUTOR}
                onSearchChange={this.onSearchChange.bind(this)}
              />
            </div>
          </Card>
        </div>

        <div className={this.state.visible ? '' : 'hidden'}>
          <Card title='修改成长日志'>
            <PractiseDiaryEditorBody
              practiseDiary={practiseDiary}
              updatePractiseDiary={this.props.updatePractiseDiary}
              closeModal={this.setModalVisible.bind(this)}
              operationType='update'
            />
          </Card>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  practiseDiaryAndComments: state.practiseDiariesInfo.practiseDiaryAndComments
})
const mapDispatchToProps = dispatch => ({
  deletePractiseDiary: (id, page, pageSize) => dispatch(actions.deletePractiseDiary(id, page, pageSize)),
  updatePractiseDiary: (practiseDiary, id) => dispatch(actions.updatePractiseDiary(practiseDiary, id))
})

export default connect(mapStateToProps, mapDispatchToProps)(PractiseDiary)
