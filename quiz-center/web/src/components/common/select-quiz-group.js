import React, { Component } from 'react'
import { Form, Select } from 'antd'
import { connect } from 'react-redux'
import { formItemLayout } from '../../constant/constant-style'
import { withRouter } from 'react-router-dom'
import * as groupAction from '../../actions/quiz-group'
import '../../less/single-choice-quiz/index.less'

const FormItem = Form.Item
const Option = Select.Option

class SelectQuizGroup extends Component {
  constructor (props) {
    super(props)
    this.state = {
      quizGroupName: localStorage.getItem('quizGroupName')
    }
  }

  componentDidMount () {
    this.props.getAllMyQuizGroup()
    this.props.changeQuizGroup(localStorage.getItem('quizGroupId'))
  }

  componentWillReceiveProps (nextProps) {
    const quizGroupName = nextProps.quizGroupName
    if (quizGroupName && quizGroupName !== this.props.quizGroupName) {
      this.setState({quizGroupName})
    }
  }

  handleChange (value) {
    const currentGroup = this.props.allMyQuizGroups.find(group => group.name === value)
    localStorage.setItem('quizGroupName', currentGroup.name)
    localStorage.setItem('quizGroupId', currentGroup.id)
    this.props.changeQuizGroup(currentGroup.id)
    this.setState({quizGroupName: currentGroup.name})
  }

  render () {
    const groups = this.props.allMyQuizGroups
    const quizGroup = this.state.quizGroupName

    return (
      <div>
        <FormItem  {...formItemLayout} label={'题组'} required>
          <Select value={quizGroup} style={{width: '50%'}}
                  onChange={this.handleChange.bind(this)}>
            { groups.map((group, index) => {
              return (
                <Option value={group.name} key={index}>{group.name}</Option>
              )
            })
            }
          </Select>
          <div className='error-tip'>{this.props.quizGroupError}</div>
        </FormItem>
      </div>
    )
  }
}

const mapStateToProps = ({quizGroupData}) => ({
  allMyQuizGroups: quizGroupData.allMyQuizGroups
})

const mapDispatchToProps = dispatch => ({
  getAllMyQuizGroup: () => dispatch(groupAction.getAllMyQuizGroups())
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Form.create()(SelectQuizGroup)))
