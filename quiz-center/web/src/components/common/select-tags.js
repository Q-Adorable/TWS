import React from 'react'
import { Select, Form } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import { formItemLayout } from '../../constant/constant-style'
import * as actions from '../../actions/tag'

const FormItem = Form.Item
const {Option} = Select

class SelectTags extends React.Component {
  componentDidMount () {
    this.props.searchTags()
  }
  handleChange (value) {
    this.props.addTags(value)
  }
  render () {
    const tags = this.props.tags || []
    let options
    if (tags.length !== 0) {
      options = tags.map((tag, index) => <Option key={tag.name}>{tag.name}</Option>)
    }
    return <FormItem
      {...formItemLayout}
      label={'标签'}
    >
      <Select
        mode='tags'
        onChange={this.handleChange.bind(this)}
        value={this.props.currentTags}>
        {options}
      </Select>
    </FormItem>
  }
}
const mapStateToProps = ({searchTags}) => ({
  tags: searchTags
})

const mapDispatchToProps = (dispatch) => ({
  searchTags: () => dispatch(actions.searchTags())
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(SelectTags))
