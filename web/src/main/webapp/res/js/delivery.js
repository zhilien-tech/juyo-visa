/**
 * Created by Chaly on 2017/1/26.
 */
var province = new kendo.data.DataSource({
    transport: {
        read: {
            dataType: "json",
            url: "/res/json/citic_provinces.json",
        }
    }
}), bank = new kendo.data.DataSource({
    transport: {
        read: {
            dataType: "json",
            url: "/res/json/citic_addresses.json",
        }
    },
    schema: {
        parse: function (resp) {
            return parseCity(resp);
        }
    }
}), parseCity = function (resp) {
    var city = $("#city").data("kendoDropDownList")
    var val = city.dataItem(city.select());
    return resp[val];
};
var model = kendo.observable({
    onDateChange: function (e) {
        var target = e.sender.element.attr("id");
        var start = $("#start").data("kendoDatePicker");
        var end = $("#end").data("kendoDatePicker");
        if (target === "start") {
            end.min(start.value());
        } else {
            start.max(end.value());
        }
    },
    onCityChange: function (e) {
        $.getJSON("/res/json/citic_addresses.json", {}, function (resp) {
            model.set("bank", parseCity(resp));
        });
    },
    getDelivery: function (val) {
        return model.get("data.delivery") === val;
    },
    province: province,
    bank: bank,
    data: {
        startDate: null,
        endDate: null,
        period: "全天",
        remark: "",
        delivery: "代取",
        city: "北京",
        address: "",
    },
});
kendo.bind($(document.body), model);
//初始化下一步按钮的点击事件
$("#next").kendoButton({
    click: function (e) {
        $.ajax({
            type: "POST",
            url: "/visa/order/save/delivery",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(model.data),
            success: function (result) {
                if (result.code === "FAIL" || result.code === "EXCEPTION") {
                    $.layer.alert(result.msg);
                } else {
                    if (parent.refresh) parent.refresh();
                    $.layer.closeAll();
                    $.layer.open({
                        type: 2,
                        title: '信息登记',
                        maxmin: true, //开启最大化最小化按钮
                        area: ['700px', '600px'],
                        content: './team.html?oid=' + result.id + '&to=' + (result.useFor === "美国" ? "usa" : "japan")
                    });
                }
            }
        });
    }
});
$(function () {
    //加载订单数据
    if ($.queryString("oid")) {
        $.getJSON("/visa/order/show/" + $.queryString("oid"), function (result) {
            model.set("data", result);
        });
    }
});
