/*function_list*/
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
$condition