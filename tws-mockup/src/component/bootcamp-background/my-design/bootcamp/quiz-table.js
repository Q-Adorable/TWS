import React from 'react'
import { Button, Col, Input, Row, Table } from 'antd'
import '../../../../style/common.css'

const Search = Input.Search

const columns = [{
  title: '描述',
  dataIndex: 'description',
  key: 'description',
  className: 'quiz__table--header'
}, {
  title: '主题',
  dataIndex: 'topic',
  key: 'topic',
  className: 'quiz__table--header'
}, {
  align: 'center',
  width: 180,
  title: '操作',
  key: 'operition',
  render: () => (
    <span className='tws-btn-group'>
      <Button type='primary'>
        预览
      </Button>
      <Button type='primary'>
        编辑
      </Button>
    </span>
  )
}]

const data = [{
  key: '1',
  description: 'xxxxxxxxxxx',
  topic: '主题1'
}, {
  key: '2',
  description: 'xxxxxxxxxxx',
  topic: '主题1'
}, {
  key: '3',
  description: 'xxxxxxxxxxx',
  topic: '主题1'
}]

const rowSelection = (isChooseBasicQuiz) => {
  return ({
    type: isChooseBasicQuiz ? 'checkbox' : 'radio',
    columnTitle: '选择'
  })
}

const QuizTable = ({ isChooseBasicQuiz }) => {
  return (
    <div>
      <Row type='flex' justify='end' className='margin--bottom'>
        <Col span={10}>
          <Search enterButton='搜索' />
        </Col>
      </Row>
      <Table rowSelection={rowSelection(isChooseBasicQuiz)} columns={columns} dataSource={data} bordered />
    </div>
  )
}

export default QuizTable
