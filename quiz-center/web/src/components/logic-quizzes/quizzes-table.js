import { Table, Icon, Divider, Tooltip, Popconfirm } from 'antd'
import React, { Component } from 'react'
import moment from 'moment'

import PreviewModal from '../common/preview-modal'

export default class QuizzesTable extends Component {
  constructor (props) {
    super(props)
    this.state = {
      visible: false,
      quiz: {}
    }
  }

  showEditorPage (quizId) {
    this.props.onShowEditorPage(quizId)
  }

  previewPage (quiz) {
    this.setState({
      visible: true,
      quiz
    })
  }

  closeModal () {
    this.setState({
      visible: false,
      quiz: {}
    })
  }

  confirm (quizId) {
    this.props.onDeleteQuiz(quizId)
  }

  render () {

    const columns = [{
      title: '描述',
      dataIndex: 'description',
      width:'20%',
      className:'description-style'
    },  {
      title: '时长(分钟)',
      dataIndex: 'timeBoxInMinutes',
      width:'20%',
      className:'description-style'
    },{
      title: '创建时间',
      dataIndex: 'createTime',
      width: '20%'
    }, {
      title: '创建者',
      dataIndex: 'maker',
      width: '20%',
      sorter: (a, b) => a.maker.length - b.maker.length
    }, {
      title: '操作',
      key: 'action',
      render: (text, record) => (
        <span>
          <Tooltip placement='top' title='预览' overlay='preview'>
            <Icon type='eye' className='icon-style' onClick={this.previewPage.bind(this, record)} />
          </Tooltip>

          <Divider type='vertical' />

          {/* <Tooltip placement='top' title='编辑' overlay='editor'>
            <Icon type='edit' className='icon-style'
              onClick={this.showEditorPage.bind(this, record.id)} />
          </Tooltip>
          <Divider type='vertical' /> */}

          <Popconfirm title='确定删除吗?'
            onConfirm={this.confirm.bind(this, record.id)}
            okText='确定' cancelText='取消'>

            <Icon type='delete' className='icon-style' />
          </Popconfirm>
        </span>
      )
    }]

    const {visible, quiz} = this.state
    const quizzes = this.props.dataSource.map(quiz => {
      quiz.createTime = moment(quiz.createTime).format('YYYY-MM-DD')
      if (quiz.tags) {
        return Object.assign({}, quiz, {tags: quiz.tags.join('、 ')})
      }
      return quiz
    })
    return (<div className={'table-style'}>
      <Table rowKey={record => record.id}
        columns={columns}
        dataSource={quizzes}
        pagination={this.props.pagination}
        bordered
        className='table-center' />
      {Object.keys(quiz).length === 0
        ? ''
        : <PreviewModal visible={visible}
          quiz={quiz}
          type={this.props.type}
          closeModal={this.closeModal.bind(this)}
        />}
    </div>)
  }
}
