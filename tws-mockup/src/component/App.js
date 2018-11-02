import React, { Component } from 'react'
import '../style/App.css'
import TwsLayout from './tws-layout'
import {
  HashRouter as Router,
  Route
} from 'react-router-dom'
import MyDesign from './bootcamp-background/my-design'
import MyVisible from './bootcamp-background/my-visible'
import Bootcamp from './bootcamp-background/my-design/bootcamp'
import BootcampPreviewList from './bootcamp-background/my-design/bootcamp/bootcamp-preview-list'

export default class App extends Component {
  render () {
    return (
      <Router>
        <TwsLayout>
          <Route path='/bootcamp/my-design' component={MyDesign} />
          <Route path='/bootcamp/visible' component={MyVisible} />
          <Route exact path='/bootcamp' component={Bootcamp} />
          <Route path='/bootcampPreview' component={BootcampPreviewList} />
        </TwsLayout>
      </Router>
    )
  }
}
