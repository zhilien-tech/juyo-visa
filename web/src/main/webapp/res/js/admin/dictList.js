require(['jquery', 'kendo.treelist.min', 'kendo.grid.min', 'kendo.combobox.min'], function ($) {
    require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
        //初始化菜单列表
        $("#grid").kendoGrid({
            dataSource: {
                transport: {
                    read: {
                        url: "sys/dict/json",
                        dataType: "json"
                    },
                    create: {
                        url: "sys/dict/create",
                        dataType: "json",
                        type: "POST",
                    },
                    update: {
                        url: "sys/dict/update",
                        dataType: "json",
                        type: "POST",
                    },
                    destroy: {
                        url: function (data) {
                            return "sys/dict/delete/" + data.id;
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
                        fields: {
                            label: {
                                validation: {
                                    required: {
                                        message: "标签不能为空!"
                                    }
                                }
                            },
                            key: {
                                validation: {
                                    required: {
                                        message: "关键字不能为空!"
                                    }, min: 3
                                }
                            },
                            value: {
                                validation: {
                                    required: {
                                        message: "值不能为空!"
                                    }
                                }
                            },
                            sort: {type: "number"},
                            description:{field:"description"}
                        }
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
            toolbar: [{name: "create", imageClass: "add"}],
            editable: {
                confirmation: true,
                mode: "popup",
                window: {
                    title: "编辑"
                }
            },
            columnMenu: true,
            columns: [
                {field: 'label', title: '标签'},
                {field: 'key', title: '关键字', editor: keyEditor},
                {field: 'value', title: '值'},
                {field: 'sort', title: '优先级'},
                {field: 'category', title: '分类'},
                {
                    field: 'description',
                    title: '描述',
                    editor: descriptionEditor,
                    template: "<span class='ellipsis'>#if(description){# #=description# #}#</span>"
                },
                {
                    title: "操作",
                    command: [
                        {name: "edit", imageClass: "edit"},
                        {name: "destroy", imageClass: "del"},
                    ]
                }
            ],
        });
        //角色描述的编辑器
        function descriptionEditor(container, options) {
            container.css("height", "87px");
            $("<textarea name='description' class='k-input k-textbox' data-bind='value:" + options.field + "' style='line-height:1.5' rows='4'/>")
                .appendTo(container);
        }

        //角色描述的编辑器
        function keyEditor(container, options) {
            $("<input name='key' data-bind='value:" + options.field + "'/>")
                .appendTo(container).kendoComboBox({
                dataSource: {
                    transport: {
                        read: {
                            url: "sys/dict/key/json",
                            dataType: "json"
                        }
                    }
                },
                filter: "contains",
                suggest: true
            });
        }
    });
});