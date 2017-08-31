/******************************************************************************
version : 1.0.0   BEGIN
******************************************************************************/ 
/*用户表增加字段*/
ALTER TABLE `sys_user`
ADD COLUMN `comId`  int (11) NULL COMMENT '公司id' AFTER `EXTRA`;

ALTER TABLE `sys_user`
ADD COLUMN `userType`  int(11) NULL COMMENT '用户类型' AFTER `comId`;

ALTER TABLE `sys_user`
ADD COLUMN `qq`  varchar(16) NULL COMMENT '联系qq' AFTER `userType`;

ALTER TABLE `sys_user`
ADD COLUMN `landline`  varchar(255) NULL COMMENT '座机号码' AFTER `qq`;

ALTER TABLE `sys_user`
ADD COLUMN `department`  varchar(64) NULL COMMENT '所属部门' AFTER `landline`;

ALTER TABLE `sys_user`
ADD COLUMN `job`  varchar(64) NULL COMMENT '用户职位' AFTER `department`;

ALTER TABLE `sys_user`
ADD COLUMN `disableUserStatus`  int(11) NULL COMMENT '用户是否禁用' AFTER `job`;

ALTER TABLE `sys_user`
ADD COLUMN `status`  int(11) NULL COMMENT '状态' AFTER `disableUserStatus`;

ALTER TABLE `sys_user`
ADD COLUMN `updateTime`  datetime NULL COMMENT '修改时间' AFTER `status`;

ALTER TABLE `sys_user`
ADD COLUMN `res1`  varchar(64) NULL COMMENT '预留字段1' AFTER `updateTime`;

ALTER TABLE `sys_user`
ADD COLUMN `res2`  varchar(64) NULL COMMENT '预留字段2' AFTER `res1`;

ALTER TABLE `sys_user`
ADD COLUMN `res3`  varchar(64) NULL COMMENT '预留字段3' AFTER `res2`;

ALTER TABLE `sys_user`
ADD COLUMN `res4`  varchar(64) NULL COMMENT '预留字段4' AFTER `res3`;

ALTER TABLE `sys_user`
ADD COLUMN `res5`  varchar(64) NULL COMMENT '预留字段5' AFTER `res4`;

/******************************************************************************
version : 1.0.0   end
******************************************************************************/ 
/******************************************************************************
version : 5.0.0   BEGIN
******************************************************************************/ 
ALTER TABLE `visa_new_order_jp`
ADD COLUMN `sixnum`  varchar(255) NULL COMMENT '东六县保存的具体内容' AFTER `landComId`,
ADD COLUMN `threenum`  varchar(255) NULL COMMENT '东三县保存的具体内容' AFTER `sixnum`;




/******************************************************************************
version : 5.0.0   end
******************************************************************************/ 
/******************************************************************************
version : 6.0.0   start
******************************************************************************/ 
ALTER TABLE `visa_new_order_jp`
ADD COLUMN `fileurl`  varchar(255) NULL COMMENT '签证文件地址' AFTER `excelurl`;
/******************************************************************************
version : 6.0.0   end
******************************************************************************/ 
/******************************************************************************
version : 7.0.0   start
******************************************************************************/ 
ALTER TABLE `visa_new_order_jp`
ADD COLUMN `operatePersonId`  int NULL COMMENT '操作人id' AFTER `fileurl`;
ALTER TABLE `visa_new_order_jp`
ADD COLUMN `completedNumber`  varchar(255) NULL COMMENT '受付番号' AFTER `operatePersonId`;
ALTER TABLE `visa_new_comebaby_jp`
ADD COLUMN `completedNumber`  varchar(255) NULL COMMENT '受付番号' AFTER `updateTime`;
ALTER TABLE `visa_new_comebaby_jp`
ADD COLUMN `sealSQUrl`  varchar(255) NULL COMMENT '送签社印章地址' AFTER `sealName`,
ADD COLUMN `sealSQName`  varchar(255) NULL COMMENT '送签社印章名称' AFTER `sealSQUrl`;

CREATE TABLE `visa_new_recentlyintojp` (
`id`  int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
`customerJpId`  int(32) NULL COMMENT '客户管理id' ,
`intoDate`  datetime NULL COMMENT '入境时间' ,
`outofDate`  datetime NULL COMMENT '出境时间' ,
`stayDays`  varchar(255) NULL COMMENT '停留天数' ,
`createTime`  datetime NULL COMMENT '创建时间' ,
PRIMARY KEY (`id`)
);

ALTER TABLE `visa_new_order_jp`
ADD COLUMN `island`  int NULL COMMENT '判断是否为日本地接社添加的' AFTER `completedNumber`;


/******************************************************************************
version : 7.0.0   end
******************************************************************************/ 
/******************************************************************************
version : 8.0.0   start
******************************************************************************/ 
ALTER TABLE `visa_new_order_jp`
MODIFY COLUMN `id`  int(11) NULL AUTO_INCREMENT COMMENT '主键' FIRST ,
ADD COLUMN `errorCode`  int NULL COMMENT '自动填表错误码' AFTER `island`,
ADD COLUMN `errorMsg`  varchar(255) NULL COMMENT '自动填表错误信息' AFTER `errorCode`;





/******************************************************************************
version : 8.0.0   end
******************************************************************************/ 

/******************************************************************************
version : 9.0.0   start
******************************************************************************/ 
