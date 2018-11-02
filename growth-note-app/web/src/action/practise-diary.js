import HTTP_CODE from '../constant/httpCode'
import * as request from '../constant/fetchRequest'
import constant from '../constant/constant'

export const refreshPractiseDiary = practiseDiaryAndComments => ({
  type: 'REFRESH_PRACTISE_DIARY_COMMENT',
  practiseDiaryAndComments
})

export const getPractiseDiary = (page = 1, pageSize = constant.PAGE_SIZE) => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(
        `./api/diaries/?page=${page}& pageSize=${pageSize}`
      )
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshPractiseDiary(res.body))
      }
    })()
  }
}

export const deletePractiseDiary = (rawId, page, pageSize) => {
  return dispatch => {
    ;(async () => {
      const res = await request.del(`./api/diaries/${rawId}`)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch(getPractiseDiary(page, pageSize))
      }
    })()
  }
}

export const createPractiseDiary = growthNote => {
  return dispatch => {
    ;(async () => {
      const res = await request.post(`./api/diaries`, growthNote)
      if (res.status === HTTP_CODE.CREATED) {
        dispatch(getPractiseDiary())
      }
    })()
  }
}

export const updatePractiseDiary = (growthNote, rawId) => {
  return dispatch => {
    ;(async () => {
      const res = await request.update(`./api/diaries/${rawId}`, growthNote)
      if (res.status === HTTP_CODE.NO_CONTENT) {
        dispatch(getPractiseDiary())
      }
    })()
  }
}
