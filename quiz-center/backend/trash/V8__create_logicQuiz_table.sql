CREATE TABLE `logicQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exampleCount` int(11),
  `easyCount` int(11),
  `normalCount` int(11),
  `hardCount` int(11),
  `available`  int(1),
  `makerId` int(11),
  `createTime` VARCHAR(128),
  `modifyTime` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;