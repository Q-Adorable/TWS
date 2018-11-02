import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import { message } from 'antd'
import { STATUS } from '../constant/constant-data'

export const addStack = (stack) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/stacks`, stack)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('发送请求到jenkins成功')
        dispatch({
          type: 'ADD_STACK',
          data: {'status': STATUS.LINE_UP, id: res.body.id}
        })
      }
    })()
  }
}

export const deleteStack = (stackId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/stacks/${stackId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch({
          type: 'DELETE_STACK',
          data: {'status': STATUS.LINE_UP, id: stackId}
        })
      }
    })()
  }
}

export const judgeStackIsDeleted = (stackId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/stacks/${stackId}`)
      if (res.status === HTTP_CODE.OK) {
        if (res.body.status === STATUS.SUCCESS) {
          message.success('删除成功')
          dispatch({
            type: 'DELETE_STACK',
            data: {'status': STATUS.SUCCESS}
          })
          dispatch(getAllStacksPage())
        } else if (res.body.status === STATUS.ERROR) {
          dispatch({
            type: 'DELETE_STACK',
            data: {'status': STATUS.ERROR}
          })
        } else {
          dispatch({
            type: 'DELETE_STACK',
            data: {'status': STATUS.PROGRESS}
          })
        }
      }
    })()
  }
}

export const searchStackStatus = (stackId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/stacks/${stackId}/status`)
      if (res.status === HTTP_CODE.OK) {
        if (res.body.status) {
          dispatch({
            type: 'ADD_STACK',
            data: res.body
          })
        } else {
          dispatch({
            type: 'GET_STACK_LOGS',
            data: res.body
          })
        }
      }
    })()
  }
}

export const getAllStacksPage = (page = 1, pageSize = 10) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/stacks/pageable?page=${page}&pageSize=${pageSize}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ALL_STACKS_PAGE',
          data: res.body
        })
      }
    })()
  }
}

export const getAllStacks = () => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/stacks`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ALL_STACKS',
          data: res.body
        })
      }
    })()
  }
}
