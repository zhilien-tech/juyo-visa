//重写startWith方法来兼容IE浏览器
String.prototype.startsWith = function (str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length)
        return false;
    if (this.substr(0, str.length) == str)
        return true;
    else
        return false;
    return true;
}
function translateZhToEn(from, to) {
    $.getJSON("/translate/google", {q: $(from).val()}, function (result) {
        $("#" + to).val(result.data).change();
    });
}
var countries = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/country_japan.json",
                dataType: "json"
            }
        }
    }),
    states = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/usa_states.json",
                dataType: "json"
            }
        }
    }),
    dafaults = {
        //父亲
        father: {},
        //母亲
        mother: {},
        //配偶
        spouse: {state: "单身"},
        //在美相关信息
        usa: {},
        //工作信息
        work: {},
        // 其他国家居民
        otherCountry: [],
        //直系亲属
        families: [],
        //历史出行记录
        histories: [],
        //教育经历
        schools: [],
        //工作经历
        works: [],
        //会说的语言
        languages: [],
        //访问过的国家
        visitCountry: [],
        //参加过的慈善组织
        charitable: [],
        finances: [],
        travel: {
            payer: "我自己",
            // 同行人
            togethers: []
        }
    },
    keys = {
        "customer.otherCountry": {},
        "customer.families": {
            lastName: "",
            firstName: "",
            lastNameEN: "",
            firstNameEN: "",
            relation: "",
            usaStatus: "",
            usaAddress: "",
            usaPhone: "",
        },
        "customer.schools": {
            startDate: "",
            endDate: "",
            name: "",
            nameEN: "",
            country: "",
            province: "",
            city: "",
            phone: "",
            zipCode: "",
            address: "",
            room: "",
            addressEN: "",
            roomEN: "",
            specialty: "",
            specialtyEN: "",
            degree: "",
            degreeEN: "",
        },
        "customer.works": {
            startDate: "",
            endDate: "",
            industry: "",
            salary: "",
            phone: "",
            current: false,
            job: "",
            jobEN: "",
            bewrite: "",
            bewriteEN: "",
            country: "",
            province: "",
            city: "",
            zipCode: "",
            name: "",
            address: "",
            room: "",
            nameEN: "",
            addressEN: "",
            roomEN: "",
            duty: "",
            dutyEN: "",
            bossLastName: "",
            bossFirstName: "",
            bossLastNameEN: "",
            bossFirstNameEN: "",
        },
        "customer.languages": {},
        "customer.visitCountry": {},
        "customer.charitable": {},
        "customer.oldPassport": {},
        "customer.oldName": {},
        "customer.army": {},
        "customer.histories": {},
        "customer.travel.togethers": {
            lastName: "",
            firstName: "",
            lastNameEN: "",
            firstNameEN: "",
            relation: "",
        },
        "customer.finances": {}
    }
/*****************************************************
 * 数据绑定
 ****************************************************/
var model = kendo.observable({
    countries: countries,
    states: states,
    onDateChange: function (e) {
        var target = e.sender.element.attr("id");
        var start = $("#signed_at").data("kendoDatePicker");
        var end = $("#expire_at").data("kendoDatePicker");
        if (target === "signed_at") {
            end.min(start.value());
        } else {
            start.max(end.value());
        }
    },
    showSaveBtn: function () {
        return !$.queryString("check");
    },
    showToolBar: function () {
        return $.queryString("check");
    },
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
        model.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = model.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    clearAll: function (key) {
        var all = model.get(key);
        if (all) all.splice(0, all.length);
    },
    add: function (key) {
        model.set(key, keys[key]);
    },
    clear: function (key) {
        model.set(key, undefined);
    },
    // 旧护照
    oldPassportEnable: function () {
        return model.get("customer.oldPassport");
    },
    // 曾用名
    oldNameEnable: function () {
        return model.get("customer.oldName");
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = model.get("customer.otherCountry");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
    // 支付人
    payType: function (type) {
        return model.get("customer.travel.payer") === type;
    },
    // 是否有同行人
    hasTogether: function () {
        var togethers = model.get("customer.travel.togethers");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;
    },
    // 婚姻状态
    spouseState: function (state) {
        return state.indexOf(model.get("customer.spouse.state")) > -1;
    },
    //是否与配偶一起
    sameAsMe: function () {
        return model.get("customer.spouse.zipCode")
            && model.get("customer.spouse.address")
            && model.get("customer.spouse.addressEN");
    },
    //是否有直系亲属在美国
    hasFamilyInUSA: function () {
        var families = model.get("customer.families");
        var state = families ? families.length > 0 : false;
        return state;
    },
    //历史入境信息
    hasHistory: function () {
        var history = model.get("customer.histories");
        var state = history ? history.length > 0 : false;
        return state;
    },
    //是否有美国驾照
    hasDriving: function () {
        var state = model.get("customer.usa.drivingLicense");
        return state;
    },
    //是否申请过移民
    hasImmigrant: function () {
        var state = model.get("customer.usa.immigrant") || model.get("customer.usa.immigrantEN");
        return state;
    },
    //是否有就签证
    hasOldVisa: function () {
        var state = model.get("customer.usa.oldVisa");
        return state;
    },
    //教育信息
    hasSchool: function () {
        var schools = model.get("customer.schools");
        var state = schools ? schools.length > 0 : false;
        return state;
    },
    //工作信息详情
    hasWorkDetail: function (state) {
        var industry = model.get("customer.work.industry");
        return !(industry === "S" || industry === "RT" || industry === "N");
    },
    //参过军
    joinArmy: function () {
        var state = model.get("customer.army");
        return state;
    },
    customer: dafaults,
});
kendo.bind($(document.body), model);
/*****************************************************
 * 开始个人信息
 ****************************************************/
$("#panelbar").kendoPanelBar({
    expandMode: "single",
    activate: function (e) {
        // $.ajax({
        //     type: "POST",
        //     url: "/visa/customer/save",
        //     contentType: "application/json",
        //     dataType: "json",
        //     data: JSON.stringify(model.customer),
        //     success: function (result) {
        //         if (parent.refresh) parent.refresh();
        //         if (result.code === "EXCEPTION") {
        //             $.layer.alert(result.msg);
        //         } else {
        //             bind(result);
        //         }
        //     }
        // });
    },
});
//身份证
$("#card_id").kendoValidator({
    rules: {
        idcard: function (input) {
            return checkCard(input.val());
        }
    },
    messages: {
        idcard: "身份证号不正确!",
    }
});
//电话
$("#mobile").kendoValidator({
    rules: {
        mobile: function (input) {
            var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则
            return reg.test(input.val()); //true
        }
    },
    messages: {
        mobile: "手机号不正确!",
    }
});
//邮箱
$("#email").kendoValidator({
    rules: {
        email: function (input) {
            var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            return reg.test(input.val()); //true
        }
    },
    messages: {
        email: "邮箱不正确!",
    }
});
//邮编
$("#home_post_code").kendoMaskedTextBox({
    mask: "000000",
});
//护照号
$("#passport_code").kendoMaskedTextBox({
    mask: "~00000000",
    rules: {
        "~": /[EG]/
    }
});
$("#old_passport").kendoMaskedTextBox({
    mask: "~00000000",
    rules: {
        "~": /[EG]/
    }
});
//丢过护照
$("#pp_lost").change(function () {
    if ($(this).is(':checked')) {
        model.add("customer.oldPassport");
    } else {
        model.clear("customer.oldPassport");
    }
});
//曾用名
$("#has_used_name").change(function () {
    if ($(this).is(':checked')) {
        model.add("customer.oldName");
    } else {
        model.clear("customer.oldName");
    }
});
//其他国家居民
$("#has_pr").change(function () {
    if ($(this).is(':checked')) {
        model.addOne("customer.otherCountry");
    } else {
        model.clearAll("customer.otherCountry");
    }
});
/*****************************************************
 * 开始出行信息
 ****************************************************/
//是否参团
$("#join_group").change(function () {
    model.set("customer.travel.team", $(this).is(':checked') ? " " : "");
});
//同行人
$("#has_other_travelers").change(function () {
    var key = "customer.travel.togethers";
    if ($(this).is(':checked')) {
        model.addOne(key);
    } else {
        model.clearAll(key);
    }
});
/*****************************************************
 * 配偶信息
 ****************************************************/
$("#same_as_home").change(function () {
    var value = $(this).is(':checked') ? " " : "";
    model.set("customer.spouse.zipCode", value);
    model.set("customer.spouse.address", value);
    model.set("customer.spouse.addressEN", value);
});
/*****************************************************
 * 亲属信息
 ****************************************************/
$("#has_immediate_relatives").change(function () {
    if ($(this).is(':checked')) {
        model.addOne("customer.families");
    } else {
        model.clearAll("customer.families");
    }
});
$("#other_relatives").change(function () {
    model.set("customer.friendInUSA", $(this).is(':checked'));
});
/*****************************************************
 * 美国相关信息
 ****************************************************/
$("#has_usa_aboard").change(function () {
    if ($(this).is(':checked')) {
        model.addOne("customer.histories");
    } else {
        model.clearAll("customer.histories");
    }
});
//美国驾照
$("#have_usa_dl").change(function () {
    model.set("customer.usa.drivingLicense", $(this).is(':checked') ? " " : "");
});
//申请移民过
$("#old_apply").change(function () {
    var value = $(this).is(':checked') ? " " : "";
    model.set("customer.usa.immigrant", value);
    model.set("customer.usa.immigrantEN", value);
});
//申请过美国签证
$("#old_apply2").change(function () {
    model.set("customer.usa.oldVisa", $(this).is(':checked') ? " " : "");
});
$("#type_same_as_previous").change(function () {
    model.set("customer.usa.sameAsThis", $(this).is(':checked'));
});
/*****************************************************
 * 教育信息
 ****************************************************/
$("#has_never_educated").change(function () {
    if ($(this).is(':checked')) {
        model.addOne("customer.schools");
    } else {
        model.clearAll("customer.schools");
    }
});
/*****************************************************
 * 其他信息
 ****************************************************/
$("#join_army").change(function () {
    if ($(this).is(':checked')) {
        model.add("customer.army");
    } else {
        model.clear("customer.army");
    }
});
/*****************************************************
 * 下一步按钮的点击事件
 ****************************************************/
function bind(result) {
    result = $.extend(true, {}, dafaults, result);
    model.set("customer", result);
    //标记拒绝项
    if ($.queryString("cid")) {
        $.getJSON("/visa/help/all", {cid: $.queryString("cid")}, function (resp) {
            $('.input-group-addon').each(function (i, element) {
                var node = $(element).parent().find(":text,select");
                if (node.data("bind")) {
                    $.each(node.data("bind").split(","), function (i, e) {
                        if ((e + "").startsWith("value:")) {
                            var source = node.closest(".row").find("*[data-params]").data("params");
                            var bind = e.substring(e.indexOf("value:") + 6, e.length);
                            var key = (source ? source + "." + bind : bind) + ":" + node.val();
                            if (resp[key] && !$(element).hasClass('.red')) {
                                $(element).addClass('red');
                            }
                        }
                    });
                }
            });
        });
    }
}
$("#next1,#save").kendoButton({
    click: function (e) {
        $.ajax({
            type: "POST",
            url: "/visa/customer/save",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(model.customer),
            success: function (result) {
                if (parent.refresh) parent.refresh();
                if (result.code === "EXCEPTION") {
                    $.layer.alert(result.msg);
                } else {
                    bind(result);
                    $.layer.msg("操作成功", {}, function () {
                        $.layer.closeAll();
                        $.layer.open({
                            type: 2,
                            title: '上传资料(3/3)',
                            maxmin: true, //开启最大化最小化按钮
                            area: ['700px', '600px'],
                            content: '/m/upload.html?cid=' + result.id + "&range=JAPAN"
                        });
                    });
                }
            }
        });
    }
});
//决绝或者通过审核
function exe(action, data) {
    $.post("/visa/customer/" + action + "/" + $.queryString("cid"), data, function (result) {
        if (parent.refresh) parent.refresh();
        $.layer.closeAll();
        $.layer.msg(result.msg);
    }, "json");
}
$("#agree,#refuse").on("click", function (e) {
    var action = $(e.target).data("process");
    var data = {};
    if (action === "agree") {
        exe(action, data);
    } else if (action === "refuse") {
        $.layer.prompt({
            formType: 2,
            value: '',
            title: '请输拒绝原因',
        }, function (value, index, elem) {
            $.layer.close(index);
            data.reason = value;
            exe(action, data);
        });
    }
});
/*****************************************************
 * 加载订单数据
 ****************************************************/
$(function () {
    var data = {cid: $.queryString("cid"),v:new Date().getTime()};
    if (data.cid) {
        $.getJSON("/visa/customer/show", data, function (result) {
            bind(result);
        });
    }
    $(document).on('click', '.input-group-addon', function () {
        var tip = $(this);
        tip.find("i").toggleClass("fa-pulse fa-spinner fa-lightbulb-o");
        var node = tip.parent().find(":text,select");
        $.each(node.data("bind").split(","), function (i, e) {
            if (e.startsWith("value:")) {
                var source = node.closest(".row").find("*[data-params]").data("params");
                var bind = e.substring(e.indexOf("value:") + 6, e.length);
                var key = (source ? source + "." + bind : bind) + ":" + node.val();
                $.getJSON("visa/help/get", {key: key, cid: $.queryString("cid")}, function (resp) {
                    if ($.queryString("check")) {
                        layer.prompt({
                            formType: 2,
                            title: "问题描述",
                            value: (resp.data) ? resp.data.msg : ''
                        }, function (msg, index, elem) {
                            var data = {
                                "id.key": key, msg: msg,
                                "id.cid": $.queryString("cid"),
                            };
                            $.post("visa/help/add", data, function (result) {
                                layer.closeAll();
                                layer.msg(result.msg);
                            }, "JSON");
                        });
                    } else if (resp.data.msg) {
                        layer.tips(resp.data.msg, tip);
                    }
                    tip.find("i").toggleClass("fa-pulse fa-spinner fa-lightbulb-o");
                });
            }
        });
    });
});
