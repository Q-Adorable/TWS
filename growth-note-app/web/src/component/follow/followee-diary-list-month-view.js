import React from 'react'
import { Calendar } from 'antd'
import { Popover, Button, Icon } from 'antd';
import moment from 'moment'
import ReactMarkdown from 'react-markdown'

const FolloweeDiaryListMonthView = ({ practiseDiaryAndComments }) => {

  function formatListViewData() {
    const initData = []
    practiseDiaryAndComments.forEach(practiseDiaryAndComment => {
      const { practiseDiary } = practiseDiaryAndComment
      initData.push({
        id: practiseDiary.id,
        createTime: moment(practiseDiary.date),
        type: practiseDiary.diaryType,
        text: practiseDiary.content.slice(0, 13),
        content: practiseDiary.content,
      })
    })
    return initData
  }

  function dateCellRender(value) {
    const initMonthViewData = formatListViewData()

    const monthViewData = initMonthViewData.filter(practiseDiary =>
      (
        practiseDiary.createTime.date() === value.date() &&
        practiseDiary.createTime.month() === value.month() &&
        practiseDiary.createTime.year() === value.year()
      )
    )

    return (
      <ul className="events" >
        {
          monthViewData.map(((item, index) => {
            let calendarButtonStyle = 'calendar__button'
            calendarButtonStyle += item.type === 'goal' ? ' calendar__button--goal' : ''
            return (
              <li key={index}>
                <Popover
                  placement='rightBottom'
                  content={<ReactMarkdown className='monthView--popover' source={item.content} />}
                  overlayStyle={{ width: 300 }}
                  trigger='click' >
                  <Button className={calendarButtonStyle}>
                    <Icon type={item.type === 'goal' ? 'environment' : 'tag'} />
                    {item.text}
                  </Button>
                </Popover>
              </li>
            )
          }))
        }
      </ul>
    )
  }

  function monthCellRender(value) {
    const initMonthViewData = formatListViewData()
    const monthlySubmittedNum = initMonthViewData.filter(practiseDiary =>
      practiseDiary.createTime.month() === value.month() &&
      practiseDiary.createTime.year() === value.year()
    ).length
    return monthlySubmittedNum > 0 && (
      <div className="notes--month">
        <span>篇数</span>
        <section>{monthlySubmittedNum}</section>
      </div>
    )
  }

  return (
    <Calendar dateCellRender={dateCellRender} monthCellRender={monthCellRender} />
  )
}

export default FolloweeDiaryListMonthView