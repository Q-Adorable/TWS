# growth-note-app

### 一 启动tws-config-server
(进入 tws-config-server 根目录)

```
    ./gradlew bootRun
```

### 二 启动后端
(进入 growth-note-app 根目录)

1.创建数据库

手动创建数据库：PractiseDiary

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
### 三 启动前端
(进入 quiz-center 根目录)

```
    cd web
    npm i
    npm start
```

备注：项目启动后，会报错，但是不影响开发，因为需要查询通知信息，启动tws-notification后端即可解决报错。