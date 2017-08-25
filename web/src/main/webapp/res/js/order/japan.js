//国家
var countrylist=[
                 {text:"美国",value:0},
                 {text:"日本",value:1},
                 ];
//性别
var genderlist=[
                {text:"男",value:1},
                {text:"女",value:0},
                ];
//状态listorder
var statuslist=[
                {text:"下单",value:15},
               {text:"已分享",value:1},
              {text:"资料填写",value:2},
              /*  {text:"初审",value:3},*/
                {text:"初审通过",value:4},
                {text:"初审拒绝",value:5},
             /*   {text:"递送",value:17},*/
                {text:"准备发招宝",value:8},
                {text:"发招宝中",value:18},
                {text:"已发招宝",value:19},
                {text:"发招宝失败",value:20},
                {text:"归国报告",value:21},
                {text:"提交失败",value:23},
                {text:"归国报告失败",value:22}
                /*                {text:"DS-160",value:7},

                {text:"准备提交使馆",value:8},
                {text:"已提交使馆",value:9},
                {text:"约签",value:10},
                {text:"返回",value:11},
                {text:"拒签",value:12},
                {text:"完成",value:13},
                {text:"EVUS",value:14}*/
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
			e.preventDefault();
			var data;
			switch (command) {
			case "share":
				if (!(data = select(e))) return;
				var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
				$.getJSON("/visa/neworderjp/share?type=customer&customerid=" + data.id, {}, function (resp) {
					if (resp.code === "SUCCESS") {
						if(index!=null){

							layer.close(index);
						}

						layer.msg("分享成功",{time: 2000});
					} else {
						$.layer.alert(resp.msg);
					}
				});
				break;
			case "delivery":
				if (!(data = select(e))) return;
				window.open("/delivery/deliveryJapan.html?oid="+data.id);
				break;
			case "notice":
				if (!(data = select(e))) return;
				var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
				$.getJSON("/visa/order/notice?type=customer&customerid=" + data.id, {}, function (resp) {
					if (resp.code === "SUCCESS") {
						if(index!=null){

							layer.close(index);
						}
						/* $.layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open(resp.data);
                            }, function (index) {
                                $.layer.closeAll();
                            });*/
						layer.msg("通知成功",{time: 2000});
					} else {
						$.layer.alert(resp.msg);
					}
				});
				break;
			case "shareall":
				if (!(data = select(e))) return;
				var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
				$.getJSON("/visa/neworderjp/shareall?type=order&orderid=" + data.id, {}, function (resp) {
					if (resp.code === "SUCCESS") {
						if(index!=null){

							layer.close(index);
						}
						/*	layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open("/delivery/deliveryJapan.html?oid="+data.id);
                            }, function (index) {
                                $.layer.closeAll();
                            });*/

						layer.msg("分享成功",{time: 2000});
					} else if(resp.code === "FAIL"){
						if(index!=null){
							layer.close(index);
						}
						$.layer.alert(resp.msg);
					}else{
						$.layer.alert(resp.msg);

					}
				});
				break;
			case "validate":
				if (!(data = select(e))) return;
				
				layer.open({
					type: 2,
					title: '下载',
					area: ['450px', '300px'],
					shadeClose: false,
					content: '/order/download.html?cid='+data.id,
					end: function(){//添加完页面点击返回的时候自动加载表格数据
						var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						parent.layer.close(index);
					}
				});
				
				break;
			case "noticeall":
				if (!(data = select(e))) return;
				var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
				$.getJSON("/visa/order/noticeall?type=order&orderid=" + data.id, {}, function (resp) {
					if (resp.code === "SUCCESS") {
						if(index!=null){

							layer.close(index);
						}
						/*$.layer.confirm('发送成功，打开预览？', {
                				btn: ['预览', '关闭']
                			}, function (index, layero) {
                				window.open(resp.data);
                			}, function (index) {
                				$.layer.closeAll();
                			});*/
						layer.msg("通知成功",{time: 2000});
					} else {
						$.layer.alert(resp.msg);
					}
				});
				break;
			case "customerEdit1":

				if (!(data = select(e))) return;
				layer.open({
					type: 2,
					title: '编辑客户信息',
					area: ['950px', '600px'],
					shadeClose: false,
					content: '/japan/japancustomerEdit.html?cid=' + data.id + "&check=true"
				});
				break;
			case "modify":
				var data = grid.dataItem($(e.currentTarget).closest("tr"));
				layer.open({
					type: 2,
					title: '编辑订单',
					area: ['950px', '600px'],
					shadeClose: false,
					content: '/japan/japanEdit.html?cid=' + data.id + "&check=true"
				});
				break;
			case "download":
				if (!(data = select(e))) return;
				var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景
				$.fileDownload("/visa/neworderjp/export?orderid=" + data.id, {
			         successCallback: function (url) {
			        		if(indexnew!=null){
			            		layer.close(indexnew);
			            	}
			         },
			         failCallback: function (html, url) {
			          
			        		if(indexnew!=null){
			            		layer.close(indexnew);
			            	}
			        		$.layer.alert("下载失败");
			        		
			         }
			     });
			/*	layer.open({
					type: 2,
					title: '下载',
					area: ['450px', '300px'],
					shadeClose: true,
					content: '/order/download.html?cid='+data.id,
					end: function(){//添加完页面点击返回的时候自动加载表格数据
						var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						parent.layer.close(index);
					}
				});*/
				break;

			default:
				$.layer.alert(command);
			break;
			}
		}
	};
}
function download(cid) {
	$.getJSON("/visa/photo/list", {cid: cid}, function (result) {
		if (result.code === "SUCCESS") {
			if (result.data.length > 0) {
				var array = [];
				$.each(result.data, function (i, e) {
					array.push({
						subHtml: "<h4>" + e.id.option.label + "</h4>",
						src: e.value,
					});
				});
				$.photos(array);
			} else {
				$.layer.alert("暂无照片");
			}
		} else {
			$.layer.alert(result.msg);
		}
	});
}
var a;
function detailInit(e) {
	a=$("<div/>").appendTo(e.detailCell).kendoGrid({
		scrollable: false,
		dataSource: {
			transport: {
				read: {
					/*type: "GET",*/
					dataType: "json",
					url: "/visa/neworderjp/childList?type=customer&orderId=" + e.data.id ,
				}
			}
		},
		schema: {
//			data: "content",
//			total: "totalElements",
			data : function(d) {
				return d.list;  //响应到页面的数据
			},
			total : function(d) {
				return d.recordCount;   //总条数
			}
		},
		columns: [
		          {field: 'chinesefullname',title: '姓名'},
		          {field: 'phone', title: '电话'},
		          {field: 'passport', title: '护照号'},
		          {field: 'gender', title: '性别',values:genderlist},
		          /*{field: 'sendtime', title: '送签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'outtime', title: '出签时间',format: "{0: yyyy-MM-dd}"},*/
		          {field: 'status', title: ' 状态',values:statuslist},
		          { 
		        	  title: "操作",
		        	  command: [

		        	            {name: "customerEdit1", imageClass:false, text: "编辑"},
		        	            /*  {name: " ", imageClass: "base fa-send", text: "递送"},

                    {name: "share", imageClass: "base fa-share-alt", text: "分享"	
                    },//,template: "<span class='ellipsis' title='#=data.sharecount#'>#=data.chinesefullname#</span>"
                    {name: "notice", imageClass: "base fa-bell-o", text: "通知"},*/
		        	            regCmd("customerEdit1"),
		        	            /* regCmd("share"),
                    regCmd("notice"),*/
		        	            /*    regCmd(" "),*/
		        	            ],
		          },
		          ]
	});
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
	height: "93%",
	selectable: "row",
	sortable: true,
	resizable: true,
	filterable: true,
	reorderable: false,
	columnMenu: true,
	scrollable: true,
	pageable: {
		refresh: true,
		pageSizes: true,
		buttonCount: 5
	},
	detailInit: detailInit,
	sortable: {
		mode: "multiple",
	},
	toolbar: false,
	dataSource: {
		transport: {
			read: {
				type: "POST",
				dataType: "json",
				url: "/visa/neworderjp/list",
				contentType: 'application/json;charset=UTF-8',
			},
			parameterMap: function (options, type) {
				/*return JSON.stringify(options);*/
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
					senddate: {type: "date"},
					outdate: {type: "date"},
					email: {type: "string"}
					/* user: {defaultValue: {id: "1", name: "管理员"}},
                    useFor: {defaultValue: "美国"},
                    amount: {type: "number", defaultValue: 1, validation: {min: 1, required: true}}*/
				}
			}
		},
		pageSize: 20,
		serverPaging: true,
		serverFiltering: true
	},
	columns: [
	          /*     {
	    field: 'rowNumber',
      	title: '序号',
      	template: "<span class='row-number'></span>",
      	width:75
      },*/

	          {
	        	  field: 'ordernumber', title: ' 订单号', width: 130,
	        	  template: "<span class='ellipsis' title='#=ordernumber#'>#=ordernumber#</span>",
	        	  editor: function (container, options) {
	        		  container.hide().prev().hide();
	        	  }
	          },
	          {field: 'linkman', title: '联系人', width: 90,template: "<span class='ellipsis' title='#=data.linkman#'>#=data.linkman?data.linkman:''#</span>"},
	          {field: 'telephone', title: '电话',template: "<span class='ellipsis' title='#=data.telephone#'>#=data.telephone?data.telephone:''#</span>"},
	          {field: 'senddate', title: '送签时间',format: "{0: yyyy-MM-dd}",template: "<span class='ellipsis' title='#=data.senddate#'>#=data.senddate?kendo.toString(data.senddate, 'yyyy-MM-dd'):''#</span>"},
	          {field: 'outdate', title: '出签时间',format: "{0: yyyy-MM-dd}",template: "<span class='ellipsis' title='#=data.outdate#'>#=data.outdate?kendo.toString(data.outdate, 'yyyy-MM-dd'):''#</span>"},
	          {field: 'headnum', title: '人数', values: ["美国", "日本"], width: 75,},
	          {field: 'completednumber', title: '受付番号', width: 100,},
	          /*{field: 'countrytype', title: '国家', width: 80,values:countrylist},*/
	          {field: 'status', title: '状态',values:statuslist, width: 80},
	          {
	        	  title: "操作", width: 240,
	        	  command: [
	        	            {name: "modify", imageClass:false, text: " 编辑"},
	        	            {name: "shareall", imageClass:false, text: "分享"},
	        	            /*{name: "delivery", imageClass:false, text: "递送"},*/
	        	            {name: "validate", imageClass:false, text: "递送"},
	        	            {name: "download", imageClass:false, text: "下载"},
	        	            regCmd("modify"),
	        	            regCmd("shareall"),
	        	            regCmd("download"),
	        	            /*regCmd("delivery"),*/
	        	            regCmd("validate"),
	        	            ]
	          }
	          ]/*,

    dataBound: function () {  
        var rows = this.items();  
        $(rows).each(function () {  
            var index = $(this).index() + 1;  
            var rowLabel = $(this).find(".row-number");  
            $(rowLabel).html(index);  
        });  
    }*/
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
	}else if(id == '4'){
		layer.msg("准备下载,请稍候",{time: 2000});
	}else if(id == '5'){
	layer.msg("等待递送!",{time: 2000});
}else if(id == '6'){
	layer.msg("数据不正确!",{time: 2000});
}
}
//页面加载时加载日历
$(function(){
	$("#start_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	$("#end_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	// 点击 下载 按钮时 触发
	/*	$(document).on('click','.k-grid-download',function(){

	});*/
	
	
});
/*//点击触发日历
$().click(function(

));*/


$(function(){
	for(var i=0;i<20;i++){
		 grid.table.on('click', 'tr:eq('+i+') td:eq(8)', function () {
	         // 双击, dataItem = grid.dataItem(row)
	     	 var row = $(this).closest("tr");
	     	 var data = grid.dataItem(row);
	     	 var status=data.status;
	     	 if(status>=20&&status!=21){
	     		 if(data!=''&&data!=null&&data!=undefined){
	     			 layer.alert(data.errormsg);
	     		 }
	     	 }
	     });
	}
});
