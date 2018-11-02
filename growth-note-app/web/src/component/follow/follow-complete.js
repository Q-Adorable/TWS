import React, {Component} from 'react'
import {Input, Button, Icon, AutoComplete, message} from 'antd'
import HTTP_CODE from '../../constant/httpCode'

import * as request from '../../constant/fetchRequest'
import '../../style/complete.css'
import FollowOptionLink from './follow-complete-option-link'

const Option = AutoComplete.Option

function searchFollowees (query, callback) {
  (async () => {
    const res = await request.get(`./api/followees/searching?nameOrEmail=${query}`)
    if (res.status !== HTTP_CODE.OK) return
    callback(res.body)
  })()
}

function renderOption (item) {
  return (
    <Option key={item.id} value={item.userName} text={item.userName}>
      {item.userName}
      ({item.email})
      <span className='global-search-item-count'>
        <FollowOptionLink item={item} />
      </span>
    </Option>
  )
}

class FollowComplete extends Component {
  state = {
    dataSource: []
  };

  handleSearch (value) {
    searchFollowees(value, (data) => {
      this.setState({
        dataSource: data || []
      })
    })
  };

  followUser (value) {
    searchFollowees(value, (data) => {
      this.setState({
        dataSource: data || []
      })
      if (data[0].followed) {
        message.warning('请勿重复关注')
        return
      }
      this.props.follow(data[0].id)
    })
    this.handleSearch(value)
  }

  render () {
    const {dataSource} = this.state
    return (
      <div>
        <h3 style={{display: 'inline-block'}} className='margin-r-2'>搜索并添加:</h3>
        <div style={{display: 'inline-block', width: 400}} className='global-search-wrapper'>
          <AutoComplete
            className='global-search'
            size='large'
            style={{width: '100%'}}
            dataSource={dataSource.map(renderOption)}
            onSearch={this.handleSearch.bind(this)}
            onSelect={this.followUser.bind(this)}
            placeholder='input here'
            optionLabelProp='text'

          >

            <Input suffix={(
              <Button className='search-btn' size='large' type='primary'>
                <Icon type='search' />
              </Button>
            )}
            />
          </AutoComplete>
        </div>
      </div>

    )
  }
}

export default FollowComplete
