/*neworderjapan_list*/
select vnoj.ordernumber,vnoj.senddate,vnoj.outdate,vcm.linkman,vcm.telephone,vnoj.id,vnoj.updatetime
,vnoj.headnum,vnoj.countrytype,vnoj.`status`,vnoj.createtime,vcm.email,vncj.chinesefullname
from visa_new_order_jp vnoj
LEFT JOIN visa_new_customersource_jp vcm on vnoj.id=vcm.order_jp_id
LEFT JOIN visa_new_customer_order_jp vncoj on vncoj.order_jp_id=vnoj.id
LEFT JOIN visa_new_customer_jp  vncj on vncoj.customer_jp_id=vncj.id

$condition

/*neworderjapan_ordernum*/
select * from visa_new_order_jp
where date(createtime)=date(now())
$condition
order by createtime desc