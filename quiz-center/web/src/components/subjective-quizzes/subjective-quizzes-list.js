import React, { Component } from 'react'
import { Button, Col, Row } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import SearchInput from '../common/search-input'
import QuizzesTable from './quizzes-table'

import * as actions from '../../actions/subjective-quizzes'
import { SUBJECTIVE_QUIZ } from '../../constant/quiz-type'

class SubjectiveQuizList extends Component {
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
    this.props.getSubjectiveQuizzes(this.state.page, this.state.pageSize)
  }

  editorQuiz (id) {
    this.props.history.push(`/subjectiveQuizzes/${id}/editor`)
  }

  onChange (page, pageSize) {
    const {selectType, searchContent} = this.state

    this.setState({page}, () => {
      this.props.getSubjectiveQuizzes(page, pageSize, selectType, searchContent)
    })
  }

  onSearch (selectType, searchContent) {
    this.setState({selectType, searchContent})

    this.props.getSubjectiveQuizzes(this.state.page, this.state.pageSize, selectType, searchContent)
  }

  addQuiz () {
    this.props.history.push('/subjectiveQuizzes/new')
  }

  deleteQuiz (quizId) {
    this.props.deleteQuiz(quizId)
  }

  render () {
    const pagination = {
      page: this.state.page,
      total: this.props.subjectiveQuizzes.totalElements,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.onChange(page, this.state.pageSize)
      }
    }
    const quizzes = this.props.subjectiveQuizzes.content
    return (
      <div>
        <Row>
          <Col span={8} offset={1}>
            <Button type='primary' onClick={this.addQuiz.bind(this)}>新增</Button>
          </Col>
          <Col span={15}>
            <SearchInput onSearch={this.onSearch.bind(this)} />
          </Col>
        </Row>
        <QuizzesTable dataSource={quizzes}
          pagination={pagination}
          onShowEditorPage={this.editorQuiz.bind(this)}
          onDeleteQuiz={this.deleteQuiz.bind(this)}
          type={SUBJECTIVE_QUIZ}
        />
      </div>
    )
  }
}
const mapStateToProps = ({subjective}) => ({
  subjectiveQuizzes: subjective.quizzes
})

const mapDispatchToProps = (dispatch) => ({
  getSubjectiveQuizzes: (page, pageSize, selectType, searchContent) => dispatch(actions.getQuizzes(page, pageSize, selectType, searchContent)),
  deleteQuiz: (quizId) => dispatch(actions.deleteQuiz(quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(SubjectiveQuizList))
