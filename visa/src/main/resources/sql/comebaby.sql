/*comebaby_list*/
select if(c.comType>1,c.landaddress,c.address) as 'address',
if(c.comType>1,c.landcomFullName,c.comFullName) as 'comFullName',
if(c.comType>1,c.landlinkman,c.linkman) as 'linkman',
if(c.comType>1,c.landtelephone,c.telephone) as 'telephone'
,c.phone,
c.comType,
c.id,
c.Fax,
c.completedNumber
FROM visa_new_comebaby_jp c
$condition

/*comebaby_sqs_djs_list*/
SELECT
	*
FROM
	(
	SELECT

		IF (
			s.comType > 1,
			s.landaddress,
			s.address
		) AS 'address',

		IF (
			s.comType > 1,
			s.landcomFullName,
			s.comFullName
		) AS 'comFullName',
	
		IF (
			s.comType > 1,
			s.landlinkman,
			s.linkman
		) AS 'linkman',
		
		IF (
			s.comType > 1,
			s.landtelephone,
			s.telephone
		) AS 'telephone',
		s.phone,
		s.comType,
		s.id,
		s.Fax,
		s.updateTime,
		s.completedNumber
	FROM
		visa_new_comebaby_jp_djs s
		where s.comType != @djsType
	UNION ALL
	SELECT

		IF (
			c.comType > 1,
			c.landaddress,
			c.address
		) AS 'address',
	
		IF (
			c.comType > 1,
			c.landcomFullName,
			c.comFullName
		) AS 'comFullName',
		
		IF (
			c.comType > 1,
			c.landlinkman,
			c.linkman
		) AS 'linkman',
		
		IF (
			c.comType > 1,
			c.landtelephone,
			c.telephone
		) AS 'telephone',
		c.phone,
		c.comType,
		c.id,
		c.Fax,
		c.updateTime,
		c.completedNumber
	FROM
		visa_new_comebaby_jp c
	$condition
) ds
ORDER BY
	ds.updateTime DESC

