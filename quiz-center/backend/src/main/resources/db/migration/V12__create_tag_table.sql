CREATE TABLE `tag`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varChar(128),
  `makerId` int(11),
  `referenceNumber` int(11) DEFAULT 0,
  `createTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
