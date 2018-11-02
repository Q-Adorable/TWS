package tag

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/v3/tags/searchTags'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            [{
                "id": 2,
               "name":"second"
            }]
        """)
        testMatchers {
            jsonPath('$.[*].id', byRegex(number()))
            jsonPath('$.[*].name', byRegex(nonBlank()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}