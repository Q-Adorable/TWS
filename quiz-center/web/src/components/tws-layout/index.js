import React, {Component} from 'react'
import { Layout } from 'antd'

import {TwsLayout} from 'tws-antd'
import LeftMenu from './left-menu'
import BreadCrumbs from './tws-bread-crumb'
import {connect} from 'react-redux'
import logo from '../../images/logo-white.png'
import {withRouter} from 'react-router-dom'
import {updateUnReadToReadById} from '../../actions/notification'

const {Sider, Content} = Layout
class Index extends Component {
  render () {
    const {user, settings, notifications} = this.props
    const { userName = '' } = user
    const { userCenterHomeUrl = '', logoutUrl = '', notificationMoreUrl } = settings

    return (
      <TwsLayout
        userName={userName}
        logoutUrl={logoutUrl}
        userCenterHomeUrl={userCenterHomeUrl}
        lang='en'
        logo={logo}
        onChange={console.log}
        twsBreadcrumb={<BreadCrumbs />}
        moreUrl={notificationMoreUrl}
        notifications={notifications}
        handleOnClick={this.props.updateUnReadToReadById}
      >
        <Content>
          <Layout style={{ padding: '24px 0', background: '#fff' }}>
            <Sider width={200} style={{ background: '#fff' }}>
              <LeftMenu />
            </Sider>
            <Content style={{ padding: '0 24px', minHeight: 280 }}>
              {this.props.children}
            </Content>
          </Layout>
        </Content>
      </TwsLayout>

    )
  }
}
const mapStateToProps = state => ({
  user: state.userInfo.user,
  settings: state.settings,
  notifications: state.notifications
})
const mapDispatchToProps = dispatch => ({
  updateUnReadToReadById: (notificationId, callback) => { dispatch(updateUnReadToReadById(notificationId, callback)) }
})
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Index))
