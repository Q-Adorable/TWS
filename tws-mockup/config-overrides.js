const {injectBabelPlugin} = require('react-app-rewired')
const rewireLess = require('react-app-rewire-less')

module.exports = function override (config, env) {
  config = injectBabelPlugin(['import', {libraryName: 'antd', style: true}], config)
  config = rewireLess(config, env, {modifyVars: {
    '@primary-color': '#108ee9',
    '@font-size-base': '14px'
  }})
  config = rewireLess.withLoaderOptions({
    javascriptEnabled: true
  })(config, env)
  return config
}
