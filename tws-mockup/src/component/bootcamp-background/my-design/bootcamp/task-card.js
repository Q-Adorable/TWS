import React, { Component } from 'react'
import { Row, Col, Card, Button, Icon, Input, Form, Select, Modal } from 'antd'
import QuizTable from './quiz-table'

const FormItem = Form.Item
const Option = Select.Option

export default class TaskCard extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isShowNewQuiz: false,
      isShowQuizRecourse: false,
      isChooseBasic: true
    }
  }

  showModal = () => {
    this.setState({
      isShowNewQuiz: true
    })
  }

  handleAddQuizOk = () => {
    if (this.state.courseResourse === 'oldQuiz') {
      this.setState({
        isShowNewQuiz: false,
        isShowQuizRecourse: true
      })
    } else {
      this.setState({
        isShowNewQuiz: false
      })
    }
  }

  handleChooseQuizOk = () => {

  }

  handleCancel = () => {
    this.setState({
      isShowNewQuiz: false,
      isShowQuizRecourse: false
    })
  }

  handleQuizResourse = (value) => {
    this.setState({
      courseResourse: value
    })
  }

  handleQuizTypeResourse = (value) => {
    if (value === 'basicQuiz') {
      this.setState({
        isChooseBasicQuiz: true
      })
    } else {
      this.setState({
        isChooseBasicQuiz: false
      })
    }
  }

  render() {
    return (
      <Row gutter={16} style={{ marginBottom: '15px' }}>
        <Col className='gutter-row' span={23}>
          <Card>
            <Col span={18} style={{ fontSize: '18', fontWeight: 'bold' }}>任务卡1</Col>
            <Col span={3}>
              <Button type='primary' onClick={this.showModal}>新增题目</Button>
              <Modal
                style={{ textAlign: 'center' }}
                title='新增题目'
                visible={this.state.isShowNewQuiz}
                closable={false}
                okText='确定'
                cancelText='取消'
                onOk={this.handleAddQuizOk}
                onCancel={this.handleCancel}
                destroyOnClose={true}
              >
                <Form>
                  <FormItem
                    label='名称'
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 13 }}
                  >
                    <Input style={{ width: '100%' }} />
                  </FormItem>
                  <FormItem
                    label='题目类型'
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 13 }}
                  >
                    <Select
                      style={{ width: '100%' }}
                      onChange={this.handleQuizTypeResourse}
                    >
                      <Option value='basicQuiz'>客观题</Option>
                      <Option value='subjectiveQuiz'>主观题</Option>
                      <Option value='logicQuiz'>TW逻辑题</Option>
                      <Option value='homeworkQuiz'>单栈编程题</Option>
                      <Option value='onlineCodingQuiz'>多语言在线编程题</Option>
                      <Option value='onlineLanguageQuiz'>单语言在线编程题</Option>
                    </Select>
                  </FormItem>
                  <FormItem
                    label='题目来源'
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 13 }}
                  >
                    <Select
                      style={{ width: '100%' }}
                      onChange={this.handleQuizResourse}
                    >
                      <Option value='newQuiz'>新建题目</Option>
                      <Option value='oldQuiz'>查看题库现有题目</Option>
                    </Select>
                  </FormItem>
                </Form>
              </Modal>
              <Modal
                style={{ textAlign: 'center' }}
                width='90%'
                title='选择题目'
                visible={this.state.isShowQuizRecourse}
                closable={false}
                okText='确定'
                cancelText='返回'
                okButtonProps={{ size: 'large' }}
                cancelButtonProps={{ size: 'large' }}
                onOk={this.handleChooseQuizOk}
                onCancel={this.handleCancel}
                destroyOnClose={true}
              >
                <QuizTable isChooseBasicQuiz={this.state.isChooseBasicQuiz} />
              </Modal>
            </Col>
            <Col span={1}><Icon type='eye' theme='outlined' style={{ fontSize: '22px' }} /></Col>
            <Col span={1}><Icon type='edit' theme='outlined' style={{ fontSize: '22px' }} /></Col>
            <Col span={1}><Icon type='delete' theme='outlined' style={{ fontSize: '22px' }} /></Col>
          </Card>
        </Col>
      </Row>
    )
  }
}
