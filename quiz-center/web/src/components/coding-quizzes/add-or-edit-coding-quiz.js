import React, { Component } from 'react'
import { Form, Input, Button, Select, Card, Spin, message } from 'antd'
import { connect } from 'react-redux'
import { formItemLayout } from '../../constant/constant-style'
import { withRouter } from 'react-router-dom'

import * as actions from '../../actions/coding-quiz'
import UrlPattern from 'url-pattern'
import SelectTags from '../common/select-tags'
import { getAllStacks } from '../../actions/stack'
import { STATUS } from '../../constant/constant-data'
import SelectQuizGroup from '../common/select-quiz-group'

const FormItem = Form.Item
const Option = Select.Option

class AddOrEditCodingQuizBody extends Component {
  constructor () {
    super()
    this.state = {
      id: -1,
      repository: '',
      stackId: 0,
      tags: [],
      stackError: '',
      repositoryError: '',
      isShowSpin: false,
      isShowResult: false,
      quizGroupId: -1,
      quizGroupError: '',
      quizGroupName: '',
    }
  }


  componentDidMount () {
    const pattern = new UrlPattern('/codingQuizzes/:id/editor')

    const urlParams = pattern.match(this.props.location.pathname) || {id: -1}
    this.setState({id: parseInt(urlParams.id, 10)})
    if (urlParams.id > 0) {
      this.props.getCodingQuiz(urlParams.id)
      console.log("this.props.codingQuiz",this.props.codingQuiz)
    }
    this.props.getAllStacks()
  }

  componentWillReceiveProps (nextProps) {
    const {codingQuiz, status, quizId} = nextProps
    const {code} = this.props.status
    const nowLogs = this.props.status.logs
    
    if (codingQuiz && codingQuiz !== this.props.codingQuiz) {
      const tags = codingQuiz.tags.map(tag => tag.name)
      const {id, stackId, definitionRepo, homeworkName, remark,quizGroupName} = codingQuiz
      this.setState({id, tags, stackId, definitionRepo,quizGroupName})
      nextProps.form.setFieldsValue({tags, stackId, definitionRepo, title: homeworkName, remark})
    } else if (status.logs !== nowLogs || (code !== STATUS.SUCCESS && code !== STATUS.ERROR)) {
      if (quizId) {
        this.props.getLogs(quizId)
      }
    } else if (this.state.isShowSpin) {
      if (code === STATUS.SUCCESS) {
        message.success('添加成功', 3)
      } else if (code === STATUS.ERROR) {
        message.error('添加失败', 3)
      }
      this.setState({isShowSpin: false})
    }
  }

  handleSubmit (e) {
    e.preventDefault()
    const {id, tags, quizGroupId} = this.state
    if (quizGroupId < -1 || quizGroupId === null) {
      this.setState({quizGroupError: '题组不能为空'})
    } else {
      this.props.form.validateFields((err, values) => {
        if (!err) {
          this.setState({isShowSpin: true, isShowResult: true})
          values = Object.assign({}, values, {tags, quizGroupId})
          id > 0 ? this.props.editCodingQuiz(values, id) : this.props.addCodingQuiz(values)
        }
      })
    }
  }

  render () {
    const {getFieldDecorator} = this.props.form
    const {tags, isShowResult, isShowSpin, id, quizGroupName} = this.state
    
    return (<Form onSubmit={this.handleSubmit.bind(this)}>
      <SelectQuizGroup
        changeQuizGroup={(quizGroupId) => {this.setState({quizGroupId: quizGroupId})}}
        quizGroupName={quizGroupName}
        quizGroupError={this.state.quizGroupError}/>

      <FormItem
        label='名称'
        {...formItemLayout}
      >
        {getFieldDecorator('title', {rules: [{required: true, message: '请填写题目名称'}]})(<Input />)}
      </FormItem>

      <FormItem
        label='备注'
        {...formItemLayout}
      >
        {getFieldDecorator('remark', {rules: []})(<Input />)}
      </FormItem>

      <FormItem
        label='stack'
        {...formItemLayout}
      >
        {getFieldDecorator('stackId', {
          rules: [{required: true, message: '请选择技术栈!'}]
        })(
          <Select
            placeholder='请选择技术栈'>
            {this.props.stacks.map(stack => <Option key={stack.id} value={stack.id}>{stack.title}</Option>)}
          </Select>
        )}
      </FormItem>

      <FormItem
        label='repository'
        {...formItemLayout}
      >
        {getFieldDecorator('definitionRepo', {
          rules: [{
            required: true,
            pattern: new RegExp(/^(?:https:\/\/)?(?:github\.com\/)(?:[^ ]+)(?:\/)(?:[^ ]+)$/),
            message: '请填入github地址!'
          }]
        })(
          <Input className='min-width'/>
        )}
      </FormItem>

      <SelectTags
        addTags={(tags) => this.setState({tags})}
        currentTags={tags}/>
      {isShowResult
        ? <Card style={{width: '100%'}}>
          {isShowSpin ? <Spin /> : ''}
          <div>{this.props.status.logs}</div>
        </Card>
        : ''}
      <FormItem
        wrapperCol={{offset: 9}}
      >
        <Button type='primary' htmlType='submit'>
          {id > 0 ? '修改' : '添加'}
        </Button>
      </FormItem>
    </Form>)
  }
}

const mapStateToProps = ({coding, stackData}) => ({
  codingQuiz: coding.quiz,
  stacks: stackData.stacks.content,
  quizId: coding.id,
  status: coding.status
})

const mapDispatchToProps = dispatch => ({
  addCodingQuiz: (quiz) => dispatch(actions.addQuiz(quiz)),
  getCodingQuiz: (quizId) => dispatch(actions.getQuiz(quizId)),
  editCodingQuiz: (quiz, id) => dispatch(actions.editQuiz(quiz, id)),
  getAllStacks: () => dispatch(getAllStacks()),
  getLogs: (quizId) => dispatch(actions.getLogs(quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditCodingQuizBody)))
