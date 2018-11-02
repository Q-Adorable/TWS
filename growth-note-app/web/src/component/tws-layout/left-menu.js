import React, { Component } from 'react'
import { Menu, Icon } from 'antd'
import { Link, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'

import defaultSidebars from '../../constant/sidebar'

class LeftMenu extends Component {
  constructor (props) {
    super(props)
    this.state = {
      selectedKey: '0'
    }
  }

  componentDidUpdate () {
    const {location} = this.props
    this.update(location)
  }

  componentDidMount () {
    const {history, location} = this.props
    history.listen(this.update.bind(this))
    this.update(location)
  }

  update (location) {
    const index = this.props.sideBars.findIndex((item) => {
      return item.linkTo === location.pathname.match(/(^\/[\w-]*)/g)[0]
    })
    if (index < 0) return
    const newIndex = '' + index
    if (this.state.selectedKey === newIndex) return
    this.setState({
      selectedKey: newIndex
    })
  }

  render () {
    return (
      <Menu
        mode='inline'
        selectedKeys={[this.state.selectedKey]}
        style={{height: '100%'}}
            >
        {
                    this.props.sideBars.map((item, key) => {
                      return (
                        <Menu.Item key={key}>
                          <Link to={item.linkTo}>
                            {item.name}
                            <Icon type={item.icon}
                              className='margin-l-2' />
                          </Link>
                        </Menu.Item>)
                    })
                }
      </Menu>
    )
  }
}

function getSidebar (user) {
  const sidebars = defaultSidebars.filter((bar) => {
    if (!bar.hasRole) return true
    const hasRole = [].concat(bar.hasRole)
    if (user.roles) {
      return hasRole.some(v => user.roles.includes(v))
    }
    return false
  })
  return sidebars || []
}

const mapStateToProps = ({user}) => ({sideBars: getSidebar(user)})
export default connect(mapStateToProps)(withRouter(LeftMenu))
