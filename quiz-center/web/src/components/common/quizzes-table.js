import { Table, Icon, Divider, Tooltip, Popconfirm, Tag } from 'antd'
import React, { Component } from 'react'
import moment from 'moment'

import PreviewModal from './preview-modal'
import {CODING_QUIZ, ONLINE_CODING_QUIZ, ONLINE_LANGUAGE_QUIZ, LOGIC_QUIZ, SUBJECTIVE_QUIZ} from '../../constant/quiz-type'

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
    const displayHomeworkName = this.props.type === CODING_QUIZ
    const displayOnlineCodingName = this.props.type === ONLINE_CODING_QUIZ
    const displayOnlineLanguageName = this.props.type === ONLINE_LANGUAGE_QUIZ
    const displayStatus = this.props.type === CODING_QUIZ || this.props.type === ONLINE_CODING_QUIZ || this.props.type === ONLINE_LANGUAGE_QUIZ
    const displayRemark = displayStatus || this.props.type === SUBJECTIVE_QUIZ
    const displayTags = this.props.type === LOGIC_QUIZ
    const isSubjective = this.props.type === SUBJECTIVE_QUIZ
    
    const columns = [{
      title: '名称',
      dataIndex: 'homeworkName',
      width: isSubjective ? '20%' : '',
      sorter: (a, b) => a.homeworkName.length - b.homeworkName.length,
      className: displayHomeworkName ? 'description-style' : 'hidden'
    }, {
      title: '名称',
      dataIndex: 'onlineCodingName',
      width: isSubjective ? '20%' : '',
      sorter: (a, b) => a.onlineCodingName.length - b.onlineCodingName.length,
      className: displayOnlineCodingName ? 'description-style' : 'hidden'
    }, {
      title: '名称',
      dataIndex: 'onlineLanguageName',
      width: isSubjective ? '20%' : '',
      sorter: (a, b) => a.onlineLanguageName.length - b.onlineLanguageName.length,
      className: displayOnlineLanguageName ? 'description-style' : 'hidden'
    }, {
      title: '备注',
      dataIndex: 'remark',
      width: isSubjective ? '20%' : '',
      sorter: (a, b) => a.remark.length - b.remark.length,
      className: displayRemark ? 'description-style' : 'hidden'
    }, {
      title: '标签',
      dataIndex: 'tags',
      width: isSubjective ? '20%' : '',
      sorter: (a, b) => a.tags.length - b.tags.length,
      className: displayTags ? 'hidden' : ''
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
      width: isSubjective ? '20%' : ''
    }, {
      title: '创建者',
      dataIndex: 'maker',
      width: isSubjective ? '20%' : '',
      sorter: (a, b) => a.maker.length - b.maker.length
    }, {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      sorter: (a, b) => a.status - b.status,
      className: displayStatus ? '' : 'hidden',
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
    return (<div className={displayRemark ? 'table-style' : ''}>
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
