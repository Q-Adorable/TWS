import React, { Component } from 'react'
import { Form, Row, Col, Input } from 'antd'

const FormItem = Form.Item
const { TextArea } = Input

export default class TaskCardFrom extends Component {
  render () {
    return (
      <Row>
        <Col span={24}>
          <Form>
            <FormItem
              hasFeedback
              label='名称'
              labelCol={{
                span: 4
              }}
              wrapperCol={{
                span: 16
              }}
            >
              <Input />
            </FormItem>
            <FormItem
              hasFeedback
              label='描述'
              labelCol={{
                span: 4
              }}
              wrapperCol={{
                span: 16
              }}
            >
              <TextArea rows={4} />
            </FormItem>
          </Form>
        </Col>
      </Row>
    )
  }
}
