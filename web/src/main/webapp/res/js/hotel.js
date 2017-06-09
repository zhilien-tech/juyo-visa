/**
 * Created by Chaly on 2017/4/11.
 */
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
                url: "visa/hotel/list",
                dataType: "json",
                type: "POST",
                contentType: 'application/json;charset=UTF-8',
            },
            create: {
                url: "visa/hotel/save",
                dataType: "json",
                type: "POST",
            },
            update: {
                url: "visa/hotel/save",
                dataType: "json",
                type: "POST",
            },
            destroy: {
                url: function (data) {
                    return "visa/hotel/del/" + data.id;
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
        {field: 'name', title: '酒店名称'},
        {field: 'phone', title: '电话'},
        {field: 'address', title: '地址'},
        {field: 'city', title: '所属城市'},
        {field: 'nameJP', title: '酒店名称(日文)'},
        {field: 'addressJP', title: '地址(日文)'},
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