package contracts.tag
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/api/v3/tags'
        body("""
                  {
                    "name":"second1"
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
