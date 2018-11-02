import React, { Component } from 'react'
import { Breadcrumb } from 'antd'
import breadcrumbs from '../../constant/breadcrumb'
import { withRouter } from 'react-router-dom'
import UrlPattern from 'url-pattern'

class TwsBreadCrumb extends Component {
  state = {
    breadCrumbs: []
  }

  componentDidMount () {
    const {history, location} = this.props
    history.listen(this.update.bind(this))
    this.update(location)
  }

  update (location) {

    const currentBreadcrumb = breadcrumbs.find(breadcrumb => {
      const pattern = new UrlPattern(breadcrumb.linkTo)
      return pattern.match(location.pathname) !== null
    })

    const pattern = new UrlPattern(currentBreadcrumb.linkTo)
    const params = pattern.match(location.pathname)
    currentBreadcrumb.breadCrumbs.map((breadcrumbs) => {
      const pattern = new UrlPattern(breadcrumbs.linkTo)
      if (params !== null) {
        breadcrumbs.linkTo = pattern.stringify({
          programId: params.programId,
          taskId: params.taskId,
          studentId: params.studentId,
          assignmentId: params.assignmentId,
          quizId: params.quizId
        })
      }
      return true
    })
    this.setState({
      breadCrumbs: currentBreadcrumb.breadCrumbs
    })
  }

  changePage (item) {
    const url = item.linkTo
    const {introduceUrl, studentUrl} = this.props.settings

    if (this.isIndex(url)) {
      window.location.href = 'https://school.thoughtworks.cn/learn/home/index.html#/app-center'
    }
    else if (this.isInstructor(url)) {
      window.location.assign(url.replace('/instructor', introduceUrl))
    }
    else if (this.isSubjective(url, item)) {
      this.props.history.push(url)
    }
    else {
      window.location.assign(studentUrl + url)
    }

  }

  isSubjective (url, item) {
    return ['我的作业', '试卷预览', '学生作业详情', '作业'].some(t => t === item.name)
  }

  isInstructor (url) {
    return url.includes('/instructor')
  }

  isIndex (url) {
    return url.includes('/AppCenter')
  }

  render () {
    return (
      <Breadcrumb style={{margin: '12px 0', fontSize: '14px'}}>
        {
          this.state.breadCrumbs.map((item, key) => {
            return (
              <Breadcrumb.Item key={key}><a
                onClick={this.changePage.bind(this, item)}>{item.name}</a></Breadcrumb.Item>
            )
          })
        }
      </Breadcrumb>
    )
  }
}

export default withRouter(TwsBreadCrumb)