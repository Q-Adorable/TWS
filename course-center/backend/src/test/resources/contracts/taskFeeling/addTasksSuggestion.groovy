import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                method 'POST'
                url '/api/feelings'
                body([
                        taskId : 1,
                        feeling: "good"
                ])
                headers {
                    header('Content-Type', 'application/json;charset=UTF-8')
                    header("id", 1)
                }
            }
            response {
                status 400
                body([
                        'message': "您已经评价过了"
                ])
                headers {
                    header('Content-Type', 'application/json;charset=UTF-8')
                }
            }
        },

        Contract.make {
            request {
                method "POST"
                url '/api/feelings'
                body([
                        taskId : 2,
                        feeling: 'good'
                ])
                headers {
                    header('Content-Type', 'application/json;charset=UTF-8')
                    header('id', 2)
                }
            }
            response {
                status 201
            }
        }
]