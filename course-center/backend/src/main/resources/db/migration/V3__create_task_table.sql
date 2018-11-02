CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topicId` int(11),
  `programId` int(11),
  `paperId` int(11),
  `title` VARCHAR(5000),
  `content` VARCHAR(10000),
  `orderNumber` int(11),
  `deadLine` TIMESTAMP,
  `createTime` TIMESTAMP default current_timestamp,
  `link` VARCHAR (2000),
  `visible` INT (11),
  `type` VARCHAR (250),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
