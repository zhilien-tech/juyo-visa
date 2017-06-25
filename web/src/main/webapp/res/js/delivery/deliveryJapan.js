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


dafaults = {
		
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
        return model.get("data.visasendtype") == val;
    },
    province: province,
    bank: bank,
    data: dafaults
});
kendo.bind($(document.body), model);

$(function () {
    //加载订单数据
    if ($.queryString("oid")) {
        $.getJSON("/visa/neworderjp/deliveryusa?orderid=" + $.queryString("oid"), function (result) {
            model.set("data", result);
        });
    }
});



//信息保存

function deliveryJpsave(){
		
			 alert(JSON.stringify(model.data));
			 alert($.queryString("oid"));
			 $.ajax({
				 type: "POST",
				 url: "/visa/neworderjp/deliveryJpsave?orderid="+$.queryString("oid"),
				 contentType: "application/json",
				 dataType: "json",
				 data: JSON.stringify(model.data),
				 success: function (result) {
					 console.log(result.code);
					 if(result.code=="SUCCESS"){
					
						 window.opener=null;
						 window.open('','_self');
						 window.close();
					 }
				 }
			 });
	 
}





