ALTER TABLE stackCommand ADD COLUMN `executePostfix` VARCHAR(16);
ALTER TABLE stackCommand CHANGE COLUMN `postfix` `sourcePostfix` VARCHAR(16) NULL DEFAULT NULL ;
