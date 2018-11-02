CREATE TABLE `subjectiveQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(15000),
  `isAvailable`  int(1) DEFAULT TRUE ,
  `makerId` int(11),
  `referenceNumber` int(11) DEFAULT 0,
  `createTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  `updateTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `remark` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;