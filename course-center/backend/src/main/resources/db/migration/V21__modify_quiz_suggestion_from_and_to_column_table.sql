ALTER TABLE quizSuggestion add column fromUserId int(11);
ALTER TABLE quizSuggestion CHANGE studentId toUserId  int(11);


update quizSuggestion suggestion ,
(
  select follow.*
  from assignment
    left join (select *
               from task) task on task.id = assignment.taskId
    left join (select *
               from follow) follow on follow.programId = task.programId

) follow

set fromUserId =follow.tutorId where follow.studentId = suggestion.toUserId