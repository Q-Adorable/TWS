CREATE TABLE `basicQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(10000),
  `answer` VARCHAR(128),
  `createTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  `updateTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `makerId` int(11),
  `type` VARCHAR(20),
  `isAvailable` int(1) DEFAULT TRUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;