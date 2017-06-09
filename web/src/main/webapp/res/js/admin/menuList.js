require(['jquery', 'kendo.treelist.min', 'kendo.dropdownlist.min', 'kendo.colorpicker.min'], function ($) {
    require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
        //编辑前的事件
        function onEdit(e) {
            //初始化上级菜单
            $("#pid").kendoDropDownList({
                dataTextField: "name",
                dataValueField: "id",
                optionLabel: "顶级菜单",
                dataSource: {
                    transport: {
                        read: {
                            url: "sys/permission/menu/json",
                            dataType: "json"
                        }
                    }
                },
                filter: "contains",
                suggest: true,
            });
            //初始化颜色选择器
            $("input[name=color]").kendoColorPicker({
                buttons: false,
                value: "#000",
                select: function (e) {
                    $("input[name=icon]").prev().find('.k-input').css("color", e.value);
                }
            });
            //初始化图标选择器
            $("input[name=icon]").kendoDropDownList({
                filter: "startswith",
                suggest: true,
                dataSource: {
                    transport: {
                        read: {
                            url: "res/plugins/font-awesome/font-awesome.json",
                            dataType: "json"
                        }
                    }
                },
                valueTemplate: "<i class='fa fa-#:data#' style='margin-right: 5px;'></i>#:data#",
                template: $("#popup_icon").html(),
            });
            //设置原有颜色
            $("input[name=icon]").prev().find('.k-input').css("color", e.model.color);
        }

        //初始化菜单列表
        $("#grid").kendoTreeList({
            dataSource: {
                transport: {
                    read: {
                        url: "sys/permission/menu/json",
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
                        if (type == "create" || type == "update") {
                            data["parent.id"] = data.pid;
                        }
                        return data;
                    },
                },
                schema: {
                    model: {
                        id: "id",
                        parentId: "pid",
                        fields: {
                            id: {field: "id", nullable: true},
                            pid: {field: "pid", type: "number"},
                            type: {field: "type", defaultValue: "F"},
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
            },
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
                },
                template: $("#popup_editor").html(),
            },
            columns: [
                {field: 'name', title: '名称', width: 150, template: $("#tree_icon").html()},
                {field: 'url', title: '资源路径'},
                {field: 'sort', title: '排序', align: 'center', width: 100},
                // {field: 'description', title: '描述', template: "<span class='ellipsis'>#=description#</span>"},
                {
                    title: "操作",
                    command: [
                        {name: "edit", imageClass: "edit"},
                        {name: "destroy", imageClass: "del"},
                        {name: "createchild", imageClass: "append"},
                    ],
                },
            ],
            edit: onEdit,
        });
    });
});
