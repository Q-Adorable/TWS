CREATE TABLE `assignmentComment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assignmentId` int(11),
  `commnetorId` int(11),
  `content` VARCHAR (10000),
  `createTime` TIMESTAMP default current_timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
