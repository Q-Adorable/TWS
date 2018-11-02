package contracts.tag
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/v3/tags/[0-9]+')), producer('/api/v3/tags/2'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "id": 2,
               "name":"second"
            }    
        """)
        testMatchers {
            jsonPath('$.id', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}