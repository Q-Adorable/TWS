import React, { Component } from 'react'
import 'antd/dist/antd.css'
import { DatePicker, Form, Input, Button, Radio } from 'antd'
import moment from 'moment'
import '../../style/PractiseDiaryEditorBody.css'
import formItemLayOut from '../../constant/formItemLayout'
import { DIARY, GOAL } from '../../constant/diary-types'

const FormItem = Form.Item
const RadioGroup = Radio.Group;
const { TextArea } = Input

const DATE_FORMAT = 'YYYY/MM/DD'

const date = new Date()
const formatedDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
let storage = window.localStorage
let initDiaryContent = '## 我做了什么\n## 学了什么\n## 有什么印象深刻的收获'
let initGoalContent = '## 目标是什么\n## 目标会持续几天\n## 每天的具体行动是什么'
export default class PractiseDiaryEditorBody extends Component {
  constructor (props) {
    super(props)
    let practiseDiary = this.props.practiseDiary
    this.state = ({
      content: practiseDiary ? practiseDiary.content : initDiaryContent,
      date: practiseDiary ? practiseDiary.date : formatedDate,
      contentId: practiseDiary ? practiseDiary.id : -1,
      diaryType: DIARY
    })
  }

  componentWillReceiveProps (nextProps) {
    if (nextProps.practiseDiary) {
      this.setState({
        content: nextProps.practiseDiary.content
      })
    }

    if (storage) {
      Object.keys(storage).forEach((storageKey) => {
        if (parseInt(storageKey, 10) === this.state.contentId) {
          this.setState({
            content: JSON.parse(storage.getItem(this.state.contentId))
          })
        }
      })
    }
  }

  handleInputChange (tag, e) {
    const stateObject = {}
    storage.setItem(this.state.contentId, JSON.stringify(e.target.value))

    stateObject[tag] = e.target.value

    if (this.state.diaryType === DIARY) {
      initDiaryContent = e.target.value
    }
    if (this.state.diaryType === GOAL) {
      initGoalContent = e.target.value
    }

    this.setState(stateObject)
  }

  handleDateChange (date) {
    if (!date) return
    this.setState({
      date: date.format('YYYY/MM/DD')
    })
  }

  submitPractiseDiary () {
    if (this.props.operationType === 'update') {
      this.props.closeModal(false)
    }

    const practiseDiary = Object.assign({}, this.props.practiseDiary || {}, this.state)
    const methodName = (this.props.operationType || 'create') + 'PractiseDiary'
    this.props[methodName](practiseDiary, practiseDiary.id, practiseDiary.diaryType)
    this.setState({ content: '' }, () => {
      storage.removeItem(this.state.contentId)
    })
  }

  cancelEditPractiseDiary () {
    if (this.props.operationType === 'update') {
      this.props.closeModal(false)
    }
    this.setState({
      content: ''
    })
  }

  handleRadioClick = (e) => {
    const diaryType = e.target.value
    this.setState({
      content: diaryType === DIARY ? initDiaryContent : initGoalContent,
      diaryType
    })
  }

  render() {

    return (
      <div className='practise-diary-editor-body-content'>
        <FormItem {...formItemLayOut} label='类型'>
          <RadioGroup onChange={this.handleRadioClick} value={this.state.diaryType}>
            <Radio value={DIARY}>日志</Radio>
            <Radio value={GOAL}>目标</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem {...formItemLayOut} label='日期:'>
          <div className='practise-diary-editor-body-item'>
            <DatePicker
              defaultValue={moment(this.state.date, DATE_FORMAT)}
              onChange={this.handleDateChange.bind(this)}
              format={DATE_FORMAT} />
          </div>
        </FormItem>
        <FormItem {...formItemLayOut} label='总结内容:'>
          <TextArea rows={5}
            value={this.state.content}
            onChange={this.handleInputChange.bind(this, 'content')}
          />
        </FormItem>

        <div className=' practise-diary-operation-button-group'>
          <Button type='primary' size='small' ghost className='button-note'
            onClick={this.submitPractiseDiary.bind(this)}>提交</Button>

          <Button size='small'
            className='button-note'
            onClick={this.cancelEditPractiseDiary.bind(this)}>取消</Button>
        </div>
      </div>
    )
  }
}
