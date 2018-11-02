import React, {Component} from 'react'
import {Form, Icon, Input, Radio, Tooltip} from 'antd'
import {connect} from 'react-redux'
import {formItemLayout, inputStyle, mdLayout, radioStyle} from '../../constant/constant-style'
import {withRouter} from 'react-router-dom'
import * as actions from '../../actions/single-choice-quiz'
import UrlPattern from 'url-pattern'
import SelectTags from '../common/select-tags'
import SelectQuizGroup from '../common/select-quiz-group'
import '../../less/single-choice-quiz/index.less'
import {TwsReactMarkdownEditor} from 'tws-antd'
import ButtonGroup from '../common/button-group'
import {SINGLE_CHOICE} from '../../constant/quiz-type'
import * as cache from '../../constant/cache-quiz'

import {getUploadUrl} from '../../constant/upload-url'
const FormItem = Form.Item
const RadioGroup = Radio.Group

class AddOrEditSingleChoiceQuizBody extends Component {
  constructor () {
    super()
    this.state = {
      id: -1,
      answer: '',
      choices: ['', '', '', ''],
      description: '',
      tags: [],
      quizGroupId: -1,
      quizGroupError: '',
      quizGroupName: '',
      choiceError: '',
      answerError: '',
      descriptionError: ''
    }
  }

  componentDidMount () {
    const pattern = new UrlPattern('/singleChoiceQuizzes/:id/editor')

    const urlParams = pattern.match(this.props.location.pathname) || {id: -1}
    this.setState({id: parseInt(urlParams.id, 10)})
    let localStorageItem = cache.getItemFromLocalStorage(SINGLE_CHOICE, parseInt(urlParams.id, 10))
    if (localStorageItem) {
      this.setState({...localStorageItem})
      return
    }
    if (urlParams.id > 0) {
      this.props.getSingleChoiceQuiz(urlParams.id)
    }
  }

  componentWillReceiveProps (nextProps) {
    const singleChoiceQuiz = nextProps.singleChoiceQuiz
    if (singleChoiceQuiz && singleChoiceQuiz !== this.props.singleChoiceQuiz) {
      let {tags, description, answer, choices, quizGroupName, id, quizGroupId} = singleChoiceQuiz
      tags = tags.map(tag => tag.name)
      this.setState({id, choices, answer, description, tags, quizGroupName, quizGroupId})
    }
  }

  isError (choices, answer, description, quizGroupId) {
    let choiceError = ''
    let answerError = ''
    let descriptionError = ''
    let quizGroupError = ''
    if (quizGroupId < 0 || quizGroupId === null) {
      quizGroupError = '题组不能为空'
    }

    if (!answer && answer !== 0) {
      answerError = '答案不能为空'
    }

    if (description === '') {
      descriptionError = '描述不能为空'
    }

    if (choiceError !== '' || answerError !== '' || descriptionError !== '' || quizGroupError !== '') {
      this.setState({choiceError: choiceError, answerError, descriptionError, quizGroupError})
      return true
    }
    return false
  }

  cleanStateOrBack (isBack) {
    isBack ? this.props.history.push('/singleChoiceQuizzes') : this.setState({
      answer: '',
      choices: ['', '', '', ''],
      description: '',
      tags: [],
      quizGroupError: '',
      quizGroupName: '',
      choiceError: '',
      answerError: '',
      descriptionError: ''
    })
  }

  handleSubmit (isBack) {
    const {choices, answer, id, description, tags, quizGroupId} = this.state
    if (!this.isError(choices, answer, description, quizGroupId)) {
      const values = Object.assign({}, this.props.singleChoiceQuiz, {
        choices,
        answer,
        description,
        tags,
        quizGroupId
      })
      const editValues = Object.assign({}, values, {id})

      id > 0 ? this.props.editSingleChoiceQuiz(editValues, () => this.cleanStateOrBack(isBack)) : this.props.addSingleChoiceQuiz(values, () => this.cleanStateOrBack(isBack))
    }
    cache.removeItemFromLocalStorage(SINGLE_CHOICE, id)
  }

  radioOnChange (e) {
    this.setState({
      answer: e.target.value,
      answerError: ''
    }, () => cache.saveItemToLocalStorage(SINGLE_CHOICE, this.state))
  }
  optionOnChange (index, e) {
    const {choices} = this.state
    choices[index] = e.target.value
    this.setState({
      choices,
      choiceError: ''
    }, () => cache.saveItemToLocalStorage(SINGLE_CHOICE, this.state))
  }

  handleDeleteSelectItem (index) {
    let {choices} = this.state
    choices = choices.filter((choice, idx) => index !== idx)
    this.setState({choices}, () => cache.saveItemToLocalStorage(SINGLE_CHOICE, this.state))
  }

  handleAddSelectItem () {
    let {choices} = this.state
    choices.push('')
    this.setState({choices}, () => cache.saveItemToLocalStorage(SINGLE_CHOICE, this.state))
  }
  handleChange (description) {
    this.setState({description}, () => cache.saveItemToLocalStorage(SINGLE_CHOICE, this.state))
  }
  handleAddTags (tags) {
    this.setState({tags}, () => cache.saveItemToLocalStorage(SINGLE_CHOICE, this.state))
  }
  render () {
    const {choices, answer, choiceError, answerError, description, descriptionError, tags, id, quizGroupName} = this.state
    const {settings} = this.props
    return (
      <div>
        <Form>
          <SelectQuizGroup
            changeQuizGroup={(quizGroupId) => {
              this.setState({quizGroupId: quizGroupId})
            }}
            quizGroupName={quizGroupName}
            quizGroupError={this.state.quizGroupError} />
          <FormItem
            {...mdLayout}
            label={'题目描述'}
                    >
            <TwsReactMarkdownEditor
              action={getUploadUrl(settings.appContextPath)}
              value={description}
              onChange={this.handleChange.bind(this)} />
            <div className='error-tip'>{descriptionError}</div>
          </FormItem>

          <FormItem {...formItemLayout}
            label={'选项'} required>
            <RadioGroup onChange={this.radioOnChange.bind(this)} value={answer}>
              {choices.map((option, index) => {
                return (
                  <Radio style={radioStyle} className='margin-b-3' value={`${index}`} key={index}>
                    <Input style={inputStyle}
                      value={option}
                      onChange={this.optionOnChange.bind(this, index)}
                                        />
                    {index > 1 && this.state.id <= 0
                    ? <Tooltip title='删除选项'>
                      <Icon type='minus-circle-o' style={operationStyle} onClick={this.handleDeleteSelectItem.bind(this, index)} />
                    </Tooltip>
                      : ''}
                  </Radio>

                )
              })}
            </RadioGroup>
            <div className='error-tip'>{answerError || choiceError}</div>
            {this.state.id <= 0
              ? <Tooltip title='添加一个选项'>
                <Icon type='plus-circle-o' style={operationStyle} onClick={this.handleAddSelectItem.bind(this)} />
              </Tooltip>
              : ''
            }

          </FormItem>
          <SelectTags
            addTags={this.handleAddTags.bind(this)}
            currentTags={tags}
                    />
          <FormItem wrapperCol={{offset: 9}}>
            <ButtonGroup handleSubmit={this.handleSubmit.bind(this)} id={id} />
          </FormItem>
        </Form>
      </div>
    )
  }
}

const operationStyle = {
  fontSize: '20px',
  marginLeft: '10px',
  cursor: 'pointer'

}
const mapStateToProps = ({singleChoice, settings}) => ({
  singleChoiceQuiz: singleChoice.quiz,
  settings
})

const mapDispatchToProps = dispatch => ({
  addSingleChoiceQuiz: (quiz, callback) => dispatch(actions.addQuiz(quiz, callback)),
  getSingleChoiceQuiz: (quizId) => dispatch(actions.getQuiz(quizId)),
  editSingleChoiceQuiz: (quiz, callback) => dispatch(actions.editQuiz(quiz, callback))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditSingleChoiceQuizBody)))
