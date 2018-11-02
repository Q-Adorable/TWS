CREATE TABLE `stack` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR (256),
  `makeId` int (11),
  `image` VARCHAR (256),
  `createTime` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;