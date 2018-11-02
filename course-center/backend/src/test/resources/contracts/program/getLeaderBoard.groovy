package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('api/programs/\\d+/leaderBoard')),
                producer('api/programs/1/leaderBoard'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header("id", 1)
            header("username", "aa")
        }

    }
    response {
        status 200
        body("""
    {
    "leaderBoard": [
      {
        "finishHomeworkPoint": 100,
        "name": "zhang",
        "finishHomeworkCount": 2,
        "rank":1,
        "studentId":11
      },
       {
        "finishHomeworkPoint": 80,
        "name": "ac",
        "finishHomeworkCount": 1,
        "rank":2,
        "studentId":21
      }
    ],
    "myRank": {
      "finishHomeworkPoint": 100,
        "name": "zhang",
        "finishHomeworkCount": 2,
        "rank":1,
        "studentId":11
    }
  }
""")
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.leaderBoard',byType())
            jsonPath('$.myRank.finishHomeworkPoint', byRegex(number()))
            jsonPath('$.myRank.finishHomeworkCount', byRegex(number()))
            jsonPath('$.myRank.rank', byRegex(number()))
            jsonPath('$.myRank.studentId', byRegex(number()))
            jsonPath('$.myRank.name', byRegex(onlyAlphaUnicode()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
