/*newcustomerjapan_list*/
select vncj.*,concat(vncj.chinesexingen,vncj.chinesenameen) as 'fullnameen' from visa_new_customer_jp vncj 
LEFT JOIN visa_new_customer_order_jp vncoj on vncj.id=vncoj.customer_jp_id
LEFT JOIN visa_new_order_jp vnoj on vnoj.id=vncoj.order_jp_id
LEFT JOIN visa_new_proposer_info_jp  aa on aa.customer_jp_id=vncj.id
where vnoj.id=@orderId
ORDER BY aa.relationproposer desc,aa.ismainproposer desc,vncj.chinesefullname desc
/*newcustomerjapan_getcustomerid*/
select vncj.* from visa_new_customer_jp vncj 
LEFT JOIN visa_new_customer_order_jp vncoj on vncj.id=vncoj.customer_jp_id
LEFT JOIN visa_new_order_jp vnoj on vnoj.id=vncoj.order_jp_id
LEFT JOIN visa_new_proposer_info_jp  aa on aa.customer_jp_id=vncj.id
where vncj.`status`=@status
ORDER BY aa.relationproposer desc,aa.ismainproposer desc,vncj.chinesefullname desc
/*newcustomerjapan_landlist*/
SELECT
	
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
	ccc.startdate
) AS 'startdate',
If(
 mm.ismainproposer=1,
mm.chinesefullname,
''
) as 'mainporposer'
FROM	visa_new_order_jp vnoj LEFT JOIN visa_new_customersource_jp vcm ON vnoj.id = vcm.order_jp_id
LEFT JOIN (select *
from (
select aa.ismainproposer,vnoj.id as 'orderid',vncj.*,concat(vncj.chinesexingen,vncj.chinesenameen) as 'fullnameen' from visa_new_customer_jp vncj
LEFT JOIN visa_new_customer_order_jp vncoj on vncj.id=vncoj.customer_jp_id
LEFT JOIN visa_new_order_jp vnoj on vnoj.id=vncoj.order_jp_id
LEFT JOIN visa_new_proposer_info_jp  aa on aa.customer_jp_id=vncj.id
ORDER BY aa.ismainproposer desc,aa.relationproposer desc,vncj.chinesefullname desc) m
 where m.id = (
	SELECT n.id from (
select vnoj.id as 'orderid',vncj.id from visa_new_customer_jp vncj
LEFT JOIN visa_new_customer_order_jp vncoj on vncj.id=vncoj.customer_jp_id
LEFT JOIN visa_new_order_jp vnoj on vnoj.id=vncoj.order_jp_id
LEFT JOIN visa_new_proposer_info_jp  aa on aa.customer_jp_id=vncj.id
ORDER BY aa.ismainproposer desc,aa.relationproposer desc,vncj.chinesefullname desc) n
where n.orderid =m.orderid
GROUP BY n.orderid
 LIMIT 0,1
)) mm on vnoj.id=mm.orderid
LEFT JOIN visa_new_comebaby_jp come ON come.id  = vnoj.sendComId
LEFT JOIN visa_employee eee ON eee.id  = vnoj.operatePersonId
LEFT JOIN visa_new_trip_jp aaa ON aaa.order_jp_id = vnoj.id 
LEFT JOIN (select *,MIN(bbb.startdate) from visa_new_dateplan_jp bbb 
GROUP BY bbb.trip_jp_id ) ccc ON ccc.trip_jp_id = aaa.id 
$condition