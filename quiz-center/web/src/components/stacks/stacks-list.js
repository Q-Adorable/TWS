import React from 'react'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'
import { Table, Icon, Row, Col, Button, Popconfirm, message } from 'antd'
import { STATUS } from '../../constant/constant-data'
import * as actions from '../../actions/stack'
import '../../less/index.less'

class Stacks extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: 10,
      isDeleted: false,
      stackId: -1
    }
  }

  componentDidMount () {
    this.props.getStacks()
  }

  componentWillReceiveProps (nextProps) {
    if (nextProps.status.status === STATUS.SUCCESS) {
      this.setState({isDeleted: false})
      return
    }
    if (nextProps.status.status === STATUS.ERROR) {
      message.error('该镜像正在使用不能被删除')
      this.setState({isDeleted: false})
      return
    }
    let isDeleting = this.state.stackId >= 0 && (nextProps.status.status === STATUS.LINE_UP || nextProps.status.status === STATUS.PROGRESS)
    if (isDeleting) {
      this.props.judgeStackIsDeleted(this.state.stackId)
    }
  }

  addStack () {
    this.props.history.push('/stacks/new')
  }

  confirm (stackId) {
    this.props.deleteStack(stackId)
    this.setState({isDeleted: true, stackId})
  }

  render () {
    const pagination = {
      current: this.state.page,
      total: this.props.total,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.setState({page})
        this.props.getStacks(page, this.state.pageSize)
      }
    }
    const columns = [{
      title: '镜像',
      dataIndex: 'image'
    }, {
      title: '创建者',
      dataIndex: 'maker',
      sorter: (a, b) => a.maker.length - b.maker.length
    }, {
      title: '创建时间',
      dataIndex: 'createTime'
    }, {
      title: '操作',
      key: 'action',
      render: (text, record) => (
        <span>
          <Popconfirm title='确定删除吗?' onConfirm={this.confirm.bind(this, record.id)} okText='确定' cancelText='取消'>
            {this.state.isDeleted && record.id === this.state.stackId ? <div><Icon type='loading' /> <span className='yellow'>{'正在删除，请稍等...'}</span></div>
              : <Icon type='delete' style={{fontSize: 16, color: '#08c'}} />}
          </Popconfirm>
        </span>
      )
    }]
    return (<div>
      <Row>
        <Col span={8} offset={1}>
          <Button className='margin-b-2' type='primary' onClick={this.addStack.bind(this)}>新增</Button>
        </Col>
      </Row>
      <Table rowKey={record => record.id}
        columns={columns}
        dataSource={this.props.stacks}
        pagination={pagination}
        bordered
        className='table-center' />
    </div>)
  }
}

const mapStateToProps = ({stackData}) => ({
  total: stackData.stacks.totalElements,
  stacks: stackData.stacks.content,
  status: stackData.status
})

const mapDispatchToProps = (dispatch) => ({
  deleteStack: (stackId) => dispatch(actions.deleteStack(stackId)),
  getStacks: (page, pageSize) => dispatch(actions.getAllStacksPage(page, pageSize)),
  judgeStackIsDeleted: (stackId) => dispatch(actions.judgeStackIsDeleted(stackId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Stacks))
