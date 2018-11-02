import React from 'react'
import { connect } from 'react-redux'
import { Button, Input, Select, AutoComplete } from 'antd'
import { withRouter } from 'react-router-dom'
import * as actions from '../../actions/tag'
import sideBars from '../../constant/sidebar'
import {searchUser} from '../../actions/user'
import {QUIZ_TYPES} from '../../constant/constant-data'

const InputGroup = Input.Group
const Option = Select.Option

class SearchInput extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      selectType: 'makerId',
      searchContent: '',
      isShowType: true
    }
  }

  componentDidMount () {
    this.props.searchTags()
    const {pathname} = this.props.location
    const sidebar = sideBars.find(sideBar => sideBar.name === '全部')
    if (pathname.indexOf(sidebar.linkTo[0]) < 0) {
      this.setState({isShowType: false})
    }
  }

  onChangeType (selectType) {
    this.setState({selectType, searchContent: ''})
  }

  onSearch () {
    this.props.onSearch(this.state.selectType, this.state.searchContent)
  }

  onChange (searchContent) {
    this.setState({searchContent})
  }

  onSelect (value) {
    const user = this.props.users.find(user => user.username === value)
    this.setState({
      searchContent: user.id
    })
  }

  searchMaker (value) {
    this.props.searchMaker(value)
  }

  render () {
    const tags = this.props.tags
    const dataSource = this.props.users.map(user => user.username)
    return <div className='margin-b-2'>
      <InputGroup compact>
        <Select
          defaultValue={this.state.selectType}
          onChange={this.onChangeType.bind(this)}
          style={{width: 90}}
        >
          {this.state.isShowType ? <Option value='type'>类型</Option> : ''}
          <Option value='makerId'>创建者</Option>
          {!this.props.isLogic ? <Option value='tags'>tags</Option> : ''}
        </Select>
        {
          this.state.selectType === 'tags'
            ? <Select
              mode='multiple'
              style={{width: 200}}
              placeholder='请输入标签'
              onChange={this.onChange.bind(this)}
          >
              {tags.map(tag => <Option key={tag.name} value={tag.name}>{tag.name}</Option>)}
            </Select>
            : this.state.selectType === 'makerId'
            ? <AutoComplete
              dataSource={dataSource}
              style={{ width: 200 }}
              onSelect={this.onSelect.bind(this)}
              onSearch={this.searchMaker.bind(this)}
              placeholder='请输入查询内容'
            />
            : <Select
              style={{width: 200}}
              placeholder='请选择类型'
              onChange={(searchContent) => this.setState({searchContent})}
            >
              {QUIZ_TYPES.map(item => <Option key={item.type} value={item.type}>{item.value}</Option>)}
            </Select>
        }

        <Button style={{width: 100}}
          type='primary'
          shape='circle'
          icon='search'
          onClick={this.onSearch.bind(this)}>搜索</Button>
      </InputGroup>
    </div>
  }
}

const mapStateToProps = ({searchTags, userInfo}) => ({
  tags: searchTags,
  users: userInfo.users
})

const mapDispatchToProps = (dispatch) => ({
  searchTags: () => dispatch(actions.searchTags()),
  searchMaker: (value) => dispatch(searchUser(value))
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(SearchInput))
