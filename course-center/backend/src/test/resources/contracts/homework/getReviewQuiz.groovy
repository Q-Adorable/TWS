package homework

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/student/\\d+/section/\\d+/review')), producer('/api/student/1/section/1/review'))
    }
    response {
        status 200
        body("""
    {
        "id":1,
        "studentId":1,
        "tutorId":1,
        "grade":1,
        "taskId":1,
        "sectionId":1,
        "status":"未完成",
        "excellence":true,   
        "createTime":"2016-09-09 00:00:00.0"
    }
""")
        bodyMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.studentId', byRegex(number()))
            jsonPath('$.status', byRegex(onlyAlphaUnicode()))
            jsonPath('$.excellence', byRegex(anyBoolean()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}