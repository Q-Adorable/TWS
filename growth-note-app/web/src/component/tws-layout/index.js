import React, {Component} from 'react'
import { withRouter } from 'react-router-dom'
import {connect} from 'react-redux'
import {Layout, Row, Col} from 'antd'

import TwsUserInfo from './tws-user-info'
import LeftMenu from './left-menu'
import TwsBreadCrumb from './tws-bread-crumb'
import logo from '../../images/logo-white.png'
import '../../less/tws-layout.less'
import Notification from './notification'
import * as notificationActions from '../../action/notification'

const { Header, Content, Footer, Sider } = Layout

class TwsLayout extends Component {
  handleOnClick (notificationId, callback) {
    this.props.updateUnReadToReadById(notificationId, callback)
  }
  render () {
    const currentYear = new Date().getFullYear()
    const {user, settings, notifications} = this.props
    const { userCenterHomeUrl, logoutUrl, notificationMoreUrl } = settings

    return (
      <Layout>
        <Header className='App-header' style={{lineHeight: '36px'}}>
          <Row>
            <Col span={6}>
              <img src={logo} className='App-logo' alt='logo' />
            </Col>
            <Col span={14}>
              <TwsUserInfo userCenterHomeUrl={userCenterHomeUrl} logoutUrl={logoutUrl} user={user} />
            </Col>
            <Col span={1} style={{marginTop: '17px', textAlign: 'right'}}>
              <Notification
                moreUrl={notificationMoreUrl}
                notifications={notifications}
                handleOnClick={this.handleOnClick.bind(this)}
              />
            </Col>
          </Row>
        </Header>
        <Content style={{ padding: '0 50px' }}>
          <TwsBreadCrumb />
          <Layout style={{ padding: '24px 0', background: '#fff' }}>
            <Sider width={200} style={{ background: '#fff' }}>
              <LeftMenu />
            </Sider>
            <Content style={{ padding: '0 24px', minHeight: 280 }}>
              {this.props.children}
            </Content>
          </Layout>
        </Content>
        <Footer style={{ textAlign: 'center' }}>
            ThoughtWorks School ©{currentYear}
        </Footer>
      </Layout>
    )
  }
}
const mapStateToProps = state => ({user: state.user, settings: state.settings, notifications: state.notifications})
const mapDispatchToProps = dispatch => ({
  updateUnReadToReadById: (notificationId, callback) => { dispatch(notificationActions.updateUnReadToReadById(notificationId, callback)) }
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(TwsLayout))
