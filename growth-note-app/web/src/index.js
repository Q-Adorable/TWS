import React from 'react'
import ReactDOM from 'react-dom'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import thunkMiddleware from 'redux-thunk'
import { initUser } from './action/user'
import { initSettings } from './action/settings'

import reducer from './reducer'
import App from './component/App'

import './style/index.css'

const store = createStore(reducer, applyMiddleware(thunkMiddleware))

store.dispatch(initUser())
store.dispatch(initSettings())

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root'))
