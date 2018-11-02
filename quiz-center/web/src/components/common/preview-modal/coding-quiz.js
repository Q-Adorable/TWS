import React from 'react'

const CodingQuiz = ({formItemLayout, FormItem, quiz}) => {
  return (
    <div>
      <FormItem {...formItemLayout} label='技术栈'>
        <span>{quiz.stack}</span>
      </FormItem>
      <FormItem {...formItemLayout} label='仓库地址'>
        <span>{quiz.definitionRepo}</span>
      </FormItem>
      <FormItem {...formItemLayout} label='描述'>
        <span>{quiz.description}</span>
      </FormItem>
    </div>)
}

export default CodingQuiz
