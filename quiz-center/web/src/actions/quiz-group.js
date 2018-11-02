import * as request from '../constant/fetch-request'
import HTTP_CODE from '../constant/http-code'
import { message } from 'antd'

export const getQuizGroups = (page = 1, pageSize = 10) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/quizGroups?page=${page}&pageSize=${pageSize}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_QUIZ_GROUPS',
          data: res.body
        })
      }
    })()
  }
}

export const joinQuizGroup = (quizGroupId) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/quizGroups/${quizGroupId}/users`)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('加入题组成功！')
        dispatch(getQuizGroups())
      }
    })()
  }
}

export const getAllMyQuizGroups = () => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/quizGroups/myQuizGroups`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_ALL_QUIZ_GROUPS',
          data: res.body
        })
      }
    })()
  }
}
export const addQuizGroup = (quizGroup, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.post(`./api/v3/quizGroups`, quizGroup)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('新增题组成功')
        callback()
      }
    })()
  }
}

export const getQuizGroup = (quizId) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/quizGroups/${quizId}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch({
          type: 'GET_QUIZ_GROUP',
          data: res.body
        })
      }
    })()
  }
}

export const editQuizGroup = (quizGroup, callback) => {
  return dispatch => {
    (async () => {
      const res = await request.update(`./api/v3/quizGroups/${quizGroup.id}`, quizGroup)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('更新成功')
        callback()
      }
    })()
  }
}

export const deleteUserFromGroup = (quizGroupId, userId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/quizGroups/${quizGroupId}/users/${userId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('删除成功')
        dispatch(getUsersFromGroup(quizGroupId))
      }
    })()
  }
}
export const deleteMySelfFromGroup = (quizGroupId) => {
  return dispatch => {
    (async () => {
      const res = await request.del(`./api/v3/quizGroups/${quizGroupId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('删除成功')
        dispatch(getQuizGroups())
      }
    })()
  }
}
export const getUsersFromGroup = (quizGroupId, page = 1, pageSize = 10) => {
  return dispatch => {
    (async () => {
      const res = await request.get(`./api/v3/quizGroups/${quizGroupId}/users?page=${page}&pageSize=${pageSize}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(dispatch({
          type: 'GET_GROUP_USERS',
          data: res.body
        }))
      }
    })()
  }
}