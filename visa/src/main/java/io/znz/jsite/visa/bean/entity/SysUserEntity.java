/**
 * SysUserEntity.java
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
 * 用户实体
 * @author   崔建斌
 * @Date	 2017年6月7日 	 
 */
@Data
@Table("sys_user")
public class SysUserEntity {

	@Column
	@Comment("主键")
	private String id;
	@Column
	private String avatar;
	@Column
	private Date birthday;
	@Column
	private Date create_date;
	@Column
	private String del_flag;
	@Column
	private String description;
	@Column
	private String email;
	@Column
	private Integer gender;
	@Column
	private Date last_visit;
	@Column
	private Integer login_count;
	@Column
	private String login_name;
	@Column
	private String name;
	@Column
	private String password;
	@Column
	private String phone;
	@Column
	private Date previous_visit;
	@Column
	private String salt;
	@Column
	private String state;
	@Column
	private String city;
	@Column
	private String brief;
	@Column
	private String extra;

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysUserEntity other = (SysUserEntity) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (brief == null) {
			if (other.brief != null)
				return false;
		} else if (!brief.equals(other.brief))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (create_date == null) {
			if (other.create_date != null)
				return false;
		} else if (!create_date.equals(other.create_date))
			return false;
		if (del_flag == null) {
			if (other.del_flag != null)
				return false;
		} else if (!del_flag.equals(other.del_flag))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (extra == null) {
			if (other.extra != null)
				return false;
		} else if (!extra.equals(other.extra))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (last_visit == null) {
			if (other.last_visit != null)
				return false;
		} else if (!last_visit.equals(other.last_visit))
			return false;
		if (login_count == null) {
			if (other.login_count != null)
				return false;
		} else if (!login_count.equals(other.login_count))
			return false;
		if (login_name == null) {
			if (other.login_name != null)
				return false;
		} else if (!login_name.equals(other.login_name))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (previous_visit == null) {
			if (other.previous_visit != null)
				return false;
		} else if (!previous_visit.equals(other.previous_visit))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((brief == null) ? 0 : brief.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((create_date == null) ? 0 : create_date.hashCode());
		result = prime * result + ((del_flag == null) ? 0 : del_flag.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((last_visit == null) ? 0 : last_visit.hashCode());
		result = prime * result + ((login_count == null) ? 0 : login_count.hashCode());
		result = prime * result + ((login_name == null) ? 0 : login_name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((previous_visit == null) ? 0 : previous_visit.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
}
