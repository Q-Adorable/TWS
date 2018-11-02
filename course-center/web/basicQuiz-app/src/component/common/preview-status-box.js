import React, { Component } from 'react'
import '../../less/index.less'
import { withRouter } from 'react-router-dom'
import {TwsEditAssignmentStatus} from 'tws-antd'

class PreviewStatusBox extends Component {
  constructor (props) {
    super(props)
    this.state = {
      grade: props.grade,
      status: '',
      excellence: 0
    }
  }

  render () {
    return (<div>
      <TwsEditAssignmentStatus
        grade={this.state.grade || 0}
        onChangeGrade={e => this.setState({grade: e.target.value})}
        status={this.state.status || '未完成'}
        onChangeStatus={e => this.setState({status: e})} />

    </div>
    )
  }
}

export default withRouter(PreviewStatusBox)
