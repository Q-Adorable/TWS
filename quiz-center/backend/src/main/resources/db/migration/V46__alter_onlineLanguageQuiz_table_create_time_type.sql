ALTER TABLE onlineLanguageQuiz MODIFY createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

update onlineLanguageQuiz set createTime = updateTime where updateTime is not null;

update onlineLanguageQuiz set createTime = now() where updateTime is null;