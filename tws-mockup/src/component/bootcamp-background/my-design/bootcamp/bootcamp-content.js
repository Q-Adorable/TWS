import React, { Component } from 'react'
import { Button, Modal } from 'antd'
import TaskCardFrom from './task-card-form'
import TaskCard from './task-card'

class BootcampContent extends Component {
  constructor (props) {
    super(props)
    this.state = { display: false, taskCardNumber: [] }
  }

  showButtonModal = () => {
    this.setState({
      display: true
    })
  }

  handleButtonOk = () => {
    const taskCard = this.state.taskCardNumber
    taskCard.push(1)
    this.setState({
      display: false,
      taskCardNumber: taskCard
    })
  }

  handleCancel = () => {
    this.setState({
      display: false
    })
  }

  render () {
    return (
      <div>
        <div style={{ marginTop: '20px', marginLeft: '30px' }}>
          {this.state.taskCardNumber.map((taskCard) => { return <TaskCard /> })}
        </div>
        <Button style={{ marginTop: '20px', marginLeft: '30px' }} size='large' icon={'plus'}
          onClick={this.showButtonModal}>新增任务卡</Button>
        <Modal
          title='新建任务卡'
          visible={this.state.display}
          onOk={this.handleButtonOk}
          onCancel={this.handleCancel}
          width={400}
        >
          <TaskCardFrom />
        </Modal>
      </div>
    )
  }
}

export default BootcampContent
