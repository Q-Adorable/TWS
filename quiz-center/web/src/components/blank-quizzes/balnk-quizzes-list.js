import React, { Component } from 'react'
import { Row, Col, Button } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import SearchInput from '../common/search-input'
import QuizzesTable from '../common/quizzes-table'

import * as actions from '../../actions/blank-quiz'
import { BLANK_QUIZ } from '../../constant/quiz-type'

class BlankQuizzesList extends Component {
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
    this.props.getBlankQuizzes()
  }

  editorPage (id) {
    this.props.history.push(`/blankQuizzes/${id}/editor`)
  }

  onChange (page, pageSize) {
    this.setState({page})
    const {selectType, searchContent} = this.state
    this.props.getBlankQuizzes(page, pageSize, selectType, searchContent)
  }

  onSearch (selectType, searchContent) {
    const defaultPage = 1
    this.setState({selectType, searchContent})
    this.props.getBlankQuizzes(defaultPage, this.state.pageSize, selectType, searchContent)
  }

  addQuiz () {
    this.props.history.push('/blankQuizzes/new')
  }

  deleteQuiz (quizId) {
    this.props.deleteQuiz(quizId)
  }

  render () {
    const pagination = {
      current: this.state.page,
      total: this.props.blankQuizzes.totalElements,
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
        <QuizzesTable dataSource={this.props.blankQuizzes.content}
          pagination={pagination}
          onShowEditorPage={this.editorPage.bind(this)}
          onDeleteQuiz={this.deleteQuiz.bind(this)}
          type={BLANK_QUIZ}
        />
      </div>
    )
  }
}
const mapStateToProps = ({blank}) => ({
  blankQuizzes: blank.quizzes
  // total: blank.quizzes.total
})

const mapDispatchToProps = (dispatch) => ({
  getBlankQuizzes: (page, pageSize, searchType, searchContent) => dispatch(actions.getQuizzes(page, pageSize, searchType, searchContent)),
  deleteQuiz: (quizId) => dispatch(actions.deleteQuiz(quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(BlankQuizzesList))
