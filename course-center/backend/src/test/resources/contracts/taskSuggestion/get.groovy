package taskSuggestion

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('api/tasks/\\d+/suggestions')), producer('api/tasks/1/suggestions'))
    }
    response {
        status 200
        body("""
                  [{
                      "taskSuggestion":{
                            "id": 1,
                            "taskId": "1",
                            "studentId": "1",
                            "content": "content",
                            "createTime": "2018-01-23 00:00:00.0"
                      },
                      "studentInfo":{
                              "id": 1,
                              "userName": "zhang",
                              "email": "zhang@qq.com",
                              "mobilePhone": "12345678901",
                              "roles": [2]
                      }       
                  }]
                """)
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.[*].taskSuggestion.id', byRegex(number()))
            jsonPath('$.[*].taskSuggestion.taskId', byRegex(number()))
            jsonPath('$.[*].taskSuggestion.studentId', byRegex(number()))
            jsonPath('$.[*].taskSuggestion.content', byRegex(nonEmpty()))
            jsonPath('$.[*].taskSuggestion.createTime', byRegex(nonEmpty()))

            jsonPath('$.[*].studentInfo.id', byRegex(number()))
            jsonPath('$.[*].studentInfo.userName', byRegex(nonEmpty()))
            jsonPath('$.[*].studentInfo.email', byRegex(email()))
            jsonPath('$.[*].studentInfo.mobilePhone', byRegex(number()))
            jsonPath('$.[*].studentInfo.roles', byType())
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}