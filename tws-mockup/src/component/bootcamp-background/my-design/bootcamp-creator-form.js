import React, { Component } from 'react'
import { Form, Row, Col, Input, Select } from 'antd'

const FormItem = Form.Item
const {TextArea} = Input
const Option = Select.Option

const children = []
for (let i = 0; i < 6; i++) {
  children.push(<Option key={'主题' + (i + 1)}>{'主题' + (i + 1)}</Option>)
}

export default class BootcampCreatorForm extends Component {
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
              label='简介'
              labelCol={{
                span: 4
              }}
              wrapperCol={{
                span: 16
              }}
            >
              <TextArea rows={4} />
            </FormItem>
            <FormItem
              hasFeedback
              label='主题'
              labelCol={{
                span: 4
              }}
              wrapperCol={{
                span: 16
              }}
            >
              <Select
                mode='multiple'
                style={{width: '100%'}}
                placeholder='Please select'
                defaultValue={['主题1']}
              >
                {children}
              </Select>
            </FormItem>

          </Form>
        </Col>
      </Row>
    )
  }
}
