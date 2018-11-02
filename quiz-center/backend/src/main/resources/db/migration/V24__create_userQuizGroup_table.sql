CREATE TABLE `userQuizGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11),
  `quizGroupId` int(11),
  `createTime` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;