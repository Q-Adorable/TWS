import React, { Component } from 'react'
import { Breadcrumb } from 'antd'
import { withRouter } from 'react-router-dom'

const defaultBreadCrumbs = ['训练营', '训练营后台', '我设计的训练营']

class TwsBreadCrumb extends Component {
  state = {
    breadCrumbs: defaultBreadCrumbs
  };
  render () {
    return (
      <Breadcrumb className='content-breadcrumb'>
        {
          this.state.breadCrumbs.map((item, key) => {
            return (
              <Breadcrumb.Item key={key}>
                {item}
              </Breadcrumb.Item>
            )
          })
        }
      </Breadcrumb>
    )
  }
}

export default withRouter(TwsBreadCrumb)
