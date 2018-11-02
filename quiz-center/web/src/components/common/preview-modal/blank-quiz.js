import React from 'react'

const BlankQuiz = ({formItemLayout, FormItem, quiz}) => {
  return (<FormItem {...formItemLayout} label='答案'>
    <span>{quiz.answer}</span>
  </FormItem>)
}

export default BlankQuiz
