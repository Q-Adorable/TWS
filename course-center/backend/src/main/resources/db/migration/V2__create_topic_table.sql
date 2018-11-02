CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createTime` TIMESTAMP default current_timestamp,
  `programId` int(11),
  `title` VARCHAR(5000),
  `orderNumber` int(11),
  `visible` INT (11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
