import React, { Component } from 'react'
import { Button, Modal, Row, Col, Table, Checkbox } from 'antd'
import CollaboratorTabs from './collaborator-tabs'
import data from '../../../../mock-data/authorization-data'
import { Link } from 'react-router-dom'

export default class BootcampHeader extends Component {
  constructor (props) {
    super(props)
    this.state = { isAddCollaborator: false, isAuthorization: false, data: data }
    this.columns = [{
      dataIndex: 'node'
    }, {
      width: 80,
      align: 'center',
      title: '可编辑',
      dataIndex: 'editable',
      render: (editable, data) => <Checkbox onChange={() => this.changeEditable(data)} checked={editable} />
    }, {
      width: 80,
      align: 'center',
      title: '可复制',
      dataIndex: 'replicable',
      render: (replicable, data) => <Checkbox onChange={() => this.changeReplicable(data)} checked={replicable}
        disabled={data.editable} />
    }]
  }

  showAddCollaborator = () => {
    this.setState({
      isAddCollaborator: true
    })
  }

  showAuthorization = () => {
    this.setState({
      isAuthorization: true
    })
  }

  handleAddCollaborator = () => {
    this.setState({
      isAddCollaborator: false
    })
  }

  cancelAddCollaborator = () => {
    this.setState({
      isAddCollaborator: false
    })
  }

  changeEditable = (data) => {
    if (!data.editable) {
      data = Object.assign(data, { editable: !data.editable, replicable: true })
    } else {
      data = Object.assign(data, { editable: !data.editable })
    }
    this.setState({
      data
    })
  }

  changeReplicable = (data) => {
    data = Object.assign(data, { replicable: !data.replicable })
    this.setState({
      data
    })
  }

  handleAuthorization = (e) => {
    this.setState({
      isAuthorization: false
    })
  }

  cancelAuthorization = () => {
    this.setState({
      isAuthorization: false
    })
  }

  render () {
    return (
      <Row>
        <Col span={4}>
          <Link to='/bootcampPreview'>
            <Button key='submit' type='primary'>
              预览
            </Button>
          </Link>
        </Col>
        <Col span={14} style={{ textAlign: 'center' }}>
          <h1>2018毕业生训练营</h1>
        </Col>
        <Col span={6} className='tws-btn-group'>
          <Button onClick={this.showAddCollaborator}>添加协作人</Button>
          <Modal
            visible={this.state.isAddCollaborator}
            closable={false}
            onCancel={this.cancelAddCollaborator}
            footer={[
              <Button key='submit' type='primary' onClick={this.handleAddCollaborator}>
                邀请协作
              </Button>
            ]}
          >
            <CollaboratorTabs />
          </Modal>
          <Button onClick={this.showAuthorization}>设置可见权限</Button>
          <Modal
            title='设置可见权限'
            visible={this.state.isAuthorization}
            closable={false}
            onCancel={this.cancelAuthorization}
            footer={[
              <Button key='submit' type='primary' onClick={this.handleAuthorization}>
                确定
              </Button>
            ]}
          >
            <Table columns={this.columns} dataSource={data} pagination={false} />
          </Modal>
        </Col>
      </Row>
    )
  }
}
