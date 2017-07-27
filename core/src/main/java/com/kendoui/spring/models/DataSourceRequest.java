/**
 * DataSourceRequest.java
 * com.kendoui.spring.models
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package com.kendoui.spring.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * 封装kendo-ui数据请求
 * <p>
 *
 * @author   朱晓川
 * @Date	 2017年7月27日 	 
 */
public class DataSourceRequest {

	private int page;
	private int pageSize;
	private int take;
	private int skip;
	private List<SortDescriptor> sort;
	private List<GroupDescriptor> group;
	private List<AggregateDescriptor> aggregate;
	private HashMap<String, Object> data;

	private FilterDescriptor filter;

	public DataSourceRequest() {
		filter = new FilterDescriptor();
		data = new HashMap<String, Object>();
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	@JsonAnySetter
	public void handleUnknown(String key, Object value) {
		data.put(key, value);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTake() {
		return take;
	}

	public void setTake(int take) {
		this.take = take;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public List<SortDescriptor> getSort() {
		return sort;
	}

	public void setSort(List<SortDescriptor> sort) {
		this.sort = sort;
	}

	public FilterDescriptor getFilter() {
		return filter;
	}

	public void setFilter(FilterDescriptor filter) {
		this.filter = filter;
	}

	public List<GroupDescriptor> getGroup() {
		return group;
	}

	public void setGroup(List<GroupDescriptor> group) {
		this.group = group;
	}

	public List<AggregateDescriptor> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<AggregateDescriptor> aggregate) {
		this.aggregate = aggregate;
	}

	public static class SortDescriptor {
		private String field;
		private String dir;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}

		@Override
		public String toString() {
			return "SortDescriptor [field=" + field + ", dir=" + dir + "]";
		}

	}

	public static class GroupDescriptor extends SortDescriptor {
		private List<AggregateDescriptor> aggregates;

		public GroupDescriptor() {
			aggregates = new ArrayList<AggregateDescriptor>();
		}

		public List<AggregateDescriptor> getAggregates() {
			return aggregates;
		}

		@Override
		public String toString() {
			return "GroupDescriptor [aggregates=" + aggregates + "]";
		}

	}

	public static class AggregateDescriptor {
		private String field;
		private String aggregate;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getAggregate() {
			return aggregate;
		}

		public void setAggregate(String aggregate) {
			this.aggregate = aggregate;
		}

		@Override
		public String toString() {
			return "AggregateDescriptor [field=" + field + ", aggregate=" + aggregate + "]";
		}

	}

	public static class FilterDescriptor {
		private String logic;
		private List<FilterDescriptor> filters;
		private String field;
		private Object value;
		private String operator;
		private boolean ignoreCase = true;

		public FilterDescriptor() {
			filters = new ArrayList<FilterDescriptor>();
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		public String getLogic() {
			return logic;
		}

		public void setLogic(String logic) {
			this.logic = logic;
		}

		public boolean isIgnoreCase() {
			return ignoreCase;
		}

		public void setIgnoreCase(boolean ignoreCase) {
			this.ignoreCase = ignoreCase;
		}

		public List<FilterDescriptor> getFilters() {
			return filters;
		}

		@Override
		public String toString() {
			return "FilterDescriptor [logic=" + logic + ", filters=" + filters + ", field=" + field + ", value="
					+ value + ", operator=" + operator + ", ignoreCase=" + ignoreCase + "]";
		}

	}

	@Override
	public String toString() {
		return "DataSourceRequest [page=" + page + ", pageSize=" + pageSize + ", take=" + take + ", skip=" + skip
				+ ", sort=" + sort + ", group=" + group + ", aggregate=" + aggregate + ", data=" + data + ", filter="
				+ filter + "]";
	}

}
