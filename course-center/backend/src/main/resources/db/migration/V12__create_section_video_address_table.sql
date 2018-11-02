CREATE TABLE `sectionVideoAddress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskId` int(11),
  `sectionId` int(11),
  `videoAddress` VARCHAR(1000),
  `createTime` TIMESTAMP default current_timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
