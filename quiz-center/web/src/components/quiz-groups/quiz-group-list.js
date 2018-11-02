import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../actions/quiz-group'
import { Button, Table, Divider, Popconfirm, Modal } from 'antd'
import GroupUsers from './group-users-list'

class QuizGroupList extends Component {
  constructor (props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: 10,
      visible: false,
      currentGroupId: -1
    }
  }

  componentDidMount () {
    this.props.getQuizGroups()
  }

  onChange (page, pageSize) {
    this.setState({page}, () => {
      this.props.getQuizGroups(page, pageSize)
    })
  }

  confirm (groupId) {
    this.props.deleteMySelfFromGroup(groupId)
  }

  handleOk = (e) => {
    this.setState({
      visible: false,
    })
  }
  handleCancel = () => {
    this.setState({
      visible: false,
    })
  }

  getCurrentUserGroupInfo (groupId) {
    this.setState({
      visible: true,
      currentGroupId: groupId
    }, () => {
      this.props.getUsersFromQuizGroup(groupId)
    })
  }

  render () {
    const {content, totalElement} = this.props.quizGroupList
    const columns = [{
      title: '名称',
      dataIndex: 'name',
      key: 'name',
    }, {
      title: '描述',
      dataIndex: 'description',
      key: 'description',
    }, {
      title: '创建者',
      dataIndex: 'maker',
      key: 'maker',
    }, {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    }, {
      title: 'Action',
      key: 'action',
      render: (text, record) => (
        record.userIsExist
          ? <span>
            <a href="javascript:" className="ant-dropdown-link"
               onClick={() => {this.props.history.push(`/quizGroups/${record.id}/editor`)}}>
              修改
            </a>
            < Divider type='vertical'/>
            <Popconfirm title='确定离开该题组吗?' onConfirm={this.confirm.bind(this, record.id)} okText='确定'
                        cancelText='取消'>
            <a href="" onClick={(e)=> {e.preventDefault()}}>删除</a>
            </Popconfirm>
            <Divider type='vertical'/>
            <a href="javascript:" className='ant-dropdown-link'
               onClick={this.getCurrentUserGroupInfo.bind(this, record.id)}>
            详情
            </a>
      </span> : <span><a href="javascript:" className='ant-dropdown-link'
                         onClick={() => {this.props.joinQuizGroup(record.id)}}>
            加入该题组
            </a></span>

      ),
    }]
    const pagination = {
      page: this.state.page,
      total: totalElement,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.onChange(page, this.state.pageSize)
      }
    }

    return (
      <div>
        <Button type="primary" onClick={() => { this.props.history.push(`/quizGroups/new`)}}>创建我的出题组</Button>
        <Table columns={columns} pagination={pagination} dataSource={content} rowKey={record => record.id}/>

        <Modal
          title="题组成员列表"
          visible={this.state.visible}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
        >
          <GroupUsers currentGroupId={this.state.currentGroupId}/>
        </Modal>
      </div>
    )
  }
}
const mapStateToProps = ({quizGroupData}) => ({
  quizGroupList: quizGroupData.quizGroups,
})

const mapDispatchToProps = dispatch => ({
  getQuizGroups: (page, pageSize) => dispatch(actions.getQuizGroups(page, pageSize)),
  deleteMySelfFromGroup: (quizGroupId) => dispatch(actions.deleteMySelfFromGroup(quizGroupId)),
  getUsersFromQuizGroup: (quizGroupId) => dispatch(actions.getUsersFromGroup(quizGroupId)),
  joinQuizGroup: (quizGroupId) => dispatch(actions.joinQuizGroup(quizGroupId))

})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(QuizGroupList))
