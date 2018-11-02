import React, { Component } from 'react'
import { connect } from 'react-redux'

import * as actions from '../../action/practise-diary'
import PractiseDiary from './practise-diary'
import PractiseDiaryEditor from './practise-diary-editor'
import constant from '../../constant/constant'
import 'antd/dist/antd.css'
import { Pagination } from 'antd'
import * as followActions from '../../action/follow'

class PractiseDiaryList extends Component {
  constructor(props) {
    super(props)
    this.state = {
      page: 1,
      pageSize: constant.PAGE_SIZE
    }
  }

  componentDidMount() {
    this.props.getPractiseDiaries(this.state.page, this.state.pageSize)
    this.props.getFollowTutors()
  }

  onChange(page) {
    this.setState({ page })
    this.props.getPractiseDiaries(page, this.state.pageSize)
  }

  render() {
    const practiseDiaryAndComments = this.props.practiseDiariesInfo.practiseDiaryAndComments || []
    return (
      <div>
        <PractiseDiaryEditor />
        {practiseDiaryAndComments.map((practiseDiaryAndComment, index) =>
          <PractiseDiary
            page={this.state.page} pageSise={this.state.pageSize}
            id={practiseDiaryAndComment.practiseDiary.id}
            key={index}
          />)}
        <Pagination className='practise-diary'
          style={{ 'display': this.props.practiseDiariesInfo.total ? '' : 'none' }}
          onChange={this.onChange.bind(this)}
          defaultCurrent={this.state.page} total={this.props.practiseDiariesInfo.total}
          defaultPageSize={constant.PAGE_SIZE} />
      </div>
    )
  }
}

const mapStateToProps = state => ({ practiseDiariesInfo: state.practiseDiariesInfo })
const mapDispatchToProps = dispatch => ({
  getPractiseDiaries: (page, pageSize) => dispatch(actions.getPractiseDiary(page, pageSize)),
  getFollowTutors: () => { dispatch(followActions.getFollowTutors()) }
})

export default connect(mapStateToProps, mapDispatchToProps)(PractiseDiaryList)
