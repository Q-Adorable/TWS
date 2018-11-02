CREATE TABLE `taskSuggestion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskId` int(11),
  `studentId` int(11),
  `content` VARCHAR (10000),
  `createTime` TIMESTAMP default current_timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
