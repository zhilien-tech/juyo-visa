// require(['jquery', 'template', 'kendo.grid.min', 'kendo.treelist.min',], function ($, template) {
//     require(['kendo.culture.zh-CN.min', 'kendo.messages.zh-CN.min'], function () {
$.ajaxSettings.traditional = true;//设置ajax请求防止数组参数多个中括号
kendo.culture("zh-CN");//切换语言
//自定义命令的点击处理事件
function commandClickHandler(e) {
    e.preventDefault();
    var row = grid.select() || $(e.currentTarget).closest("tr");
    var user = this.dataItem(row);
    if (!user) {
        alert("请先选择需要操作的数据行");
        return;
    }
    var command = $(e.currentTarget).data("command");
    if (command == "pwd") {
        openPasswordWindow(user);
    } else if (command == "org") {
        openOrgWindow(user);
    } else if (command == "role") {
        openRoleWindow(user);
    }
}

//注册命令
function registerCommand(command) {
    return {
        name: command,
        className: "k-grid-" + command,
        attr: "style='display:none;'",
        click: commandClickHandler
    };
}

//创建命令按钮模板字符串
function createCommandTemplate(color) {
    var html =
        '<a class="k-button k-button-icontext #=className#" data-command="#=name#">' +
        '   <span class="#=iconClass# #=imageClass#" style="color:{0};"></span>#=text#' +
        '</a>';
    return kendo.format(html, "\\" + color);
}

//初始化列表
var grid = $("#grid").kendoGrid({
    dataSource: {
        transport: {
            read: {
                url: "sys/user/json",
                dataType: "json"
            },
            update: {
                url: "sys/user/update",
                type: "POST",
            },
            create: {
                url: "sys/user/create",
                type: "POST",
            },
            destroy: {
                url: function (data) {
                    return "sys/user/delete/" + data.id;
                },
                dataType: "json",
                type: "POST",
            },
            parameterMap: function (data, type) {
                if (type == "update" || type == "create") {
                    data.birthday = kendo.format("{0:yyyy-MM-dd}", new Date(data.birthday));
                }
                return data;
            },
        },
        schema: {
            total: "total",
            data: "rows",
            model: {
                id: "id",
                editable: function (field) {
                    if ("loginName" == field) {
                        return !this.id;
                    } else if (this.fields[field]) {
                        return this.fields[field].editable != false;
                    } else {
                        return true;
                    }
                },
                fields: {
                    loginName: {
                        validation: {
                            required: {
                                message: "帐号不能为空!"
                            }
                        }
                    },
                    password: {
                        validation: {
                            required: {
                                message: "密码不能为空!"
                            }
                        }
                    },
                    repassword: {
                        validation: {
                            confirm: {
                                value: function (input) {
                                    if (input && input.is("[name='repassword']")) {
                                        return $("[name='password']").val() == input.val();
                                    }
                                    return true;
                                },
                                message: "两次输入的密码不一致!"
                            }
                        }
                    },
                    birthday: {type: "date"},
                    gender: {field: "gender", defaultValue: 0},
                    createDate: {type: "date", editable: false},
                    previousVisit: {type: "date", editable: false},
                    loginCount: {field: "loginCount", editable: false},
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
    toolbar: [
        {name: "create", imageClass: "add"},
        {
            name: "role",
            text: "角色分配",
            imageClass: "k-icon base fa-mortar-board",
            template: createCommandTemplate("#1989FA"),
        },
        {
            name: "pwd",
            text: "修改密码",
            imageClass: "k-icon base fa-key",
            template: createCommandTemplate("#F8CD0F"),
        },
        {
            name: "org",
            text: "组织机构",
            imageClass: "k-icon base fa-sitemap",
            template: createCommandTemplate("#A34AEF"),
        },
    ],
    editable: {
        confirmation: true,
        mode: "popup",
        window: {
            title: "编辑"
        }
    },
    columnMenu: true,
    columns: [
        {field: 'loginName', title: '帐号'},
        {field: 'password', title: '密码', width: 150, menu: false, hidden: true},
        {field: 'repassword', title: '重复密码', width: 150, menu: false, hidden: true},
        {field: 'name', title: '昵称', width: 100},
        {
            field: 'gender', title: '性别', width: 80,
            values: [
                {text: "女", value: 0},
                {text: "男", value: 1},
                {text: "保密", value: 2}
            ]
        },
        {field: 'birthday', title: '生日', width: 150, format: "{0:yyyy-MM-dd}"},
        {field: 'email', title: '邮箱', width: 150},
        {field: 'phone', title: '电话', width: 150},
        {field: 'loginCount', title: '登录次数', width: 100, hidden: true},
        {field: 'createDate', title: '注册时间', format: "{0:yyyy-MM-dd}", hidden: true},
        {field: 'previousVisit', title: '上次登录', format: "{0:yyyy-MM-dd HH:mm:ss}", hidden: true},
        {
            title: "操作",
            command: [
                {name: "edit", imageClass: "edit"},
                {name: "destroy", imageClass: "del"},
                registerCommand("pwd"),
                registerCommand("org"),
                registerCommand("role"),
            ],
        },
    ],
    edit: function (e) {
        $("label[for=loginCount]").closest(".k-edit-label").hide().next().hide();
        $("label[for=createDate]").closest(".k-edit-label").hide().next().hide();
        $("label[for=previousVisit]").closest(".k-edit-label").hide().next().hide();
        var pwd = $("input[name=password]");
        var repwd = $("input[name=repassword]");
        if (!e.model.isNew()) {
            $("label[for=password]").closest(".k-edit-label").hide().next().hide();
            $("label[for=repassword]").closest(".k-edit-label").hide().next().hide();
            pwd.removeAttr("required");
            repwd.removeAttr("confirm");
        } else {
            pwd.attr("type", "password");
            repwd.attr("type", "password");
        }
    }
}).data("kendoGrid");

//打开用户密码修改窗口
function openPasswordWindow(dataItem) {
    var JQuery = $(template("popup_password", dataItem));
    var validator = JQuery.kendoValidator({
        rules: {
            confirm: function (input) {
                if (input && input.is("[name='repassword']")) {
                    return $("[name='password']").val() == input.val();
                }
                return true;
            }
        }
    }).data("kendoValidator");
    var window = JQuery.kendoWindow({
        modal: true,
        title: "修改密码",
    }).on("click", ".k-grid-update,.k-grid-cancel", function (e) {
        var clazz = $(e.currentTarget).attr("class");
        if (clazz.indexOf("k-grid-update") > 0) {
            if (validator.validate()) {
                var password = JQuery.find("input[name=password]").val();
                $.post("sys/user/updatePwd", {id: dataItem.id, password: password}, function (result) {
                    window.close();
                    window.destroy();
                });
            }
        } else {
            window.close();
            window.destroy();
        }
    }).data("kendoWindow").center().open();
}

//打开角色管理窗口
function openRoleWindow(user) {
    var JQuery = $(template("popup_role", user)),
        grid = JQuery.find("#role_grid").kendoGrid({
            height: 340,
            selectable: "multiple,row",
            sortable: true,
            filterable: true,
            dataSource: {
                transport: {
                    read: {
                        url: "sys/role/json",
                        dataType: "json"
                    }
                },
                schema: {
                    total: "total",
                    data: "rows",
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
            columns: [
                {field: 'id', hidden: true, template: "<span roleid='#: id #'>#: id # </span>"},
                {field: 'name', title: '角色名称'},
                {field: 'roleCode', title: '角色编码'},
                {field: 'description', title: '描述'}
            ],
            dataBound: function (e) {
                //获取用户拥有角色,并选中
                $.getJSON("sys/user/" + user.id + "/role", function (data) {
                    var selector = [];
                    $.each(data, function (i, val) {
                        var uid = $("span[roleid=" + val + "]").closest("tr").data("uid");
                        selector.push("tr[data-uid='" + uid + "']")
                    });
                    grid.select(selector.join(","));
                });
            }
        }).data("kendoGrid"),
        wnd = JQuery.find("#wnd").kendoWindow({
            height: 400,
            width: 600,
            modal: true,
            scrollable: false,
            title: "用户角色管理",
        }).on("click", ".k-grid-update,.k-grid-cancel", function (e) {
            var clazz = $(e.currentTarget).attr("class");
            if (clazz.indexOf("k-grid-update") > 0) {
                var rows = grid.select(), ids = [];
                $.each(rows, function (i, row) {
                    ids.push(grid.dataItem(row).id);
                });
                $.post("sys/user/" + user.id + "/updateRole", {"ids": ids}, function (result) {
                    wnd.close();
                    wnd.destroy();
                });
            } else {
                wnd.close();
                wnd.destroy();
            }
        }).data("kendoWindow").one("activate", function () {
            grid.resize();
        }).center().open();
}

//打开组织结构管理窗口
function openOrgWindow(user) {
    var JQuery = $(template("popup_org", user)),
        tree = JQuery.find("#org_grid").kendoTreeList({
            dataSource: {
                transport: {
                    read: {
                        url: "sys/org/list/json",
                        dataType: "json"
                    }
                },
                schema: {
                    model: {
                        parentId: "pid",
                        fields: {
                            pid: {field: "pid", type: "number"},
                            sort: {field: "sort", type: "number"},
                        },
                        expanded: false
                    }
                }
            },
            height: 340,
            filterable: true,
            sortable: true,
            resizable: true,
            selectable: "multiple,row",
            columns: [
                {field: 'pid', hidden: true, template: "<span orgid='#: id #'>#: id # </span>"},
                {field: 'name', title: '机构名称', template: "<i class='tree-file'></i>#:data.name#"},
                {field: 'code', title: '机构代码'},
                {field: 'sort', title: '优先级'}
            ],
            dataBound: function (e) {
                //获取用户拥有角色,并选中
                $.getJSON("sys/user/" + user.id + "/org", function (data) {
                    var selector = [];
                    $.each(data, function (i, val) {
                        var uid = $("span[orgid=" + val + "]").closest("tr").data("uid");
                        selector.push("tr[data-uid='" + uid + "']")
                    });
                    tree.select($(selector.join(",")));
                });
            }
        }).data("kendoTreeList"),
        wnd = JQuery.find("#wnd").kendoWindow({
            width: 600,
            modal: true,
            height: 400,
            scrollable: false,
            title: "用户所属机构管理",
        }).on("click", ".k-grid-update,.k-grid-cancel", function (e) {
            var clazz = $(e.currentTarget).attr("class");
            if (clazz.indexOf("k-grid-update") > 0) {
                var rows = tree.select(), ids = [];
                $.each(rows, function (i, row) {
                    ids.push(tree.dataItem(row).id);
                });
                $.post("sys/user/" + user.id + "/updateOrg", {"ids": ids}, function (result) {
                    wnd.close();
                    wnd.destroy();
                });
            } else {
                wnd.close();
                wnd.destroy();
            }
        }).data("kendoWindow").one("activate", function () {
            tree.resize();
        }).center().open();
}
//     });
// });