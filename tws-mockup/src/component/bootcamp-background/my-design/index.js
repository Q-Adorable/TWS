import React, { Component } from 'react'
import { Row } from 'antd'
import BootcampCard from './bootcamp-card'
import BootcampCreatorChoice from './bootcamp-creator-choice'
import BackgroundHeader from '../background-header'
import trainingCourses from '../../../mock-data/training-courses'

class MyDesign extends Component {
  render () {
    return (
      <div>
        <BackgroundHeader />
        <Row gutter={16}>
          <BootcampCreatorChoice />
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

export default MyDesign
