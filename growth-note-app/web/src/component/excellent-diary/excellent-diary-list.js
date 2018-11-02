import React, {Component} from 'react'
import { Button, Col, Pagination, Row } from 'antd'
import {connect} from 'react-redux'
import * as actions from '../../action/excellent-diary'
import constant from '../../constant/constant'
import ExcellentDiary from './excellent-diary'

class ExcellentPractiseDiary extends Component {
  constructor (props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: constant.PAGE_SIZE
    }
  }

  componentDidMount () {
    this.props.getExcellentDiaries()
  }

  onChange (page) {
    this.setState({page})
    this.props.getExcellentDiaries(page, this.state.pageSize)
  }
  showAndCloseAllContent () {
    this.setState({
      isShowAndCloseAllContent: !this.state.isShowAndCloseAllContent
    })
  }
  render () {
    const excellentDiariesAndComments = this.props.excellentDiariesInfo.excellentDiariesAndComments || []
    return (<div>
      <Row type='flex' justify='end' >
        <Col >
          <Button type='primary' size='small' ghost className='button-note'
            onClick={this.showAndCloseAllContent.bind(this)}>
            {this.state.isShowAndCloseAllContent ? '收起日志全文' : '展开日志全文'}
          </Button>
        </Col>
      </Row>
      <Row>&nbsp;</Row>
      {excellentDiariesAndComments.map((excellentDiaryAndComments, index) =>
        <ExcellentDiary id={excellentDiaryAndComments.excellentDiary.id} key={index}
          isShowAndCloseAllContent={this.state.isShowAndCloseAllContent}
                />
            )}

      <Pagination className='practise-diary'
        style={{'display': this.props.excellentDiariesInfo.total ? '' : 'none'}}
        onChange={this.onChange.bind(this)}
        defaultCurrent={this.state.page} total={this.props.excellentDiariesInfo.total}
        defaultPageSize={constant.PAGE_SIZE} />
    </div>)
  }
}

const mapStateToProps = state => ({excellentDiariesInfo: state.excellentDiariesInfo})
const mapDispatchToProps = dispatch => ({
  getExcellentDiaries: (page, pageSize) => dispatch(actions.getExcellentDiary(page, pageSize))
})

export default connect(mapStateToProps, mapDispatchToProps)(ExcellentPractiseDiary)
