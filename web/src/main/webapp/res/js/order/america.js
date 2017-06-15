//国家
var countrylist=[
    {text:"美国",value:0},
    {text:"日本",value:1},
   
    
  ];
//状态listorder
var statuslist=[
    {text:"草稿",value:0},
    {text:"提交中",value:1},
    {text:"同意",value:2},
    {text:"拒绝",value:3}
    
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
                    $.getJSON("/visa/order/share/" + data.id, {}, function (resp) {
                        if (resp.code === "SUCCESS") {
                            $.layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open(resp.data);
                            }, function (index) {
                                $.layer.closeAll();
                            });
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
                        shadeClose: true,
                        content: '/m/americaEdit.html?cid=' + data.id + "&check=true"
                    });
                    break;
                case "customerEdit":
                	var data = grid.dataItem($(e.currentTarget).closest("tr"));
                	layer.open({
                		type: 2,
                		title: '编辑客户信息',
                		area: ['950px', '600px'],
                		shadeClose: true,
                		content: '/order/customerEdit.html?cid=' + data.id + "&check=true"
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
function detailInit(e) {
    $("<div/>").appendTo(e.detailCell).kendoGrid({
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
            {field: 'gender', title: '性别', width: 60},
            {field: 'sendtime', title: '送签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'outtime', title: '出签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'status', title: ' 状态',values:statuslist},
            {
                title: "操作", width: 308,
                command: [
                    {name: "customerEdit", imageClass: "base fa-pencil", text: "编辑"},
                    {name: " ", imageClass: "base fa-send", text: "递送"},
                    {name: " ", imageClass: "base fa-share-alt", text: "分享"},
                    {name: " ", imageClass: "base fa-bell-o", text: "通知"},
                    regCmd("customerEdit"),
                    regCmd(" "),
                    regCmd(" "),
                    regCmd(" "),
                ],
            },
        ]
    });
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    height: "100%",
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
        {field: 'linkman', title: '联系人', width: 90,template: "<span class='ellipsis' title='#=data.linkman#'>#=data.linkman#</span>"},
        {field: 'email', title: '邮箱'},
        {field: 'sendtime', title: '送签时间',format: "{0: yyyy-MM-dd }" },
        {field: 'outtime', title: '出签时间',format: "{0: yyyy-MM-dd }"},
        {field: 'headcount', title: '人数', values: ["美国", "日本"], width: 80,},
        {field: 'countrytype', title: '国家', width: 80,values:countrylist},
        {field: 'status', title: '状态',values:statuslist, width: 80,},
        {
            title: "操作", width: 320,
            command: [
                {name: "modify", imageClass: "base fa-pencil orange", text: " 编辑"},
                {name: "share", imageClass: "base fa-share-alt green", text: "批量分享"},
                {name: " ", imageClass: "base fa-bell-o green", text: "批量约签通知"},
                regCmd("modify"),
                regCmd("share"),
                regCmd(" "),
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
		  layer.msg("刷新成功",{time: 2000});
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



