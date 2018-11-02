import React, { Component } from 'react'
import { Button, Col, Row } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import SearchInput from '../common/search-input'
import QuizzesTable from './quizzes-table'

import * as actions from '../../actions/logic-quiz'

class LogicQuizzes extends Component {
  constructor (props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: 10
    }
  }

  componentDidMount () {
    this.props.getLogicQuizzes()
  }

  onChange (page, pageSize) {
    this.setState({page})
    this.props.getLogicQuizzes(page, pageSize)
  }

  onSearch (selectType, searchContent) {
    const defaultPage = 1
    this.props.getLogicQuizzes(defaultPage, selectType, searchContent)
  }
  editorQuiz (id) {
    this.props.history.push(`/logicQuizzes/${id}/editor`)
  }
  editorPage (quizId) {
    this.props.history.push(`/logicQuizzes/${quizId}/editor`)
  }

  addQuiz () {
    this.props.history.push(`/logicQuizzes/new`)
  }

  deleteQuiz (quizId) {
    this.props.deleteQuiz(quizId)
  }

  render () {
    const dataSource = this.props.logicQuizzes.content || []
    console.log( this.props.logicQuizzes)
    const pagination = {
      current: this.state.page,
      total: this.props.logicQuizzes.totalElements,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.onChange(page, this.state.pageSize)
      }
    }
    return (
      <div>
        <Row>
          <Col span={8} offset={1}>
            <Button type='primary' onClick={this.addQuiz.bind(this)}>新增</Button>
          </Col>
          <Col span={15}>
            <SearchInput isLogic onSearch={this.onSearch.bind(this)} />
          </Col>

        </Row>
        <QuizzesTable
          dataSource={dataSource}
          pagination={pagination}
          onShowEditorPage={this.editorQuiz.bind(this)}
          onDeleteQuiz={this.deleteQuiz.bind(this)}
          type='LOGIC_QUIZ'

        />
      </div>
    )
  }
}
const mapStateToProps = ({logicQuizData}) => ({
  logicQuizzes: logicQuizData.logicQuizzes
})

const mapDispatchToProps = dispatch => ({
  getLogicQuizzes: (page, selectType, searchContent) => dispatch(actions.getLogicQuizzes(page, selectType, searchContent)),
  deleteQuiz: (quizId) => dispatch(actions.deleteQuiz(quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(LogicQuizzes))
