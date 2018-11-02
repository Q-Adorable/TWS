CREATE TABLE `myWorkspace` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createTime` TIMESTAMP default current_timestamp,
  `userId`  int(11),
  `programId`  int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
