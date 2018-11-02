package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url value(consumer(regex('api/programs/\\d+')), producer('api/programs/1'))
        body("""
                  {
                            "title": "edit title",
                            "createTime": "2016-09-09 00:00:00.0",
                            "startTime": "2017-09-09 00:00:00.0",
                            "endTime": "2017-09-09 00:00:00.0",
                            "creatorId":1,
                            "category": "付费",
                            "introduction": "edit introduction"
                  }
                   """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}

