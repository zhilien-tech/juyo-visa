
//签证类型
var visatypelist=[
                  {text:"东三县",value:2},
                  {text:"单次",value:0},
                  {text:"新三县",value:3},
                  {text:"冲绳",value:4},
                  {text:"普通三年",value:5}
                  ];

var defaults = {
		
};

var sendComp=new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/systemstatistic/compSelectfind?compType="+1,
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});
var landComp=new kendo.data.DataSource({
	serverFiltering: true,
	transport: {
		read: {
			dataType: "json",
			url: "/visa/systemstatistic/compSelectfind?compType="+2,
		},
		parameterMap: function (options, type) {
			if (options.filter) {
				return {filter: options.filter.filters[0].value};
			}
		},
	}
});

var viewModel = kendo.observable({
	sendComp:sendComp,
	landComp:landComp,
    customer: defaults
    
});
kendo.bind($(document.body), viewModel);

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
	height: "89%",
	selectable: "row",
	sortable: true,
	resizable: true,
	filterable: true,
	reorderable: true,
	columnMenu: true,
	scrollable: true,
	pageable: {
		refresh: true,
		pageSizes: true,
		buttonCount: 5
	},
	detailInit: false,
	sortable: {
		mode: "multiple",
	},
	toolbar: false,
	dataSource: {
		transport: {
			read: {
				type: "POST",
				dataType: "json",
				url: "/visa/systemstatistic/list",
				contentType: 'application/json;charset=UTF-8',
			},
			parameterMap: function (options, type) {
				var parameter = {
						pageNumber : options.page,   //当前页
						pageSize : options.pageSize, //每页显示个数
						start_time:$("#start_time").val(),
						end_time:$("#end_time").val(),
						keywords:$("#keywords").val(),
						sqs_id:$("#sqs_id").val(),
						djs_id:$("#djs_id").val()
				};
				return kendo.stringify(parameter);
			},
		},
		schema: {
			/* data: "content",
            total: "totalElements",*/
			data : function(d) {
				return d.list;  //响应到页面的数据
			},
			total : function(d) {
				return d.recordCount;   //总条数
			},
			model: {
				id: "id",
				fields: {
					sendtime: {type: "date"},
					outtime: {type: "date"}
				}
			}
		},
		pageSize: 20,
		serverPaging: true,
		serverFiltering: true
	},
	columns: [
	          {
	      	    field: 'rowNumber',
	            	title: '序号',
	            	template: "<span class='row-number'></span>",
	            	width:75
	          },
	          {
	        	  field: 'ordernumber', title: ' 订单号', width: 150,
	        	  template: "<span class='ellipsis' title='#=ordernumber#'>#=ordernumber#</span>",
	        	  editor: function (container, options) {
	        		  container.hide().prev().hide();
	        	  }
	          },
	          {field: 'comfullname', title: '送签社', width:150},
	          {field: 'username', title: '操作人', width:110},
	          {field: 'landcomfullname', title: '地接社', width:150},
	          {field: 'completednumber', title: '受付番号', width:140},
	          {field: 'chinesefullname', title: '主申请人', width: 100},
	          {field: 'headnum', title: '人数', width:80},
	          {field: 'visatype', title: '签证类型', values:visatypelist, width:100}

	          ],
	          dataBound:function () {
	      	    var rows = this.items();  
	      	    $(rows).each(function () {  
	      	        var index = $(this).index() + 1;  
	      	        var rowLabel = $(this).find(".row-number");  
	      	        $(rowLabel).html(index);  
	      	    });  
	          },
}).data("kendoGrid");

//页面刷新
function successCallback(id){
	//grid.GetJQuery().refresh();
	grid.dataSource.read();
	if(id == '1'){
		layer.msg("添加成功",{time: 2000});
	}else if(id == '2'){
		layer.msg("修改成功",{time: 2000});
	}else if(id == '3'){
		layer.msg("操作成功",{time: 2000});
	}
}
//页面加载时加载日历
$(function(){
	$("#sendSigned").kendoDropDownList();//送签社 初始化
	$("#groundConnection").kendoDropDownList();//地接社 初始化
	$("#start_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	$("#end_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
});