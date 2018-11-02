import React, {Component} from 'react'
import { withRouter } from 'react-router-dom'
import {Layout, Row, Col} from 'antd'

import TwsUserInfo from './tws-user-info'
import LeftMenu from './left-menu'
import TwsBreadCrumb from './tws-bread-crumb'
import logo from '../../images/logo-white.png'
import '../../less/tws-layout.less'
import Notification from './notification'

const { Header, Content, Footer, Sider } = Layout

class TwsLayout extends Component {
  handleOnClick (notificationId, callback) {
    this.props.updateUnReadToReadById(notificationId, callback)
  }
  render () {
    const currentYear = new Date().getFullYear()

    return (
      <Layout>
        <Header className='App-header'>
          <Row>
            <Col span={6}>
              <img src={logo} className='App-logo' alt='logo' />
            </Col>
            <Col span={14}>
              <TwsUserInfo />
            </Col>
            <Col span={1} className='header-bell'>
              <Notification />
            </Col>
          </Row>
        </Header>
        <Content className='main-content'>
          <TwsBreadCrumb />
          <Layout className='content-layout'>
            <Sider width={200} className='content-layout-sider'>
              <LeftMenu />
            </Sider>
            <Content className='content-children-content'>
              {this.props.children}
            </Content>
          </Layout>
        </Content>
        <Footer className='footer'>
            ThoughtWorks School ©{currentYear}
        </Footer>
      </Layout>
    )
  }
}

export default withRouter(TwsLayout)
