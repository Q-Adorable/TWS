CREATE TABLE `TWLogicQuizSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `userId` int(11),
  `quizId` int(11),
  `submitTime` TIMESTAMP,
  `startTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('initialized', 'started', 'finished'),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;