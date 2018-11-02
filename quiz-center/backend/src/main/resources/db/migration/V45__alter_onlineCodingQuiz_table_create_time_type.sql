ALTER TABLE onlineCodingQuiz MODIFY createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

update onlineCodingQuiz set createTime = updateTime where updateTime is not null;

update onlineCodingQuiz set createTime = now() where updateTime is null;