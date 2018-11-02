import React, {Component} from 'react'
import {connect} from 'react-redux'
import {message} from 'antd'

// import * as comments from '../../action/index'
import * as follow from '../../action/follow'
import HTTP_CODE from '../../constant/httpCode'
import * as request from '../../constant/fetchRequest'

class FollowOptionLink extends Component {
  toggleFollow (item) {
    if (item.followed) {
      (async () => {
        const res = await request.del(`./api/followees/${item.id}`)
        if (res.status === HTTP_CODE.NO_CONTENT) {
          message.success('取消关注成功')
          this.props.getContactList()
        }
      })()
    } else {
      (async () => {
        const res = await request.post(`./api/followees/${item.id}`)
        if (res.status === HTTP_CODE.CREATED) {
          message.success('关注成功')
          this.props.getContactList()
        }
      })()
    }
  }

  render () {
    const {item} = this.props
    const actionStr = item.followed ? <span style={{float: 'right', color: '#C0C0C0'}}>已关注</span>
      : <span style={{float: 'right'}}>未关注</span>
    return actionStr
  }
}

const mapStateToProps = state => ({contacts: state.diariesAndContactInfoList, contactUsers: state.contactUsers})

const mapDispatchToProps = dispatch => ({
  getContactList: () => dispatch(follow.getFolloweeListAndDiaries()),
  search: (value) => dispatch(follow.search(value)),
  saveContactUer: (contactUserId) => dispatch(follow.saveFollowee(contactUserId)),
  getContactDiariesAndComs: (contactUserId) => dispatch(follow.getFolloweeDiariesAndComments(contactUserId))
})

export default connect(mapStateToProps, mapDispatchToProps)(FollowOptionLink)
