/*loginauthority_user_function_all*/
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
INNER JOIN visa_new_company_job cj ON cj.jobId = cfm.jobId
INNER JOIN visa_new_emp_job ej ON cj.id = ej.comJobId
WHERE
	ej.empId =@userId
AND ej.`status` =@status