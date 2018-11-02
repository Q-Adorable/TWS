package excellentQuizComment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "api/v2/excellentQuizComments"
        body([
                studentId   : 1,
                quizId      : 2,
                assignmentId: 3,
                content     : 'test content'
        ])
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header(id: 1)
            header(userName: 'acey')
        }
    }
    response {
        status 201
    }
}
