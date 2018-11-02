export default [
  {
    linkTo: '/student/:studentId/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId/excellent-quizzes',
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
      }, {
        name: '作业',
        linkTo: '/student/:studentId/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId/excellent-quizzes'
      }

    ]
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
      }, {
        name: '我的作业',
        linkTo: '/student/program/:programId/task/:taskId/assignment/:assignmentId/quiz/:quizId'
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
    linkTo: '/program/:programId/task/:taskId/assignment/:assignmentId/preview',
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
        linkTo: '/program/:programId/task/:taskId/assignment/:assignmentId/preview'
      }]
  }]
