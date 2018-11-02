alter table `practiseDiary` drop column `practiseDiaryId`;
alter table `practiseDiary` drop column `operationType`;
alter table `practiseDiary` change  column `diaryAuthorId` `authorId` INT ;