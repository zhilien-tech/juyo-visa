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
                    if (!(data = select(e))) return;
                    $.layer.open({
                        type: 2,
                        title: '信息收集表',
                        maxmin: true, //开启最大化最小化按钮
                        area: ['700px', '600px'],
                        content: '/m/newCustomer.html?cid=' + data.id + "&check=true"
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
        {field: 'id', title: '序号'},
        {field: 'passport', title: '公司名称'},
        {field: 'name', title: '客户来源', template: '#= data.lastName + data.firstName #'},
        {field: 'gender', title: '联系人'},
        {field: 'idCardNo', title: '手机'},
        {field: 'state', title: '邮箱'},
        {
            title: "操作", width: 98,
            command: [
                {name: "edit", imageClass: "base fa-pencil-square-o purple", text: "编辑"},
                regCmd("edit"),
            ],
        },
    ]
}).data("kendoGrid");

//注册全局刷新以方便其他页面调用刷新
parent.refresh = function () {
    grid.dataSource.read();
    grid.refresh();
}
