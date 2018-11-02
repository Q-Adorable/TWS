package basicQuiz

import org.springframework.cloud.contract.spec.Contract

[Contract.make {
    request {
        method 'GET'
        url '/api/v3/basicQuizzes?type=SINGLE_CHOICE'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "content": [{
                    "id": 1,
                    "description": "单选题",
                    "type": "SINGLE_CHOICE",
                    "answer": "1",
                    "choices": ["0", "1", "2", "3"],
                    "createTime": "123456",
                    "updateTime": "123456",
                    "isAvailable": true,
                    "tags": ["jb"]
                }],
                "totalPages": 1,
                "totalElements": 1,
                "size": 10,
                "number": 0
            }
        """)
        testMatchers {
            jsonPath('$.content[*].id', byRegex(number()))
            jsonPath('$.content[*].description', byRegex(nonBlank()))
            jsonPath('$.content[*].type', byRegex(nonBlank()))
            jsonPath('$.content[*].answer', byRegex(nonBlank()))
            jsonPath('$.content[*].createTime', byRegex(nonBlank()))
            jsonPath('$.content[*].updateTime', byRegex(nonBlank()))
            jsonPath('$.content[*].choices', byType {
                minOccurrence(0)
                maxOccurrence(4)
            })
            jsonPath('$.content[*].tags', byType {
                minOccurrence(0)
            })
            jsonPath('$.totalPages', byRegex(number()))
            jsonPath('$.totalElements', byRegex(number()))
            jsonPath('$.size', byRegex(number()))
            jsonPath('$.number', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
},
Contract.make {
    request {
        method 'GET'
        url ('/api/v3/basicQuizzes?type=MULTIPLE_CHOICE') {
            queryParameters {
                parameter("page", 1)
                parameter("pageSize", 10)
            }
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "content": [{
                    "id": 1,
                    "description": "单选题",
                    "type": "SINGLE_CHOICE",
                    "answer": "1",
                    "choices": ["0", "1", "2", "3"],
                    "createTime": "1234563",
                    "updateTime": "122354245",
                    "isAvailable": true,
                    "tags": ["js"]
                }],
                "totalPages": 1,
                "totalElements": 1,
                "size": 10,
                "number": 0
            }
        """)
        testMatchers {
            jsonPath('$.content', byType {
                minOccurrence(1)
                maxOccurrence(10)
            })
            jsonPath('$.content[*].id', byRegex(number()))
            jsonPath('$.content[*].description', byRegex(nonBlank()))
            jsonPath('$.content[*].type', byRegex(nonBlank()))
            jsonPath('$.content[*].answer', byRegex(nonBlank()))
            jsonPath('$.content[*].createTime', byRegex(nonBlank()))
            jsonPath('$.content[*].updateTime', byRegex(nonBlank()))
            jsonPath('$.content[*].choices', byType {
                minOccurrence(0)
                maxOccurrence(4)
            })
            jsonPath('$.content[*].tags', byType {
                minOccurrence(0)
            })
            jsonPath('$.totalPages', byRegex(number()))
            jsonPath('$.totalElements', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
},Contract.make {
    request {
        method 'GET'
        url ('/api/v3/basicQuizzes?type=BASIC_BLANK_QUIZ') {
            queryParameters {
                parameter("page", 1)
                parameter("pageSize", 10)
            }
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "content": [{
                    "id": 1,
                    "description": "填空题",
                    "type": "BASIC_BLANK_QUIZ",
                    "answer": "1",
                    "createTime": "1234563",
                    "updateTime": "122354245",
                    "isAvailable": true,
                    "tags": ["js"]
                }],
                "totalPages": 1,
                "totalElements": 1,
                "size": 10,
                "number": 0
            }
        """)
        testMatchers {
            jsonPath('$.content', byType {
                minOccurrence(1)
                maxOccurrence(10)
            })
            jsonPath('$.content[*].id', byRegex(number()))
            jsonPath('$.content[*].description', byRegex(nonBlank()))
            jsonPath('$.content[*].answer', byRegex(nonBlank()))
            jsonPath('$.content[*].createTime', byRegex(nonBlank()))
            jsonPath('$.content[*].updateTime', byRegex(nonBlank()))
            jsonPath('$.content[*].tags', byType {
                minOccurrence(0)
            })
            jsonPath('$.totalPages', byRegex(number()))
            jsonPath('$.totalElements', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}]
