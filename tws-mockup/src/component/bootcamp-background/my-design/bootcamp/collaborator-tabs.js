import React, { Component } from 'react'
import { Tabs, Transfer } from 'antd'
import collaboratorsList from '../../../../mock-data/collaborator-list'
import '../../../../style/common.css'

const TabPane = Tabs.TabPane

export default class CollaboratorTabs extends Component {

  state = {
    targetKeys: []
  }

  filterOption = (inputValue, option) => {
    return option.description.indexOf(inputValue) > -1;
  }

  handleChange = (targetKeys) => {
    this.setState({ targetKeys });
  }

  render() {
    return (
      <Tabs defaultActiveKey='0' type='card'>
        {
          collaboratorsList.map((collaborators, key) => {
            return (
              <TabPane tab={collaborators.name} key={key}>
                <Transfer
                  className={'align--center'}
                  dataSource={collaborators.data}
                  showSearch
                  filterOption={this.filterOption}
                  targetKeys={this.state.targetKeys}
                  onChange={this.handleChange}
                  render={item => item.name}
                />
              </TabPane>
            )
          })}
      </Tabs>
    )
  }
}
