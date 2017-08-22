/*sqsjptotal_list_data*/
SELECT
	ojp.id,
	ojp.ordernumber,
	sqs.comFullName,
	emp.fullName AS username,
	djs.landcomFullName,
	ojp.completedNumber,
	pinfo.fullname,
	ojp.headnum,
	ojp.visatype,
	pinfo.ismainproposer,
	ojp.userid,
	ojp.customer_manager_id,
	ojp.area,
	ojp.ishurry,
	ojp.triptype,
	ojp.paytype,
	ojp.money,
	ojp.senddate,
	ojp.outdate,
	ojp.remark,
	ojp.countrytype,
	ojp.`status`,
	ojp.updatetime,
	ojp.createtime,
	ojp.sharenum,
	ojp.sendnum,
	ojp.customerSource,
	ojp.comId,
	ojp.sendComId,
	ojp.landComId,
	ojp.sixnum,
	ojp.threenum,
	ojp.excelurl,
	ojp.fileurl,
	ojp.operatePersonId,
	ojp.island,
	ojp.errorCode,
	ojp.errorMsg
FROM
	visa_new_order_jp ojp
LEFT JOIN visa_new_comebaby_jp sqs ON sqs.id = ojp.sendComId
LEFT JOIN visa_new_comebaby_jp_djs djs ON djs.id = ojp.landComId
LEFT JOIN visa_employee emp ON emp.id = ojp.operatePersonId
LEFT JOIN visa_new_proposer_info_jp pinfo ON pinfo.order_jp_id = ojp.id
AND pinfo.ismainproposer = 1
$condition