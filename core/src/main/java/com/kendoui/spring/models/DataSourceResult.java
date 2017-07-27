/**
 * DataSourceResult.java
 * com.kendoui.spring.models
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package com.kendoui.spring.models;

import java.util.List;
import java.util.Map;

/**
 * 封装kendo-ui返回结果
 * <p>
 *
 * @author   朱晓川
 * @Date	 2017年7月27日 	 
 */
public class DataSourceResult {

	private long total;

	private List<?> data;

	private Map<String, Object> aggregates;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Map<String, Object> getAggregates() {
		return aggregates;
	}

	public void setAggregates(Map<String, Object> aggregates) {
		this.aggregates = aggregates;
	}

}
