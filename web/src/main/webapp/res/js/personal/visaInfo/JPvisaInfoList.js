//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显基本信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/visainfo/listjpvisainfo', function (resp) {
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     });
}
var firstPartJP;
var secondPartJP ;
var thirdPartJP ;
var country;
var countrystatus;
//初始化各个组件
$(function(){
	//折叠板 效果初始化
    $("#panelbar").kendoPanelBar({
         expandMode: "single" //设置展开模式只能展开单个
    });
	
    var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
		$(".input-group input").attr("k-state-disabled");
		$(".input-group input").addClass("k-state-disabled");
	}else if(aa == 1){
		//隐藏编辑按钮
		$(".editBtn").hide();
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
    
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
	country = JSON.parse(unescape($.queryString("country")));
    countrystatus=$.queryString("countrystatus");
	/*-------------------------小灯泡 效果--------------------------*/
	/*firstPartJP = JSON.parse(unescape($.queryString("firstPartJP")));//获取 错误 信息
	secondPartJP = JSON.parse(unescape($.queryString("secondPartJP")));//获取 错误 信息
	thirdPartJP = JSON.parse(unescape($.queryString("thirdPartJP")));//获取 错误 信息
	$('label').each(function(){
			
			var labelText=$(this).text();//获取 页面上所有的字段 名称
			labelText = labelText.split(":");
			labelText.pop();
			labelText = labelText.join(":");//截取 :之前的信息
			
			for(var i=0;i<thirdPartJP.length;i++){
				if(labelText==thirdPartJP[i]){
					$(this).next().find('input').css('border-color','#f17474');
					$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
					$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
				}
			}
	});*/
	/*-------------------------end 小灯泡 效果--------------------------*/
	
	
});
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
		workinfoJp: {},//工作信息
		financeJpList:[],//财务信息
		oldpassportJp: {},
		oldnameJp: {},
		orthercountryJpList: [],
		recentlyintojpJpList: []
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
        return viewModel.get("customer.oldpassportJp");
    },
    // 曾用名
    oldNameEnable: function () {
        return viewModel.get("customer.oldnameJp");
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = viewModel.get("customer.orthercountryJpList");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
    customer: dafaults,
});
kendo.bind($(document.body), viewModel);
/*****************************************************
 * 开始个人信息
 ****************************************************/

//丢过护照
$("#pp_lost").change(function () {
	viewviewModel.set("customer.passportlose", $(this).is(':checked') ? " " : "");
});
//曾用名
$("#has_used_name").change(function () {
   if ($(this).is(':checked')) {
        viewModel.add("customer.oldName");
    } else {
        viewModel.clear("customer.oldName");
    }
});
//其他国家居民
$("#has_pr").change(function () {
    if ($(this).is(':checked')) {
        viewModel.addOne("customer.orthercountryJpList");
    } else {
        viewModel.clearAll("customer.orthercountryJpList");
    }
});
/**********************************************
*编辑保存日本签证信息
**********************************************/
function updateVisaInfoJPSave(){
	$.ajax({
		 type: "POST",
		 url: "/visa/visainfo/updateVisaInfoJPSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			layer.msg("保存成功",{time:2000});
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
             layer.msg('保存失败',{time:2000});
         }
	});
}
//点击保存时
$("#nextStepBtn").click(function(){
	$.ajax({
		 type: "POST",
		 url: "/visa/visainfo/updateVisaInfoJPSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			layer.msg("保存成功",{time:2000});
			 window.location.href='/myvisa/transactVisa/visaProgressImg.html?country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
             layer.msg('保存失败',{time:2000});
         }
	});
});