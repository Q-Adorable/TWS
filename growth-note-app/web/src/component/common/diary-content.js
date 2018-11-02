import React, { Component } from 'react'
import ReactMarkdown from 'react-markdown'
import overFlow from '../../constant/diaryOverflow'
import '../../style/App.css'
import constant from '../../constant/constant'

export default class App extends Component {
  constructor (props) {
    super(props)
    this.state = {
      isShowAllContent: false,
      isShowAndCloseAllContent: false
    }
  }
  componentWillReceiveProps (props) {
    this.setState({
      isShowAllContent: props.isShowAndCloseAllContent
    })
  }
  showAllContent () {
    this.setState({
      isShowAllContent: !this.state.isShowAllContent
    })
  }

  render () {
    const content = this.props.content
    return (
      <div>
        <div className='practise-diary-note-content-div '>
          {
            this.state.isShowAllContent
              ? <ReactMarkdown className='mark-down-wrap' source={content} />
              : <ReactMarkdown className='mark-down-wrap' source={overFlow(content)} />
          }
        </div>

        <div className={content.length > constant.SHOW_MAX_DIARY_CONTENT ? 'show-all-content' : 'hidden'}
          onClick={this.showAllContent.bind(this)}>{this.state.isShowAllContent ? '收起' : '点击查看全文'}
        </div>
      </div>
    )
  }
}
