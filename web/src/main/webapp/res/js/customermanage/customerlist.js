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
                    $.getJSON("/visa/personalinfo/share/" + data.id, {}, function (resp) {
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
                case "express":
                    if (!(data = select(e))) return;
                    $.layer.open({
                        type: 1,
                        title: '递送信息',
                        area: '700px',
                        shadeClose: true,
                        content: kendo.template($("#tpl_delivery").html())(data)
                    });
                    break;
                case "modify":
                    var data = grid.dataItem($(e.currentTarget).closest("tr"));
                    $.layer.open({
                        type: 2,
                        title: '编辑订单',
                        area: ['700px', '600px'],
                        shadeClose: true,
                        content: '/personalinfo/edit.html' + (data ? '?oid=' + data.id : '')
                    });
                    break;
                case "patch":
                    if (!(data = select(e))) return;
                    $.layer.open({
                        type: 2,
                        title: '补录信息',
                        area: ['700px', '600px'],
                        shadeClose: true,
                        content: '/personalinfo/patch.html' + (data ? '?oid=' + data.id : '')
                    });
                    break;
                case "pdf":
                    if (!(data = select(e))) return;
                    $.fileDownload("/visa/personalinfo/export/" + data.id, {
                        successCallback: function (url) {
                            $.layer.alert('文件不存在 :' + url);
                        },
                        failCallback: function (html, url) {
                            if (html.indexOf('<') >= 0) {
                                html = $(html).text();
                            }
                            var json = JSON.parse(html);
                            $.layer.alert(json.msg);
                        }
                    });
                    break;
                case "usa":
                    if (!(data = select(e))) return;
                    $.layer.open({
                        type: 2,
                        title: '信息收集表',
                        maxmin: true, //开启最大化最小化按钮
                        area: ['700px', '600px'],
                        content: '/m/usa.html?cid=' + data.id + "&check=true"
                    });
                    break;
                case "japan":
                    if (!(data = select(e))) return;
                    $.layer.open({
                        type: 2,
                        title: '信息收集表',
                        maxmin: true, //开启最大化最小化按钮
                        area: ['700px', '600px'],
                        content: '/m/japan.html?cid=' + data.id + "&check=true"
                    });
                    break;
                case "download":
                    if (!(data = select(e))) return;
                    $.fileDownload("/visa/customer/download/" + data.id, {
                        successCallback: function (url) {
                            $.layer.alert('文件不存在 :' + url);
                        },
                        failCallback: function (html, url) {

                            var json = JSON.parse(html);
                            $.layer.alert(json.msg);
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
function detailInit(e) {
    $("<div/>").appendTo(e.detailCell).kendoGrid({
        scrollable: false,
        dataSource: {
            transport: {
                read: {
                    type: "GET",
                    dataType: "json",
                    url: "/visa/personalinfo/show/" + e.data.id + "?type=customer",
                }
            }
        },
        columns: [
            {field: 'id', title: '客户编号'},
            {field: 'passport', title: '护照号'},
            {
                field: 'name',
                title: '姓名',
                template: "<a href='javascript:download(#=data.id#);'>#= data.lastName + data.firstName #</a>"
            },
            {field: 'gender', title: '性别', width: 60},
            {field: 'idCardNo', title: '身份证'},
            {field: 'state', title: ' 状态'},
            {
                title: "操作", width: 228,
                command: [
                    {name: "usa", imageClass: "ic-offset flag-icon flag-icon-us", text: "美国"},
                    {name: "japan", imageClass: "ic-offset flag-icon flag-icon-jp", text: "日本"},
                    {name: "download", imageClass: "base fa-cloud-download purple", text: "下载"},
                    regCmd("usa"),
                    regCmd("japan"),
                    regCmd("download"),
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
        {name: "modify", imageClass: "base fa-cart-arrow-down green", text: "修改密码"},
        {name: "modify", imageClass: "base fa-cart-arrow-down green", text: "编辑"},
    ],
    dataSource: {
        transport: {
            read: {
                type: "POST",
                dataType: "json",
                url: "/visa/customermanagement/customerlist",
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
            field: 'id', title: ' 信息id', width: 150,
            template: "<span class='ellipsis' title='#=id#'>#=id#</span>",
            editor: function (container, options) {
                container.hide().prev().hide();
            }
        },
        {
            field: 'name', title: '名称', width: 90,
            template: "<span class='ellipsis' title='#=data.name#'>#=data.name#</span>"
        },
        {
        	field: 'avatar', title: '联系人', width: 90,
        	template: "<span class='ellipsis' title='#=data.avatar#'>#=data.avatar#</span>"
        },
        {
            field: 'birthday', title: '出生日期', template: "<span class='ellipsis' title='#=data.birthday#'>#=data.birthday#</span>"
        },
        {
        	field: 'email', title: '出生日期', template: "<span class='ellipsis' title='#=data.email#'>#=data.email#</span>"
        },
        {
            title: "操作", width: 369,
            command: [
                {name: "modify", imageClass: "base fa-pencil orange", text: " 编辑"},
                {name: "patch", imageClass: "base fa-calendar-plus-o purple", text: "补录"},
                {name: "express", imageClass: "base fa-eye blue", text: "递送"},
                {name: "share", imageClass: "base fa-share-alt green", text: "分享"},
                {name: "pdf", imageClass: "base fa-file-pdf-o red", text: "PDF"},
                regCmd("modify"),
                regCmd("patch"),
                regCmd("share"),
                regCmd("express"),
                regCmd("pdf"),
            ]
        }
    ]
}).data("kendoGrid");