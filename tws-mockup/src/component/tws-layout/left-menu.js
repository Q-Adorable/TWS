import React, { Component } from 'react'
import { Menu, Icon } from 'antd'
import defaultSidebars from '../../constant/sidebar'

const { SubMenu } = Menu
class LeftMenu extends Component {
  render () {
    return (
      <Menu
        mode='inline'
        className='menu-header'
        defaultOpenKeys={['训练营']}
        defaultSelectedKeys={['训练营后台']}
            >
        {
          defaultSidebars.map((subMenu) => {
            return (
              <SubMenu
                key={subMenu.name}
                title={
                  <span>
                    <Icon type={subMenu.icon} className='margin-l-2' />
                    {subMenu.name}
                  </span>
                  }
                >
                {
                    subMenu.items.map((item) => {
                      return (
                        <Menu.Item key={item.name}>
                          <Icon type={item.icon} />
                          {item.name}
                        </Menu.Item>
                      )
                    })
                  }
              </SubMenu>
            )
          }
          )
        }
      </Menu>
    )
  }
}
export default LeftMenu
