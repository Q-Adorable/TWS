import React from 'react'
import ReactDOM from 'react-dom'
import App from './components/App'
import { AppContainer } from 'react-hot-loader'
import { Provider } from 'react-redux'
import {createStore, applyMiddleware} from 'redux'
import thunkMiddleware from 'redux-thunk'
import reducer from './reducers'
import {initSettings} from './actions/settings'
import {initUser} from './actions/user'

const store = createStore(reducer, applyMiddleware(thunkMiddleware))
// 初始化环境变量
store.dispatch(initSettings())
store.dispatch(initUser())
const render = Component => {
  ReactDOM.render(
    <Provider store={store}>
      <AppContainer>
        <Component />
      </AppContainer>
    </Provider>,
        document.getElementById('root')
    )
}
render(App)

if (module.hot) {
  module.hot.accept('./components/App', () => { render(App) })
}
