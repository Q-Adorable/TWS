import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/tasks/\\d+/sections/\\d+/excellentAssignmentVideo')),
                producer('/api/tasks/1/sections/1/excellentAssignmentVideo'))
    }
    response {
        status 200
        body("""
                {
                        "id"          : 1,
                        "taskId"      : 1,
                        "sectionId"   : 1,
                        "createTime"  : "1516860064000",
                        "videoAddress": "https://media.w3.org/2010/05/sintel/trailer.mp4"
                }
        """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.createTime', byRegex('(\\d\\d\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T\\d+:\\d+:\\d+.\\d+[+]\\d+'))
        }
    }
}