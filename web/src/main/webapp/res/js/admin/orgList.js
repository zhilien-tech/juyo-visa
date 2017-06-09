require(['jquery', 'kendo.treelist.min', 'kendo.dropdownlist.min'], function ($) {
    require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
        //组织结构操作的数据源
        var dataSource = {
            transport: {
                read: {
                    url: "sys/org/list/json",
                    dataType: "json"
                },
                create: {
                    url: "sys/org/create",
                    dataType: "json",
                    type: "POST",
                },
                update: {
                    url: "sys/org/update",
                    dataType: "json",
                    type: "POST",
                },
                destroy: {
                    url: function (data) {
                        return "sys/org/delete/" + data.id;
                    },
                    dataType: "json",
                    type: "POST",
                },
                parameterMap: function (data, type) {
                    if (type == "create" || type == "update") {
                        data["parent.id"] = data.pid;
                    }
                    return data;
                },
            },
            schema: {
                model: {
                    parentId: "pid",
                    fields: {
                        pid: {field: "pid", type: "number"},
                        sort: {field: "sort", type: "number"},
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
        };

        //初始化菜单列表
        $("#grid").kendoTreeList({
            dataSource: dataSource,
            height: "100%",
            filterable: true,
            toolbar: [
                {name: "create", imageClass: "add"},
            ],
            sortable: true,
            resizable: true,
            selectable: "row",
            editable: {
                confirmation: true,
                mode: "popup",
                window: {
                    title: "编辑"
                }
            },
            columns: [
                {field: 'pid', title: '上级组织', hidden: true, editor: parentEditor},
                {field: 'name', title: '机构名称', template: "<i class='tree-file'></i>#:data.name#"},
                {field: 'code', title: '机构代码'},
                {field: 'sort', title: '优先级'},
                {
                    title: "操作",
                    command: [
                        {name: "edit", imageClass: "edit"},
                        {name: "destroy", imageClass: "del"},
                    ],
                },
            ],
        });

        //上级组织的编辑器
        function parentEditor(container, options) {
            $("<input id='pid' name='pid' data-bind='value:" + options.field + "'/>")
                .appendTo(container).kendoDropDownList({
                suggest: true,
                filter: "contains",
                dataValueField: "id",
                dataTextField: "name",
                optionLabel: "顶级组织",
                dataSource: {
                    transport: {
                        read: {
                            url: "sys/org/list/json?id=" + options.model.id,
                            dataType: "json"
                        },
                    }
                }
            });
        }
    });
});