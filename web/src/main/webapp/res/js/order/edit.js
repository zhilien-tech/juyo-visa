/**
 * Created by Chaly on 2017/4/1.
 */
var defaults = {
    contact: "",
    mobile: "",
    email: "",
    user: {
        id: "1",
    },
    source: "淘宝",
    useFor: "美国",
    travelDate: "",
    sendDate: "",
    estimateDate: "",
    tripType: "代",
    dataFrom: "快递",
    postid: "",
    address: "",
    invoice: false,
    invoiceSum: 0,
    invoiceContent: "",
    invoiceTitle: "",
    visaType: "单次",
    urgent: false,
    customers: [
        {
            lastName: "",
            firstName: "",
            passport: "",
            main: true,
            depositSource: "",
            depositMethod: "",
            depositSum: 0,
            depositCount: 1,
            receipt: false,
            insurance: false,
            outDistrict: false,
        }
    ]
}, model = kendo.observable({
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
        if (all.length == 1) {
            $.layer.alert("至少要有一个申请人");
            return;
        }
        all.splice(all.indexOf(e.data), 1);
        //如果剩一个申请人则置为主申请人
        if (all.length == 1) {
            var item = all.pop();
            item.main = true;
            all.unshift(item);
        }
    },
    not: function (flag) {
        return !flag;
    },
    master: function () {
        var result = [];
        $.each(model.order.customers, function (index, item) {
            if(item.main){
                var data = {text: item.lastName + item.firstName};
                data.value = $.hashCode(data.text);
                result.push(data);
            }
        });
        return result;
    },
    order: defaults
});
kendo.bind($(document.body), model);
var onSave = function (e) {
    $("#next").unbind();
    $.ajax({
        type: "POST",
        url: "/visa/order/save/edit",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(model.order),
        success: function (result) {
            if (parent.refresh) parent.refresh();
            if (result.code === "EXCEPTION" || result.code === "FAIL") {
                $.layer.alert(result.msg);
            } else {
                $.layer.msg("操作成功", {}, function () {
                	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                	//$.layer.closeAll();
                	parent.layer.close(index);
                	window.parent.successCallback('3');
                	 //alert(parent.$("#na").val());
                });
            }
            $("#next").bind("click", onSave);
        }
    });
 /*   document.forms[0].action="/visa/order/list";
	document.forms[0].submit();*/

}
$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("oid");
    if (oid) {
        $.getJSON("/visa/order/show/" + oid, function (resp) {
            model.set("order", $.extend(true, defaults, resp));
        });
    }
    //折叠面板的显隐切换
    $(document).on("click", ".k-link", function () {
        $(this).find(".k-icon").toggleClass("k-i-arrow-60-down k-i-arrow-n");
        $(this).next().toggle();
    });
    //保存按钮的点击事件
    $("#next").bind("click", onSave);
});