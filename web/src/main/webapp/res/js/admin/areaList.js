require(['jquery', 'kendo.treelist.min', 'kendo.dropdownlist.min', 'kendo.columnmenu.min'], function ($) {
    require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
        var dataSource = new kendo.data.TreeListDataSource({
            transport: {
                read: {
                    url: "sys/area/json",
                    dataType: "json"
                },
                create: {
                    url: "sys/area/create",
                    dataType: "json",
                    type: "POST",
                },

                destroy: {
                    url: function (data) {
                        return "sys/area/delete/" + data.id;
                    },
                    dataType: "json",
                    type: "POST",
                },
                parameterMap: function (data, type) {
                    if (data.pid > 0) {
                        data["parent.id"] = data.pid;
                    }
                    if (type == "create") {
                        delete data.id;
                    }
                    return data;
                }
            },
            schema: {
                model: {
                    parentId: "pid",
                    fields: {
                        pid: {field: "pid", type: "number"},
                        hasChildren: {field: "state", type: "boolean"},
                    },
                    expanded: false
                },
                errors: function (response) {
                    if (response.code == "EXCEPTION") {
                        return response.msg;
                    }
                }
            },
            error: function (e) {
                alert(e.errors);
            },
        });
        //初始化菜单列表
        $("#grid").kendoTreeList({
            dataSource: dataSource,
            height: "100%",
            sortable: true,
            resizable: true,
            columnMenu: true,
            filterable: true,
            toolbar: [
                {name: "create", imageClass: "add"},
            ],
            pageable: {
                refresh: true,
                pageSizes: true,
                buttonCount: 5
            },
            editable: {
                confirmation: true,
                mode: "popup",
                window: {
                    title: "编辑"
                }
            },
            columns: [
                {field: 'name', title: '区域名称', expandable: true, template: $("#tree_icon").html()},
                {field: 'shortName', title: '简称', width: 200, hidden: true},
                {field: 'cityCode', title: '城市区号', width: 200},
                {field: 'zipCode', title: '邮政编码', width: 200},
                {field: 'pinyin', title: '拼音码', hidden: true},
                {field: 'lng', title: '精度', hidden: true},
                {field: 'lat', title: '纬度', hidden: true},
                {field: 'level', title: '层级', hidden: true},
                {field: 'mergerName', title: '全称', hidden: true},
                {
                    title: "操作",
                    command: [
                        {name: "edit", imageClass: "edit"},
                        {name: "destroy", imageClass: "del"},
                        {name: "createchild", imageClass: "append"},
                    ]
                }
            ]
        });
    });
});
