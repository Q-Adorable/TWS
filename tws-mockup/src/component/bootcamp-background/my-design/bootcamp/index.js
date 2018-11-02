import React, { Component } from 'react'
import { Layout } from 'antd'
import BootcampHeader from './bootcamp-header'
import BootcampContent from './bootcamp-content'
import BootcampFooter from './bootcamp-footer'

import '../../../../style/bootcamp-background.css'
import '../../../../style/common.css'

const { Header, Content, Footer } = Layout

export default class Index extends Component {
  render () {
    return (
      <Layout className='full--height'>
        <Header className='bootcamp__header'>
          <BootcampHeader />
        </Header>
        <Content className='bootcamp__content'>
          <BootcampContent />
        </Content>
        <Footer className='bootcamp__footer'>
          <BootcampFooter />
        </Footer>
      </Layout>
    )
  }
}
