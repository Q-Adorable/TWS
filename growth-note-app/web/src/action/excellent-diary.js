import HTTP_CODE from '../constant/httpCode'
import * as request from '../constant/fetchRequest'
import constant from '../constant/constant'
import * as practiseDiary from './practise-diary'
import { message } from 'antd'

export const refreshExcellentDiary = excellentDiaries => ({
  type: 'REFRESH_EXCELLENT_DIARY',
  excellentDiaries
})

export const getExcellentDiary = (page = 1, pageSize = constant.PAGE_SIZE) => {
  return dispatch => {
    ;(async () => {
      const res = await request.get(
        `./api/excellentDiaries?page=${page}& pageSize=${pageSize}`
      )
      if (res.status === HTTP_CODE.OK) {
        dispatch(refreshExcellentDiary(res.body))
      }
    })()
  }
}

export const addExcellentDiary = excellentDiaryId => {
  return dispatch => {
    ;(async () => {
      const res = await request.post(`./api/excellentDiaries`, {
        diaryId: excellentDiaryId
      })
      if (res.status === HTTP_CODE.CREATED) {
        message.success('推荐成功')
        dispatch(practiseDiary.getPractiseDiary())
      } else if (res.status === HTTP_CODE.CONFLICT) {
        message.success('已推荐，查看优秀日志')
      } else {
        message.error('操作失败')
      }
    })()
  }
}

export const cancelExcellentDiary = excellentDiaryId => {
  return dispatch => {
    ;(async () => {
      const res = await request.del(
        `./api/excellentDiaries/${excellentDiaryId}`
      )
      if (res.status === HTTP_CODE.NOT_FOUND) {
        message.success('找不到该日志')
      } else if (res.status === HTTP_CODE.NO_CONTENT) {
        message.success('已成功取消优秀日志')
        dispatch(getExcellentDiary())
      } else {
        message.error('操作失败')
      }
    })()
  }
}
