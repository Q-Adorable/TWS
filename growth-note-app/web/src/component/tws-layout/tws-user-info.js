import React, { Component } from 'react'
import { Icon, Menu, Dropdown, Button } from 'antd'

export default class TwsUserInfo extends Component {
  handleMenuClick (userCenterHomeUrl, logoutUrl, e) {
    if (e.key === '1') {
      window.location.href = userCenterHomeUrl
    } else if (e.key === '2') {
      window.location.href = logoutUrl
    }
  }
  render () {
    const {user, userCenterHomeUrl, logoutUrl} = this.props

    const menu = (
      <Menu onClick={this.handleMenuClick.bind(this, userCenterHomeUrl, logoutUrl)}>
        <Menu.Item key='1'>
          <span>
            <Icon type='user' className='margin-r-1' />个人中心
      </span>
        </Menu.Item>
        <Menu.Item key='2'>
          <span>
            <Icon type='close' className='margin-r-1' />退出
      </span>
        </Menu.Item>
      </Menu>
    )
    return (
      <Dropdown overlay={menu} placement='bottomRight'>
        <Button style={{'float': 'right'}} className='margin-t-3'>{user.userName} <Icon type='down' /></Button>
      </Dropdown>
    )
  }
}
