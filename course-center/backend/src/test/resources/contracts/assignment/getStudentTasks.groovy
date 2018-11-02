package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'get'
        url value(consumer("api/v2/programs/\\d+/students/\\d+/assignments"),
                producer("api/v2/programs/1/students/21/assignments"))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
                [{
                    "task":{
                        "id":1,
                        "programId":1,
                        "topicId":1,
                        "assignments":[],
                        "content":"aa"
                    },
                    "assignment":{
                        "id":1,
                        "taskId":1,
                        "type":"SUBJECTIVE_QUIZ",
                        "title":"title",
                        "selectedQuizzes":[{"id":1,"quizId":2,"assignmentId":1}],
                        "status":"unfinished"
                    },
                    "topic":{}
               }]""")
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.[*].task', byRegex(nonEmpty()))
            jsonPath('$.[*].assignment', byRegex(nonEmpty()))
            jsonPath('$.[*].topic', byRegex(nonEmpty()))
        }
    }
}