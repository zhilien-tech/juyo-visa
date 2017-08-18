/*personalInfo_list*/
SELECT
	d.deptName,
	j.jobName,	
	e.id,
	e.comId,
	e.fullName,
	e.telephone,
	e.`password`,
	e.salt,
	e.userType,
	e.qq,
	e.landline,
	e.email,
	e.department,
	e.job,
	e.disableUserStatus,
	e.`status`,
	e.createTime,
	e.updateTime,
	e.remark,
	e.countryType,
	e.pId,
	e.res4,
	e.res5
FROM
	visa_employee e
INNER JOIN visa_new_emp_job uj ON e.id = uj.empId
INNER JOIN visa_new_company_job cj ON cj.id = uj.comJobId
INNER JOIN visa_new_job j ON j.id = cj.jobId
INNER JOIN visa_new_department d ON d.id = j.deptId
$condition

/*personalInfo_djs_1507-001_data*/
SELECT
	e.id,
	e.comId,
	e.fullName,
	e.telephone,
	e.`password`,
	e.salt,
	e.userType,
	e.qq,
	e.landline,
	e.email,
	e.department,
	e.job,
	e.disableUserStatus,
	e.`status`,
	e.createTime,
	e.updateTime,
	e.remark,
	e.countryType,
	e.pId,
	e.res4,
	e.res5
FROM
	visa_employee e
$condition