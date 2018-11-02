import React, { Component } from 'react'
import PractiseDiaryList from './my-practise-diary/practise-diary-list'
import ContactDiary from './follow/followee-diary-list'
import ExcellentPractiseDiary from './excellent-diary/excellent-diary-list'
import '../style/App.css'
import ContactList from './follow/followee-list'

import TwsLayout from './tws-layout'
import { HashRouter as Router, Route } from 'react-router-dom'

export default class App extends Component {
  render () {
    return (
      <Router>
        <TwsLayout>
          <Route exact path='/' component={PractiseDiaryList} />
          <Route path='/practise-diaries' component={PractiseDiaryList} />
          <Route exact path='/followees' component={ContactList} />
          <Route exact path='/followees/:id' component={ContactDiary} />
          <Route path='/excellent-diaries' component={ExcellentPractiseDiary} />
        </TwsLayout>
      </Router>
    )
  }
}
