/**
 * Created by Chaly on 2017/4/8.
 */
var defaults = {
    entry: {},
    depart: {},
    trips: []
}, flights = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/flight/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
}), hotels = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/hotel/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
}), scenic = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/scenic/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});


var model = kendo.observable({
    flights: flights,
    hotels: hotels,
    scenic: scenic,
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
        model.get(key).push({
            lastName: "",
            firstName: "",
            passport: "",
            main: false,
            depositSource: "",
            depositMethod: "",
            depositSum: 0,
            depositCount: 1,
            receipt: false,
            insurance: false,
            outDistrict: false,
        });
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = model.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
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
    onSpotChange: function (e) {
        console.log(e)
    },
    getSpot: function (data) {
        if (data.spot) {
            return data.spot.split(",")
        }
    },
    order: defaults,
});
kendo.bind($(document.body), model);
var onSave = function (e) {
    $("#next").unbind();
    $.ajax({
        type: "POST",
        url: "/visa/order/save/patch",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(model.order),
        success: function (result) {
            if (parent.refresh) parent.refresh();
            if (result.code === "EXCEPTION" || result.code === "FAIL") {
                $.layer.alert(result.msg);
            } else {
                $.layer.msg("操作成功", {}, function () {
                    $.layer.closeAll();
                });
            }
            $("#next").bind("click", onSave);
        }
    });
}
$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("oid");
    if (oid) {
        $.getJSON("/visa/order/show/" + oid, function (resp) {
            model.set("order", $.extend(true, defaults, resp));
        });
    }
    $("#next").click(onSave);
});