import React, {Component} from 'react'
import {Checkbox} from 'antd'
import {TwsReactMarkdownPreview} from 'tws-antd'

export default class MultipleChoice extends Component {
  changeAnswer (quiz, selects) {
    this.props.modifyAnswer(quiz, selects.filter(s => !isNaN(s)).sort().join(','))
  }

  getValue (multipleChoiceQuiz) {
    const preview = this.props.preview
    if (preview) {
      return multipleChoiceQuiz.answer.split(',').map(answer => parseInt(answer, 10))
    }
    if (multipleChoiceQuiz) {
      return multipleChoiceQuiz.userAnswer.split(',').map(answer => parseInt(answer, 10))
    }
    return []
  }

  getCorrect (multipleChoiceQuiz) {
    if (multipleChoiceQuiz.isCorrect) {
      return '**✓**'
    } else if (multipleChoiceQuiz.isCorrect === '') {
      return ''
    } else {
      return '*✗*'
    }
  }
  getAnswer (multipleChoiceQuiz) {
    const correctChoices = multipleChoiceQuiz.answer || ''
    const answersArray = correctChoices.split(',')
    const choices = multipleChoiceQuiz.choices || []
    return answersArray.map((answerIndex) => {
      const correctChoice = choices.find((choice, index) => parseInt(answerIndex) === index) || {}
      return correctChoice.choice
    }).join(`、`)
  }

  render () {
    const multipleChoiceQuiz = this.props.quiz
    const isCorrect = this.getCorrect(multipleChoiceQuiz)
    const answer = `参考答案：${this.getAnswer(multipleChoiceQuiz)}`
    const status = multipleChoiceQuiz.answer || this.props.isTutor > 0
    return <div className='margin-b-3'>
      <TwsReactMarkdownPreview source={`${this.props.index + 1}.${multipleChoiceQuiz.description} ${isCorrect}`} />
      <Checkbox.Group className='margin-l-3'
        onChange={this.changeAnswer.bind(this, multipleChoiceQuiz)}
        disabled={!!status}
        defaultValue={this.getValue(multipleChoiceQuiz)}
        >
        {
            multipleChoiceQuiz.choices.map((option, index) => {
              return <Checkbox className='margin-l-1 radio-style' key={index} value={index}>{option.choice}</Checkbox>
            })
          }
      </Checkbox.Group>
      {status ? <TwsReactMarkdownPreview source={answer} /> : ''}
    </div>
  }
}
