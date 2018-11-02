CREATE TABLE `singleChoiceQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(10000),
  `answer` VARCHAR(128),
  `available`  int(1),
  `createTime` VARCHAR(128),
  `modifyTime` VARCHAR(128),
  `makerId` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;