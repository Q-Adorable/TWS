import React, {Component} from 'react'
import {Card, Row, Col, Avatar} from 'antd'
import {connect} from 'react-redux'
import '../../style/App.css'
import * as follow from '../../action/follow'
import FollowComplete from './follow-complete'
import {withRouter} from 'react-router-dom'

class FolloweeList extends Component {
  componentDidMount () {
    this.props.getContactList()
  }

  getContactDiaries (followeeId) {
    this.props.history.push('/followees/' + followeeId)
  }

  getCompleteName (userInfo) {
    const name = userInfo.name || ''
    const userName = userInfo.userName || ''

    return `${name}(@${userName})`
  }
  unFollow (followeeId) {
    this.props.unFollow(followeeId)
  }

  render () {
    return (<div>
      <Row className='margin-b-3'>
        <Col span={24} className='evaluation-diary'>
          <a href='https://school.thoughtworks.cn/bbs/topic/728/%E5%A6%82%E4%BD%95%E8%AF%84%E4%BB%B7%E6%88%90%E9%95%BF%E6%97%A5%E5%BF%97' target='_blank' rel='noopener noreferrer'>如何评价成长日志</a>
        </Col>
      </Row>
      <Row className='margin-b-3'>
        <Col span={24}>
          <FollowComplete
            follow={this.props.follow}
          />
        </Col>
      </Row>
      <Row gutter={32} className='margin-b-1'>

        {this.props.contacts.map((contact, index) => {
          let userInfo = contact.userInfo || {}
          let practiseDiaryList = contact.practiseDiaryList || []
          const completeName = this.getCompleteName(userInfo)

          return <Col xs={{span: 8}} lg={{span: 8}} key={index}>
            <Card key={index} bordered noHovering className='practise-diary'
              title={
                <a onClick={this.getContactDiaries.bind(this, userInfo.id)}>
                  <div className='margin-t-3 center avatar-position'>
                    <Avatar style={{color: '#f56a00', backgroundColor: '#fde3cf'}}
                      size='large'>
                      <div
                        className='contact-avatar-name'>{userInfo.userName ? userInfo.userName.split('')[0] : ''}
                      </div>
                    </Avatar>
                  </div>
                  <div style={{color: '#808080'}} className='contact-name'> {completeName}</div>
                </a>}>

              <p className='title-text-position'>
                                        已经更新日志: {practiseDiaryList.length}篇</p>
              <p className='title-text-position'>
                                        最近一次更新时间: {practiseDiaryList.length > 0 ? practiseDiaryList[0].date.split(' ')[0] : ''}</p>
              <a onClick={this.unFollow.bind(this, userInfo.id)} style={{float: 'right'}}>取消关注</a>
            </Card>
          </Col>
        }
                    )}
      </Row>
    </div>
    )
  }
}

const mapStateToProps = state => ({contacts: state.diariesAndContactInfoList, contactUsers: state.contactUsers})

const mapDispatchToProps = dispatch => ({
  getContactList: () => dispatch(follow.getFolloweeListAndDiaries()),
  unFollow: (followeeId) => dispatch(follow.unFollow(followeeId)),
  follow: (followeeId) => dispatch(follow.follow(followeeId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(FolloweeList))
