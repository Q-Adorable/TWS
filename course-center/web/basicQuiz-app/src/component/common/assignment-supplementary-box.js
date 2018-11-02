import React, {Component} from 'react'
import {Button, Icon, message} from 'antd'
import {connect} from 'react-redux'
import '../../less/index.less'
import {withRouter} from 'react-router-dom'
import {TwsReactMarkdownEditor, TwsReactMarkdownPreview} from 'tws-antd'
import {getUploadUrl} from '../../constant/upload-url'

const NOT_EXIT_ID = -1

class AssignmentContentAndAnswerBox extends Component {
  constructor (props) {
    super(props)
    this.state = {
      currentEditCommentId: NOT_EXIT_ID,
      supplement: ''
    }
  }
  submitSupplementary (id) {
    const {supplement} = this.state
    const {assignmentId} = this.props
    if (supplement.trim() === '') {
      message.warning('亲，内容不能为空哦')
    } else {
      this.props.submitSupplementary(id, supplement, assignmentId)
      this.setState({
        currentEditCommentId: NOT_EXIT_ID
      })
    }
  }

  render () {
    const {reviewQuizId, supplement, userType, settings} = this.props
    const type = supplement ? '修改' : '添加'
    const {currentEditCommentId} = this.state

    return (<div>
      <div className='margin-t-3'>
        <h3>补充说明&nbsp;&nbsp;
            {userType ? ''
                : <a href='javascript:'
                  onClick={() => this.setState({currentEditCommentId: reviewQuizId, supplement: supplement || ''})}>
                  <span>{type}</span><Icon type='edit' />
                </a>}
        </h3>
        {currentEditCommentId !== reviewQuizId
             ? supplement
                   ? <div className='margin-t-2 mark-down-wrap'>
                     <TwsReactMarkdownPreview source={supplement} />
                   </div>
                   : ''
             : <div className='margin-t-2 mark-down-wrap'>
               <div className='margin-b-1'>
                 <h3>{type}补充内容</h3>
                 <TwsReactMarkdownEditor value={supplement}
                  action={getUploadUrl(settings.appContextPath)}
                  onChange={supplement => this.setState({supplement})} />
                 <Button className='margin-t-2' type="primary"
                   onClick={this.submitSupplementary.bind(this, reviewQuizId)}>确定</Button>
                 <Button className='margin-t-2 margin-l-2'
                   onClick={() => this.setState({currentEditCommentId: NOT_EXIT_ID})}>取消</Button>
               </div>
             </div>}
      </div>
    </div>
    )
  }
}
const mapStateToProps = state => ({
  settings: state.settings
})

export default withRouter(connect(mapStateToProps, null)(AssignmentContentAndAnswerBox))
