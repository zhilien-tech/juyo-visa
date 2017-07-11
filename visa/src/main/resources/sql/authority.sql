/*authority_list*/
SELECT
	c.comName,
	c.createTime,
	d.id AS deptId,
	d.deptName,
	j.id AS jobId,
	GROUP_CONCAT(DISTINCT j.jobName) AS jobName,
	f.id AS functionId,
	GROUP_CONCAT(DISTINCT f.funName) AS moduleName
FROM
	visa_new_company c
LEFT JOIN visa_new_department d ON c.id = d.comId
LEFT JOIN visa_new_job j ON d.id = j.deptId
LEFT JOIN visa_new_com_fun_job_map cfm ON j.id = cfm.jobId
LEFT JOIN visa_new_company_function_map cf ON cf.id = cfm.comFunId
LEFT JOIN visa_new_function f ON f.id = cf.funId
$condition

/*authority_company_function*/
SELECT
	f.id,
	f.parentId,
	f.funName,
	f.url,
	f.`level`,
	f.createTime,
	f.updateTime,
	f.remark,
	f.sort,
	f.portrait
FROM
	visa_new_function f
LEFT JOIN visa_new_company_function_map cfm ON f.id=cfm.funId
WHERE cfm.comId=@comId

/*authority_dept_job_list*/
SELECT
	j.id AS jobId,
	j.jobName,
	d.id AS deptId,
	d.deptName
FROM
	visa_new_job j
INNER JOIN visa_new_department d ON d.id = j.deptId
WHERE
	j.id =@jobId

/*authority_job_function*/
SELECT
	f.id,
	f.parentId,
	f.funName,
	f.url,
	f.`level`,
	f.createTime,
	f.updateTime,
	f.remark,
	f.sort,
	f.portrait
FROM
	visa_new_function f
INNER JOIN visa_new_company_function_map cf ON f.id = cf.funId
INNER JOIN visa_new_com_fun_job_map cfm ON cf.id = cfm.comFunId
WHERE cfm.jobId=@jobId
/*authority_dept_data*/
SELECT
	d.id,
	d.comId,
	d.deptName,
	d.createTime,
	d.updateTime,
	d.remark
FROM
	visa_new_department d
INNER JOIN visa_new_company c ON c.id = d.comId
WHERE
	c.id =@comId
AND d.deptName =@deptName
/*authority_company_job*/
SELECT
	j.id,
	j.deptId,
	j.jobName,
	j.createTime,
	j.updateTime,
	j.remark
FROM
	visa_new_job j
INNER JOIN visa_new_department d ON d.id = j.deptId
INNER JOIN visa_new_company c ON c.id = d.comId
WHERE
	c.id =@comId
AND j.jobName =@jobName
/*authority_company_job_update*/
SELECT
	j.id,
	j.deptId,
	j.jobName,
	j.createTime,
	j.updateTime,
	j.remark
FROM
	visa_new_job j
INNER JOIN visa_new_department d ON d.id = j.deptId
INNER JOIN visa_new_company c ON c.id = d.comId
WHERE
	c.id =@comId
AND j.jobName =@jobName
AND j.id !=@jobId