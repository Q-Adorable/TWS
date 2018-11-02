package basicQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "api/v2/assignments/1/answers"
        body("""
                 [{                    
                    "userAnswer":"1",  
                    "quizId":1         
                },{                   
                    "userAnswer":"5",  
                    "quizId":1         
                }]                    
        """)
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
