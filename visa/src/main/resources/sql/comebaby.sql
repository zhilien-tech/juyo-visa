/*comebaby_list*/
select if(c.comType>1,c.landaddress,c.address) as 'address',
if(c.comType>1,c.landcomFullName,c.comFullName) as 'comFullName',
if(c.comType>1,c.landlinkman,c.linkman) as 'linkman',
if(c.comType>1,c.landtelephone,c.telephone) as 'telephone'
,c.phone,
c.comType,
c.id,
c.Fax
FROM visa_new_comebaby_jp c
$condition