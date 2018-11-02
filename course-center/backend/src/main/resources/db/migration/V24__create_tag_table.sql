CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR (225),
  `createTime` TIMESTAMP default current_timestamp,
  `creatorId`  int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
