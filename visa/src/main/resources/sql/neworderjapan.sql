/*neworderjapan_list*/
SELECT
	vnoj.ordernumber,
	vnoj.senddate,
	vnoj.outdate,
	vcm.linkman,
	vcm.telephone,
	vnoj.id,
	vnoj.updatetime,
	vnoj.headnum,
	vnoj.countrytype,
	vnoj.`status`,
	vnoj.createtime,
	vncj.email,
	vncj.chinesefullname,
	vnoj.completedNumber,
vnoj.errorMsg
FROM
	visa_new_order_jp vnoj
LEFT JOIN visa_new_customersource_jp vcm ON vnoj.id = vcm.order_jp_id
LEFT JOIN visa_new_customer_order_jp vncoj ON vncoj.order_jp_id = vnoj.id
LEFT JOIN visa_new_customer_jp vncj ON vncoj.customer_jp_id = vncj.id
$condition

/*neworderjapan_ordernum*/
select * from visa_new_order_jp
where date(createtime)=date(now())
$condition
order by createtime desc
/*neworderjapan_porposerorder*/


SELECT * FROM `visa_new_proposer_info_jp` a
where a.order_jp_id=@orderid
ORDER BY a.relationproposer desc,a.ismainproposer desc,a.xing

/*neworderjapan_mainproposer*/
SELECT
	vncj.id 'customerid',
	vnoj.ordernumber,
	vnoj.completedNumber,
	come.comFullName,
	eee.fullName,
	vnoj.headnum,
	vnoj.senddate,
	vnoj.outdate,
	vnoj.id,
	vnoj.updatetime,
	vnoj.countrytype,
	vnoj.`status`,
	vnoj.createtime,
	vnoj.visatype,

IF (
	aaa.oneormore = 0,
	aaa.startdate,
	bbb.startdate
) AS 'startdate',
MIN(bbb.startdate) as 'aaaa'
FROM	visa_new_order_jp vnoj LEFT JOIN visa_new_customersource_jp vcm ON vnoj.id = vcm.order_jp_id
LEFT JOIN visa_new_customer_order_jp vncoj ON vncoj.order_jp_id = vnoj.id 
LEFT JOIN visa_new_customer_jp vncj ON vncoj.customer_jp_id = vncj.id 
LEFT JOIN visa_new_comebaby_jp come ON come.id  = vnoj.sendComId
LEFT JOIN visa_employee eee ON eee.id  = vnoj.operatePersonId
LEFT JOIN visa_new_trip_jp aaa ON aaa.order_jp_id = vnoj.id 
LEFT JOIN visa_new_dateplan_jp bbb ON bbb.trip_jp_id = aaa.id 

$condition
/*neworderjapan_nomainproposer*/



