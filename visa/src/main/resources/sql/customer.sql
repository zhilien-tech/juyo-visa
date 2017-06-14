/*customer_list*/
select vnc.id,vnc.passport,vnc.email,vnc.chinesefullname,vnc.phone,vnc.gender,vno.sendtime,vno.outtime,vnc.`status` from  visa_new_customer vnc 
LEFT JOIN visa_new_customer_order vnco on vnco.customerid=vnc.id
LEFT JOIN visa_new_order vno on vno.id=vnco.orderid
where vno.id=@orderId