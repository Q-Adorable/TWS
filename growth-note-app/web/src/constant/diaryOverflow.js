import constant from './constant'

export default function overFlow (diary) {
  const maxLength = constant.SHOW_MAX_DIARY_CONTENT
  if (diary.length > maxLength) {
    return diary.substring(0, maxLength) + '...'
  }
  return diary
}
