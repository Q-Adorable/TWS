import React, { Component } from 'react'
import { Row, Col, Button} from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import SearchInput from '../common/search-input'
import QuizzesTable from '../common/quizzes-table'

import * as actions from '../../actions/multiple-choice-quiz'
import {MULTIPLE_CHOICE} from '../../constant/quiz-type'

class MultipleChoiceQuizzes extends Component {
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

  editorPage (id) {
    this.props.history.push(`/multipleChoiceQuizzes/${id}/editor`)
  }

  addQuiz () {
    this.props.history.push(`/multipleChoiceQuizzes/new`)
  }

  onChange (page, pageSize) {
    this.setState({page})
    const {selectType, searchContent} = this.state

    this.props.getQuizzes(page, selectType, searchContent)
  }

  onSearch (selectType, searchContent) {
    const defaultPage = 1
    this.setState({selectType, searchContent})
    this.props.getQuizzes(defaultPage, selectType, searchContent)
  }

  deleteQuiz (quizId) {
    this.props.deleteQuiz(quizId)
  }

  render () {
    const pagination = {
      current: this.state.page,
      total: this.props.total,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.onChange(page, this.state.pageSize)
      }
    }

    const multipleChoiceQuizzes = this.props.multipleChoiceQuizzes

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
        <QuizzesTable dataSource={multipleChoiceQuizzes}
          pagination={pagination}
          onShowEditorPage={this.editorPage.bind(this)}
          onDeleteQuiz={this.deleteQuiz.bind(this)}
          type={MULTIPLE_CHOICE}
        />
      </div>
    )
  }
}
const mapStateToProps = ({multipleChoice}) => ({
  multipleChoiceQuizzes: multipleChoice.quizzes.content,
  total: multipleChoice.quizzes.totalElements
})

const mapDispatchToProps = (dispatch) => ({
  getQuizzes: (page, searchType, searchContent) => dispatch(actions.getQuizzes(page, searchType, searchContent)),
  deleteQuiz: (quizId) => dispatch(actions.deleteQuiz(quizId))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(MultipleChoiceQuizzes))
