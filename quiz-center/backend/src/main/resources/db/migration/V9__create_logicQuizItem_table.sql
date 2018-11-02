CREATE TABLE `logicQuizItem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `initializedBox` VARCHAR (128),
  `stepsString` text,
  `count` int (11),
  `questionZh` VARCHAR (256),
  `stepsLength` int (11),
  `maxUpdateTimes` VARCHAR (128),
  `answer` VARCHAR (256),
  `descriptionZh` text,
  `chartPath` VARCHAR (256),
  `infoPath` VARCHAR (256),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;