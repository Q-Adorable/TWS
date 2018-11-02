import React, { Component } from 'react'
import { Input, Col, Row } from 'antd'
import { TwsReactMarkdownPreview } from 'tws-antd'

export default class BasicBlankQuiz extends Component {
  changeAnswer (blankQuiz, e) {
    this.props.modifyAnswer(blankQuiz, e.target.value)
  }

  getValue (blankQuiz) {
    if (blankQuiz.isCorrect) {
      return '**✓**'
    } else if (blankQuiz.isCorrect === '') {
      return ''
    } else {
      return '*✗*'
    }
  }

  render () {
    let blankQuiz = this.props.quiz
    let isCorrect = this.getValue(blankQuiz)
    const answer = `参考答案：${blankQuiz.answer}`
    return <div className='margin-b-3'>
      <TwsReactMarkdownPreview source={` ${this.props.index + 1}.${blankQuiz.description} ${isCorrect}`} />
      <Row>
        {blankQuiz.answer || this.props.isTutor
          ? <Col span={24} style={{margin: '5px 0 25px 0'}}>
            <TwsReactMarkdownPreview source={blankQuiz.userAnswer} />
            <div className='margin-b-1' />
            <TwsReactMarkdownPreview source={answer} />
          </Col>
          : <Col span={20}>
            <Input onChange={this.changeAnswer.bind(this, blankQuiz)} />
          </Col>
        }
      </Row>
    </div>
  }
}
