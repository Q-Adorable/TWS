package tag
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url value(consumer(regex('/api/v3/tags/[0-9]+')), producer('/api/v3/tags/2'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
        body ("""
                 {
                    "name":"12304"
                 } 
                """
        )
    }
    response {
        status 204
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
