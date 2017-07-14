package io.znz.jsite.visa.dto;

import lombok.Data;

/**
* @ClassName: DeptJobForm
* @author 崔建斌 
* @date 2016年11月18日 下午2:04:15
 */
@Data
public class DeptJobForm {

	//部门id/
	private long deptId;

	//部门名称
	private String deptName;

	//职位名称
	private String jobJson;

	//部门id
	private long jobId;

	//部门名称
	private String jobName;

	//功能id
	private String functionIds;

}
