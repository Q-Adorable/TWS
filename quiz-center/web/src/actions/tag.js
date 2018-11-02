import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import { message } from 'antd'

export const addTag = (tag, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/tags`, tag)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('新增标签成功')
        callback()
        dispatch({
          type: 'ADD_TAG'
        })
      }
    })()
  }
}

export const getTag = (tagId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/tags/${tagId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_TAG',
          data: res.body
        })
      }
    })()
  }
}

export const editTag = (tag, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`./api/v3/tags/${tag.id}`, tag)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('更新成功')
        callback()
      }
    })()
  }
}

export const getAllTags = (page = 1, pageSize = 10) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/tags?page=${page}&pageSize=${pageSize}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ALL_TAGS',
          data: res.body
        })
      }
    })()
  }
}

export const searchTags = () => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/tags/searchTags`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'SEARCH_TAGS',
          data: res.body
        })
      }
    })()
  }
}

export const deleteTag = (tagId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/tags/${tagId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('删除成功')
        dispatch(getAllTags())
      }
    })()
  }
}
