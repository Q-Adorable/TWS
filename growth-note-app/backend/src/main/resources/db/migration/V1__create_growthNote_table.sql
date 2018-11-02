CREATE TABLE `practiseDiary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createTime` TIMESTAMP default current_timestamp,
  `date` TIMESTAMP default current_timestamp,
  `content` VARCHAR(10000),
  `practiseDiaryId` int(11),
  `diaryAuthorId` int(11),
  `operationType` enum('CREATE', 'UPDATE', 'DELETE'),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;