/**
 * Created by Chaly on 2017/3/13.
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
                url: "visa/photo/option/list",
                dataType: "json"
            },
            create: {
                url: "visa/photo/option/save",
                dataType: "json",
                type: "POST",
            },
            update: {
                url: "visa/photo/option/save",
                dataType: "json",
                type: "POST",
            },
            destroy: {
                url: function (data) {
                    return "visa/photo/option/del/" + data.id;
                },
                dataType: "json",
                type: "POST",
            },
        },
        schema: {
            model: {
                id: "id",
                fields: {
                    label: {field: "label"},
                    size: {field: "size", type: "number"},
                    width: {field: "width", type: "number"},
                    height: {field: "height", type: "number"},
                    ext: {field: "ext"},
                    required: {field: "required", type: "boolean"},
                    useFor: {field: "useFor", defaultValue: "通用"},
                },
            },
            data: function (response) {
                return response.data.content;
            },
            total: function (response) {
                return response.data.totalElements;
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
        {field: 'label', title: '名称', template: "<span class='ellipsis' title='#=label#'>#=label#</span>"},
        {field: 'key', title: '关键字'},
        {field: 'size', title: '大小'},
        {field: 'width', title: '宽'},
        {field: 'height', title: '高'},
        {field: 'ext', title: '扩展名', template: "<span class='ellipsis' title='#=ext#'>#=ext#</span>"},
        {
            field: 'required',
            title: '必填',
            template: "<span class='ellipsis' style='color:#=required?'red':'green'#'>#=required?'是':'否'#</span>"
        },
        {
            field: 'useFor', title: ' 作用域', width: 100,
            template: "<span class='ellipsis'>#=useFor#</span>",
            values: [
                "通用", "美国", "日本"
            ]
        },
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
