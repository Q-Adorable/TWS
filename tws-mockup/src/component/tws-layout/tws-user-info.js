import React, { Component } from 'react'
import { Icon, Menu, Dropdown, Button } from 'antd'

export default class TwsUserInfo extends Component {
  render () {
    const menu = (
      <Menu>
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
        <Button className='margin-t-3 header-dropdown'>admin<Icon type='down' /></Button>
      </Dropdown>
    )
  }
}
