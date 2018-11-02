import React, { Component } from 'react'
import { Card, Col, Button, Modal } from 'antd'
import { Link } from 'react-router-dom'

const confirm = Modal.confirm;

export default class BootcampCard extends Component {
  constructor (props) {
    super(props)
  }

  handleClick = () => {
    confirm({
      title: '添加到「我设计的」?',
      centered: true,
      okText: '确定',
      cancelText: '取消',
      onOk() {},
      onCancel() {},
    })
  }

  render () {
    return (
      <Col span={8}>
        <div className='trainingCourse__div'>
          <Card
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
            actions={[<Button onClick={this.handleClick}>复制</Button>]}
            key={this.props.key}
          />
        </div>
      </Col>
    )
  }
}
