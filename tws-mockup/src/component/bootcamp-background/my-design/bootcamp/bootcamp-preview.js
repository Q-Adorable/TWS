import React, { Component } from 'react'
import { Card, Row, Col } from 'antd'

export default class BootcampPreview extends Component {
  render() {
    return (
      <Card>
        <Row>
          <Col style={{ fontSize: '18', fontWeight: 'bold' }}>
            任务卡1
          </Col>
        </Row>
        <Row>
          <Col>
            123
          </Col>
        </Row>
      </Card>
    )
  }
}
