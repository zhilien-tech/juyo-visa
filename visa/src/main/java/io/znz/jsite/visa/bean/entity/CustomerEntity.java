/**
 * CustomerEntity.java
 * io.znz.jsite.visa.bean.entity
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.bean.entity;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月9日 	 
 */
@Data
@Table("visa_customer")
public class CustomerEntity {
	@Column
	@Comment("主键")
	private long id;
	@Column
	private String address;
	@Column
	private String address_en;
	@Column
	private String birth_city;
	@Column
	private String birth_city_cn;
	@Column
	private String birth_country;
	@Column
	private String birth_province;
	@Column
	private String birth_province_cn;
	@Column
	private Date birthday;
	@Column
	private String city;
	@Column
	private String code;
	@Column
	private String country;
	@Column
	private Date create_date;
	@Column
	private int deposit_count;
	@Column
	private String deposit_method;
	@Column
	private String deposit_source;
	@Column
	private double deposit_sum;
	@Column
	private String email;
	@Column
	private Date start_date;
	@Column
	private String files;
	@Column
	private String first_name;
	@Column
	private String first_name_en;
	@Column
	private char firend_in_usa;
	@Column
	private String gender;
	@Column
	private int home;
	@Column
	private String id_card_no;
	@Column
	private char insurance;
	@Column
	private String issue_city;
	@Column
	private Date end_date;
	@Column
	private String issue_province;
	@Column
	private String last_name;
	@Column
	private String last_name_en;
	@Column
	private char main;
	@Column
	private String mobile;
	@Column
	private String old_first_name;
	@Column
	private String old_first_name_en;
	@Column
	private String old_last_name;
	@Column
	private String old_last_name_en;
	@Column
	private char out_district;
	@Column
	private String passport;
	@Column
	private String phone;
	@Column
	private String province;
	@Column
	private String reason;
	@Column
	private char receipt;
	@Column
	private String main_ralation;
	@Column
	private String room;
	@Column
	private String room_en;
	@Column
	private String state;
	@Column
	private String type;
	@Column
	private String zip_code;
	@Column
	private int army_id;
	@Column
	private int old_passport_id;
	@Column
	private int spouse_id;
	@Column
	private int usa_id;

}
