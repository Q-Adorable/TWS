import React, {Component} from 'react'
import {Badge, Icon, Popover} from 'antd'

class Notification extends Component {
  state = {
    clicked: false,
    hovered: false
  };

  handleHoverChange = (visible) => {
    this.setState({
      hovered: visible,
      clicked: false
    })
  }

  render () {
    const hoverContent = (<div style={{width: '400px'}}>
      {
        <p className='messageStyle'>
          <div>暂无最新消息</div>
        </p>
                }
      <p className='moreStyle'>
        <a href='/href'>
                        查看全部
                    </a>
      </p>
    </div>
        )
    return (
      <Popover
        content={hoverContent}
        title='最新消息'
        trigger='hover'
        placement='bottom'
        visible={this.state.hovered}
        onVisibleChange={this.handleHoverChange}
        arrowPointAtCenter
            >
        <Badge count={0}>
          <Icon type='bell' className='popover-badge-icon' />
        </Badge>
      </Popover>)
  }
}

export default Notification
