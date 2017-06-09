/**
 * CustomerAddForm.java
 * io.znz.jsite.visa.forms.customerform
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.customerform;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.uxuexi.core.web.form.AddForm;

/**
 * 客户添加扩展类
 * @author   崔建斌
 * @Date	 2017年6月9日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerAddForm extends AddForm {
	//主键
	private Integer id;
	//序号
	private Integer serialNumber;
	//公司全名
	private String fullComName;
	//客户来源
	private Integer customerSource;
	//联系人
	private String linkman;
	//手机
	private String telephone;
	//邮箱
	private String email;
	//状态
	private Integer status;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//备注
	private String remark;
	//预留字段1
	private String res1;
	//预留字段2
	private String res2;
	//预留字段3
	private String res3;
	//预留字段4
	private String res4;
	//预留字段5
	private String res5;
}
