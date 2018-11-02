export default [
  {
    linkTo: '/program/:programId/task/:taskId/section/:sectionId/choice-quiz-answer',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/'
      },
      {
        name: '作业答案',
        linkTo: '/program/:programId/task/:taskId/section/:sectionId/choice-quiz-answer'
      }]
  }, {
    linkTo: '/program/:programId/task/:taskId/assignment/:assignmentId/basic-quiz-answer',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/'
      },
      {
        name: '作业答案',
        linkTo: '/program/:programId/task/:taskId/assignment/:assignmentId/basic-quiz-answer'
      }]
  }, {
    linkTo: '/student/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/'
      }, {
        name: '任务卡列表',
        linkTo: '/student/program/:programId/task'
      },
      {
        name: '任务卡',
        linkTo: '/student/program/:programId/task/:taskId'
      },
      {
        name: '我的作业',
        linkTo: '/student/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId'
      }]
  }, {
    linkTo: '/student/program/:programId/task/:taskId/section/:sectionId/finished-assignment',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/'
      }, {
        name: '任务卡',
        linkTo: '/student/program/:programId/task'
      }, {
        name: '作业',
        linkTo: '/student/program/:programId/task/:taskId/section/:sectionId/finished-assignment'
      }]
  }, {
    linkTo: '/tutor/program/:programId/task/:taskId/student/:studentId/assignment/:assignmentId/quiz/:quizId',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/'
      }, {
        name: '我辅导的训练营',
        linkTo: '/tutor/program/:programId'
      }, {
        name: '学生作业详情',
        linkTo: '/tutor/program/:programId/task/:taskId/student/:studentId/assignment/:assignmentId/quiz/:quizId'
      }]
  }, {
    linkTo: '/tutor/program/:programId/task/:taskId/student/:studentId/divider/assignment/:assignmentId/quiz/:quizId',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/'
      }, {
        name: '我辅导的训练营',
        linkTo: '/tutor/program/:programId'
      }, {
        name: '作业列表',
        linkTo: '/tutor/program/:programId/student/:studentId'
      }, {
        name: '学生作业详情',
        linkTo: '/tutor/program/:programId/task/:taskId/student/:studentId/divider/assignment/:assignmentId/quiz/:quizId'
      }]
  }, {
    linkTo: '/program/:programId/task/:taskId/section/:sectionId/preview',
    breadCrumbs: [
      {
        name: '思特沃克学院',
        linkTo: '/AppCenter'
      }, {
        name: '训练营中心',
        linkTo: '/instructor'
      }, {
        name: '我管理的训练营',
        linkTo: '/instructor/program/:programId'
      }, {
        name: '任务卡',
        linkTo: '/instructor/program/:programId/task/:taskId'
      }, {
        name: '试卷预览',
        linkTo: '/program/:programId/task/:taskId/section/:sectionId/preview'
      }]
  }]
