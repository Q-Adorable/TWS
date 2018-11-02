import React from 'react'
import {Button, Icon} from 'antd'

export default class ButtonGroup extends React.Component {
  handleSubmit () {
    this.props.handleSubmit(true)
  }
  handleSubmitContinue () {
    this.props.handleSubmit(false)
  }

  render () {
    return <div>
      {
            this.props.id > 0
                ? <Button type='primary' onClick={this.handleSubmit.bind(this)}>修改</Button>
                : <div>
                  <Button type='primary' ghost style={{marginRight: 40}} onClick={this.handleSubmit.bind(this)}><Icon type='left' />添加并返回</Button>
                  <Button type='primary' onClick={this.handleSubmitContinue.bind(this)}>添加并继续<Icon type='right' /></Button>
                </div>
        }
    </div>
  }
}
