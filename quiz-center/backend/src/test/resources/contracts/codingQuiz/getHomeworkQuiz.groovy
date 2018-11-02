package codingQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/v3/homeworkQuizzes/[0-9]+')), producer('/api/v3/homeworkQuizzes/1'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
{
    "id": 1,
    "description": "编程题",
    "evaluateScript": "/homework-script/check-readme.sh",
    "templateRepository": "",
    "makerId": 1,
    "homeworkName": "homework",
    "createTime": 123456,
    "answerPath": "/homework-answer/check-readme",
    "stackId": 1,
    "rawId": 1,
    "answerDescription": null,
    "updateTime": "2018-01-02",
    "tags": []
}   
        """)
        testMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.createTime', byRegex(number()))
            jsonPath('$.updateTime', byRegex(nonBlank()))
            jsonPath('$.tags', byType {
                minOccurrence(0)
            })
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
