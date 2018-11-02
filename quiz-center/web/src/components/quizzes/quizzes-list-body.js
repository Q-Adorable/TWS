import React, {Component} from 'react'
import {Tag, Table, Col, Row} from 'antd'
import {withRouter} from 'react-router-dom'
import {connect} from 'react-redux'
import moment from 'moment'

import * as actions from '../../actions/quiz'
import {quizType} from '../../constant/quiz-type'
import SearchInput from '../common/search-input'

class Quizzes extends Component {
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
    const {searchContent, selectType} = this.state

    this.props.getQuizzes(page, selectType, searchContent)
  }

  onSearch (selectType, searchContent) {
    const defaultPage = 1
    this.setState({
      selectType,
      searchContent
    })
    this.props.getQuizzes(defaultPage, selectType, searchContent)
  }
  render () {
    const dataSource = this.props.quizzes.content.map(quiz => {
      quiz.createTime = moment(quiz.createTime).format('YYYY-MM-DD')
      if (quiz.tags) {
        quiz = Object.assign({}, quiz, {tags: quiz.tags.map(tag => <Tag key={tag.id}>{tag.name}</Tag>)})
      }
      return Object.assign({}, quiz, {type: quizType[quiz.type]})
    })

    const columns = [{
      title: '类型',
      dataIndex: 'type',
      sorter: (a, b) => a.type.length - b.type.length
    }, {
      title: '引用次数',
      dataIndex: 'referenceNumber',
      width: '12%',
      sorter: (a, b) => a.referenceNumber - b.referenceNumber,
      className: 'hidden'
    }, {
      title: '创建时间',
      dataIndex: 'createTime'
    }, {
      title: '创建者',
      dataIndex: 'maker',
      sorter: (a, b) => a.maker.length - b.maker.length
    }, {
      title: '标签',
      dataIndex: 'tags',
      width: '35%',
      sorter: (a, b) => a.tags.length - b.tags.length
    }]
    const pagination = {
      current: this.state.page,
      total: this.props.quizzes.totalElements,
      pageSize: this.state.pageSize,
      onChange: (page) => {
        this.onChange(page, this.state.pageSize)
      }
    }
    return (
      <div>
        <Row>
          <Col offset={9}>
            <SearchInput hasQuizType onSearch={this.onSearch.bind(this)} />
          </Col>
        </Row>
        <Table rowKey={record => record.id + record.type}
          columns={columns}
          dataSource={dataSource}
          pagination={pagination}
          bordered
          className='table-center' />
      </div>
    )
  }
}
const mapStateToProps = ({quizzes}) => ({
  quizzes
})

const mapDispatchToProps = dispatch => ({
  getQuizzes: (page, searchType, searchContent) => dispatch(actions.getQuizzes(page, searchType, searchContent))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Quizzes))
