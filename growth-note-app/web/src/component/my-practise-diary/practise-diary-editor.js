import React, {Component} from 'react'
import {Card} from 'antd'
import {connect} from 'react-redux'
import * as actions from '../../action/practise-diary'
import 'antd/dist/antd.css'

import PractiseDiaryEditorBody from './practise-diary-editor-body'

class PractiseDiaryEditor extends Component {
  render () {
    return (<div className='practise-diary'>
      <Card title='新的日志'
        extra={<a href='https://school.thoughtworks.cn/bbs/topic/1230/%E5%A6%82%E4%BD%95%E5%86%99%E4%B8%80%E7%AF%87%E4%BC%98%E7%A7%80%E7%9A%84%E6%88%90%E9%95%BF%E6%97%A5%E5%BF%97' target='_blank' rel='noopener noreferrer'>
                      如何写一篇优秀的成长日志
                  </a>} bordered className='title-text-position' noHovering>
        <PractiseDiaryEditorBody createPractiseDiary={this.props.submitPractiseDiary} operationType='create' />
      </Card>
    </div>)
  }
}

const mapDispatchToProps = dispatch => ({
  submitPractiseDiary: practiseDiary => dispatch(actions.createPractiseDiary(practiseDiary))
})

export default connect(() => {
  return {}
}, mapDispatchToProps)(PractiseDiaryEditor)
