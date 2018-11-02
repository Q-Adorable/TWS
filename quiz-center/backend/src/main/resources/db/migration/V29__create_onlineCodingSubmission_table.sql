CREATE TABLE `onlineCodingSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `quizId` int(11),
  `userId` int(11),
  `userAnswer` VARCHAR (10000),
  `submitTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `status` int(11),
  `answerLanguage` VARCHAR (255),
  `result` VARCHAR (10000),
  `buildNumber` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
