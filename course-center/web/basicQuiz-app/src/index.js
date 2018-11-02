import React from 'react'
import ReactDOM from 'react-dom'
import App from './App'
import { Provider } from 'react-redux'
import {createStore, applyMiddleware} from 'redux'
import thunkMiddleware from 'redux-thunk'
import reducer from './reducers'
import 'antd/dist/antd.css'
import { initSettings } from './actions/settings'

const store = createStore(reducer, applyMiddleware(thunkMiddleware))
store.dispatch(initSettings())
ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
    document.getElementById('root'))
