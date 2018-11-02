# course-center

### 一 启动tws-config-server
(进入 tws-config-server 根目录)

```
    ./gradlew bootRun
```

### 二 启动后端
(进入 course-center 根目录)

1.创建 `ProgramCenter` 数据库

```
    docker-compose up -d
```
2.初始化数据库表结构

```
    cd backend
    ./gradlew flywayMigrate  
```

3.启动后端

```
    cd backend
    ./gradlew bootRun
```

### 二 启动前端
(进入 course-center 根目录)

- 主观题(启动后页面会报错是正常的，直接关闭该页面)

```
    cd web/subjective-app
    npm i
    npm start
```

- 客观题(启动后页面会报错是正常的，直接关闭该页面)

```
  cd web/basicQuiz-app
  npm i
  npm start
```

### 三 搭建本地测试环境

(进入后端项目(/backend))

在`src/main/resouces`目录下添加本地application-test.yml文件,内容如下:

```yaml
quizCenter: http://localhost:10003

server:
  port: 8090
database:
  url: jdbc:mysql://localhost:3307/course_center_test?useUnicode=true&characterEncoding=utf-8
  username: root
  password: root


stubrunner:
  ids:
   - cn.thoughtworks.school:userCenterBackend:+:stubs:10001
   - cn.thoughtworks.school:backend:+:stubs:10002
   - cn.thoughtworks.school:quizCenterBackend:+:stubs:10003
   - cn.thoughtworks.school.userCenter:backend:+:stubs:10004
  repositoryRoot: http://ec2-54-222-235-15.cn-north-1.compute.amazonaws.com.cn:8081/repository/maven-snapshots/
  stubs-mode: remote

flyway:
  url: ${database.url}
  user: ${database.username}
  password: ${database.password}
  locations: classpath:/db.migration

```

**注意database下的相关配置需要修改为本地测试数据库的配置!!!**

如果本地环境不存在config-server, 则需要添加application.yml文件, 内容如下:

```yaml
app:
  contextPath: ~
notificationCenter: http://localhost:8087
userCenter: http://tws-user-center:23456/home
quizCenter: http://tws-quiz-center:23456  # staging 环境 quizCenter，本地测试可以换成 http://localhost:8088

server:
  port: 8090
database:
  url: jdbc:mysql://localhost:3307/course_center?useUnicode=true&characterEncoding=utf-8
  username: root
  password: root
spring:
  datasource:
    url: ${database.url}
    username: ${database.username}
    password: ${database.password}
    driver-class-name: com.mysql.jdbc.Driver
  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
  cache:
    ehcache:
      config: ehcache.xml
amazonProperties:
  endpointUrl: https://s3.cn-north-1.amazonaws.com
  accessKey: AKIAPJIBO4LFDJ6E554A
  secretKey: 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
  picBucketName: tws-upload
  rootPath: https://s3.cn-north-1.amazonaws.com.cn
  image-directory: images
  temp-image-directory: images/temp
```

**同样需要注意database下的相关配置需要修改为本地数据库的配置!!!**

