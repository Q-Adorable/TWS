import HTTP_CODE from '../constant/httpCode'
import * as request from '../constant/fetchRequest'
import * as practiseDiary from './practise-diary'
import * as excellentDiary from './excellent-diary'
import * as follow from './follow'

export const submitComments = (followeeId, id, commentContent, callback) => {
  const comments = {
    practiseDiaryId: id,
    commentContent
  }
  return dispatch => {
    ;(async () => {
      const res = await request.post(`./api/comments`, comments)
      if (res.status === HTTP_CODE.CREATED) {
        callback()
        dispatch(practiseDiary.getPractiseDiary())
        dispatch(excellentDiary.getExcellentDiary())
        if (followeeId === -1) {
          return
        }
        dispatch(follow.getFolloweeDiariesAndComments(followeeId))
      }
    })()
  }
}
