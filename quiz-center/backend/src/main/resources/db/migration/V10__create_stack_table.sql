CREATE TABLE `stack` (
  `stackId` int(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR (256),
  `makerId` int (11),
  `title`VARCHAR (256),
  `definition` VARCHAR (256),
  `createTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  `isAvailable` int(1) DEFAULT TRUE ,
  `referenceNumber` INT(1) DEFAULT 0,
  `buildNumber` INT(11),
  PRIMARY KEY (`stackId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- title 这个字段应该删掉