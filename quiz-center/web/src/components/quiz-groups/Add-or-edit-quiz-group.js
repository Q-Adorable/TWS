import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../actions/quiz-group'
import { Form, Input } from 'antd'
import { mdLayout } from '../../constant/constant-style'
import ButtonGroup from '../common/button-group'
import { TwsReactMarkdownEditor } from 'tws-antd'
import {getUploadUrl} from '../../constant/upload-url'
const FormItem = Form.Item

class QuizGroup extends Component {
  constructor (props) {
    super(props)
    this.state = {
      id: -1,
      name: '',
      nameError: '',
      description: ''
    }
  }

  componentDidMount () {
    this.setState({id: parseInt(this.props.match.params.id, 10)}, () => {
      const {id} = this.state
      if (id > 0) {
        this.props.getQuizGroup(id)
      }
    })
  }

  componentWillReceiveProps (nextProps) {
    const quizGroup = nextProps.quizGroup
    if (quizGroup && quizGroup !== this.props.singleChoiceQuiz) {
      const {description, name, id} = quizGroup
      this.setState({id, description, name})
    }
  }

  cleanStateOrBack (isBack) {
    isBack ? this.props.history.push('/quizGroups') : this.setState({
      description: '',
      name: '',
      nameError: ''
    })
  }

  handleSubmit (isBack) {
    if (!this.state.name) {
      this.setState({nameError: '题组名称不能为空'})
    } else {
      const {id, description, name} = this.state
      let localStorageDescription = window.localStorage.getItem('description')
      if (localStorageDescription) {
        window.localStorage.removeItem('description')
      }
      const quizGroup = Object.assign({}, {id, description, name})
      id > 0 ? this.props.editQuizGroup(quizGroup, () => this.cleanStateOrBack(isBack)) : this.props.addQuizGroup(quizGroup, () => this.cleanStateOrBack(isBack))
    }
  }

  render () {
    const {nameError, id} = this.state
    const {settings} = this.props
    return (<Form>
      <FormItem
        {...mdLayout}
        label='题组名称' required
        >
        <Input value={this.state.name} onChange={(e) => { this.setState({name: e.target.value}) }} />
        <div className='error-tip'>{nameError}</div>
      </FormItem>
      <FormItem
        {...mdLayout}
        label='题组描述'
        >
        <TwsReactMarkdownEditor
          action={getUploadUrl(settings.appContextPath)}
          value={this.state.description}
          onChange={(description) => { this.setState({description}) }} />
      </FormItem>
      <FormItem wrapperCol={{offset: 9}}>
        <ButtonGroup handleSubmit={this.handleSubmit.bind(this)} id={id} />
      </FormItem>
    </Form>
    )
  }
}
const mapStateToProps = ({quizGroupData, settings}) => ({
  quizGroup: quizGroupData.quizGroup,
  settings
})

const mapDispatchToProps = dispatch => ({
  addQuizGroup: (quizGroup, callback) => dispatch(actions.addQuizGroup(quizGroup, callback)),
  editQuizGroup: (quizGroup, callback) => dispatch(actions.editQuizGroup(quizGroup, callback)),
  getQuizGroup: (quizGroupId) => dispatch(actions.getQuizGroup(quizGroupId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(QuizGroup))
