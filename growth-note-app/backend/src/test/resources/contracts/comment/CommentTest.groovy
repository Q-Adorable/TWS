package comment

import org.springframework.cloud.contract.spec.Contract
[
        Contract.make {
            request {
                method 'POST'
                url '/api/comments'
                body([
                    commentTime : "2018-09-01",
                    commentContent : "test comment",
                    practiseDiaryId : 1,
                    commentAuthorId : 1,
                ])
                headers {
                    contentType(applicationJsonUtf8())
                    header('id', value(consumer(regex('\\d+')), producer(1)))
                }
            }
            response {
                status 201
                body("""
                  {
                          "uri": "/api/users/1/comments/3"
                  }
                """)
                bodyMatchers {
                    jsonPath('$.uri', byRegex('/api/users/\\d+/comments/\\d+'))
                }
                headers {
                    contentType(applicationJsonUtf8())
                }
            }
        }
]
