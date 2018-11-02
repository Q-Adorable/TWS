import React from 'react'
import UrlPattern from 'url-pattern'
import {withRouter} from 'react-router-dom'
import {Form, Input} from 'antd'
import {connect} from 'react-redux'
import SelectQuizGroup from '../common/select-quiz-group'
import {formItemLayout, mdLayout} from '../../constant/constant-style'
import SelectTags from '../common/select-tags'
import * as actions from '../../actions/subjective-quizzes'
import {TwsReactMarkdownEditor} from 'tws-antd'
import ButtonGroup from '../common/button-group'
import {getUploadUrl} from '../../constant/upload-url'
import {SUBJECTIVE_QUIZ} from '../../constant/quiz-type'
import * as cache from '../../constant/cache-quiz'

const FormItem = Form.Item

class AddOrEditSubjectiveQuizBody extends React.Component {
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
      remark: ''
    }
  }

  componentDidMount () {
    const pattern = new UrlPattern('/subjectiveQuizzes/:id/editor')
    const urlParams = pattern.match(this.props.location.pathname) || {id: -1}
    this.setState({id: parseInt(urlParams.id, 10)})
    let localStorageItem = cache.getItemFromLocalStorage(SUBJECTIVE_QUIZ, parseInt(urlParams.id, 10))
    if (localStorageItem) {
      this.setState({...localStorageItem})
      this.props.form.setFieldsValue({
        remark: localStorageItem.remark
      })
      return
    }
    
    if (urlParams.id > 0) {
      this.props.getSubjectiveQuiz(parseInt(urlParams.id, 10))
    }
  }

  componentWillReceiveProps (nextProps) {
    const {subjectiveQuiz} = nextProps

    if (this.state.id > 0 && subjectiveQuiz && this.props.subjectiveQuiz !== subjectiveQuiz) {
      this.setState({
        description: subjectiveQuiz.description,
        tags: subjectiveQuiz.tags.map(tag => tag.name),
        quizGroupName: subjectiveQuiz.quizGroupName,
        quizGroupId: subjectiveQuiz.quizGroupId,
        remark:subjectiveQuiz.remark
      })
      const tagsNames = subjectiveQuiz.tags.map(tag => tag.name)
      nextProps.form.setFieldsValue({
        tags: tagsNames,
        remark: subjectiveQuiz.remark
      })
    }
  }

  cleanStateOrBack (isBack) {
    isBack ? this.props.history.push('/subjectiveQuizzes') : this.setState({
      description: '',
      descriptionError: '',
      tags: [],
      quizGroupError: '',
      quizGroupName: ''
    })
  }

  handleSubmit (isBack) {
    const {id, description, tags, quizGroupId} = this.state
    if (!this.state.description) {
      this.setState({descriptionError: '描述不能为空'})
    } else if (this.state.quizGroupId < 0 || this.state.quizGroupId === null) {
      this.setState({quizGroupError: '题组选择不能为空'})
    } else {
      this.props.form.validateFields((err, values) => {
        if (!err) {
          let localStorageDescription = window.localStorage.getItem('description')
          if (localStorageDescription) {
            window.localStorage.removeItem('description')
          }
          const quiz = Object.assign({}, values, {id, description, tags, quizGroupId})
          id > 0 ? this.props.editSubjectiveQuiz(quiz, () => this.cleanStateOrBack(isBack)) : this.props.addSubjectiveQuiz(quiz, () => this.cleanStateOrBack(isBack))
        }
      })
    }
    cache.removeItemFromLocalStorage(SUBJECTIVE_QUIZ, id)
  }

  changeDescription (description) {
    this.setState({description}, () => cache.saveItemToLocalStorage(SUBJECTIVE_QUIZ, this.state))
  }
  handleAddTags (tags) {
    this.setState({tags}, () => cache.saveItemToLocalStorage(SUBJECTIVE_QUIZ, this.state))
  }
  handleChangeValue (e) {
    let remark = e.target.value
    this.setState({remark}, () => cache.saveItemToLocalStorage(SUBJECTIVE_QUIZ, this.state))
  }
  render () {
    const {getFieldDecorator} = this.props.form
    const {settings} = this.props
    const {descriptionError, tags, id, quizGroupName} = this.state
    return (<Form>
      <SelectQuizGroup
        changeQuizGroup={(quizGroupId) => {
          this.setState({quizGroupId: quizGroupId})
        }}
        quizGroupName={quizGroupName}
        quizGroupError={this.state.quizGroupError} />
      <FormItem
        {...mdLayout}
        label='题目描述' required
            >
        <TwsReactMarkdownEditor value={this.state.description}
          action={getUploadUrl(settings.appContextPath)}
          onChange={this.changeDescription.bind(this)} />
        <div className='error-tip'>{descriptionError}</div>
      </FormItem>
      <FormItem
        {...formItemLayout}
        label='备注'
            >
        {getFieldDecorator('remark', {
          rules: [{
            required: true, message: '备注信息'
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

const mapStateToProps = (state) => ({
  subjectiveQuiz: state.subjective.quiz,
  settings: state.settings
})

const mapDispatchToProps = (dispatch) => ({
  addSubjectiveQuiz: (subjectiveQuiz, callback) => dispatch(actions.addQuiz(subjectiveQuiz, callback)),
  getSubjectiveQuiz: (quizId) => dispatch(actions.getQuiz(quizId)),
  editSubjectiveQuiz: (quiz, callback) => dispatch(actions.editQuiz(quiz, callback))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditSubjectiveQuizBody)))
