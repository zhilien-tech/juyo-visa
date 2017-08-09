/*personalInfo_list*/
SELECT
	e.id,
	e.fullName,
	e.telephone,
	e.qq,
	e.landline,
	e.email,
	d.deptName,
	j.jobName,
	e.`status`
FROM
	visa_employee e
INNER JOIN visa_new_emp_job uj ON e.id = uj.empId
INNER JOIN visa_new_company_job cj ON cj.id = uj.comJobId
INNER JOIN visa_new_job j ON j.id = cj.jobId
INNER JOIN visa_new_department d ON d.id = j.deptId
$condition