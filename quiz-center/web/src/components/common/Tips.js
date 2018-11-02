import React from 'react'
import { Card, Button } from 'antd'

export default class Tips extends React.Component {
  createOne () {
  }
  returnList () {
  }
  render () {
    return <Card style={{ width: 300 }}>
      <p>创建成功，您是继续创建还是返回列表？</p>
      <Button onClick={this.createOne.bind(this)}>继续新建</Button>
      <Button onClick={this.returnList.bind(this)}>返回列表</Button>
    </Card>
  }
}
