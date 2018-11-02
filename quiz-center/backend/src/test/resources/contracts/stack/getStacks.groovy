package contracts.stack
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/v3/stacks'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            [{
            "description":"first image",
            "image":"node:6.9.5"
            }]
        """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}

