const MAP_CHECKOUT_COUNT = [0, 1, 2, 3]
const STATUS = {
  // LOCKED: 1,
  // ACTIVE: 2,
  PROGRESS: 3,
  SUCCESS: 2,
  ERROR: 0,
  LINE_UP: 6
}
const time = {
  MINUTE_PER_HOUR: 60,
  SECONDS_PER_MINUTE: 60,
  HOURS_PER_DAY: 24,
  MILLISECOND_PER_SECONDS: 1000
}

const QUIZ_TYPES = [{
  type: 'SINGLE_CHOICE',
  value: '单选题'
}, {
  type: 'MULTIPLE_CHOICE',
  value: '多选题'
}, {
  type: 'BASIC_BLANK_QUIZ',
  value: '填空题'
}, {
  type: 'HOMEWORK_QUIZ',
  value: '单栈编程题'
}, {
  type: 'ONLINE_HOMEWORK_QUIZ',
  value: '多语言在线编程题'
}, {
  type: 'ONLINE_LANGUAGE_QUIZ',
  value: '单语言在线编程题'
}, {
  type: 'SUBJECTIVE_QUIZ',
  value: '主观题'
}, {
  type: 'LOGIC_QUIZ',
  value: '逻辑题'
}]
export {MAP_CHECKOUT_COUNT, STATUS, time, QUIZ_TYPES}
