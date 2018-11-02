CREATE TABLE `program` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(5000),
  `createTime` TIMESTAMP default current_timestamp,
  `startTime` TIMESTAMP  null ,
  `endTime` TIMESTAMP  null,
  `creatorId` int(11),
  `introduction` VARCHAR (1000),
  `category` VARCHAR (100),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
