/*template_select_test*/
select vo.*,vc.phone,vc.last_name from visa_order vo 
LEFT JOIN visa_order_customer voc on vo.id=voc.order_id
LEFT JOIN visa_customer vc on vc.id=voc.customer_id
$condition
/*orderlist_custominfo_phone*/
select * from visa_customer_management vcm