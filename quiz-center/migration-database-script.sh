#!/usr/bin/env bash


# 使用此脚本的迁移数据库的步骤：
# 1. 迁移出PaperCenter 的数据库 导入进 QuizCenter App 中
# 2. 创建好QuizCenter App 的数据库 QuizCenter 及QuizCenter 中的表
# 3. 当前目录下执行命令./migration-database-script
# 4. 迁移成功后删除 QuizCenter App 中的 PaperCenter

docker exec -i QuizCenter mysql -u root -proot < ./migration-data.sql

