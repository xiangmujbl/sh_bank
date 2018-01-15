
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(6,'userRegister','用户注册','用户注册','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(7,'userQuery','用户查询','用户查询','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(8,'createContract','创建合同','创建合同','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(9,'cancelContract','撤销合同','撤销合同','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(10,'sendSmsCode','发送短信验证码','发送短信验证码','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(11,'sealManage','图章管理','图章管理','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(12,'queryContract','查看合同','查看合同','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(13,'httpDownload','HTTP下载合同','HTTP下载合同','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(14,'ftpDownload','FTP上传合同','FTP上传合同','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(15,'certBund','证书绑定','证书绑定','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(16,'certUnBund','证书解绑','证书解绑','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(17,'HJCreateContract','互金平台创建合同','互金平台创建合同','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(18,'HJQueryContract','互金平台查看合同','互金平台查看合同','2015-12-29 03:10:11',1);
                                                                                                 
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(19,'signPageZip','合同页面签署(ZIP)','合同页面签署(ZIP)','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(20,'certSignPageZip','合同页面硬件证书签署(ZIP)','合同页面硬件证书签署(ZIP)','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(21,'signSlientZip','静默无验证码签署(ZIP)','静默无验证码签署(ZIP)','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(22,'signSlientZipByCode','静默有验证码签署(ZIP)','静默有验证码签署(ZIP)','2015-12-29 03:10:11',1);
                                                                                                 
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(23,'signPagePdf','合同页面签署(PDF)','合同页面签署(PDF)','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(24,'signSlientPdf','静默无验证码签署(PDF)','静默无验证码签署(PDF)','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(25,'signSlientPdfByCode','静默有验证码签署(PDF)','静默有验证码签署(PDF)','2015-12-29 03:10:11',1);
insert into c_auth (id,auth_num,auth_name, auth_desc, update_time, auth_type) values(26,'signSlientPdfAll','静默自动代签(PDF)','静默自动代签(PDF)','2015-12-29 03:10:11',1);



insert into `c_role` (`id`, `role_name`, `update_time`, `role_desc`, `role_type`) values('2','interPop','2016-03-09 13:09:26','互金pdf签署','2');
insert into `c_role` (`id`, `role_name`, `update_time`, `role_desc`, `role_type`) values('3','interAdmin','2016-03-09 17:41:11','互金zip签署','2');
insert into `c_role` (`id`, `role_name`, `update_time`, `role_desc`, `role_type`) values('4','标准对接用户','2015-12-23 00:00:00','标准对接用户','2');
insert into `c_role` (`id`, `role_name`, `update_time`, `role_desc`, `role_type`) values('5','非标对接用户1','2015-12-23 00:00:00','非标对接用户','2');


insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','6');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','7');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','8');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','9');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','10');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','11');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','12');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','13');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','14');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','15');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','16');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','19');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','20');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','21');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('4','22');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','6');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','7');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','8');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','9');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','10');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','11');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','12');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','13');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','14');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','15');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','16');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','23');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','24');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','25');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('5','26');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','6');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','7');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','9');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','10');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','11');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','13');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','14');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','15');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','16');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','17');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','18');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','19');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','20');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','21');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('3','22');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','6');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','7');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','9');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','10');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','11');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','13');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','14');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','15');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','16');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','17');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','18');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','23');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','24');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','25');
insert into `c_role_auth_relation` (`role_id`, `auth_id`) values('2','26');


INSERT INTO `c_platform` (id,`program`, `app_id`, `app_secret_key`, `company_name`, `create_time`, `status`, `type`, `business_license_no`) VALUES
(11,'中国云签', '78f8RlcB2o', '78f8RlcB2o', '中国云签', '2016-01-12 13:42:28', 1, 1, '10000000001');


INSERT INTO `c_sms_template` (`id`, `content`, `update_time`, `sms_type`) VALUES
(1, '尊敬的用户,注册短信验证码为#,2分钟内有效，如非本人操作,请忽略本短信！', '2015-12-30 00:00:00', 'register'),
(2, '尊敬的用户,签署短信验证码为#,2分钟内有效，如非本人操作,请忽略本短信！', '2016-01-20 03:15:25', 'sign');

INSERT INTO `c_identity` (`uuid`, `account`, `password`, `type`, `source`, `is_authentic`, `is_admin`, `regist_time`, `platform_id`, `status`, `status_time`) VALUES
( 'YUNSIGN_SERVER', 'YUNSIGN_SERVER', '12345678', 9, 1, 1, 1, '2016-01-14 15:38:04', 11, 1, '2016-01-14 15:38:04');

