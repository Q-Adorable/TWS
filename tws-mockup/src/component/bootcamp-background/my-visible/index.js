import React, { Component } from 'react'
import { Row } from 'antd'
import BootcampCard from './my-visible-card'
import BackgroundHeader from '../background-header'
import trainingCourses from '../../../mock-data/training-courses'

class MyVisible extends Component {
  render () {
    return (
      <div>
        <BackgroundHeader />
        <Row gutter={16}>
          {
            trainingCourses.map((trainingCourse, key) => {
              return (<BootcampCard trainingCourse={trainingCourse} key={key} />)
            })
          }
        </Row>
      </div>
    )
  }
}

export default MyVisible
