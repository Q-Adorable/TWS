import React, { Component } from 'react'
import { Card, Col, Icon } from 'antd'
import { Link } from 'react-router-dom'

export default class BootcampCard extends Component {
  constructor (props) {
    super(props)
    this.state = {
      menu: []
    }
  }

  handleMouseMove = () => {
    this.setState({
      menu: [
        <Icon className='action-icon' title='提交招生' type='user-add' />,
        <Icon className='action-icon' title='查看吐槽' type='eye' />,
        <Icon className='action-icon' title='停止运行' type='stop' />
      ]
    })
  }

  handleMouseLeave = () => {
    this.setState({
      menu: []
    })
  }

  render () {
    return (
      <Col span={8}>
        <div className='trainingCourse__div'>
          <Card
            onMouseMove={this.handleMouseMove}
            onMouseLeave={this.handleMouseLeave}
            extra={<label>{this.props.trainingCourse.status}</label>}
            title={this.props.trainingCourse.name}
            className='trainingCourse__card'
            hoverable
            cover={
              <Link to='/bootcamp'>
                <img alt={this.props.trainingCourse.name} src={this.props.trainingCourse.image}
                  className='trainingCourse__image'
                />
              </Link>}
            actions={this.state.menu}
            key={this.props.key}
          />
        </div>
      </Col>
    )
  }
}
