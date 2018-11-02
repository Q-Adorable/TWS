import React from 'react'
import {Col, Row} from 'antd'

const LogicQuiz = ({quiz}) => {
  return <Row>
    <Col span={6} offset={2}>
        简单：<span>{quiz.easyCount}</span>
    </Col>
    <Col span={6}>
        一般：<span>{quiz.normalCount}</span>
    </Col>
    <Col span={6}>
        困难：<span>{quiz.hardCount}</span>
    </Col>
  </Row>
}

export default LogicQuiz
