import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/v3/subjectiveQuizzes'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
            "totalPages": 1,
            "content":[{
                "id": 2,
               "description":"subjectiveQuizTest"
            }] }
        """)
        testMatchers {
            jsonPath('$.totalPages', byRegex(number()))
            jsonPath('$.content[*].id', byRegex(number()))
            jsonPath('$.content[*].description', byRegex(nonBlank()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}