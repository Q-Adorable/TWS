package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'get'
        url value(consumer("api/v2/tutor/students/\\d+/tasks/\\d+/assignments"),
                producer("api/v2/tutor/students/21/tasks/1/assignments"))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 21)
        }
    }
    response {
        status 200
        body("""
                [{
                   "id": 1,
                   "taskId": 1,
                   "type": "SUBJECTIVE_QUIZ",
                   "title": "assignment",
                   "selectedQuizzes":[],
                   "status": "已完成2/4"
               }]""")
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.[*].id', byRegex(number()))
            jsonPath('$.[*].taskId', byRegex(number()))
            jsonPath('$.[*].selectedQuizzes', byType())
            jsonPath('$.[*].type', byRegex(nonEmpty()))
            jsonPath('$.[*].title', byRegex(nonEmpty()))
            jsonPath('$.[*].status', byRegex(nonEmpty()))
        }
    }
}