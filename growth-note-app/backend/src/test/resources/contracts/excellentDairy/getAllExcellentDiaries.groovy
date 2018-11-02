package excellentDairy

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "GET"
        url '/api/excellentDiaries'
    }
    response {
        status 200
        body("""
            {
              "total": 1,
              "excellentDiariesAndComments": [
                {
                  "comments": [
                    {
                      "commentAuthorInfo": {
                        "mobilePhone": "12345678901",
                        "roles": [
                          2
                        ],
                        "id": 1,
                        "username": "zhang",
                        "email": "zhang@qq.com"
                      },
                      "comment": {
                        "id": 1,
                        "commentTime": "2018-01-20 00:00:00.0",
                        "commentContent": "commentContent",
                        "practiseDiaryId": 1,
                        "commentAuthorId": 1
                      }
                    }
                  ],
                  "excellentDiary": {
                    "id": 1,
                    "createTime": "2012-12-12 00:00:00.0",
                    "date": "2018-01-01 00:00:00.0",
                    "content": "content",
                    "authorId": 1
                  },
                  "diaryAuthorInfo": {
                    "mobilePhone": "12345678901",
                    "roles": [
                      2
                    ],
                    "id": 1,
                    "username": "zhang",
                    "email": "zhang@qq.com"
                  }
                }
              ]
            }
        """)
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}