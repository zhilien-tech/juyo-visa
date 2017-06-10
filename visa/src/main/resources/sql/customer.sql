/*customer_list*/
select vc.id,vc.passport,vc.email,vc.country,vc.first_name,vc.last_name,vc.gender,vc.id_card_no,vc.state,vc.phone,vc.start_date,vc.end_date from visa_customer vc 
LEFT JOIN visa_order_customer voc on vc.id=voc.customer_id
LEFT JOIN visa_order vo on vo.id=voc.order_id
where vo.id=@orderId