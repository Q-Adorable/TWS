import React, { Component } from 'react'
import { Radio } from 'antd'
import { Link } from 'react-router-dom'

const { Button: RadioButton, Group: RadioGroup } = Radio

export default class BackgroundHeader extends Component {
  constructor(props) {
    super(props)
    this.state = {
      designButton: '',
      visibleButton: 'radio-group-button'
    }
  }
  handleOnClickDesign = () => {
    this.setState({
      designButton: '',
      visibleButton: 'radio-group-button'
    })
  }

  handleOnClickVisible = () => {
    this.setState({
      designButton: 'radio-group-button',
      visibleButton: ''
    })
  }

  render() {
    return (
      <RadioGroup className='radio-group' defaultValue='我设计的' size='large'>
        <Link to='/bootcamp/my-design'>
          <RadioButton className={this.state.designButton} onClick={this.handleOnClickDesign} value='我设计的'>我设计的</RadioButton>
        </Link>
        <Link to='/bootcamp/visible'>
          <RadioButton className={this.state.visibleButton} onClick={this.handleOnClickVisible} value='我可见的'>我可见的</RadioButton>
        </Link>
      </RadioGroup>
    )
  }
}
