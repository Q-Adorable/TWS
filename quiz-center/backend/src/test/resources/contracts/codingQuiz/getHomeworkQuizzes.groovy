package codingQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/v3/homeworkQuizzes'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
{
"content":
[{
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
    "updateTime": "2018-02-04",
    "tags": ["js"]
},{
    "id": 2,
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
    "updateTime": "2018-04-01",
    "tags": ["html"]
}],
"totalPages": 1,
"totalElements": 1,
"size": 10,
"number": 0
}
        """)
        testMatchers {
            jsonPath('$.content[*].id', byRegex(number()))
            jsonPath('$.content[*].createTime', byRegex(number()))
            jsonPath('$.content[*].updateTime', byRegex(nonBlank()))
            jsonPath('$.totalElements', byRegex(number()))
            jsonPath('$.totalPages', byRegex(number()))
            jsonPath('$.content[*].tags', byType {
                minOccurrence(0)
            })
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
