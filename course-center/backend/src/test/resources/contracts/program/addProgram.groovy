package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url 'api/programs'
        body("""
                  {
                            "title": "program title",
                            "createTime": "2016-09-09 00:00:00.0",
                            "startTime": "2017-09-09 00:00:00.0",
                            "endTime": "2017-09-09 00:00:00.0",
                            "creatorId":1,
                            "category": "付费",
                            "introduction": "program introduction"
                  }
                   """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
    }
    response {
        status 201
    }
}

