import React from 'react'
import {TwsReactMarkdownPreview} from 'tws-antd'

const OnlineLanguageQuiz = ({formItemLayout, FormItem, quiz}) => {
  return (
    <div>
      <FormItem {...formItemLayout} label='语言'>
        <span>{quiz.language}</span>
      </FormItem>
      <FormItem {...formItemLayout} label='启动代码'>
        <TwsReactMarkdownPreview source={`
\`\`\`
${quiz.initCode}
\`\`\`
`} />
      </FormItem>
      <FormItem {...formItemLayout} label='测试数据'>
        <TwsReactMarkdownPreview source={`
\`\`\`
${quiz.testData}
\`\`\`
`} />
      </FormItem>
    </div>)
}

export default OnlineLanguageQuiz
