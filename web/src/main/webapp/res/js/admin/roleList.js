require(['jquery', 'template', 'kendo.splitter.min', 'kendo.grid.min', 'kendo.treelist.min'], function ($) {
    require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
        $.ajaxSettings.traditional = true;//设置ajax请求防止数组参数多个中括号
        $("#horizontal").kendoSplitter({
            panes: [
                {collapsible: true},
                {collapsible: true, size: "300"},
            ]
        });
        //角色描述的编辑器
        function descriptionEditor(container, options) {
            container.css("height", "87px");
            $("<textarea name='description' class='k-input k-textbox' data-bind='value:" + options.field + "' style='line-height:1.5' rows='4'/>")
                .appendTo(container);
        }

        var grid = $("#role_grid").kendoGrid({
            height: "100%",
            selectable: "row",
            sortable: true,
            filterable: true,
            dataSource: {
                transport: {
                    read: {
                        url: "sys/role/json",
                        dataType: "json"
                    },
                    create: {
                        url: "sys/role/create",
                        dataType: "json",
                        type: "POST",
                    },
                    update: {
                        url: "sys/role/update",
                        dataType: "json",
                        type: "POST",
                    },
                    destroy: {
                        url: function (data) {
                            return "sys/role/delete/" + data.id;
                        },
                        dataType: "json",
                        type: "POST",
                    },
                    parameterMap: function (data, type) {
                        if (type == "create") {
                            delete data.id;
                        }
                        return data;
                    },
                },
                schema: {
                    total: "total",
                    data: "rows",
                    model: {
                        id: "id",
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
                pageSize: 20,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true
            },
            pageable: {
                refresh: true,
                pageSizes: true,
                buttonCount: 5
            },
            toolbar: [
                {name: "create", imageClass: "add"}
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
                {field: 'name', title: '角色名称'},
                {field: 'roleCode', title: '角色编码'},
                {field: 'home', title: '首页路径', hidden: true},
                {field: 'sort', title: '优先级', hidden: true},
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
                    ],
                },
            ],
            change: function (e) {
                var rid = this.dataItem(this.select()).id;
                //获取角色拥有权限
                $.getJSON("sys/role/" + rid + "/json", {}, function (data) {
                    var selector = [];
                    $.each(data, function (i, e) {
                        var uid = $("span[pid=" + e + "]").closest("tr").data("uid");
                        selector.push("tr[data-uid='" + uid + "']")
                    });
                    tree.select($(selector.join(",")));
                });
                tree.clearSelection();
            }
        }).data("kendoGrid")
        tree = $("#permission_grid").kendoTreeList({
            dataSource: {
                transport: {
                    read: {
                        url: "sys/permission/json",
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
            toolbar: [
                {
                    name: "save4role",
                    text: "保存授权",
                    imageClass: "save",
                    attr: "style:color='blue'",
                    click: function () {
                        var row = grid.select();
                        if (!row) {
                            alert("请选择操作的要操作的角色");
                            return;
                        }
                        var rows = tree.select(), ids = [];
                        $.each(rows, function (i, row) {
                            ids.push(tree.dataItem(row).id);
                        });
                        $.post("sys/role/" + grid.dataItem(row).id + "/updatePermission", {"ids": ids}, function (result) {
                            alert("操作成功!");
                        });
                    }
                },
            ],
            sortable: true,
            resizable: true,
            selectable: "multiple,row",
            columns: [
                {field: 'name', title: '名称', width: 150, template: $("#tree_icon").html()},
            ]
        }).data("kendoTreeList");
    });
});