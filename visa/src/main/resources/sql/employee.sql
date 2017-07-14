/*employee_list_data*/
SELECT
	e.id,
	e.comId,
	e.fullName,
	d.id AS deptId,
	d.deptName,
	j.id AS jobId,
	j.jobName,
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
	e.res3,
	e.res4,
	e.res5
FROM
	visa_employee e
INNER JOIN visa_new_emp_job ej ON e.id = ej.empId
INNER JOIN visa_new_company_job cj ON cj.id = ej.comJobId
INNER JOIN visa_new_job j ON j.id = cj.jobId
INNER JOIN visa_new_department d ON d.id = j.deptId
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
/*employee_select_dept_list*/
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

/*employee_update_old_user*/
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
	e.res3,
	e.res4,
	e.res5
FROM
	visa_employee e
LEFT JOIN visa_new_emp_job ej ON e.id=ej.empId
LEFT JOIN visa_new_company_job cj ON cj.id=ej.comJobId
$condition

/*employee_add_emp_job_data*/
SELECT
	ej.id,
	ej.empId,
	ej.comJobId,
	ej.`status`,
	ej.hireDate,
	ej.leaveDate,
	ej.remark
FROM
	visa_new_emp_job ej
INNER JOIN visa_new_company_job cj ON cj.id = ej.comJobId
INNER JOIN visa_new_company c ON c.id = cj.comId
INNER JOIN visa_new_job j ON j.id = cj.jobId
WHERE
	ej.empId =@userId
AND ej.`status` =@statusId

/*employee_update_data*/
SELECT
	e.id,
	e.comId,
	e.fullName,
	j.deptId,
	j.jobName,
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
	e.res3,
	e.res4,
	e.res5
FROM
	visa_employee e
LEFT JOIN visa_new_emp_job ej ON e.id = ej.empId
LEFT JOIN visa_new_company_job cj ON cj.id = ej.comJobId
LEFT JOIN visa_new_job j ON j.id = cj.jobId
WHERE
	e.id =@userId