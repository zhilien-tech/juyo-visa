/**
 * Created by Chaly on 2017/4/11.
 */
function timeEditor(container, options) {
    $('<input data-role="timepicker" data-bind="value:' + options.field + '"/>').appendTo(container);
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    height: "100%",
    selectable: "row",
    sortable: true,
    resizable: true,
    filterable: true,
    pageable: {
        refresh: true,
        pageSizes: true,
        buttonCount: 5
    },
    toolbar: [
        {name: "create", imageClass: "base fa-plus green", text: "新增"},
    ],
    editable: {
        confirmation: true,
        mode: "popup",
        window: {title: "编辑"}
    },
    dataSource: {
        transport: {
            read: {
                url: "visa/flight/list",
                dataType: "json",
                type: "POST",
                contentType: 'application/json;charset=UTF-8',
            },
            create: {
                url: "visa/flight/save",
                dataType: "json",
                type: "POST",
            },
            update: {
                url: "visa/flight/save",
                dataType: "json",
                type: "POST",
            },
            destroy: {
                url: function (data) {
                    return "visa/flight/del/" + data.id;
                },
                dataType: "json",
                type: "POST",
            },
            parameterMap: function (options, type) {
                if (type === "read") {
                    options = JSON.stringify(options);
                }
                return options;
            }
        },
        schema: {
            model: {
                id: "id",
            },
            data: function (response) {
                return response.content;
            },
            total: function (response) {
                return response.totalElements;
            },
            errors: function (response) {
                if (response.code == "EXCEPTION") {
                    return response.msg;
                }
            }
        },
        error: function (e) {
            $.layer.alert(e.errors);
        },
        pageSize: 20,
        serverPaging: true,
        serverFiltering: true,
        serverSorting: true
    },
    columns: [
        {field: 'from', title: '起飞机场'},
        {field: 'to', title: '降落机场'},
        {field: 'line', title: '航班号'},
        {field: 'departure', title: '起飞时间', format: "{0:HH:mm}", editor: timeEditor},
        {field: 'landing', title: '着陆时间', format: "{0:HH:mm}", editor: timeEditor},
        {field: 'fromTerminal', title: '起飞航站楼'},
        {field: 'fromCity', title: '起飞城市'},
        {field: 'toTerminal', title: '降落航站楼'},
        {field: 'toCity', title: '降落城市'},
        {
            title: "操作",
            width: 156,
            command: [
                {name: "edit", imageClass: "base fa-pencil orange", text: " 编辑"},
                {name: "destroy", imageClass: "base fa-trash red", text: "删除"},
            ],
        },
    ]
}).data("kendoGrid");