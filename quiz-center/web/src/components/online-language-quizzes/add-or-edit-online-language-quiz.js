import React, { Component } from 'react'
import { Form, Input, Button, Select, message } from 'antd'
import { connect } from 'react-redux'
import { formItemLayout } from '../../constant/constant-style'
import { withRouter } from 'react-router-dom'

import * as actions from '../../actions/online-language-quiz'
import UrlPattern from 'url-pattern'
import SelectTags from '../common/select-tags'
import {getAllStacks} from '../../actions/stack'
import { STATUS } from '../../constant/constant-data'
import AceEditor from 'react-ace';

import 'brace/theme/monokai';
const languages = [
  'javascript',
  'java',
  'csharp',
  'python',
  'xml',
  'ruby',
  'sass',
  'markdown',
  'mysql',
  'json',
  'html',
  'handlebars',
  'golang',
  'elixir',
  'typescript',
  'css',
  'c_cpp'
]

languages.forEach((lang) => {
  require(`brace/mode/${lang}`)
  require(`brace/snippets/${lang}`)
})

const FormItem = Form.Item
const Option = Select.Option

class AddOrEditOnlineLanguageQuizBody extends Component {
  constructor () {
    super()
    this.state = {
      id: -1,
      repository: '',
      stackId: 0,
      tags: [],
      stackError: '',
      language: '',
      repositoryError: '',
      isShowResult: false,
      languages:[]
    }
  }
  
  
  componentDidMount () {
    
    this.props.getAllStacks()
    this.props.getAllLanguages(()=>{this.setLanguages()})
    const pattern = new UrlPattern('/onlineLanguageQuizzes/:id/editor')

    const urlParams = pattern.match(this.props.location.pathname) || {id: -1}
    this.setState({id: parseInt(urlParams.id, 10)})
    if (urlParams.id > 0) {
      this.props.getLanguageQuiz(urlParams.id)
    }
  }

  componentWillReceiveProps (nextProps) {
    
    const {onlineLanguageQuiz} = nextProps
    const {code} = nextProps.status

    if (onlineLanguageQuiz && onlineLanguageQuiz !== this.props.onlineLanguageQuiz) {
      let {id, tags, stackId, onlineLanguageName, language, description, initCode, testData, answerDescription, answer, remark} = onlineLanguageQuiz
      tags = tags.map(tag => tag.name)
      this.setState({id, tags, stackId, language})
      nextProps.form.setFieldsValue({tags, stackId, title: onlineLanguageName, language, description, initCode, testData, answerDescription, answer, remark})
    } else if (this.state.isShowResult) {
      if (code === STATUS.SUCCESS) {
        message.success('添加成功', 3)
      } else if (code === STATUS.ERROR) {
        message.error('添加失败', 3)
      }
      this.setState({isShowResult: false})
    }
  }

  handleSubmit (e) {
    e.preventDefault()
    const {id, tags} = this.state
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState({isShowResult: true})
        values = Object.assign({}, values, {tags})
        id > 0 ? this.props.editLanguageQuiz(values, id) : this.props.addLanguageQuiz(values)
      } else {
        this.setState({isShowResult: false})
        message.warning('请填写相关字段')
      }
    })
  }

  setTemplate(){
    this.props.form.setFieldsValue({initCode:this.props.initCode.initCode,testData:this.props.initCode.testcase})
  }
  setLanguages(){
    this.setState({languages:this.props.languages})
    // this.setState({languages:languages})
  }
  languageSelect(value){
    console.log(value)
    this.props.getLanguageTemplate(value,()=>{this.setTemplate()})
    this.setState({language: value})
  
  }
  render () {
    const {getFieldDecorator} = this.props.form
    const {tags, language, id} = this.state

    return (<Form onSubmit={this.handleSubmit.bind(this)}>
      <FormItem
        label='名称'
        {...formItemLayout}
      >
        {getFieldDecorator('title', {rules: [{required: true, message: '请填写题目名称'}]})(<Input />)}
      </FormItem>

      <FormItem
        label='语言'
        {...formItemLayout}
      >
        {getFieldDecorator('language', {
          rules: [{required: true, message: '请填写题目语言'}]
        })(
          <Select
            placeholder='请选择语言'
            onSelect={this.languageSelect.bind(this)}>
            {/* {this.props.stacks.map(stack => <Option key={stack.id} value={stack.id}>{stack.title}</Option>)} */}
            {this.state.languages.map((language, index) => <Option key={index} value={language}>{language}</Option>)}
          
          </Select>
        )}
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
        label='题目描述'
        {...formItemLayout}
      >
        {getFieldDecorator('description', {
          initialValue: '//请填写题目描述',
          rules: [{required: true, message: '请填写题目描述'}]
        })(
          <AceEditor
            mode='markdown'
            theme='monokai'
            fontSize={14}
            width={'100%'}
            showPrintMargin
            showGutter
            highlightActiveLine
            setOptions={{
              enableBasicAutocompletion: false,
              enableLiveAutocompletion: true,
              enableSnippets: false,
              showLineNumbers: true,
              tabSize: 2
            }} />
        )}
      </FormItem>

      <FormItem
        label='测试数据'
        {...formItemLayout}
      >
        {getFieldDecorator('testData', {
          initialValue: '',
          rules: [{required: true, message: '请填写测试数据'}]
        })(
          <AceEditor
            mode='json'
            theme='monokai'
            fontSize={14}
            width={'100%'}
            showPrintMargin
            showGutter
            highlightActiveLine
            setOptions={{
              enableBasicAutocompletion: false,
              enableLiveAutocompletion: true,
              enableSnippets: false,
              showLineNumbers: true,
              tabSize: 2
            }} />
        )}
      </FormItem>

      <FormItem
        label='启动代码'
        {...formItemLayout}
      >
        {getFieldDecorator('initCode', {
          // initialValue: '123',
          rules: [{required: true, message: '请填写启动代码'}]
        })
        (
          <AceEditor
            mode={language}
            theme='monokai'
            fontSize={14}
            width={'100%'}
            showPrintMargin
            showGutter
            highlightActiveLine
            setOptions={{
              enableBasicAutocompletion: false,
              enableLiveAutocompletion: true,
              enableSnippets: false,
              showLineNumbers: true,
              tabSize: 2
            }}
            />
        )}
      </FormItem>

      <FormItem
        label='答案描述'
        {...formItemLayout}
      >
        {getFieldDecorator('answerDescription', {
          initialValue: '//请填写答案描述',
          rules: [{required: true, message: '请填写答案描述'}]
        })(
          <AceEditor
            mode='markdown'
            theme='monokai'
            fontSize={14}
            width={'100%'}
            showPrintMargin
            showGutter
            highlightActiveLine
            setOptions={{
              enableBasicAutocompletion: false,
              enableLiveAutocompletion: true,
              enableSnippets: false,
              showLineNumbers: true,
              tabSize: 2
            }} />
        )}
      </FormItem>

      <FormItem
        label='参考答案'
        {...formItemLayout}
      >
        {getFieldDecorator('answer', {
          initialValue: '//请填写参考答案',
          rules: [{required: true, message: '请填写参考答案'}]
        })(
          <AceEditor
            mode={language}
            theme='monokai'
            fontSize={14}
            width={'100%'}
            showPrintMargin
            showGutter
            highlightActiveLine
            setOptions={{
              enableBasicAutocompletion: false,
              enableLiveAutocompletion: true,
              enableSnippets: false,
              showLineNumbers: true,
              tabSize: 2
            }} />
        )}
      </FormItem>

      <FormItem
        label='备注'
        {...formItemLayout}
      >
        {getFieldDecorator('remark', {rules: []})(<Input />)}
      </FormItem>

      <SelectTags
        addTags={(tags) => this.setState({tags})}
        currentTags={tags} />
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

const mapStateToProps = ({onlineLanguage, stackData}) => ({
  onlineLanguageQuiz: onlineLanguage.quiz,
  stacks: stackData.stacks.content,
  quizId: onlineLanguage.id,
  status: onlineLanguage.status,
  initCode: onlineLanguage.initCode,
  languages: onlineLanguage.languages
})

const mapDispatchToProps = dispatch => ({
  addLanguageQuiz: (quiz) => dispatch(actions.addQuiz(quiz)),
  getLanguageQuiz: (quizId) => dispatch(actions.getQuiz(quizId)),
  editLanguageQuiz: (quiz, id) => dispatch(actions.editQuiz(quiz, id)),
  getAllStacks: () => dispatch(getAllStacks()),
  getLanguageTemplate: (language,callback) => dispatch(actions.getLanguageTemplate(language,callback)),
  getAllLanguages:(callback) => dispatch(actions.getAllLanguages(callback))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(AddOrEditOnlineLanguageQuizBody)))
