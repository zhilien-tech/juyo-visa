
var defaults = {
    entry: {},
    depart: {},
    trips: [],
    financeJpList:[]/*财务信息~*/
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
}),keys = {
        "customer.financeJpList": {}/*财务信息~*/
    };


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
    
    
    $("#sex").kendoDropDownList();//性别 状态 下拉框初始化
	$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
	$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
	$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
	
	//操作 编辑 按钮时
	$(".editBtn").click(function(){
		$(this).addClass("hide");//编辑 按钮隐藏
		$(".cancelBtn").removeClass("hide");//取消 按钮显示
		$(".saveBtn").removeClass("hide");//保存 按钮显示
		$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
		$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
	});
	
	//操作 取消 按钮时
	$(".cancelBtn").click(function(){
		$(this).addClass("hide");//取消 按钮隐藏
		$(".saveBtn").addClass("hide");//保存 按钮隐藏
		$(".editBtn").removeClass("hide");//编辑 按钮显示
		$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
		$(".input-group input").attr("disabled");//添加 不可编辑的属性
	});
	
	//操作 保存 按钮时
	$(".saveBtn").click(function(){
		$(this).addClass("hide");//保存 按钮隐藏
		$(".cancelBtn").addClass("hide");//取消 按钮隐藏
		$(".editBtn").removeClass("hide");//编辑 按钮显示
		$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
		$(".input-group input").attr("disabled");//添加 不可编辑的属性
	});
});