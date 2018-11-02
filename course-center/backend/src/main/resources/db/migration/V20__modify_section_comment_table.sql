ALTER TABLE sectionComment add column assignmentId int(11);
ALTER TABLE sectionComment add column quizId int(11);
ALTER TABLE sectionComment change column commentatorId  commenterId int(11);
ALTER  TABLE sectionComment RENAME TO excellentQuizComment;