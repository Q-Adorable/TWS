import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Pagination, Button, Row, Col } from 'antd'
import * as follow from '../../action/follow'
import FolloweeDiary from './followee-diary'
import '../../style/App.css'
import constant from '../../constant/constant'
import FolloweeDiaryListMonthView from './followee-diary-list-month-view'

class FolloweeDiaryList extends Component {
  constructor(props) {
    super(props)
    this.state = {
      followeeId: '',
      page: 1,
      pageSize: constant.PAGE_SIZE,
      isShowAndCloseAllContent: false,
      isShowMonthView: false
    }
  }

  componentDidMount() {
    const { location } = this.props
    this.update(location.pathname)
  }

  update(pathname) {
    const followeeId = parseInt(pathname.match(/\d+$/g)[0], 10)
    if (!followeeId) return
    this.setState({
      followeeId
    })
    this.props.getContactDiariesAndComs(followeeId)
  }
  showAndCloseAllContent() {
    this.setState({
      isShowAndCloseAllContent: !this.state.isShowAndCloseAllContent
    })
  }
  onChange(page) {
    this.setState({ page })
    this.props.getContactDiariesAndComs(this.state.followeeId, page, this.state.pageSize)
  }
  showAndCloseMonthView() {
    this.setState({
      isShowMonthView: !this.state.isShowMonthView
    })
  }
  render() {
    const practiseDiaryAndComments = this.props.contactDiaryAndComList.practiseDiaryAndComments || []
    const practiseDiaryListView =
      <div>
        {
          practiseDiaryAndComments.map(
            (diaryAndComment, index) =>
              (
                <div key={index} className='practise-diary'>
                  <FolloweeDiary
                    id={diaryAndComment.practiseDiary.id}
                    key={index}
                    followeeId={this.state.followeeId}
                    isShowAndCloseAllContent={this.state.isShowAndCloseAllContent}
                  />
                </div>
              )
          )
        }
        <Pagination
          style={{ 'display': this.props.contactDiaryAndComList.total ? '' : 'none' }}
          onChange={this.onChange.bind(this)}
          defaultCurrent={this.state.page}
          total={this.props.contactDiaryAndComList.total}
          defaultPageSize={constant.PAGE_SIZE}
        />
      </div>
    return (
      <div>
        <Row type='flex' justify='end' gutter={{ xs: 8, sm: 16, md: 24 }}>
          <Col className={this.state.isShowMonthView ? 'hidden' : null} >
            <Button type='primary' size='small' ghost className='button-note'
              onClick={this.showAndCloseAllContent.bind(this)}>
              {this.state.isShowAndCloseAllContent ? '收起日志全文' : '展开日志全文'}
            </Button>
          </Col>
          <Col>
            <Button
              type='primary'
              size='small'
              ghost
              className='button-note'
              onClick={() => this.showAndCloseMonthView()} >
              {this.state.isShowMonthView ? '列表视图' : '月视图'}
            </Button>
          </Col>
        </Row>
        <Row>&nbsp;</Row>
        {
          !this.state.isShowMonthView ?
            practiseDiaryListView :
            <FolloweeDiaryListMonthView practiseDiaryAndComments={[...practiseDiaryAndComments]} />
        }
      </div >
    )
  }
}

const mapStateToProps = state => ({ contactDiaryAndComList: state.contactUserDiariesAndComments })
const mapDispatchToProps = dispatch => ({
  getContactDiariesAndComs: (followeeId, page, pageSize) => dispatch(follow.getFolloweeDiariesAndComments(followeeId, page, pageSize))
})

export default connect(mapStateToProps, mapDispatchToProps)(FolloweeDiaryList)
