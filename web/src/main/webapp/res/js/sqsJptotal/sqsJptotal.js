//注册命令
/*function regCmd(command) {
    var select = function (e) {
        var data = $(e.delegateTarget).data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
        if (!data) $.layer.alert("请先选择需要操作的数据行");
        return data;
    };
    return {
        name: command,
        className: "k-grid-" + command,
        attr: "style='display:none;'",
        click: function (e) {
        	
        }
    };
}*/
var sqscount;
var djscount;
var recordcount;
//送签社搜索
var sendCom=new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/neworderjp/downloadselectfind?a="+1,
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});
//地接社搜索
var landCom=new kendo.data.DataSource({
	serverFiltering: true,
	transport: {
		read: {
			dataType: "json",
			url: "/visa/neworderjp/downloadselectfind?a="+2,
		},
		parameterMap: function (options, type) {
			if (options.filter) {
				return {filter: options.filter.filters[0].value};
			}
		},
	}
});
//签证类型
var visatypeStatus=[
	{text:"单次",value:0},           
	{text:"一年单次",value:1},           
	//{text:"东三县",value:2},           
	//{text:"新三县",value:3},           
	{text:"冲绳东北三年",value:4},           
	{text:"普通三年多次",value:5},           
	{text:"普通五年",value:6}      
];
var defaults = {
		
};

//数据绑定
var viewModel = kendo.observable({
	sendCom:sendCom,
	landCom:landCom,
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
    sortable: {
        mode: "multiple",
    },
    toolbar: false,
    dataSource: {
        transport: {
            read: {
                type: "POST",
                dataType: "json",
                url: "/visa/sqsjptotal/sqsjptotalList",
                contentType: 'application/json;charset=UTF-8'
            },
            parameterMap: function (options, type) {
            	var parameter = {
                        pageNumber : options.page,    //当前页
                        pageSize : options.pageSize,//每页显示个数
                        start_time:$("#start_time").val(),
                        end_time:$("#end_time").val(),
                        keyword:$("#keyword").val(),//检索条件
                        sqs_id:$("#sqs_id").val(),
						djs_id:$("#djs_id").val()
                    };
               return kendo.stringify(parameter);
            },
        },
        schema: {
        	data : function(d) {
        		var sqslist = d.list;
        		if(sqslist != "" && sqslist  != null && sqslist != undefined){
        			sqscount = sqslist[0].sqscount;//送签社
        			djscount = sqslist[0].djscount;//地接社
        			recordcount = d.recordCount;// 总订单
        		}else{
        			recordcount = 0;
        		}
        		if($("#sqs_id option:selected").text()=="送签社" && $("#sqs_id").val() == ""){
					$("#sqs_count").html(sqscount);
				}else{
					$("#sqs_count").html(1);
				}
				if($("#djs_id option:selected").text()=="地接社" && $("#djs_id").val() == ""){
					$("#djs_count").html(djscount);
				}else{
					$("#djs_count").html(1);
				}
				$("#total_count").html(recordcount);
				if(sqslist == "" || sqslist == null || sqslist == undefined){
					$('.k-grid-content span').remove();
					$('.k-grid-content').append('<span style="display: block;text-align: center;line-height:3rem;color: #dcdcdc;font-size: 13px;">暂&nbsp;无&nbsp;可&nbsp;用&nbsp;数&nbsp;据</span>');
				}else{
					$('.k-grid-content span').remove();
				}
                return sqslist;  //响应到页面的数据
            },
            total : function(d) {
                return d.recordCount;   //总条数
            },
            model: {
                id: "id",
            }
        },
        pageSize: 20,
        serverPaging: true,
        serverFiltering: true
    },
    columns: [
        {	
        	field: 'number', title: '序号', width: 75,
        	template: "<span class='row-number'></span>" 
        },
        {field: 'ordernumber', title: ' 订单号', width: 150},
        {field: 'comfullname', title: '送签社',template: "<span class='ellipsis' title='#=data.comfullname#'>#=data.comfullname?data.comfullname:''#</span>"},
        {field: 'username', title: '操作人'},
        {field: 'landcomfullname', title: '地接社',template: "<span class='ellipsis' title='#=data.landcomfullname#'>#=data.landcomfullname?data.landcomfullname:''#</span>"},
        {field: 'completednumber', title: '受付番号'},
        {field: 'mainporposer', title: '主申请人', width: 100},
        {field: 'headnum', title: '人数', width: 80},
        {field: 'visatype', title: '签证类型',values:visatypeStatus, width:100}
    ],
    dataBound:function () {
	    var rows = this.items();  
	    $(rows).each(function () {  
	        var index = $(this).index() + 1;  
	        var rowLabel = $(this).find(".row-number");  
	        $(rowLabel).html(index);  
	    });  
    }
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
	//$("#sendSigned").kendoDropDownList();//送签社 初始化
	//$("#groundConnection").kendoDropDownList();//地接社 初始化
	$("#start_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	$("#end_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	
	$("#sqs_id").kendoDropDownList({
		optionLabel: "送签社",
		dataTextField: "comFullName",
		dataValueField: "id",
		dataSource: {
			transport: {
				read: {
					dataType: "json",
					url: "/visa/sqsjptotal/compSelectfind?compType=1",
				}
			}
		}
	});
	
	$("#djs_id").kendoDropDownList({
		optionLabel: "地接社",
		dataTextField: "landcomFullName",
		dataValueField: "id",
		dataSource: {
			transport: {
				read: {
					dataType: "json",
					url: "/visa/sqsjptotal/compSelectfind?compType=2",
				}
			}
		}
	});
});
//列表页检索
$("#searchBtn").on('click', function () {
	grid.dataSource.read();
})
//日期
function startTime(){
	$("#searchBtn").click();
}
function endTime(){
	$("#searchBtn").click();
}
//送签社
function sqsChange(){
	$("#searchBtn").click();
}
//地接社
function djsChange(){
	$("#searchBtn").click();
}
//搜索回车事件
function onkeyEnter(){
	 if(event.keyCode==13){
		 $("#searchBtn").click();
	 }
}
