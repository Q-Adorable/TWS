import React, { Component } from 'react'
import { Card, Col, Modal, Radio } from 'antd'
import plusImage from '../../../images/plus.png'
import BootcampCreatorForm from './bootcamp-creator-form'
import { connect } from 'react-redux'

const RadioButton = Radio.Button
const RadioGroup = Radio.Group

class BootcampCreatorChoice extends Component {
  constructor(props) {
    super(props)
    this.state = { 
      isShowChoice: false, 
      isShowCreateBootcamp: false,
      choiceMode: 'new'
    }
  }

  showModal = () => {
    this.setState({
      isShowChoice: true,
      isShowCreateBootcamp: false
    })
  }

  handleOk = () => {
    if (this.state.choiceMode === 'new') {
      this.setState({
        isShowChoice: false,
        isShowCreateBootcamp: true,
      })
    } else {
      this.setState({
        isShowChoice: false,
        isShowCreateBootcamp: false,
      })
    }
  }

  handleCreate = () => {
    this.setState({
      isShowChoice: false,
      isShowCreateBootcamp: false,
    })
  }

  handleCancel = () => {
    this.setState({
      isShowChoice: false,
      isShowCreateBootcamp: false
    })
  }

  handleChange = (value) => {
    this.setState({
      choiceMode: value.target.value,
    })
  }

  render () {
    return (
      <div>
        <Col span={8}>
          <Card
            onClick={this.showModal}
            title='新建训练营'
            className='trainingCourse__card'
            hoverable
            cover={<img alt='new course' src={plusImage} className='trainingCourse__image' />}
          />
        </Col>
        <Modal
          visible={this.state.isShowChoice}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
          okText="确认"
          cancelText="取消"
          closable={false}
          width={450}
        >
          <RadioGroup defaultValue='new' onChange={this.handleChange}>
            <RadioButton value='new' className='choice-button'>新建训练营</RadioButton>
            <RadioButton value='createByCopy' className='choice-button'>复制对我可见的训练营</RadioButton>
          </RadioGroup>
        </Modal>
        <Modal
          title='新建训练营'
          visible={this.state.isShowCreateBootcamp}
          onOk={this.handleCreate}
          onCancel={this.handleCancel}
          okText="确认"
          cancelText="取消"
          width={400}
        >
          <BootcampCreatorForm />
        </Modal>
      </div>
    )
  }
}

const mapStateToProps = () => ({})
const mapDispatchToProps = (dispatch) => ({
  addBootcamp: () => {
    dispatch({
      type: 'ADD_BOOTCAMP',
      data: {}
    })
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(BootcampCreatorChoice)
