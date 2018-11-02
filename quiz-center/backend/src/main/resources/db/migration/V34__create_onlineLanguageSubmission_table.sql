CREATE TABLE `onlineLanguageSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `quizId` int(11),
  `userId` int(11),
  `userAnswer` text,
  `submitTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `status` int(11),
  `answerLanguage` VARCHAR (255),
  `result` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
