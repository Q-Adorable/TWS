import React, {Component} from 'react'
import {Radio} from 'antd'
import {TwsReactMarkdownPreview} from 'tws-antd'
const RadioGroup = Radio.Group

export default class SingleChoice extends Component {
  changeAnswer (quiz, e) {
    const answer = e.target.value
    this.props.modifyAnswer(quiz, answer.toString())
  }

  getValue (singleQuiz) {
    const preview = this.props.preview
    if (preview) {
      return parseInt(singleQuiz.answer, 10)
    }
    if (singleQuiz) {
      return parseInt(singleQuiz.userAnswer, 10)
    }
    return -1
  }

  getCorrect (singleQuiz) {
    if (singleQuiz.isCorrect) {
      return '**✓**'
    } else if (singleQuiz.isCorrect === '') {
      return ''
    } else {
      return '*✗*'
    }
  }
  getAnswer (singleQuiz) {
    const choices = singleQuiz.choices || []
    const correctChoice = choices.find((choice, index) => index === parseInt(singleQuiz.answer)) || {}
    return correctChoice.choice
  }
  render () {
    const singleQuiz = this.props.quiz
    const isCorrect = this.getCorrect(singleQuiz)
    const answer = `参考答案：${(this.getAnswer(singleQuiz))}`
    const status = singleQuiz.answer || this.props.isTutor
    return <div className='margin-b-3'>
      <TwsReactMarkdownPreview source={`${singleQuiz.description}  ${isCorrect}`} />
      <RadioGroup
        onChange={this.changeAnswer.bind(this, singleQuiz)}
        defaultValue={this.getValue(singleQuiz)}
        disabled={!!status}>
        {singleQuiz.choices.map((option, index) => <Radio className='margin-l-4 radio-style' key={index} value={index}>{option.choice}</Radio>)}
      </RadioGroup>
      {status ? <TwsReactMarkdownPreview source={answer} /> : ''}
    </div>
  }
}
