import React from 'react'
import {Col, Row, Checkbox} from 'antd'

import {MAP_CHECKOUT_COUNT} from '../../../constant/constant-data'

const MultipleChoice = ({formItemLayout, FormItem, quiz}) => {
  return (<FormItem {...formItemLayout} label='选项'>
    {MAP_CHECKOUT_COUNT.map(count => {
      const {answer, choices} = quiz
      const answers = answer
      return (<Row key={count}>
        <Col span={2}>
          <Checkbox disabled
            defaultChecked={answers.some(answer => parseInt(answer, 10) === count)}
          />
        </Col>
        <Col span={20}>
          <span>{choices[count]}</span>
        </Col>
      </Row>)
    })}
  </FormItem>)
}

export default MultipleChoice
