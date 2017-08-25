/*japanLand_select*/
select a.* from   visa_new_order_jp a
LEFT JOIN  visa_new_customer_order_jp b  on a.id = b.order_jp_id
LEFT JOIN  visa_new_customer_jp   c  on b.customer_jp_id=c.id
$condition
GROUP BY a.id