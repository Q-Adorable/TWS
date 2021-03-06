import { Table, Icon, Divider, Tooltip, Popconfirm, Tag } from 'antd'
import React, { Component } from 'react'
import moment from 'moment'

import PreviewModal from '../common/preview-modal'

const STATUS_STYLES = [{
  code: 0,
  text: '添加失败',
  style: 'fail'
}, {
  code: 2,
  text: '添加成功',
  style: 'success'
}, {
  code: 6,
  text: '排队中',
  style: 'waiting'
}, {
  code: 1,
  text: '加载中',
  style: 'waiting'
}]

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
      title: '名称',
      dataIndex: 'onlineLanguageName',
      sorter: (a, b) => a.onlineLanguageName.length - b.onlineLanguageName.length,
      className:  'description-style'
    }, {
      title: '备注',
      dataIndex: 'remark',
      sorter: (a, b) => a.remark.length - b.remark.length,
      className: 'description-style'
    }, {
      title: '标签',
      dataIndex: 'tags',
      sorter: (a, b) => a.tags.length - b.tags.length,
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
    }, {
      title: '创建者',
      dataIndex: 'maker',
      sorter: (a, b) => a.maker.length - b.maker.length
    }, {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      sorter: (a, b) => a.status - b.status,
      render: (text, record) => {
        const statusStyle = STATUS_STYLES.find(s => s.code === record.status) || {style: '', text: ''}

        return <span className={`homework-status-${statusStyle.style}`}>{statusStyle.text}</span>
      }
    }, {
      title: '操作',
      key: 'action',
      render: (text, record) => (
        <span>
          <Tooltip placement='top' title='预览' overlay='preview'>
            <Icon type='eye' className='icon-style' onClick={this.previewPage.bind(this, record)} />
          </Tooltip>

          {this.props.disabledEdit
            ? ''
            : <span>
              <Divider type='vertical' />

              <Tooltip placement='top' title='编辑' overlay='editor'>
                <Icon type='edit' className='icon-style'
                  onClick={this.showEditorPage.bind(this, record.id)} />
              </Tooltip>
            </span>
          }
          <Divider type='vertical' />

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
        return Object.assign({}, quiz, {tags: quiz.tags.map(tag => <Tag key={tag.id}>{tag.name}</Tag>)})
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
