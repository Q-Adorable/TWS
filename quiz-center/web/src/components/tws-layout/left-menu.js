import React, { Component } from 'react'
import { Icon, Menu } from 'antd'
import { Link, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import UrlPattern from 'url-pattern'

import defaultSidebars from '../../constant/sidebar'

const SubMenu = Menu.SubMenu

class LeftMenu extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selectedKey: '0'
        }
    }

    componentDidMount() {
        const { history, location } = this.props
        history.listen(this.update.bind(this))

        this.update(location.pathname)
    }

    update(pathname) {
        const index = this.props.sideBars.findIndex((item) => this.find(item, pathname))
        let newIndex
        if (index >= 0) {
            newIndex = index + ''
        } else {
            // todo hardCode 出错风险太大了
            const subItem = this.props.sideBars[2].subMenu.find((item) => this.find(item, pathname))
            if (subItem) {
                if (subItem.index < 0) return
                newIndex = subItem.index + ''
            } else {
                return -1
            }
        }
        if (this.state.selectedKey === newIndex) return
        this.setState({
            selectedKey: newIndex
        })
    }

    find(item, pathname) {
        const currentLinkArray = item.linkTo.map((link) => {
            const pattern = new UrlPattern(link)
            const urlParams = pattern.match(this.props.location.pathname)
            return urlParams ? this.props.location.pathname : link
        })
        return currentLinkArray.includes(pathname)
    }

    render() {
        return (
            <Menu
                mode='inline'
                selectedKeys={[this.state.selectedKey]}
                style={{ height: '100%' }}
                defaultOpenKeys={['sub']}
            >
                {
                    this.props.sideBars.map((item, key) => {
                        if (item.subMenu) {
                            return <SubMenu key={'sub'}
                                title={<span>{item.name}<Icon type={item.icon}
                                    className='margin-l-2' /></span>}>
                                {item.subMenu.map((item1) => {

                                    return <Menu.Item key={item1.index}>
                                        <Link to={item1.linkTo[0]}
                                            onClick={() => this.update(item1.linkTo[0])}>{item1.name}
                                            <Icon type={item1.icon} className='margin-l-2' />
                                        </Link>
                                    </Menu.Item>
                                })
                                }
                            </SubMenu>
                        } else {
                            return (
                                <Menu.Item key={key}>
                                    <Link to={item.linkTo[0]} onClick={() => this.update(item.linkTo[0])}>
                                        {item.name}
                                        <Icon type={item.icon} className='margin-l-2' />
                                    </Link>
                                </Menu.Item>
                            )
                        }
                    })
                }
            </Menu>
        )
    }
}

function getSidebar(user) {
    return defaultSidebars
}

const mapStateToProps = ({ user }) => ({ sideBars: getSidebar(user) })
export default connect(mapStateToProps)(withRouter(LeftMenu))
