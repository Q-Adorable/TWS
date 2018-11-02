import React from 'react'
import {Form, Input} from 'antd'
import {withRouter} from 'react-router-dom'
import {connect} from 'react-redux'

import {formItemLayout} from '../../constant/constant-style'
import UrlPattern from 'url-pattern'
import * as actions from '../../actions/tag'
import ButtonGroup from '../common/button-group'

const FormItem = Form.Item

class AddOrEditTags extends React.Component {
  constructor (props) {
    super(props)

    this.state = {
      id: -1
    }
  }

  componentWillMount () {
    const pattern = new UrlPattern('/tags/:id/editor')
    const urlParams = pattern.match(this.props.location.pathname) || {id: -1}
    this.setState({id: parseInt(urlParams.id, 10)})

    if (urlParams.id > 0) {
      this.props.getTag(parseInt(urlParams.id, 10))
    }
  }

  componentWillReceiveProps (nextProps) {
    const {tag} = nextProps

    if (tag && this.props.tag !== tag) {
      nextProps.form.setFieldsValue({
        name: tag.name
      })
    }
  }

  cleanStateOrBack (isBack) {
    isBack ? this.props.history.push('/tags') : this.props.form.resetFields()
  }

  handleSubmit (isBack) {
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const {id} = this.state
        const tag = Object.assign({}, values, {id})

        id > 0 ? this.props.editTag(tag, () => this.cleanStateOrBack(isBack)) : this.props.addTag(tag, () => this.cleanStateOrBack(isBack))
      }
    })
  }

  render () {
    const { getFieldDecorator } = this.props.form

    return (<Form>
      <FormItem {...formItemLayout} label='名称'>
        {getFieldDecorator('name', {
          rules: [{
            required: true, message: '请输入标签名称'
          }]
        })(
          <Input className='min-width' />
        )}
      </FormItem>
      <FormItem wrapperCol={{offset: 7}}>
        <ButtonGroup handleSubmit={this.handleSubmit.bind(this)} id={this.state.id} />
      </FormItem>
    </Form>)
  }
}

const mapStateToProps = ({tagData}) => ({
  tag: tagData.tag
})

const mapDispatchToProps = (dispatch) => ({
  addTag: (tag, callback) => dispatch(actions.addTag(tag, callback)),
  getTag: (tagId) => dispatch(actions.getTag(tagId)),
  editTag: (tag, callback) => dispatch(actions.editTag(tag, callback))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditTags)))
