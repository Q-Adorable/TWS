CREATE TABLE `stackCommand`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64),
  `compile` varchar(128),
  `execute` varchar(128),
  `postfix` varchar(16),
  `templateCode` varchar(1024),
  `testcase` varchar(512),
  `createtime` timestamp not null default current_timestamp,
  `updatetime` timestamp not null default current_timestamp on update current_timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;