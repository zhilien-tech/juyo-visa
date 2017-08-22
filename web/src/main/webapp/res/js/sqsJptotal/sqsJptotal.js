//页面加载时加载日历
$(function(){
	$("#sendSigned").kendoDropDownList();//送签社 初始化
	$("#groundConnection").kendoDropDownList();//地接社 初始化
	$("#start_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	$("#end_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
});
//签证类型
var visatypeStatus=[
	{text:"单次",value:0},           
	{text:"一年单次",value:1},           
	{text:"东三县",value:2},           
	{text:"新三县",value:3},           
	{text:"冲绳",value:4},           
	{text:"普通三年",value:5},           
	{text:"普通五年",value:6}      
];
//注册命令
function regCmd(command) {
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
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    height: "93%",
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
                contentType: 'application/json;charset=UTF-8',
            },
            parameterMap: function (options, type) {
            	var parameter = {
                        pageNumber : options.page,    //当前页
                        pageSize : options.pageSize,//每页显示个数
                        start_time:$("#start_time").val(),
                        end_time:$("#end_time").val(),
                        keywords:$("#keywords").val(),
                        state:$("#state").val()
                    };
               return kendo.stringify(parameter);
            },
        },
        schema: {
        	data : function(d) {
                return d.list;  //响应到页面的数据
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
        {field: 'comfullname', title: '送签社'},
        {field: 'username', title: '操作人'},
        {field: 'landcomfullname', title: '地接社'},
        {field: 'completednumber', title: '受付番号'},
        {field: 'fullname', title: '主申请人', width: 100},
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