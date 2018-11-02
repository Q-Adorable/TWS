ALTER TABLE homeworkQuiz MODIFY createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

update homeworkQuiz set createTime = updateTime where updateTime is not null;

update homeworkQuiz set createTime = now() where updateTime is null;