//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);


//注册命令
function regCmd(command) {
    var select = function (e) {
        var data = grid.dataItem($(e.currentTarget).closest("tr"));
        if (!data) {
            $.layer.alert("请先选择需要操作的数据行");
        }
        return data;
    };
    return {
        name: command,
        className: "k-grid-" + command,
        attr: "style='display:none;'",
        click: function (e) {
            e.preventDefault();
            var data;
            switch (command) {
                case "edit":
                	var data = grid.dataItem($(e.currentTarget).closest("tr"));
                    if (!(data = select(e))){
                    	return;
                    }else{
                    	layer.open({
                    		type: 2,
                    		title: '编辑',
                    		maxmin: true, //开启最大化最小化按钮
                    		area: ['850px', '500px'],
                    		content: '/function/updateFunction.html?funId=' + data.id
                    	});
                    }
                    break;
                default:
                    $.layer.alert(command);
                    break;
            }
        }
    };
}
//添加功能
function addFunction(){
  layer.open({
	    type: 2,
	    title:false,
	    closeBtn:true,
	    fix: false,
	    maxmin: true,
	    shadeClose: false,
	    title: '添加',
	    area: ['850px', '500px'],
	    content: '/function/addFunction.html',
	    end: function(){//添加完页面点击返回的时候自动加载表格数据
	    	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			parent.layer.close(index);
	    }
	 });
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    pageSize: 20,
    height: "93%",
    sortable: true,
    editable: false,
    resizable: true,
    filterable: true,
    selectable: "row",
    serverPaging: true,
    serverSorting: true,
    serverFiltering: true,
    pageable: {
        refresh: true,
        pageSizes: true,
        buttonCount: 5
    },
    sortable: {
        mode: "multiple",
    },
    dataSource: {
        transport: {
            read: {
                type: "POST",
                dataType: "json",
                url: "/visa/function/functionlist",
                contentType: 'application/json;charset=UTF-8',
            },
            parameterMap: function (options, type) {
            		var parameter = {
                        pageNumber : options.page,    //当前页
                        pageSize : options.pageSize,//每页显示个数
                        keyword:$("#keyword").val(),//检索条件
                    };
               return kendo.stringify(parameter);
            }
        },
        schema: {
        	data : function(d) {
                return d.list;  //响应到页面的数据
            },
            total : function(d) {
                return d.recordCount;//总条数
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
        	title: '序号',
        	field: 'serialnumber',
        	template: "<span class='row-number'></span>" 
        },
        {
        	title: '功能名称',
        	field: 'funname'
        	//template: '#= data.fullcomname#'
        },
        {
        	title: ' 访问地止', 
        	field: 'url'
        },
        {
        	title: '功能等级',
        	field: 'level' 
        	//template: '#= data.linkman#'
        },
        {
        	title: '备注',
        	field: 'remark'
        	//template: '#= data.telephone#'
        },
        {
            title: "操作", width: 98,
            command: [
                {name: "edit", imageClass:false, text: "编辑"},
                regCmd("edit")
            ]
        }
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


//注册全局刷新以方便其他页面调用刷新
parent.refresh = function () {
    grid.dataSource.read();
    grid.refresh();
}
//事件提示
function successCallback(id){
	grid.dataSource.read();
	if(id == '1'){
		layer.msg("添加成功",{time:2000});
	}else if(id == '2'){
		layer.msg("修改成功",{time:2000});
	}else if(id == '3'){
		layer.msg("删除成功",{time:2000});
	}
}
//列表页检索
$("#searchBtn").on('click', function () {
	grid.dataSource.read();
})
//搜索回车事件
function onkeyEnter(){
	 if(event.keyCode==13){
		 $("#searchBtn").click();
	 }
}