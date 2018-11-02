import React from 'react'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'
import { Table, Divider, Tooltip, Icon, Row, Col, Button, Popconfirm } from 'antd'

import * as actions from '../../actions/tag'

class TagsList extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: 10
    }
  }

  componentDidMount() {
    this.props.getTags()
  }

  showEditorPage(id) {
    this.props.history.push(`/tags/${id}/editor`)
  }

  addQuiz() {
    this.props.history.push('/tags/new')
  }
  confirm(tagId) {
    this.props.deleteTag(tagId)
  }
  render() {
    const pagination = {
      current: this.state.page,
      total: this.props.total,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.setState({ page })
        this.props.getTags(page, this.state.pageSize)
      }
    }

    const columns = [{
      title: '名称',
      dataIndex: 'name',
      width: '12%',
      sorter: (a, b) => a.name - b.name
    }, {
      title: '引用次数',
      dataIndex: 'referenceNumber',
      width: '12%',
      sorter: (a, b) => a.referenceNumber - b.referenceNumber,
      className: 'hidden'
    }, {
      title: '创建者',
      dataIndex: 'maker',
      sorter: (a, b) => a.maker.length - b.maker.length
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
      sorter: (a, b) => a.createTime.length - b.createTime.length,
      render: (text, record) => (<span>{text ? text.slice(0, 10) : ''}</span>)
    }, {
      title: '操作',
      key: 'action',
      render: (text, record) => (
        <span>
          <Tooltip placement='top' title='编辑' overlay='editor'>
            <Icon type='edit' style={{ fontSize: 16, color: '#08c' }}
              onClick={this.showEditorPage.bind(this, record.id)} />
          </Tooltip>

          <Divider type='vertical' />
          <Popconfirm title='确定删除吗?' onConfirm={this.confirm.bind(this, record.id)} okText='确定' cancelText='取消'>

            <Icon type='delete' style={{ fontSize: 16, color: '#08c' }} />
          </Popconfirm>
        </span>
      )
    }]
    return (<div>
      <Row>
        <Col span={8} offset={1}>
          <Button className='margin-b-2' type='primary' onClick={this.addQuiz.bind(this)}>新增</Button>
        </Col>
      </Row>
      <Table rowKey={record => record.id}
        columns={columns}
        dataSource={this.props.tags}
        pagination={pagination}
        bordered
        className='table-center' />
    </div>)
  }
}

const mapStateToProps = ({ tagData }) => ({
  total: tagData.tags.totalElements,
  tags: tagData.tags.content
})

const mapDispatchToProps = (dispatch) => ({
  getTags: (page, pageSize) => dispatch(actions.getAllTags(page, pageSize)),
  deleteTag: (tagId) => dispatch(actions.deleteTag(tagId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(TagsList))
