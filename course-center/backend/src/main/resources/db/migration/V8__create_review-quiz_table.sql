CREATE TABLE `reviewQuiz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `studentId` int(11),
  `tutorId` int (11),
  `grade` int(11),
  `taskId` int(11),
  `sectionId` int(11),
  `status` VARCHAR (10000),
  `excellence` int(11),
  `createTime` TIMESTAMP default current_timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
