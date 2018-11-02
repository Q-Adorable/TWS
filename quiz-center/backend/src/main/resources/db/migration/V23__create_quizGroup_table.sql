CREATE TABLE `quizGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(225),
  `description` VARCHAR(10000),
  `createTime` VARCHAR(128),
  `modifyTime` VARCHAR(128),
  `makerId` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;