package basicQuiz

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                method 'POST'
                url value(consumer(regex('/api/v3/basicQuizzes/students/\\d+/assignments/\\d+/quizzes')),
                        producer('/api/v3/basicQuizzes/students/26/assignments/4/quizzes'))
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
                }
            }
            response {
                status 200
                body("""
                 [
                    true,false
                 ] 
                """)
                headers {
                    header('Content-Type', 'application/json;charset=UTF-8')
                }
            }

        }
]
