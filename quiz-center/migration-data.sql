-- use QuizCenter;
-- SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY','')); 用于 group by 报错 
-- Tip: 在迁移 QuizCenter 数据前需要先迁移 ProgramCenter 数据

-- 所有题迁移到quiz表
insert into QuizCenter.quiz(quizId,type)
 Select basicQuiz.id,type
 from PaperCenter.basicQuiz basicQuiz;

insert into QuizCenter.quiz(quizId,type)
 Select quiz.id,"SUBJECTIVE_QUIZ"
 from PaperCenter.subjectiveQuiz quiz;

insert into QuizCenter.quiz(quizId,type)
 Select quiz.id,"HOMEWORK_QUIZ"
 from PaperCenter.homeworkQuiz quiz;

--  客观题题目迁移
insert into QuizCenter.basicQuiz(id,description,answer,type,makerId)
 Select basicQuiz.id,description,answer,type,paper.makerId
 from PaperCenter.basicQuiz basicQuiz
 left join(Select quizId,  sectionId from PaperCenter.sectionQuiz) sectionQuiz on 
    sectionQuiz.quizId = basicQuiz.id 
 left join(Select id,paperId from PaperCenter.section) section on
    section.id = sectionQuiz.sectionId 
 left join(Select id,makerId from PaperCenter.paper) paper on paper.id = section.paperId
group by id;

-- 选择题选项迁移
insert into QuizCenter.basicQuizChoices(basicQuizId,`index`,choice)
 Select basicQuizId,`index`,choice
 from PaperCenter.basicQuizChoices;
 
-- 客观题提交答案迁移
    -- 单选题 & 填空题
insert into QuizCenter.basicQuizSubmission(assignmentId,quizId,userId,userAnswer,isCorrect)
Select  quizSubmit.sectionId assignmentId,itemPost.basicQuizId quizId,sheet.examerId userId,
    itemPost.userAnswer,itemPost.userAnswer=basicQuiz.answer from PaperCenter.basicQuizItemPost itemPost 
left join( Select id,sectionId,scoreSheetId from PaperCenter.basicQuizSubmit) quizSubmit 
    on quizSubmit.id = itemPost.basicQuizSubmitId
left join(Select id,examerId from PaperCenter.scoreSheet) sheet 
    on sheet.id = quizSubmit.scoreSheetId
left join(Select id,answer from PaperCenter.basicQuiz) basicQuiz 
    on basicQuiz.id = itemPost.basicQuizId
where itemPost.type = "SINGLE_CHOICE" or  itemPost.type = "BASIC_BLANK_QUIZ";

    -- 多选题
insert into QuizCenter.basicQuizSubmission(assignmentId,quizId,userId,userAnswer,isCorrect)
Select  quizSubmit.sectionId assignmentId,itemPost.basicQuizId quizId,sheet.examerId userId,
    itemPost.userAnswer,itemPost.userAnswer=basicQuizChoices.answer
    from PaperCenter.basicQuizItemPost itemPost 
left join( Select id,sectionId,scoreSheetId from PaperCenter.basicQuizSubmit) quizSubmit 
    on quizSubmit.id = itemPost.basicQuizSubmitId
left join(Select id,examerId from PaperCenter.scoreSheet) sheet 
    on sheet.id = quizSubmit.scoreSheetId
left join(Select basicQuizId,group_concat(basicQuizChoices.index) answer 
    from PaperCenter.basicQuizChoices group by basicQuizId) basicQuizChoices 
    on basicQuizChoices.basicQuizId = itemPost.basicQuizId
where itemPost.type = "MULTIPLE_CHOICE";

-- 主观题迁移
insert into QuizCenter.subjectiveQuiz(id,description,makerId)
 Select quiz.id,description,paper.makerId
 from PaperCenter.subjectiveQuiz quiz
 left join(Select quizId,  sectionId from PaperCenter.sectionQuiz) sectionQuiz on 
    sectionQuiz.quizId = quiz.id 
 left join(Select id,paperId from PaperCenter.section) section on
    section.id = sectionQuiz.sectionId 
 left join(Select id,makerId from PaperCenter.paper) paper on paper.id = section.paperId
group by id;


-- 主观题提交答案迁移
    -- 1. 添加 taskId 辅助字段
ALTER TABLE QuizCenter.subjectiveSubmission add taskId int;
    -- 2. 迁移数据
insert into QuizCenter.subjectiveSubmission(taskId,assignmentId,quizId,userId,userAnswer)
Select  task.id,quizSubmit.sectionId assignmentId,itemPost.subjectiveQuizId quizId,sheet.examerId userId,
    itemPost.userAnswer
    from PaperCenter.subjectiveQuizItemPost itemPost 
left join( Select id,sectionId,scoreSheetId from PaperCenter.subjectiveQuizSubmit) quizSubmit 
    on quizSubmit.id = itemPost.subjectiveQuizSubmitId
left join(Select id,examerId,paperId from PaperCenter.scoreSheet) sheet    on sheet.id = quizSubmit.scoreSheetId
join(select id,paperId from ProgramCenter.task) task on task.paperId = sheet.paperId;
    -- 3. 处理重复 assignmentId 数据
update QuizCenter.subjectiveSubmission subjectiveSubmission set assignmentId = 
    (select id from ProgramCenter.assignment assignment 
    where assignment.taskId = subjectiveSubmission.taskId and assignment.type = 'SUBJECTIVE_QUIZ'  limit 1);
    -- 4. 删除 taskId 辅助字段
    ALTER TABLE QuizCenter.subjectiveSubmission drop column taskId;



-- 编程题迁移
insert into QuizCenter.homeworkQuiz(id,description,evaluateScript,templateRepository,answerDescription,answerPath,stackId,makerId,createTime,status)
 Select id,description,evaluateScript,templateRepository,answerDescription,answerPath,stackId,makerId,createTime,2
 from PaperCenter.homeworkQuiz;

 
-- 编程题提交答案迁移
    -- 1. 添加 taskId 辅助字段
    ALTER TABLE QuizCenter.homeworkSubmission add taskId int;
    -- 2. 迁移数据
insert into QuizCenter.homeworkSubmission(taskId,assignmentId,quizId,userId,userAnswer,answerBranch,status,result)
Select  task.id,section.id assignmentId,quizSubmit.homeworkQuizId quizId,sheet.examerId userId,
    itemPost.userAnswerRepo userAnswer,itemPost.branch userBranch,itemPost.status,itemPost.result
    from PaperCenter.homeworkPostHistory itemPost 
left join( Select id,homeworkQuizId,scoreSheetId from PaperCenter.homeworkSubmit) quizSubmit 
    on quizSubmit.id = itemPost.homeworkSubmitId
left join(Select id,examerId,paperId from PaperCenter.scoreSheet) sheet 
    on sheet.id = quizSubmit.scoreSheetId
left join(Select id,paperId from PaperCenter.section) section on section.paperId = sheet.paperId
join(select id,paperId from ProgramCenter.task) task on task.paperId = sheet.paperId;
    -- 3. 处理重复 assignmentId 数据
    update QuizCenter.homeworkSubmission homeworkSubmission set assignmentId = 
        (select id from ProgramCenter.assignment assignment 
        where assignment.taskId = homeworkSubmission.taskId and assignment.type = 'HOMEWORK_QUIZ' limit 1);
  -- 4. 删除 taskId 辅助字段
    ALTER TABLE QuizCenter.homeworkSubmission drop column taskId;

-- 逻辑题数量迁移
insert into QuizCenter.logicQuiz(id,exampleCount,easyCount,normalCount,hardCount)
 Select id,exampleCount,easyCount,normalCount,hardCount
 from PaperCenter.logicQuiz;

 -- 逻辑题迁移
insert into QuizCenter.logicQuizItem(id,initializedBox,stepsString,count,questionZh,stepsLength,maxUpdateTimes,answer,descriptionZh,chartPath,infoPath)
 Select id,initializedBox,stepsString,count,questionZh,stepsLength,maxUpdateTimes,answer,descriptionZh,chartPath,infoPath
 from PaperCenter.logicQuizItem;


-- 逻辑题提交答案迁移
insert into QuizCenter.logicSubmission(assignmentId,quizId,quizItemId,userId,userAnswer,isCorrect)
Select  section.id assignmentId,quizSubmit.logicQuizId quizId,itemPost.quizItemId,sheet.examerId userId,
    itemPost.userAnswer,itemPost.userAnswer=quizItem.answer from PaperCenter.logicQuizItemPost itemPost 
 join( Select id,logicQuizId,scoreSheetId from PaperCenter.logicQuizSubmit) quizSubmit 
    on quizSubmit.id = itemPost.logicQuizSubmitId
 join(Select id,examerId,paperId from PaperCenter.scoreSheet) sheet 
    on sheet.id = quizSubmit.scoreSheetId
 join(Select id,paperId,type from PaperCenter.section) section on section.paperId = sheet.paperId 
    and section.type='logicQuizzes'
 join(Select id,answer from PaperCenter.logicQuizItem) quizItem on quizItem.id=itemPost.quizItemId;

-- 技术栈
insert into QuizCenter.stack(stackId,description,definition,title)
 Select stackId,description,definition,definition
 from PaperCenter.stack;