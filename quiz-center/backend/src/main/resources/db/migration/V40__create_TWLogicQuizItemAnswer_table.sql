CREATE TABLE `TWLogicQuizItemAnswer`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `submissionId` int(10),
  `answer` int(11),
  `quizItemId` int(11),
  `isCorrect` TINYINT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (submissionId) REFERENCES TWLogicQuizSubmission(id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;