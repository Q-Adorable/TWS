CREATE TABLE `codingQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text,
  `evaluateScript` VARCHAR(1024),
  `templateRepository` VARCHAR(1024),
  `answerDescription` text,
  `answerPath` VARCHAR(1024),
  `available`  int(1),
  `stackId` int(11),
  `makerId` int(11),
  `createTime` VARCHAR(128),
  `modifyTime` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;