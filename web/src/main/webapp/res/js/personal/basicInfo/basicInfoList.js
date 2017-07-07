//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显基本信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/basicinfo/listBasicinfo', function (resp) {
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     });
}
//初始化各个组件
$(function(){
	var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
		$("#sex").kendoDropDownList({enable:false});//性别 状态 下拉框初始化
		$("#marital_status").kendoDropDownList({enable:false});//婚姻状况 下拉框初始化
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
		$("#birthDate").data("kendoDatePicker").enable(false);//出生日期 不可编辑
		$("#passporttype").kendoDropDownList({enable:false});//护照类型 不可编辑
		$("#s_birthday").data("kendoDatePicker").enable(false);//结婚日期 不可编辑
		//操作 编辑 按钮时
		$(".editBtn").click(function(){
			$(this).addClass("hide");//编辑 按钮隐藏
			$(".cancelBtn").removeClass("hide");//取消 按钮显示
			$(".saveBtn").removeClass("hide");//保存 按钮显示
			$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
			$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
			$("#sex").data("kendoDropDownList").enable(true);//性别 状态为 可编辑
			$("#marital_status").data("kendoDropDownList").enable(true);//婚姻状况 状态为 可编辑
			$("#birthDate").data("kendoDatePicker").enable(true);//出生日期 可编辑
			$("#passporttype").data("kendoDropDownList").enable(true);//护照类型 可编辑
			$("#s_marriage_date").data("kendoDatePicker").enable(true);//结婚日期可编辑
			$("#s_birthday").data("kendoDatePicker").enable(true);//结婚日期 不可编辑
		});
		
		//操作 取消 按钮时
		$(".cancelBtn").click(function(){
			$(this).addClass("hide");//取消 按钮隐藏
			$(".saveBtn").addClass("hide");//保存 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			$("#sex").kendoDropDownList({enable:false});//性别 不可编辑
			$("#marital_status").kendoDropDownList({enable:false});//婚姻状况 状态为 不可编辑
			$("#birthDate").data("kendoDatePicker").enable(false);//出生日期 不可编辑
			$("#s_marriage_date").data("kendoDatePicker").enable(false);//结婚日期 不可编辑
			$("#s_birthday").data("kendoDatePicker").enable(false);//结婚日期 不可编辑
			$("#passporttype").kendoDropDownList({enable:false});//护照类型 不可编辑
		});
		
		//操作 保存 按钮时
		$(".saveBtn").click(function(){
			$(this).addClass("hide");//保存 按钮隐藏
			$(".cancelBtn").addClass("hide");//取消 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			$("#sex").kendoDropDownList({enable:false});//性别 状态为 不可编辑
			$("#marital_status").kendoDropDownList({enable:false});//婚姻状况 不可编辑
			$("#birthDate").data("kendoDatePicker").enable(false);//出生日期 不可编辑
			$("#passporttype").kendoDropDownList({enable:false});//护照类型 不可编辑
		});
	}else if(aa == 1){//表示从签证进度跳转至此页面
		$("#sex").kendoDropDownList({enable:true});//性别 状态 下拉框初始化
		$("#marital_status").kendoDropDownList({enable:true});//婚姻状况 下拉框初始化
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
		//隐藏编辑按钮
		$(".editBtn").hide();
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
});
/*------------------------------------------------container---------------------------------------------------*/
//客户来源
var customersourceEnum=[
    {text:"线上",value:1},
    {text:"OTS",value:2},
    {text:"直客",value:3},
    {text:"线下",value:4}
  ];
function translateZhToEn(from, to) {
    $.getJSON("/translate/google", {q: $(from).val()}, function (result) {
        $("#" + to).val(result.data).change();
    });
}
var countries = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/country.json",
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
			passportlose:{},
			oldname:{},
			authenticatorcode:{},
			communicahomeaddress:{},
			orthercountrylist:[],
			father:{},
			mother:{},
			relation:[],
			spouse:{},
			usainfo:{},
			teachinfo:[],
			recentlyintousalist:[],
			workinfo:{},
			oldworkslist:[],
			languagelist:[],
			visitedcountrylist:[],
			workedplacelist:[],
			army:{},
			othernationality:[],
			socialinsurancenum:{},
			taxpayerauthenticat:{},
			commhomeaddress:{}
    },
    keys = {
		"customer.orthercountrylist":{},
		"customer.recentlyintousalist":{},
		"customer.oldworkslist":{},
		"customer.languagelist":{},
		"customer.visitedcountrylist":{},
		"customer.workedplacelist":{},
		"customer.relation":{
			xing:""
		},
		"customer.teachinfo":{},
		"customer.othernationality":{},
		"customer.socialinsurancenum":{},
		"customer.taxpayerauthenticat":{},
		"customer.commhomeaddress":{}
	}
;
//护照类型
var passportTypeEnum=[
    {text:"旅游护照",value:1}
  ];
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
    customer: dafaults,
    countries:countries,
    passportTypeEnum:passportTypeEnum,
    customersourceEnum:customersourceEnum,
    states:states,
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
        console.log(key);
        viewModel.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    // 是否有同行人
    hasTogether: function () {
    	var togethers = viewModel.get("customer.peerList");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;
    },
    clearAll: function (key) {
        var all = viewModel.get(key);
        if (all) all.splice(0, all.length);
    },
    // 支付人
    payType: function (type) {
        return viewModel.get("customer.trip.paypersion") === type;
    },
    // 婚姻状态
    spouseState: function (state) {
    	var marriage = viewModel.get("customer.spouse.marrystatus");
    	return state.indexOf(marriage) > -1;
    },
    //是否与配偶一起
    sameAsMe: function () {
        return viewModel.get("customer.spouse.spousezipcode")
            && viewModel.get("customer.spouse.spouselinkaddress")
            && viewModel.get("customer.spouse.spouselinkaddressen");
    },
    //是否有直系亲属在美国
    hasFamilyInUSA: function () {
        var families = viewModel.get("customer.relation");
        console.log(families);
        var state = families ? families.length > 0 : false;
        return state;
    },
    //历史入境信息
    hasHistory: function () {
        var history = viewModel.get("customer.recentlyintousalist");
        var state = history ? history.length > 0 : false;
        return state;
    },
    //是否有美国驾照
    hasDriving: function () {
        var state = viewModel.get("customer.usainfo.usadriveport");
        return state;
    },
    //是否申请过移民
    hasImmigrant: function () {
        var state = viewModel.get("customer.usainfo.instruction") || viewModel.get("customer.usainfo.instructionen");
        return state;
    },
    //是否有就签证
    hasOldVisa: function () {
        var state = viewModel.get("customer.usainfo.visaport");
        return state;
    },
    //教育信息
    hasSchool: function () {
        var schools = viewModel.get("customer.teachinfo");
        var state = schools ? schools.length > 0 : false;
        return state;
    },
    //工作信息详情
    hasWorkDetail: function (state) {
        var industry = viewModel.get("customer.workinfo.jobstatus");
        return !(industry === "S" || industry === "RT" || industry === "N");
    },
    add: function (key) {
    	viewModel.set(key, keys[key]);
    },
    clear: function (key) {
    	viewModel.set(key, undefined);
    },
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
    // 旧护照
    oldPassportEnable: function () {
        return viewModel.get("customer.passportlose");
    },
    // 曾用名
    oldNameEnable: function () {
        return viewModel.get("customer.oldname");
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = viewModel.get("customer.orthercountrylist");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
    //美国纳税人认证码
    usaAuthenticatorCode:function(){
    	return viewModel.get("customer.authenticatorcode");
    },
    //通信地址与家庭地址是否一致
    usaCommunicaHomeAddress:function(){
    	return viewModel.get("customer.communicahomeaddress");
    }
});
kendo.bind($(document.body), viewModel);//数据绑定结束

//丢过护照
$("#pp_lost").change(function () {
	viewModel.set("customer.passportlose", $(this).is(':checked') ? " " : "");
});
//曾用名
$("#has_used_name").change(function () {
	viewModel.set("customer.oldname", $(this).is(':checked') ? " " : "");
});
//其他国家居民
$("#has_pr").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.addOne("customer.orthercountrylist");
    } else {
    	viewModel.clearAll("customer.orthercountrylist");
    }
});
/*****************************************************
 * 配偶信息
 ****************************************************/
$("#same_as_home").change(function () {
    var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.spouse.spousezipcode", value);
    viewModel.set("customer.spouse.spouselinkaddress", value);
    viewModel.set("customer.spouse.spouselinkaddressen", value);
});
//美国纳税人认证码
$("#usa_authenticator_code").change(function () {
	viewModel.set("customer.authenticatorcode", $(this).is(':checked') ? " " : "");
});
//通信地址与家庭地址是否一致
$("#communica_home_address").change(function () {
	viewModel.set("customer.communicahomeaddress", $(this).is(':checked') ? " " : "");
});
/*------------------------------------------------end container---------------------------------------------------*/
/**********************************************
*编辑保存基本信息
**********************************************/
function saveBaseInfoData(){
	$.ajax({
		 type: "POST",
		 url: "/visa/basicinfo/updateBaseInfoData",
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
//点击下一步时跳转至签证信息
$("#nextStepBtn").click(function(){
	$.ajax({
		 type: "POST",
		 url: "/visa/basicinfo/updateBaseInfoData",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			layer.msg("操作成功",{time:2000});
			window.location.href='/personal/visaInfo/visaInfoList.html?typeId=1';
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
             layer.msg('操作失败',{time:2000});
         }
	});
});