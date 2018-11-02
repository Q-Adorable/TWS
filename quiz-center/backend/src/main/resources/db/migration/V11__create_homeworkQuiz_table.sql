CREATE TABLE `homeworkQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text,
  `evaluateScript` VARCHAR(1024),
  `templateRepository` VARCHAR(1024),
  `answerDescription` text,
  `answerPath` VARCHAR(1024),
  `stackId` int(11),
  `makerId` int(11),
  `createTime` int(11) ,
  `updateTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `rawId` INT(11),
  `homeworkName` VARCHAR(128),
  `isAvailable` int(1) DEFAULT TRUE,
  `definitionRepo` varchar(128),
  `status` int(1),
  `buildNumber` INT(11),
  `remark` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;