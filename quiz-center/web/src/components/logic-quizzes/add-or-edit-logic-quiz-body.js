import React, { Component } from 'react'
import { Button, Col, Form, InputNumber, Row, Icon } from 'antd'
import { connect } from 'react-redux'
import { logicFormItemLayout, mdLayout } from '../../constant/constant-style'
import { withRouter } from 'react-router-dom'
import * as actions from '../../actions/logic-quiz'
import '../../less/single-choice-quiz/index.less'
import SelectQuizGroup from '../common/select-quiz-group'
import {TwsReactMarkdownEditor} from 'tws-antd'
import {getUploadUrl} from '../../constant/upload-url'
import * as cache from '../../constant/cache-quiz'
import {LOGIC_QUIZ} from '../../constant/quiz-type'
const FormItem = Form.Item

class AddOrEditLogicQuizBody extends Component {
  constructor (props) {
    super(props)
    this.state = {
      id: -1,
      quizGroupId: -1,
      quizGroupError: '',
      descriptionError: '',
      description: ''
    }
  }
  componentDidMount () {
    let localStorageItem = cache.getItemFromLocalStorage(LOGIC_QUIZ, -1)
    if (localStorageItem) {
      this.setState({...localStorageItem})
    }
  }
  cleanStateOrBack (isBack) {
    isBack ? this.props.history.push('/logicQuizzes') : this.props.form.resetFields()
  }

  handleSubmit (isBack) {
    let descriptionError = ''
    let quizGroupError = ''

    const {quizGroupId, description} = this.state
    if (quizGroupId < 0 || quizGroupId === null) {
      quizGroupError = '题组不能为空'
    }
    if (description === '') {
      descriptionError = '描述不能为空'
    }
    this.setState({descriptionError, quizGroupError})
    this.props.form.validateFields((err, values) => {
      const logicQuizzes = Object.assign({}, values, {quizGroupId, description})
      if (!err) { this.props.addLogicQuiz(logicQuizzes, () => this.cleanStateOrBack(isBack)) }
    })
  }
  changeDescription (description) {
    this.setState({description}, () => cache.saveItemToLocalStorage(LOGIC_QUIZ, this.state))
  }
  render () {
    const {getFieldDecorator} = this.props.form
    const {settings} = this.props
    return (
      <div>
        <Form>
          <Row>
            <Col span={18} offset={4}>
              <SelectQuizGroup
                changeQuizGroup={(quizGroupId) => { this.setState({quizGroupId: quizGroupId}) }}
                quizGroupName={''}
                quizGroupError={this.state.quizGroupError} />
            </Col>
          </Row>
          <Row>
            <Col span={18} offset={4}>
              <FormItem
                {...mdLayout}
                label='题目描述' required
            >
                <TwsReactMarkdownEditor
                  action={getUploadUrl(settings.appContextPath)}
                  value={this.state.description}
                  onChange={this.changeDescription.bind(this)} />
                <div className='error-tip'>{this.state.descriptionError}</div>
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={18} offset={4}>
              <FormItem {...mdLayout} label='时长(分钟)'>
                {getFieldDecorator('timeBoxInMinutes', {
                  rules: [{
                    required: true, message: '请输入时间'
                  }]
                })(
                  <InputNumber mode='timeBoxInMinutes'
                    min={1} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={4} offset={5}>
              <FormItem {...logicFormItemLayout} label={(<b> 简单 </b>)}>
                {getFieldDecorator('easyCount', {
                  rules: [{
                    required: true, message: '请输入简单题个数'
                  }]
                })(
                  <InputNumber mode='easyCount'
                    min={0} />
                )}
              </FormItem>
            </Col>
            <Col span={4}>
              <FormItem {...logicFormItemLayout} label={(<b> 一般 </b>)}>
                {getFieldDecorator('normalCount', {
                  rules: [{
                    required: true, message: '请输入一般题个数'
                  }]
                })(
                  <InputNumber mode='normalCount'
                    min={0} />
                )}
              </FormItem>
            </Col>
            <Col span={4}>
              <FormItem {...logicFormItemLayout} label={(<b> 困难 </b>)}>
                {getFieldDecorator('hardCount', {
                  rules: [{
                    required: true, message: '请输入困难题个数'
                  }]
                })(
                  <InputNumber mode='hardCount'
                    min={0} />
                )}
              </FormItem>
            </Col>
          </Row>
          <FormItem wrapperCol={{offset: 9}}>
            <Button type='primary' ghost style={{marginRight: 40}} onClick={this.handleSubmit.bind(this, true)}><Icon
              type='left' />添加并返回</Button>
            <Button type='primary' onClick={this.handleSubmit.bind(this, false)}>添加并继续<Icon type='right' /></Button>
          </FormItem>
        </Form>
      </div>
    )
  }
}

const mapStateToProps = ({logicQuiz, settings}) => ({
  logicQuiz,
  settings
})

const mapDispatchToProps = dispatch => ({
  addLogicQuiz: (quiz, callback) => dispatch(actions.addLogicQuiz(quiz, callback))
  // getLogicQuiz: (quizId) => dispatch(actions.getLogicQuizzes(quizId)),
  // editLogicQuiz: (quiz) => dispatch(actions.editQuiz(quiz))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditLogicQuizBody)))
