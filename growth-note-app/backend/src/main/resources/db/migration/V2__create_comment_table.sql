CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `practiseDiaryId` int(11),
  `commentTime` TIMESTAMP default current_timestamp,
  `commentAuthorId` int(11),
  `commentContent`  VARCHAR(225),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;