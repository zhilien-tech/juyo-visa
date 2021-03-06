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
    {text:"初审",value:3},
    {text:"初审通过",value:4},
    {text:"初审拒绝",value:5},
    {text:"待送",value:6},
    {text:"DS-160",value:7},
    {text:"准备提交使馆",value:8},
    {text:"已提交使馆",value:9},
    {text:"约签",value:10},
    {text:"返回",value:11},
    {text:"拒签",value:12},
    {text:"完成",value:13},
    {text:"EVUS",value:14}
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
                    $.getJSON("/visa/order/share?type=customer&customerid=" + data.id, {}, function (resp) {
                        if (resp.code === "SUCCESS") {
                			if(index!=null){
            					layer.close(index);
            				}
                			/*layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open("/delivery/deliveryUSA.html?oid="+data.id);
                            }, function (index) {
                                $.layer.closeAll();
                            });*/
                        	layer.msg("分享成功",{time: 2000});
                        } else {
                            $.layer.alert(resp.msg);
                        }
                    });
                    break;
                case "delivery":
                	if (!(data = select(e))) return;
                	window.open("/delivery/deliveryUSA.html?oid="+data.id);
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
                			grid.dataSource.read();
                		} else {
                			$.layer.alert(resp.msg);
                		}
                	});
                	break;
                case "shareall":
                	if (!(data = select(e))) return;
                	 var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	$.getJSON("/visa/order/shareallnew?type=order&orderid=" + data.id, {}, function (resp) {
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
                			
                			 layer.msg("分享成功",{time: 2000});
                			 grid.dataSource.read();
                		} else {
                			$.layer.alert(resp.msg);
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
                			grid.dataSource.read();
                		} else {
                			$.layer.alert(resp.msg);
                		}
                	});
                	break;
                case "modify":
                    var data = grid.dataItem($(e.currentTarget).closest("tr"));
                    
                    layer.open({
                        type: 2,
                        title: '编辑订单',
                        area: ['950px', '600px'],
                        shadeClose: false,
                        content: '/m/americaEdit.html?cid=' + data.id + "&check=true"
                    });
                    $(this).blur();//禁止焦点
                    break;
                case "customerEdit":
                	/*var data = grid.dataItem($(e.currentTarget).closest("tr"));*/
                	/*var data = a.dataItem($(e.currentTarget).closest("tr"));*/
                	var data1 = grid.dataItem($(e.currentTarget).closest("tr"));
                	   if (!(data = select(e))) return;
                	layer.open({
                		type: 2,
                		title: '编辑客户信息',
                		area: ['950px', '600px'],
                		shadeClose: false,
                		content: '/order/customerEdit.html?cid=' + data.id + "&check=true"
                	});
                	
                	$(this).blur();//禁止焦点
                	break;
                case "validate":
                	if (!(data = select(e))) return;
                	var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	$.getJSON("/visa/order/validate?type=order&customerid=" + data.id, {}, function (resp) {
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
                			
                			layer.msg("等待递送！",{time: 2000});
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
                    url: "/visa/order/childList?type=customer&orderId=" + e.data.id ,
                }
            }
        },
        schema: {
//          data: "content",
//          total: "totalElements",
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
            {field: 'gender', title: '性别', width: 60,values:genderlist},
            {field: 'sendtime', title: '送签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'outtime', title: '出签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'status', title: ' 状态',values:statuslist},
            { 
                title: "操作", width: 230,
                command: [
                    {name: "customerEdit", imageClass:false, text: "编辑"},
                    {name: "share", imageClass:false, text: "分享"},//,template: "<span class='ellipsis' title='#=data.sharecount#'>#=data.chinesefullname#</span>"
                    {name: "delivery", imageClass:false, text: "递送"},
                    {name: "notice", imageClass:false, text: "通知"},
                   /* {name: "validate", imageClass:false, text: "验证"},*/
                    regCmd("validate"),//美国验证要走的方法
                    regCmd("customerEdit"),
                    regCmd("share"),
                    regCmd("notice"),
                    regCmd("delivery"),
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
    reorderable: true,
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
                url: "/visa/order/list",
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
                	sendtime: {type: "date"},
                	outtime: {type: "date"},
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
        {
            field: 'ordernumber', title: ' 订单号', width: 150,
            template: "<span class='ellipsis' title='#=ordernumber#'>#=ordernumber#</span>",
            editor: function (container, options) {
                container.hide().prev().hide();
            }
        },
        {field: 'linkman', title: '联系人', width: 90,template: "<span class='ellipsis' title='#=data.linkman#'>#=data.linkman?data.linkman:''#</span>"},
        {field: 'email', title: '电话',template: "<span class='ellipsis' title='#=data.telephone#'>#=data.telephone?data.telephone:''#</span>"},
        {field: 'sendtime', title: '送签时间',format: "{0: yyyy-MM-dd }",template: "<span class='ellipsis' title='#=data.sendtime#'>#=data.sendtime?kendo.toString(data.sendtime, 'yyyy-MM-dd'):''#</span>"},
        {field: 'outtime', title: '出签时间',format: "{0: yyyy-MM-dd }",template: "<span class='ellipsis' title='#=data.outtime#'>#=data.outtime?kendo.toString(data.outtime, 'yyyy-MM-dd'):''#</span>"},
        {field: 'headcount', title: '人数', values: ["美国", "日本"], width: 80,},
        {field: 'countrytype', title: '国家', width: 80,values:countrylist},
        {field: 'status', title: '状态',values:statuslist, width: 80,},
        {
            title: "操作", width: 250,
            command: [
                {name: "modify", imageClass:false, text: " 编辑"},
                {name: "shareall", imageClass:false, text: "批量分享"},
                {name: "noticeall", imageClass:false, text: "批量约签通知"},
                regCmd("modify"),
                regCmd("shareall"),
                regCmd("noticeall"),
            ]
        }
    ]
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
	$("#start_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	$("#end_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
});
/*//点击触发日历
$().click(function(
		
));*/



