import React from 'react'
import {Radio} from 'antd'

import {radioStyle} from '../../../constant/constant-style'

const RadioGroup = Radio.Group

const SingleChoice = ({formItemLayout, FormItem, quiz}) => {
  const {choices, answer} = quiz

  return (<FormItem {...formItemLayout} label='选项'>
    <RadioGroup value={answer}>
      {choices.map((option, index) => {
        return (
          <Radio style={radioStyle} value={`${index}`} key={index} disabled>
            <span>{option}</span>
          </Radio>
        )
      })}
    </RadioGroup>
  </FormItem>)
}

export default SingleChoice
