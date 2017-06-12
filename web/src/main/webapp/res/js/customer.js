/**
 * Created by Chaly on 2017/1/26.
 */
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
                case "usa":
                    if (!(data = select(e))) return;
                    alert(JSON.stringify(data));
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
                case "photo":
                    if (!(data = select(e))) return;
                    $.getJSON("/visa/photo/list", {cid: data.id}, function (result) {
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
                    break;
                default:
                    $.layer.alert(command);
                    break;
            }
        }
    };
}
//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    pageSize: 20,
    height: "100%",
    sortable: true,
    editable: true,
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
                url: "visa/customer/list",
                contentType: 'application/json;charset=UTF-8',
            },
            parameterMap: function (options, type) {
                if (type === "read") {
                    options = JSON.stringify(options);
                }
                return options;
            }
        },
        schema: {
            data: "content",
            total: "totalElements",
            model: {
                id: "id",
            },
        },
    },
    columns: [
        {field: 'id', title: '客户编号'},
        {field: 'passport', title: '护照号'},
        {field: 'name', title: '姓名', template: '#= data.lastName + data.firstName #'},
        {field: 'gender', title: '性别'},
        {field: 'idCardNo', title: '身份证'},
        {field: 'state', title: ' 状态'},
        {
            title: "操作", width: 298,
            command: [
                {name: "usa", imageClass: "ic-offset flag-icon flag-icon-us", text: "美国"},
                {name: "japan", imageClass: "ic-offset flag-icon flag-icon-jp", text: "日本"},
                {name: "photo", imageClass: "base fa-file-photo-o red", text: "照片"},
                {name: "download", imageClass: "base fa-cloud-download purple", text: "下载"},
                regCmd("usa"),
                regCmd("japan"),
                regCmd("photo"),
                regCmd("download"),
            ],
        },
    ]
}).data("kendoGrid");

//注册全局刷新以方便其他页面调用刷新
parent.refresh = function () {
    grid.dataSource.read();
    grid.refresh();
}
