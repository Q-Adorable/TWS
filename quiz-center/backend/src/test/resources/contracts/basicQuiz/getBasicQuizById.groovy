package basicQuiz
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/v3/basicQuizzes/[0-9]+')), producer('/api/v3/basicQuizzes/1'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "id": 1,
                "answer":"1",
                "type":"SINGLE_CHOICE",
                "description":"单选题",
                "choices": ["0", "1", "2", "3"],
                "createTime": "1234563",
                "updateTime": "122354245",
                "isAvailable": true,
                "tags": ["java"]
            }    
        """)
        testMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.createTime', byRegex(nonBlank()))
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
