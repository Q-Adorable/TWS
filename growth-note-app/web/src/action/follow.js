import HTTP_CODE from '../constant/httpCode'
import * as request from '../constant/fetchRequest'
import constant from '../constant/constant'
import { message } from 'antd'

export const refreshFolloweeListAndDiaries = followeeListAndDiaries => ({
  type: 'REFRESH_PRACTISE_CONTACTS',
  followeeListAndDiaries
})
export const refreshFolloweTutors = users => ({
  type: 'REFRESH_CONTACT_USERS',
  users
})
export const getFollowUsers = users => ({
  type: 'REFRESH_CONTACT_USERS',
  users
})

export const refreshFollowee = followees => ({
  type: 'REFRESH_CONTACT_USER',
  followees
})

export const refreshFolloweeDiariesAndComments = followeeDiariesAndComments => ({
  type: 'REFRESH_CONTACT_USER_DIARY_COMMENTS',
  followeeDiariesAndComments
})

export const search = value => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(`./api/users?nameOrEmail=${value}`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshFollowee(res.body))
      }
    })()
  }
}

export const getFolloweeListAndDiaries = () => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(`./api/followees`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshFolloweeListAndDiaries(res.body))
      }
    })()
  }
}
export const getFollowTutors = () => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(`./api/followees/tutors`)
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshFolloweTutors(res.body))
      }
    })()
  }
}

export const saveFollowee = followeeId => {
  return dispatch => {
    ;(async () => {
      const res = await request.post(`./api/users/1/follows/${followeeId}`)
      if (res.status === HTTP_CODE.CREATED) {
        dispatch(getFolloweeListAndDiaries())
      }
    })()
  }
}

export const getFolloweeDiariesAndComments = (
  followeeId,
  page = 1,
  pageSize = constant.PAGE_SIZE
) => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(
        `./api/followees/${followeeId}/practise-diaries?page=${page}&pageSize=${pageSize}`
      )
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshFolloweeDiariesAndComments(res.body))
      }
    })()
  }
}

export const unFollow = followeeId => {
  return dispatch => {
    ;(async () => {
      const res = await request.del(`./api/followees/${followeeId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('取消关注成功')
        dispatch(getFolloweeListAndDiaries())
      }
    })()
  }
}
export const follow = followeeId => {
  return dispatch => {
    ;(async () => {
      const res = await request.post(`./api/followees/${followeeId}`)
      if (res.status === HTTP_CODE.CREATED) {
        message.success('关注成功')
        dispatch(getFolloweeListAndDiaries())
      }
    })()
  }
}
