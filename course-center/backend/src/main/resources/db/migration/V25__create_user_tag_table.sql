CREATE TABLE `userTag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createTime` TIMESTAMP default current_timestamp,
  `operatorId`  int(11),
  `studentId`  int(11),
  `programId`  int(11),
  `tagId`  int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
