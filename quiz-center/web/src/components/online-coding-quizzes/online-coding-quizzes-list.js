import React, { Component } from 'react'
import { Row, Col, Button } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import moment from 'moment'

import SearchInput from '../common/search-input'
import QuizzesTable from './quizzes-table'
import { ONLINE_CODING_QUIZ } from '../../constant/quiz-type'

import * as actions from '../../actions/online-coding-quiz'

class OnlineCodingQuizzes extends Component {
  constructor (props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: 10,
      selectType: '',
      searchContent: ''
    }
  }

  componentDidMount () {
    this.props.getQuizzes()
  }

  onChange (page, pageSize) {
    this.setState({page})
    const {selectType, searchContent} = this.state
    this.props.getQuizzes(page, selectType, searchContent)
  }

  onSearch (selectType, searchContent) {
    const defaultPage = 1
    this.props.getQuizzes(defaultPage, selectType, searchContent)
  }

  editorPage (quizId) {
    this.props.history.push(`/onlineCodingQuizzes/${quizId}/editor`)
  }

  addQuiz () {
    this.props.history.push(`/onlineCodingQuizzes/new`)
  }

  deleteQuiz (quizId) {
    this.props.deleteQuiz(quizId)
  }

  render () {
    const dataSource = this.props.onlineCodingQuizzes.content.map(quiz => {
      const {createTime} = quiz
      quiz.createTime = moment(createTime).format('YYYY-MM-DD')

      return quiz
    })
    const pagination = {
      current: this.state.page,
      total: this.props.onlineCodingQuizzes.totalElements,
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
            <SearchInput hasQuizType onSearch={this.onSearch.bind(this)} />
          </Col>
        </Row>
        <QuizzesTable dataSource={dataSource} pagination={pagination}
          onShowEditorPage={this.editorPage.bind(this)}
          onDeleteQuiz={this.deleteQuiz.bind(this)}
          type={ONLINE_CODING_QUIZ}
        />
      </div>
    )
  }
}
const mapStateToProps = ({onlineCoding}) => ({
  onlineCodingQuizzes: onlineCoding.quizzes
})

const mapDispatchToProps = dispatch => ({
  getQuizzes: (page, searchType, searchContent) => dispatch(actions.getQuizzes(page, searchType, searchContent)),
  deleteQuiz: (quizId) => dispatch(actions.deleteQuiz(quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(OnlineCodingQuizzes))
