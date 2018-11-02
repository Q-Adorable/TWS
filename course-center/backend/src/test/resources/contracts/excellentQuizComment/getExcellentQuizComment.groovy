import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/v2/excellentQuizComments/students/\\d+/assignments/\\d+/quizzes/\\d+')),
                producer('/api/v2/excellentQuizComments/students/1/assignments/1/quizzes/1'))
    }
    response {
        status 200
        body("""
                [{
                         "studentId"  : 1,
                         "userName": "acey",
                         "content":"hello world",
                         "commenterId":21
                 }]
        """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.[*].studentId', byRegex(number()))
            jsonPath('$.[*].userName', byRegex(nonEmpty()))
            jsonPath('$.[*].content', byRegex(onlyAlphaUnicode()))
            jsonPath('$.[*].commenterId', byRegex(number()))
        }
    }
}