import React from 'react'
import { withRouter } from 'react-router-dom'
import UrlPattern from 'url-pattern'
import { Form, Input } from 'antd'
import { connect } from 'react-redux'

import { formItemLayout, mdLayout } from '../../constant/constant-style'
import SelectTags from '../common/select-tags'
import * as actions from '../../actions/blank-quiz'
import { TwsReactMarkdownEditor } from 'tws-antd'
import ButtonGroup from '../common/button-group'
import SelectQuizGroup from '../common/select-quiz-group'
import {BLANK_QUIZ} from '../../constant/quiz-type'
import * as cache from '../../constant/cache-quiz'

import {getUploadUrl} from '../../constant/upload-url'
const FormItem = Form.Item

class AddOrEditBlankQuizBody extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      id: -1,
      description: '',
      descriptionError: '',
      tags: [],
      quizGroupId: -1,
      quizGroupError: '',
      quizGroupName: '',
      answer: ''
    }
  }

  componentDidMount () {
    const pattern = new UrlPattern('/blankQuizzes/:id/editor')
    const urlParams = pattern.match(this.props.location.pathname) || {id: -1}
    this.setState({id: parseInt(urlParams.id, 10)})
    let localStorageItem = cache.getItemFromLocalStorage(BLANK_QUIZ, parseInt(urlParams.id, 10))
    if (localStorageItem) {
      this.setState({...localStorageItem})
      this.props.form.setFieldsValue({
        answer: localStorageItem.answer
      })
      return
    }
    if (urlParams.id > 0) {
      this.props.getBlankQuiz(parseInt(urlParams.id, 10))
    }
  }

  componentWillReceiveProps (nextProps) {
    const {blankQuiz} = nextProps

    if (this.state.id > 0 && blankQuiz && this.props.blankQuiz !== blankQuiz) {
      const tags = blankQuiz.tags.map(tag => tag.name)
      this.setState({description: blankQuiz.description, tags, quizGroupName: blankQuiz.quizGroupName, quizGroupId: blankQuiz.quizGroupId})
      nextProps.form.setFieldsValue({
        answer: blankQuiz.answer
      })
    }
  }

  cleanStateOrBack (isBack) {
    isBack ? this.props.history.push('/blankQuizzes') : this.setState({
      description: '',
      descriptionError: '',
      tags: []
    }, () => {
      this.props.form.resetFields()
    })
  }

  handleSubmit (isBack) {
    this.setState({descriptionError: ''})
    const {id, description, tags, quizGroupId} = this.state
    if (quizGroupId < 0 || quizGroupId === null) {
      this.setState({quizGroupError: '请选择题组'})
    } else if (this.state.description === '') {
      this.setState({descriptionError: '请输入描述'})
    } else {
      this.props.form.validateFields((err, values) => {
        if (!err) {
          const quiz = Object.assign({}, values, {id, description, tags, quizGroupId})
          id > 0 ? this.props.editBlankQuiz(quiz, () => this.cleanStateOrBack(isBack)) : this.props.addBlankQuiz(quiz, () => this.cleanStateOrBack(isBack))
        }
      })
    }
    cache.removeItemFromLocalStorage(BLANK_QUIZ, id)
  }
  handleChange (description) {
    this.setState({description}, () => cache.saveItemToLocalStorage(BLANK_QUIZ, this.state))
  }
  handleAddTags (tags) {
    this.setState({tags}, () => cache.saveItemToLocalStorage(BLANK_QUIZ, this.state))
  }
  handleChangeValue (e) {
    let answer = e.target.value
    this.setState({answer}, () => cache.saveItemToLocalStorage(BLANK_QUIZ, this.state))
  }
  render () {
    const {getFieldDecorator} = this.props.form
    const {settings} = this.props
    const {id, description, tags, descriptionError, quizGroupName} = this.state
    return (<Form>

      <SelectQuizGroup
        changeQuizGroup={(quizGroupId) => { this.setState({quizGroupId: quizGroupId}) }}
        quizGroupName={quizGroupName}
        quizGroupError={this.state.quizGroupError} />
      <FormItem
        {...mdLayout}
        label='题目描述' required
      >
        <TwsReactMarkdownEditor
          action={getUploadUrl(settings.appContextPath)}
          value={description}
          onChange={this.handleChange.bind(this)} />
        <div className='error-tip'>{descriptionError}</div>
      </FormItem>
      <FormItem
        {...formItemLayout}
        label='答案'
      >
        {getFieldDecorator('answer', {
          rules: [{
            required: true, message: '请输入答案'
          }]
        })(
          <Input onChange={this.handleChangeValue.bind(this)} />
        )}
      </FormItem>
      <SelectTags addTags={this.handleAddTags.bind(this)} currentTags={tags} />
      <FormItem wrapperCol={{offset: 9}}>
        <ButtonGroup handleSubmit={this.handleSubmit.bind(this)} id={id} />
      </FormItem>
    </Form>)
  }
}

const mapStateToProps = ({blank, settings}) => ({
  blankQuiz: blank.quiz,
  settings
})

const mapDispatchToProps = (dispatch) => ({
  addBlankQuiz: (quiz, callback) => dispatch(actions.addQuiz(quiz, callback)),
  getBlankQuiz: (quizId) => dispatch(actions.getQuiz(quizId)),
  editBlankQuiz: (quiz, callback) => dispatch(actions.editQuiz(quiz, callback))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditBlankQuizBody)))
