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
	workinfoJp: {},
	financeJpList:[],
	oldpassportJp: {},
	oldnameJp: {},
	orthercountryJpList: [],
	recentlyintojpJpList: [],
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
},
    keys = {
        "customer.financeJpList": {},
        "customer.orthercountryJpList": {},
        "customer.recentlyintojpJpList": {}
       
        
    }
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
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
        viewModel.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    clearAll: function (key) {
        var all = viewModel.get(key);
        if (all) all.splice(0, all.length);
    },
    add: function (key) {
        viewModel.set(key, keys[key]);
    },
    clear: function (key) {
        viewModel.set(key, undefined);
    },
    // 旧护照
    oldPassportEnable: function () {
        ///return viewModel.get("customer.oldpassportJp");
    	var oldPassportEnable = viewModel.get("customer.oldpassportJp");
    	var state = oldPassportEnable ? oldPassportEnable.length > 0 : false;
        return state;
    },
    // 曾用名
    oldNameEnable: function () {
        ///return viewModel.get("customer.oldnameJp");
    	var oldNameEnable = viewModel.get("customer.oldnameJp");
    	var state = oldNameEnable ? oldNameEnable.length > 0 : false;
        return state;
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = viewModel.get("customer.orthercountryJpList");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
    master: function () {
        var result = [];
        $.each(viewModel.customer.customers, function (index, item) {
            if(item.main){
                var data = {text: item.lastName + item.firstName};
                data.value = $.hashCode(data.text);
                result.push(data);
            }
        });
        return result;
    },
    customer: dafaults,
});
kendo.bind($(document.body), viewModel);
/*****************************************************
 * 开始个人信息
 ****************************************************/

//丢过护照
$("#pp_lost").change(function () {
	///viewviewModel.set("customer.passportlose", $(this).is(':checked') ? " " : "");
	viewModel.set("customer.oldpassportJp", $(this).is(':checked') ? " " : "");
});
//曾用名
$("#has_used_name").change(function () {
   /*///if ($(this).is(':checked')) {
        viewModel.add("customer.oldName");
    } else {
        viewModel.clear("customer.oldName");
    }*/
	/*viewviewModel.set("customer.oldname", $(this).is(':checked') ? " " : "");*/
	viewModel.set("customer.oldnameJp", $(this).is(':checked') ? " " : "");
});
//其他国家居民
$("#has_pr").change(function () {
    if ($(this).is(':checked')) {
        viewModel.addOne("customer.orthercountryJpList");
    } else {
        viewModel.clearAll("customer.orthercountryJpList");
    }
});






//信息保存
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#saveCustomerData").on("click",function(){
	if(validatable.validate()){
		console.log(JSON.stringify(viewModel.customer));
		/*var error=JSON.stringify(map);
		if(error.length>15){
			
			viewModel.set("customer.errorinfo",JSON.stringify(map));
			map.clear();
		}*/
		 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
		$.ajax({
			 type: "POST",
			 url: "/visa/newcustomerjp/customerSave",
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				 if(indexnew!=null){
						
						layer.close(index);
						}
				 
				 console.log(result);
				 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				 parent.layer.close(index);
				 window.parent.successCallback('1');
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
				 console.log(XMLHttpRequest);
				 console.log(textStatus);
				 console.log(errorThrown);
	            layer.msg('保存失败!',{time:2000});
	         }
		});
	}
	
});


//通过或者拒绝的方法
function agreeOrRefuse(flag){
	
	
	
	/* viewModel.set("customer.errorinfo",JSON.stringify(map));*/
	 var error=JSON.stringify(map);
	 map.clear();
	
	
	
	
	var id=viewModel.get("customer.id");
	$.ajax({
		 type: "POST",
		 url: "/visa/newcustomerjp/agreeOrRefuse?flag="+flag+"&customerid="+id+"&error="+error,
		 success: function (result){
			 console.log(result);
			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			 parent.layer.close(index);
			 window.parent.successCallback('3');
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
			 console.log(XMLHttpRequest);
			 console.log(textStatus);
			 console.log(errorThrown);
            layer.msg('操作失败!',{time:2000});
         }
	});
}

$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("cid");
    if (oid) {
        $.getJSON("/visa/newcustomerjp/showDetail?customerid=" + oid, function (resp) {
        	viewModel.set("customer", $.extend(true, dafaults, resp));
        	
      /*  	var reason=viewModel.get("customer.errorinfo");
        	var map=new Map();
        	map=eval("("+reason+")");
        	for (var key in map){
        		var a = map[key];//获取到 错误信息 数据
        		for(var i=0;i<a.length;i++){
        			var reasonnew=a[i].key;//获取到  错误信息 字段名称
        			
        			$('label').each(function(){
        				var labelText=$(this).text();//获取 页面上所有的字段 名称
        				labelText = labelText.split(":");
        				labelText.pop();
        				labelText = labelText.join(":");//截取 :之前的信息
        				for(var i=0;i<reasonnew.length;i++){
        					///console.log("labelText的值：==="+labelText);
        					///console.log("reasonnew[i]的值：==="+reasonnew);
        					if(labelText==reasonnew){
        						///console.log("labelText的值：==="+labelText);
            					///console.log("reasonnew[i]的值：==="+reasonnew);
        						$(this).next().find('input').css('border-color','#f17474');///input
        						$(this).next().find('.k-state-default').css('border-color','#f17474');//data(span)
        						$(this).next().find('.k-dropdown').css('border-color','#f17474');//select(span)
        						$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
        					}
        				}
        			});
        		}
        	}*/
        	
        	/*-------------------------小灯泡 回显---------------------*/
        	var reason=viewModel.get("customer.errorinfo");
        	var map=new Map();
        	map=eval("("+reason+")");
        	for (var key in map){
        		var a = map[key];//获取到 错误信息 数据
        		for(var i=0;i<a.length;i++){
        			var reasonnew=a[i].key;//获取到  错误信息 字段名称
        			
        			$('label').each(function(){
        				var labelText=$(this).text();//获取 页面上所有的字段 名称
        				labelText = labelText.split(":");
        				labelText.pop();
        				labelText = labelText.join(":");//截取 :之前的信息
        				for(var i=0;i<reasonnew.length;i++){
        					///console.log("labelText的值：==="+labelText);
        					///console.log("reasonnew[i]的值：==="+reasonnew);
        					if(labelText==reasonnew){
        						///console.log("labelText的值：==="+labelText);
            					///console.log("reasonnew[i]的值：==="+reasonnew);
        						$(this).next().find('input').css('border-color','#f17474');///input
        						$(this).next().find('.k-state-default').css('border-color','#f17474');//data(span)
        						$(this).next().find('.k-dropdown').css('border-color','#f17474');//select(span)
        						$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
        					}
        				}
        			});
        		}
        	}
        	
        	/*--------------------------end 小灯泡 回显----------------------*/
        	
        });
    }
});
