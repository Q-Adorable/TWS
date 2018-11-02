import React from 'react'
import {Modal, Form} from 'antd'
import {TwsReactMarkdownPreview} from 'tws-antd'
import {quizType} from '../../../constant/quiz-type'
import {previewModalStyle} from '../../../constant/constant-style'
import MultipleChoice from './multiple-choice'
import BlankQuiz from './blank-quiz'
import SingleChoice from './single-choice'
import CodingQuiz from './coding-quiz'
import OnlineCodingQuiz from './online-coding-quiz'
import OnlineLanguageQuiz from './online-language-quiz'
import LogicQuiz from './logic-quiz'

const MAP_COMPONENT = {
  'MULTIPLE_CHOICE': MultipleChoice,
  'BLANK_QUIZ': BlankQuiz,
  'SINGLE_CHOICE': SingleChoice,
  'CODING_QUIZ': CodingQuiz,
  'ONLINE_CODING_QUIZ': OnlineCodingQuiz,
  'ONLINE_LANGUAGE_QUIZ': OnlineLanguageQuiz,
  'LOGIC_QUIZ': LogicQuiz
}

const PreviewModal = ({quiz, visible, closeModal, type}) => {
  const RenderComponent = MAP_COMPONENT[type]

  return (
    <Modal
      title={`预览${quizType[type]}`}
      visible={visible}
      footer={null}
      onCancel={closeModal}
    >
      <Form>

        {type === 'LOGIC_QUIZ' ? '' : <Form.Item {...previewModalStyle} label='标签'>
          <span>{quiz.tags}</span>
        </Form.Item>}
     
        {type !== 'CODING_QUIZ'
          ? <Form.Item {...previewModalStyle} label='描述'>
            <TwsReactMarkdownPreview source={quiz.description} />
          </Form.Item> : ''}

          {type === 'LOGIC_QUIZ' ?  <Form.Item {...previewModalStyle} label='时长'>
           <span>{quiz.timeBoxInMinutes}</span>
          </Form.Item>:'' }

        {type === 'CODING_QUIZ' || type === 'SUBJECTIVE_QUIZ'
          ? <Form.Item {...previewModalStyle} label='备注'>
            <span>{quiz.remark}</span>
          </Form.Item> : ''}
        {RenderComponent
          ? <RenderComponent
            quiz={quiz}
            FormItem={Form.Item}
            formItemLayout={previewModalStyle} />
          : ''}
      </Form>
    </Modal>
  )
}

export default Form.create()(PreviewModal)
