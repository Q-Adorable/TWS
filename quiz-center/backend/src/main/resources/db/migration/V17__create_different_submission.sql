CREATE TABLE `basicQuizSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `quizId` int(11),
  `userId` int(11),
  `userAnswer` VARCHAR (255),
  `submitTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `isCorrect` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `homeworkSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `quizId` int(11),
  `userId` int(11),
  `userAnswer` VARCHAR (255),
  `submitTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `status` int(11),
  `answerBranch` VARCHAR (255),
  `result` VARCHAR (255),
  `buildNumber` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `subjectiveSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `quizId` int(11),
  `userId` int(11),
  `userAnswer` VARCHAR (255),
  `submitTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `logicSubmission`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(10),
  `quizId` int(11),
  `userId` int(11),
  `userAnswer` VARCHAR (255),
  `submitTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `isCorrect` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;