CREATE TABLE `assignmentQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(11),
  `quizId` VARCHAR (225),
  `createTime` TIMESTAMP default current_timestamp,
  `creatorId`  int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;