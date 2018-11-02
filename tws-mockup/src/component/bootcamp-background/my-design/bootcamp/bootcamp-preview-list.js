import React, { Component } from 'react'
import { Button, Row, Col, Layout, Card } from 'antd'
import { Link } from 'react-router-dom'
import BootcampPreview from './bootcamp-preview'

const { Header, Content, Footer } = Layout

export default class BootcampPreviewList extends Component {
  render() {
    return (
      <Layout className='full-height'>
        <Header className='bootcamp__header'>
          <Col span={24} style={{ textAlign: 'center' }}>
            <h1>2018毕业生训练营</h1>
          </Col>
        </Header>
        <Content className='bootcamp__content'>
          <Row style={{ marginBottom: '15px' }}>
            <Col className='bootcamp-row' span={22}>
              <Card bordered={false} className='bootcamp-description'>
                训练营简介
              </Card>
            </Col>
          </Row>
          <Row>
            <Col className='bootcamp-row' span={22}>
              <BootcampPreview />
            </Col>
          </Row>
        </Content>
        <Footer className='bootcamp__footer'>
          <Link to='bootcamp'>
            <Button style={{ float: 'right' }}>返回</Button>
          </Link>
        </Footer>
      </Layout>
    )
  }
}
