require(['jquery', 'template', 'kendo.splitter.min', 'kendo.grid.min', 'kendo.treelist.min'], function ($) {
    require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
        $("#horizontal").kendoSplitter({
            panes: [
                {collapsible: true, size: "300"},
                {collapsible: true},
            ]
        });
        //角色描述的编辑器
        function descriptionEditor(container, options) {
            container.css("height", "87px");
            $("<textarea name='description' class='k-input k-textbox' data-bind='value:" + options.field + "' style='line-height:1.5' rows='4'/>")
                .appendTo(container);
        }

        //注册命令
        function registerCommand(command) {
            return {
                name: command,
                className: "k-grid-" + command,
                attr: "style='display:none;'",
                click: function (e) {
                    e.preventDefault();
                    var row = tree.select();
                    var menu = tree.dataItem(row);
                    if (!menu) {
                        alert("请先选择需要操作的菜单");
                        return;
                    }
                    var mid = tree.dataItem(tree.select()).id;
                    $.post("sys/permission/createBase/" + mid, {}, function (data) {
                        grid.dataSource.read();
                    });
                }
            };
        }

        var tree = $("#menu_grid").kendoTreeList({
                dataSource: {
                    transport: {
                        read: {
                            url: "sys/permission/menu/json",
                            dataType: "json"
                        }
                    },
                    schema: {
                        model: {
                            id: "id",
                            parentId: "pid",
                            fields: {
                                pid: {field: "pid", type: "number"},
                            },
                            expanded: false
                        }
                    }
                },
                height: "100%",
                filterable: true,
                sortable: true,
                selectable: "row",
                columns: [
                    {field: 'name', title: '名称', width: 150, template: $("#tree_icon").html()},
                ],
                dataBound: function (e) {
                    tree.select($("#menu_grid tr:eq(1)"));
                },
                change: function (e) {
                    var mid = tree.dataItem(tree.select()).id;
                    grid.setDataSource(new kendo.data.DataSource({
                        transport: {
                            read: {
                                url: "sys/permission/ope/json",
                                dataType: "json"
                            },
                            create: {
                                url: "sys/permission/create",
                                dataType: "json",
                                type: "POST",
                            },
                            update: {
                                url: "sys/permission/update",
                                dataType: "json",
                                type: "POST",
                            },
                            destroy: {
                                url: function (data) {
                                    return "sys/permission/delete/" + data.id;
                                },
                                dataType: "json",
                                type: "POST",
                            },
                            parameterMap: function (data, type) {
                                if (type == "read") {
                                    data.pid = mid;
                                }
                                if (type == "create" || type == "update") {
                                    data["parent.id"] = mid;
                                }
                                return data;
                            },
                        },
                        schema: {
                            total: "total",
                            data: "rows",
                            model: {
                                id: "id",
                                fields: {
                                    id: {field: "id", nullable: true},
                                    type: {field: "type", defaultValue: "O"},
                                },
                            }
                        },
                    }));
                }
            }).data("kendoTreeList"),
            grid = $("#func_grid").kendoGrid({
                height: "100%",
                sortable: true,
                filterable: true,
                toolbar: [
                    {name: "create", imageClass: "add"},
                    {name: "createBase", imageClass: "base tags", text: "添加基本操作"},
                ],
                editable: {
                    confirmation: true,
                    mode: "popup",
                    window: {
                        title: "编辑"
                    }
                },
                resizable: true,
                columns: [
                    {field: 'name', title: '权限名称'},
                    {field: 'code', title: '权限编码'},
                    {field: 'url', title: '访问路径'},
                    {field: 'sort', title: '优先级'},
                    {
                        field: 'description',
                        title: '描述',
                        editor: descriptionEditor,
                        template: "<span class='ellipsis'>#=description#</span>"
                    },
                    {
                        title: "操作",
                        command: [
                            {name: "edit", imageClass: "edit"},
                            {name: "destroy", imageClass: "del"},
                            registerCommand("createBase"),
                        ],
                    },
                ]
            }).data("kendoGrid");
    });
});