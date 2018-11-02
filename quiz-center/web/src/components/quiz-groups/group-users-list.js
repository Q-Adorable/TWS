import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../actions/quiz-group'
import { Table, Popconfirm } from 'antd'

class QuizGroupList extends Component {
  constructor (props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: 10,
    }
  }

  onChange (page, pageSize) {
    this.setState({page}, () => {
      this.props.getUsersFromQuizGroup(page, pageSize,this.props.currentGroupId)
    })
  }

  confirm (currentGroupId,userId) {
   this.props.deleteUserFromGroup(currentGroupId,userId)
  }

  render () {
    const users = this.props.users
    const columns = [{
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
    }, {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
    }, {
      title: 'Action',
      key: 'action',
      render: (text, record) => (
        <span>
         <Popconfirm title="确定删除题组中的该成员吗?" onConfirm={this.confirm.bind(this,this.props.currentGroupId,record.id)} okText="确定"
                     cancelText="取消">
         <a href="" onClick={e => {e.preventDefault()}}>删除</a>
         </Popconfirm>
    </span>
      ),
    }]
    const pagination = {
      page: this.state.page,
      total: this.props.users.length,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.onChange(page, this.state.pageSize)
      }
    }

    return (
      <div>
          <Table columns={columns} pagination={pagination} dataSource={users} rowKey={record => record.id}/>
      </div>
    )
  }
}
const mapStateToProps = ({quizGroupData}) => ({
  users: quizGroupData.users
})

const mapDispatchToProps = dispatch => ({
  getUsersFromQuizGroup: (quizGroupId,page, pageSize) => dispatch(actions.getUsersFromGroup(quizGroupId,page, pageSize)),
  deleteUserFromGroup:(currentGroupId,userId)=>dispatch(actions.deleteUserFromGroup(currentGroupId,userId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(QuizGroupList))
