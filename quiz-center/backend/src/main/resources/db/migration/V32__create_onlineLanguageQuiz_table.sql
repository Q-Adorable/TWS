CREATE TABLE `onlineLanguageQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text,
  `testData` text,
  `initCode` text,
  `language` VARCHAR(128),
  `answerDescription` text,
  `answer` text,
  `makerId` int(11),
  `createTime` int(11) ,
  `updateTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `rawId` INT(11),
  `onlineLanguageName` VARCHAR(128),
  `isAvailable` int(1) DEFAULT TRUE,
  `status` int(1),
  `remark` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;