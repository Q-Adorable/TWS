import React from 'react'
import { Form, Input, Button, Spin, Card, message } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import { TwsReactMarkdownEditor } from 'tws-antd'
import { formItemLayout, mdLayout } from '../../constant/constant-style'
import * as actions from '../../actions/stack'
import { STATUS} from '../../constant/constant-data'
import {getUploadUrl} from '../../constant/upload-url'
import '../../less/index.less'

const FormItem = Form.Item
class AddOrEditStacks extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      description: '',
      descriptionError: '',
      imageStatus: '',
      isShowResult: false,
      isShowSpin: false
    }
  }

  handleSubmit () {
    const {description} = this.state
    if (!this.state.description) {
      this.setState({descriptionError: '描述不能为空'})
    }
    this.props.form.validateFields((err, values) => {
      if (!err) {
        values = Object.assign({}, values, {description})
        this.props.addStack(values)
        this.setState({isShowResult: true, isShowSpin: true})
      }
    })
  }

  componentWillReceiveProps (nextProps) {
    this.setState({imageStatus: nextProps.stackStatus}, () => {
      if (!(nextProps.logsData.logs === this.props.logsData.logs && this.props.stackStatus === STATUS.SUCCESS)) {
        if (nextProps.stack.id < 0) { return }
        this.props.searchStackStatus(nextProps.stack.id)
      } else {
        message.success('success')
        this.setState({isShowSpin: false})
      }
    })
  }

  render () {
    const {getFieldDecorator} = this.props.form
    const {settings} = this.props
    const {isShowSpin, isShowResult} = this.state
    return (<Form>
      <FormItem {...mdLayout} label='技术栈描述' required>
        <TwsReactMarkdownEditor
          action={getUploadUrl(settings.appContextPath)}
          value={this.state.description}
          onChange={(description) => this.setState({description})} />
      </FormItem>
      <FormItem {...formItemLayout} label='image'>
        {getFieldDecorator('image', {
          rules: [{
            required: true, message: '请输入image,格式如：node:8-alpine'
          }]
        })(
          <Input style={{width: '100%'}} />
        )}
        <div className='error-tip'>{this.state.descriptionError}</div>
      </FormItem>
      <FormItem wrapperCol={{span: 20, offset: 2}}>
        <div className='gray'>{isShowResult
          ? <Card style={{width: '100%'}}>
            {isShowSpin ? <div><Spin /><span className='yellow'>请稍等...</span></div> : ''}
            <div>{this.props.logsData.logs}</div>
          </Card>
          : ''}</div>
      </FormItem>
      <FormItem wrapperCol={{span: 8, offset: 8}}>
        <Button type='primary' onClick={this.handleSubmit.bind(this)}>添加</Button>
      </FormItem>
    </Form>)
  }
}

const mapStateToProps = ({stackData, settings}) => ({
  stackStatus: stackData.status,
  logsData: stackData.logs,
  stack: stackData.stack,
  settings
})

const mapDispatchToProps = (dispatch) => ({
  addStack: (stack) => dispatch(actions.addStack(stack)),
  searchStackStatus: (stackId) => dispatch(actions.searchStackStatus(stackId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditStacks)))
