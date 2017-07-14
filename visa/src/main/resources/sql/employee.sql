/*employee_list_data*/
SELECT
	ve.id,
	ve.comId,
	ve.fullName,
	ve.telephone,
	ve.`password`,
	ve.userType,
	ve.qq,
	ve.landline,
	ve.email,
	ve.department,
	ve.job,
	ve.disableUserStatus,
	ve.`status`,
	ve.createTime,
	ve.updateTime,
	ve.remark,
	ve.salt,
	ve.countryType,
	ve.res3,
	ve.res4,
	ve.res5
FROM
	visa_employee ve
$condition
/*employee_query_deptname_list*/
SELECT
	d.id,
	d.comId,
	d.deptName,
	d.createTime,
	d.updateTime,
	d.remark
FROM
	visa_new_department d
LEFT JOIN visa_new_company c ON c.id = d.comId
$condition