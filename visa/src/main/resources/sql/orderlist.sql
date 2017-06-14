/*template_select_test*/
select vno.id,vno.ordernumber,vcm.linkman,vcm.email,vno.sendtime,vno.outtime,vno.headcount,vno.countrytype,vno.`status`,vnc.phone,vnc.chinesexing,vnc.chinesename
,vnc.chinesefullname,vno.updatetime
from visa_new_order vno
LEFT JOIN visa_customer_management vcm on vno.cus_management_id=vcm.id
LEFT JOIN visa_new_customer_order vnco on vnco.orderid=vno.id
LEFT JOIN visa_new_customer vnc on vnc.id=vnco.customerid
$condition
/*orderlist_custominfo_phone*/
select * from visa_customer_management vcm