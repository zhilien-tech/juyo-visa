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