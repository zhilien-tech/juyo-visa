/**
 * Created by Chaly on 2017/1/26.
 */

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
                        area: ['700px', '600px'],
                        shadeClose: true,
                        content: '/m/americaEdit.html?cid=' + data.id + "&check=true"
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
                    type: "GET",
                    dataType: "json",
                    url: "/visa/order/show/" + e.data.id + "?type=customer",
                }
            }
        },
        columns: [
            /*{field: 'id', title: '客户编号'},*/
            {field: 'name',title: '姓名',template: "<a href='javascript:download(#=data.id#);'>#= data.lastName + data.firstName #</a>"},
            {field: 'passport', title: '电话'},
            {field: 'passport', title: '护照号'},
            {field: 'gender', title: '性别', width: 60},
            /*{field: 'idCardNo', title: '身份证'},*/
            {field: ' ', title: '送签时间'},
            {field: ' ', title: '出签时间'},
            {field: 'state', title: ' 状态'},
            {
                title: "操作", width: 308,
                command: [
                    {name: " ", imageClass: "base fa-pencil", text: "编辑"},
                    {name: " ", imageClass: "base fa-send", text: "递送"},
                    {name: " ", imageClass: "base fa-share-alt", text: "分享"},
                    {name: " ", imageClass: "base fa-bell-o", text: "通知"},
                    regCmd(" "),
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
    toolbar: [
        {name: "modify", imageClass: "base fa-cart-arrow-down green", text: "下单"},
    ],
    dataSource: {
        transport: {
            read: {
                type: "POST",
                dataType: "json",
                url: "/visa/order/list",
                contentType: 'application/json;charset=UTF-8',
            },
            parameterMap: function (options, type) {
                return JSON.stringify(options);
            },
        },
        schema: {
            data: "content",
            total: "totalElements",
            model: {
                id: "id",
                fields: {
                    startDate: {type: "date"},
                    endDate: {type: "date"},
                    email: {type: "email"},
                    user: {defaultValue: {id: "1", name: "管理员"}},
                    useFor: {defaultValue: "美国"},
                    amount: {type: "number", defaultValue: 1, validation: {min: 1, required: true}}
                }
            }
        },
        pageSize: 20,
        serverPaging: true,
        serverFiltering: true,
        serverSorting: true
    },
    columns: [
        {
            field: 'id', title: ' 订单号', width: 150,
            template: "<span class='ellipsis' title='#=id#'>#=id#</span>",
            editor: function (container, options) {
                container.hide().prev().hide();
            }
        },
        {field: 'contact', title: '联系人', width: 90,template: "<span class='ellipsis' title='#=data.contact#'>#=data.contact#</span>"},
        {field: 'email', title: '邮箱', template: "<span class='ellipsis' title='#=data.email#'>#=data.email#</span>"},
        {field: 'email', title: '送签时间', template: "<span class='ellipsis' title='#=data.email#'>#=data.email#</span>"},
        {field: 'email', title: '出签时间', template: "<span class='ellipsis' title='#=data.email#'>#=data.email#</span>"},
        {field: 'useFor', title: '人数', values: ["美国", "日本"], width: 80,},
        {field: 'useFor', title: '国家', values: ["美国", "日本"], width: 80,},
        {field: 'useFor', title: '状态', values: ["美国", "日本"], width: 80,},
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
	alert(111);
	//grid.GetJQuery().refresh();
	grid.dataSource.read();
	 /* if(id == '1'){
		  layer.msg("添加成功",{time: 2000});
	  }else if(id == '2'){
		  layer.msg("修改成功",{time: 2000});
	  }else if(id == '3'){
		  layer.msg("刷新成功",{time: 2000});
	  }*/
  }