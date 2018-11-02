import React, { Component } from 'react'
import logo from '../../images/logo-white.png'
import '../../less/index.less'
import {TwsLayout} from 'tws-antd'
import BreadCrumbs from './course-center-breadcrumb'
import {connect} from 'react-redux'
import * as action from '../../actions/user'
import * as notificationActions from '../../actions/notification'

class Index extends Component {
  componentDidMount () {
    this.props.getUserInfo()
  }
  handleOnClick (notificationId, callback) {
    this.props.updateUnReadToReadById(notificationId, callback)
  }
  render () {
    const {user, settings, notifications} = this.props
    const { notificationMoreUrl, logoutUrl, userCenterHomeUrl } = settings
    return (
      <TwsLayout
        userName={user.userName || ''}
        logoutUrl={logoutUrl}
        userCenterHomeUrl={userCenterHomeUrl}
        lang='en' logo={logo}
        onChange={console.log}
        twsBreadcrumb={<BreadCrumbs settings={settings} />}
        moreUrl={notificationMoreUrl}
        notifications={notifications}
        handleOnClick={this.handleOnClick.bind(this)}
      >
        {this.props.children}
      </TwsLayout>

    )
  }
}
const mapStateToProps = state => ({user: state.user, notifications: state.notifications, settings: state.settings})
const mapDispatchToProps = dispatch => ({
  getUserInfo: () => dispatch(action.initUser()),
  updateUnReadToReadById: (notificationId, callback) => { dispatch(notificationActions.updateUnReadToReadById(notificationId, callback)) }

})
export default connect(mapStateToProps, mapDispatchToProps)(Index)
