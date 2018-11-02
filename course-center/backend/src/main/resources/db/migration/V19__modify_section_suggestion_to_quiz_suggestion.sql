ALTER TABLE sectionSuggestion add column assignmentId int(11);
ALTER TABLE sectionSuggestion add column quizId int(11);
ALTER  TABLE sectionSuggestion RENAME TO quizSuggestion;