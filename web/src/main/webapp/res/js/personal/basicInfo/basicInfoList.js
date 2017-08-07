//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);

//美国基本信息曾用名根据中文自动转为拼音
function getPinYin(){
	var oldxing = $("#old_usrname_cn").val();
	var oldxingen = pinyinUtil.getPinyin(oldxing, '', false, false);
	viewModel.set("customer.oldname.oldxingen",oldxingen.toUpperCase());
	
	var oldname = $("#old_given_name_cn").val();
	var oldnameen = pinyinUtil.getPinyin(oldname, '', false, false);
	viewModel.set("customer.oldname.oldnameen",oldnameen.toUpperCase());
}

//页面加载时回显基本信息
window.onload = function(){
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
	var logintype1=$.queryString("logintype");
	var after;
	if(logintype1==5){
		var country = JSON.parse(unescape($.queryString("country")));
		after=localhostPaht +'/visa/basicinfo/listBasicinfo?logintype=5&customerId1='+country.id;
		$.getJSON(after, function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
			viewModel.set("customer.passporttype", 1);
			viewModel.set("customer.commhomeaddress.issuingCountry", "CHN");
			//预览 按钮
			var photoname= '<a href="#">'
				+ viewModel.get("customer.photoname")
				+ '</a>'
				var phoneurl=viewModel.get("customer.phoneurl");
			if(phoneurl!=null&&phoneurl!=''){
				$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
				$("#photoname").html(photoname);
			}
			$(document).on('click','#preview',function(){
				$('#light').css('display','block');
				$('#fade').css('display','block');
				var phoneurl=viewModel.get("customer.phoneurl");
				if(phoneurl!=null&&phoneurl!=''){
					$("#imgId").attr('src',phoneurl);
				}
			});
			//菊花加载完毕
			if(indexnew!=null){
				layer.close(indexnew);
			}
		});
	}else{
		after=localhostPaht +'/visa/basicinfo/listBasicinfo';
		$.getJSON(after, function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
			viewModel.set("customer.passporttype", 1);
			viewModel.set("customer.commhomeaddress.issuingCountry", "CHN");
			//预览 按钮
			var photoname= '<a href="#">'
				+ viewModel.get("customer.photoname")
				+ '</a>'
				var phoneurl=viewModel.get("customer.phoneurl");
			if(phoneurl!=null&&phoneurl!=''){
				$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
				$("#photoname").html(photoname);
			}
			$(document).on('click','#preview',function(){
				$('#light').css('display','block');
				$('#fade').css('display','block');
				var phoneurl=viewModel.get("customer.phoneurl");
				if(phoneurl!=null&&phoneurl!=''){
					$("#imgId").attr('src',phoneurl);
				}
			});
		});
		//菊花加载完毕
		if(indexnew!=null){
			layer.close(indexnew);
		}
	}
}

var firstPart ;
var secondPart ;
var thirdPart ;
var country;
var countrystatus;
//初始化各个组件
$(function(){
	//初始化上传按钮
	uploadFile();
	//$(".swfupload").attr("disabled","disabled");
	//$("#preview").attr("disabled",  "disabled");
	var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
		//$("#sex").kendoDropDownList({enable:false});//性别 状态 下拉框初始化
		$("#marital_status").kendoDropDownList({enable:false});//婚姻状况 下拉框初始化
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		//$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		//$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
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
			//$("#sex").data("kendoDropDownList").enable(true);//性别 状态为 可编辑
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
			//$("#sex").kendoDropDownList({enable:false});//性别 不可编辑
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
			//$("#sex").kendoDropDownList({enable:false});//性别 状态为 不可编辑
			$("#marital_status").kendoDropDownList({enable:false});//婚姻状况 不可编辑
			$("#birthDate").data("kendoDatePicker").enable(false);//出生日期 不可编辑
			$("#passporttype").kendoDropDownList({enable:false});//护照类型 不可编辑
		});
	}else if(aa == 1){//表示从签证进度跳转至此页面
		//$("#sex").kendoDropDownList({enable:true});//性别 状态 下拉框初始化
		$("#marital_status").kendoDropDownList({enable:true});//婚姻状况 下拉框初始化
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		//$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		//$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
		//隐藏编辑按钮
		$(".editBtn").hide();
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
	country = JSON.parse(unescape($.queryString("country")));
    countrystatus=$.queryString("countrystatus");
	/*-------------------------小灯泡 效果--------------------------*/
	/*firstPart = JSON.parse(unescape($.queryString("firstPart")));//获取 错误 信息
	secondPart = JSON.parse(unescape($.queryString("secondPart")));//获取 错误 信息
	thirdPart = JSON.parse(unescape($.queryString("thirdPart")));//获取 错误 信息
	$('label').each(function(){
			var labelText=$(this).text();//获取 页面上所有的字段 名称
			labelText = labelText.split(":");
			labelText.pop();
			labelText = labelText.join(":");//截取 :之前的信息
			if(secondPart!=null&&secondPart!=''){
				for(var i=0;i<secondPart.length;i++){
					//console.log(labelText+"==="+firstPart[i]);
					if(labelText==secondPart[i]){
						$(this).next().find('input').css('border-color','#f17474');
						$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
						$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
					}
				}
			}
	});*/
	/*-------------------------end 小灯泡 效果--------------------------*/
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
var passportnum=0;
var oldnamenum=0;
var authenticatorcodeNum=0;
var communicahomeaddressNum=0;
var viewModel = kendo.observable({
    customer: dafaults,
    countries:countries,
    passportTypeEnum:passportTypeEnum,
    customersourceEnum:customersourceEnum,
    states:states,
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
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
    	/*var oldPassport = viewModel.get("customer.passportlose");
    	console.log(JSON.stringify(oldPassport));*/
    	//var state = oldPassport ? oldPassport.length > 0 : false;
    	/*var a=viewModel.get("customer.passportlose.id");
    	if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(passportnum<4){
    			passportnum++;
    			return false;
    		}else {return true};
    	}
    	return false;*/
    	
    	var state = viewModel.get("customer.passportlose.passport") 
		|| viewModel.get("customer.passportlose.reason")
		|| viewModel.get("customer.passportlose.reasonen");
		//|| viewModel.get("customer.passportlose.sendcountry")
    	return state;
    },
    // 曾用名
    oldNameEnable: function () {
    	var oldnames = JSON.stringify(viewModel.get("customer.oldnameJp.oldname"));
    	console.log("曾用名:_____"+oldnames);
    	var state = viewModel.get("customer.oldname.oldname") 
    				|| viewModel.get("customer.oldname.oldnameen")
    				|| viewModel.get("customer.oldname.oldxing")
    				|| viewModel.get("customer.oldname.oldxingen");
    	return state;
    	/*var beforeName = viewModel.get("customer.oldname");
    	var state = beforeName ? beforeName.length > 0 : false;
        return state;*/
    	/*var a=viewModel.get("customer.oldname.id");
    	if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(oldnamenum<4){
    			oldnamenum++;
    			return false;
    		}else {return true};
    	}
    	return false; // 07-27*/
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = viewModel.get("customer.orthercountrylist");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
    //美国纳税人认证码
    usaAuthenticatorCode:function(){
    	var country=viewModel.get("customer.taxpayerauthenticat.country");
    	if(country=="CHN"){
    		country = "";
    	}else{
    		country = viewModel.get("customer.taxpayerauthenticat.country");
    	}
    	var state = viewModel.get("customer.taxpayerauthenticat.city") 
					|| country
					|| viewModel.get("customer.taxpayerauthenticat.homeAddress")
					|| viewModel.get("customer.taxpayerauthenticat.postCode")
					|| viewModel.get("customer.taxpayerauthenticat.province");
    	return state;
    	/*var authenticationCode=viewModel.get("customer.taxpayerauthenticat");
    	var state = authenticationCode ? authenticationCode.length > 0 : false;
    	return state;*/
    	/*var a=viewModel.get("customer.taxpayerauthenticat.id");
    	if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(authenticatorcodeNum<4){
    			authenticatorcodeNum++;
    			return false;
    		}else {return true};
    	}
    	return false;*/
    	
    },
    //通信地址与家庭地址是否一致
    usaCommunicaHomeAddress:function(){
    	/*var addressUnified=viewModel.get("customer.commhomeaddress");
    	var state = addressUnified ? addressUnified.length > 0 : false;
    	return state;*/
    	//var a=viewModel.get("customer.commhomeaddress");
    	/*if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(communicahomeaddressNum<4){
    			communicahomeaddressNum++;
    			return false;
    		}else {return true};
    	}
    	return false;*/
    	/*var issuingCountry=viewModel.get("customer.commhomeaddress.issuingCountry");
    	if(issuingCountry=="CHN"){
    		return issuingCountry=undefined;
    	}else{
    		return issuingCountry;
    	}*/
    	var issuingCountry=viewModel.get("customer.commhomeaddress.issuingCountry");
    	if(issuingCountry=="CHN"){
    		issuingCountry = "";
    	}else{
    		issuingCountry = viewModel.get("customer.commhomeaddress.issuingCountry");
    	}
    	var passportType = viewModel.get("customer.commhomeaddress.passportType");
    	if(passportType==1){
    		passportType = "";
    	}else{
    		passportType = viewModel.get("customer.commhomeaddress.passportType");
    	}
    	var state = viewModel.get("customer.commhomeaddress.mainPhoneNum") 
		|| viewModel.get("customer.commhomeaddress.minorPhoneNum")//undefined
		|| viewModel.get("customer.commhomeaddress.workPhoneNum")//undefined
		|| viewModel.get("customer.commhomeaddress.email")
		|| issuingCountry
		|| passportType;
    	//|| viewModel.get("customer.commhomeaddress.issuingCountry")
    	//alert(state);
    	return state;
    }
});
kendo.bind($(document.body), viewModel);//数据绑定结束

//丢过护照
$("#pp_lost").change(function () {
	//viewModel.set("customer.passportlose", $(this).is(':checked') ? " " : "");
	/*var a={"sendcountry":"CHN","customerid":0,"id":0,"passport":"","reason":"","reasonen":""};
	viewModel.set("customer.passportlose", $(this).is(':checked') ? a : "");*/
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.passportlose.passport", value);
    viewModel.set("customer.passportlose.reason", value);
    viewModel.set("customer.passportlose.reasonen", value);
    //viewModel.set("customer.passportlose.sendcountry", value);
});
//曾用名
$("#has_used_name").change(function () {
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.oldname.oldname", value);
    viewModel.set("customer.oldname.oldnameen", value);
    viewModel.set("customer.oldname.oldxing", value);
    viewModel.set("customer.oldname.oldxingen", value);
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
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.taxpayerauthenticat.city", value);
    viewModel.set("customer.taxpayerauthenticat.country", "CHN");
    viewModel.set("customer.taxpayerauthenticat.homeAddress", value);
    viewModel.set("customer.taxpayerauthenticat.postCode", value);
    viewModel.set("customer.taxpayerauthenticat.province", value);
	///viewModel.set("customer.authenticatorcode", $(this).is(':checked') ? " " : "");
	/*var a={"city":"","country":"","createTime":null,"customerId":0,"homeAddress":"","id":0,"postCode":"","province":"","remark":"","status":0,"updateTime":null};
	var b={"city":"","country":"","createTime":null,"customerId":0,"homeAddress":"","id":-1,"postCode":"","province":"","remark":"","status":0,"updateTime":null};
	viewModel.set("customer.taxpayerauthenticat", $(this).is(':checked') ? a : b);*/
});
//通信地址与家庭地址是否一致
$("#communica_home_address").change(function () {
	//viewModel.set("customer.commhomeaddress", $(this).is(':checked') ? " " : "");
	/*var a={"createTime":null,"customerId":0,"email":"","expirationDate":null,"id":0,"issueDate":null,"issuingCity":"","issuingCountry":"CHN","issuingLocalCountry":"","issuingProvince":"","mainPhoneNum":"","minorPhoneNum":"","passportBookNum":"","passportNumber":"","passportType":0,"remark":"","status":0,"updateTime":null,"workPhoneNum":""};
	var b={"createTime":null,"customerId":0,"email":"","expirationDate":null,"id":-1,"issueDate":null,"issuingCity":"","issuingCountry":"CHN","issuingLocalCountry":"","issuingProvince":"","mainPhoneNum":"","minorPhoneNum":"","passportBookNum":"","passportNumber":"","passportType":0,"remark":"","status":0,"updateTime":null,"workPhoneNum":""};
	viewModel.set("customer.commhomeaddress", $(this).is(':checked') ? a : b);*/
	
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.commhomeaddress.mainPhoneNum", value);
    viewModel.set("customer.commhomeaddress.minorPhoneNum", value);
    viewModel.set("customer.commhomeaddress.workPhoneNum", value);
    viewModel.set("customer.commhomeaddress.email", value);
    viewModel.set("customer.commhomeaddress.issuingCity", value);
    viewModel.set("customer.commhomeaddress.issuingCountry", "CHN");
    viewModel.set("customer.commhomeaddress.passportType", 1);
	
});
/*------------------------------------------------end container---------------------------------------------------*/

/**********************************************
*编辑保存基本信息
**********************************************/
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
function saveBaseInfoData(){
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
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
		//菊花加载完毕
		if(indexnew!=null){
			layer.close(indexnew);
		}
	}else{
		//验证————————————————————————————————————
	    $('.k-tooltip-validation').each(function(){
	    	var verificationText=$(this).text().trim();//获取验证的文字信息
	    	var labelVal=$(this).parents('.form-group').find('label').text();//获取验证信息 对应的label名称
	    	labelVal = labelVal.split(":");
	    	labelVal.pop();
	    	labelVal = labelVal.join(":");//截取 :之前的信息
	    	var person=new Object();
	    	person.text=labelVal;
	    	person.error="";
	    	if(verificationText.indexOf("不能为空")>0){
	    		emptyNum.push(person);
	    	}else{
	    		errorNum.push(person);
	    	}
	    	///console.log("-获取验证的文字信息是："+verificationText+"                -获取验证信息 对应的label名称是："+labelVal);
	    });
	    //菊花加载完毕
		if(indexnew!=null){
			layer.close(indexnew);
		}
	    //end 验证————————————————————————————————
		var str="";
		if(emptyNum.length>0){
			for(var i=0;i<emptyNum.length;i++){
				str+=emptyNum[i].text+",";
			}
			str+="不能为空！"
		}
		if(errorNum.length>0){
			for(var i=0;i<errorNum.length;i++){
				str+=errorNum[i].text+",";
			}
			str+="格式不正确！";
		}
		$.layer.alert(str);
		//用完清空
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
	}
}
var logintype;
//点击下一步时跳转至签证信息
$("#nextStepBtn").click(function(){
	logintype=$.queryString("logintype");
	var after;
	var before;
	if(logintype==5){
		after="/visa/basicinfo/updateBaseInfoData?logintype=5";
		before='/personal/visaInfo/visaInfoList.html?logintype=5&typeId=1&firstPart='
			  +escape(JSON.stringify(firstPart))+"&secondPart="
			  +escape(JSON.stringify(secondPart))+"&thirdPart="
			  +escape(JSON.stringify(thirdPart))+"&country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus+"&orderId="+$.queryString('orderId');
	}else{
		after="/visa/basicinfo/updateBaseInfoData";
		before='/personal/visaInfo/visaInfoList.html?typeId=1&firstPart='
			  +escape(JSON.stringify(firstPart))+"&secondPart="
			  +escape(JSON.stringify(secondPart))+"&thirdPart="
			  +escape(JSON.stringify(thirdPart))+"&country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
	}
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: after,
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				layer.msg("操作成功",{time:2000});
				window.location.href=before;
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
	             layer.msg('操作失败',{time:2000});
	         }
		});
	}else{
		//验证————————————————————————————————————
	    $('.k-tooltip-validation').each(function(){
	    	var verificationText=$(this).text().trim();//获取验证的文字信息
	    	var labelVal=$(this).parents('.form-group').find('label').text();//获取验证信息 对应的label名称
	    	labelVal = labelVal.split(":");
	    	labelVal.pop();
	    	labelVal = labelVal.join(":");//截取 :之前的信息
	    	var person=new Object();
	    	person.text=labelVal;
	    	person.error="";
	    	if(verificationText.indexOf("不能为空")>0){
	    		emptyNum.push(person);
	    	}else{
	    		errorNum.push(person);
	    	}
	    });
	    //end 验证————————————————————————————————
		var str="";
		if(emptyNum.length>0){
			for(var i=0;i<emptyNum.length;i++){
				str+=emptyNum[i].text+",";
			}
			str+="不能为空！"
		}
		if(errorNum.length>0){
			for(var i=0;i<errorNum.length;i++){
				str+=errorNum[i].text+",";
			}
			str+="格式不正确！";
		}
		$.layer.alert(str);
		//用完清空
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
	}
});
//文件上传
function uploadFile(){
	var index=null;
	$.fileupload1 = $('#uploadFile').uploadify({
		'auto' : true,//选择文件后自动上传
		'formData' : {
			'fcharset' : 'uft-8'
		},
		'buttonText' : '上传',//按钮显示的文字
		'fileSizeLimit' : '3000MB',
		'fileTypeDesc' : '文件',//在浏览窗口底部的文件类型下拉菜单中显示的文本
		 'fileTypeExts' : '*.png;*.jpg; *.jpeg; *.gif;',//上传文件的类型
		'swf' : '/res/upload/uploadify.swf',//指定swf文件
		'multi' : false,//multi设置为true将允许多文件上传
		'successTimeout' : 1800, 
		'uploader' : localhostPaht+'/visa/passportinfo/uploadFile',
		//下面的例子演示如何获取到vid
		'onUploadStart':function(file){
			index = layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
		},
		 'onUploadSuccess': function(file, data, response) {
			 var fileName = file.name;//文件名称
			 var photoname= '<a id="downloadA"  href="#">'
		            + viewModel.get("customer.photoname")
		            + '</a>'
			 if(index!=null){
				layer.close(index);
			 }
			 /*显示 预览 按钮*/
		    viewModel.set("customer.phoneurl",data);
		    viewModel.set("customer.photoname",photoname);
            $("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
            $(document).on('click','#preview',function(){
	           	$('#light').css('display','block');
	           	$('#fade').css('display','block');
	           	var phoneurl=viewModel.get("customer.phoneurl");
	           	if(phoneurl!=null&&phoneurl!=''){
	           		$("#imgId").attr('src',phoneurl);
	           	}
           });
		},
       //加上此句会重写onSelectError方法【需要重写的事件】
       'overrideEvents': ['onSelectError', 'onDialogClose'],
       //返回一个错误，选择文件的时候触发
       'onSelectError':function(file, errorCode, errorMsg){
       	//console.log(errorMsg);
           switch(errorCode) {
               case -110:
                   alert("文件 ["+file.name+"] 大小超出系统限制！");
                   break;
               case -120:
                   alert("文件 ["+file.name+"] 大小异常！");
                   break;
               case -130:
                   alert("文件 ["+file.name+"] 类型不正确！");
                   break;
           }
       },
       'onUploadError':function(file, errorCode, errorMsg, errorString){
       }
	});
}
