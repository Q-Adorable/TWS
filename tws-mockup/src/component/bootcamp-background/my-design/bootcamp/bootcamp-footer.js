import React from 'react'
import { message, Button, Col } from 'antd'
import { Link } from 'react-router-dom'
import '../../../../style/common.css'

const success = () => {
  message.success('已保存')
}

const BootcampFooter = () => {
  return (
    <Col span={6} offset={18} >
      <Link to='/'>
        <Button onClick={success} className='margin--right'>保存训练营</Button>
      </Link>
      <Button type='danger'>删除训练营</Button>
    </Col>
  )
}

export default BootcampFooter
