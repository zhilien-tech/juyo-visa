//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显基本信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/basicinfo/basicJPInfoList', function (resp){
		var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     	//菊花加载完毕
		if(indexnew!=null){
			layer.close(indexnew);
		}
     	//预览 按钮
   	   /*var phoneurl=viewModel.get("customer.phoneurl");
    	 if(phoneurl!=null&&phoneurl!=''){
    		$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
    	 }
        $(document).on('click','#preview',function(){
        	$('#light').css('display','block');
        	$('#fade').css('display','block');
        	var phoneurl=viewModel.get("customer.phoneurl");
        	if(phoneurl!=null&&phoneurl!=''){
        		$("#imgId").attr('src',phoneurl);
        	}
        });*/
     });
}
var firstPartJP;
var secondPartJP ;
var thirdPartJP ;
var country;
var countrystatus;
//初始化各个组件
$(function(){
	//初始化上传按钮
	uploadFile();
	
	var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
		//$("#sex").kendoDropDownList({enable:false});//性别 状态 下拉框初始化不可编辑
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		$("#birthDate").data("kendoDatePicker").enable(false);//出生日期 不可编辑
		/*$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
		$("#validDate").data("kendoDatePicker").enable(false);//有效期限不可编辑*/
		//操作 编辑 按钮时
		$(".editBtn").click(function(){
			$(this).addClass("hide");//编辑 按钮隐藏
			$(".cancelBtn").removeClass("hide");//取消 按钮显示
			$(".saveBtn").removeClass("hide");//保存 按钮显示
			//$("#sex").data("kendoDropDownList").enable(true);//性别 状态 下拉框初始化可编辑
			$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
			$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
			$("#birthDate").data("kendoDatePicker").enable(true);//出生日期 不可编辑
			//$("#signedDate").data("kendoDatePicker").enable(true);//签发日期 不可编辑
			//$("#validDate").data("kendoDatePicker").enable(true);//有效期限不可编辑
		});
		
		//操作 取消 按钮时
		$(".cancelBtn").click(function(){
			$(this).addClass("hide");//取消 按钮隐藏
			$(".saveBtn").addClass("hide");//保存 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			//$("#sex").data("kendoDropDownList").enable(false);//性别 状态 下拉框初始化不可编辑
		});
		
		//操作 保存 按钮时
		$(".saveBtn").click(function(){
			$(this).addClass("hide");//保存 按钮隐藏
			$(".cancelBtn").addClass("hide");//取消 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
		});
	}else if(aa == 1){
		//隐藏编辑按钮
		$(".editBtn").hide();
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		//$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		//$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期限
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
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
			for(var i=0;i<secondPartJP.length;i++){
				if(labelText==secondPartJP[i]){
					$(this).next().find('input').css('border-color','#f17474');
					$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
					$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
				}
			}
	}); */
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
			oldnameJp:{},
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
			taxpayerauthenticat:{},//日本纳税人认证码
			commhomeaddress:{},
			financeJpList:[],
			recentlyintojpJpList: []
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
		"customer.financeJpList":{},
		"customer.recentlyintojpJpList":{}
	}
;

//护照类型
var passportTypeEnum=[
    {text:"旅游护照",value:1}
  ];
/*****************************************************
 * 数据绑定
 ****************************************************/
var taxpayerauthenticatNum=0;
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
    	/*var oldPassportEnable = viewModel.get("customer.passportlose");
        var state = oldPassportEnable ? oldPassportEnable.length > 0 : false;
        return state;*/
        //return viewModel.get("customer.passportlose");
    	var state = viewModel.get("customer.passportlose.passport") 
		|| viewModel.get("customer.passportlose.reason")
		|| viewModel.get("customer.passportlose.reasonen");
		//|| viewModel.get("customer.passportlose.sendcountry")
    	return state;
    },
    // 曾用名
    oldNameEnable: function () {
    	/*var oldNameEnable = viewModel.get("customer.oldname");
        var state = oldNameEnable ? oldNameEnable.length > 0 : false;
        return state;*/
        ///return viewModel.get("customer.oldname");
    	/*var oldnames = JSON.stringify(viewModel.get("customer.oldnameJp.oldname"));
    	var oldnameens =JSON.stringify(viewModel.get("customer.oldnameJp.oldnameen"));
    	var oldxings =JSON.stringify(viewModel.get("customer.oldnameJp.oldxing"));
    	var oldxingens =JSON.stringify(viewModel.get("customer.oldnameJp.oldxingen"));
    	if(oldnames==" " || oldnameens==" " || oldxings==" " || oldxingens==" "){
    		oldnames = oldnames.replace(/\s+/g,"");
    		oldnameens = oldnameens.replace(/\s+/g,"");
    		oldxings = oldxings.replace(/\s+/g,"");
    		oldxingens = oldxingens.replace(/\s+/g,"");
    	}
    	console.log(oldnames);*/
    	var oldnames = JSON.stringify(viewModel.get("customer.oldnameJp.oldname"));
    	console.log("曾用名:_____"+oldnames);
    	var state = viewModel.get("customer.oldnameJp.oldname")
		|| viewModel.get("customer.oldnameJp.oldnameen")
		|| viewModel.get("customer.oldnameJp.oldxing")
		|| viewModel.get("customer.oldnameJp.oldxingen");
    	//开关下面字段验证
    	if(state){
    		$("input[oldname='oldname']").each(function(){
    			var labelTxt=$(this).parent().prev().text().trim();
    			labelTxt = labelTxt.split(":");
    			labelTxt.pop();
    			labelTxt = labelTxt.join(":");
				$(this).attr('validationMessage',labelTxt+"不能为空");
    			$(this).attr('required','required');
    		});
    	}else{
    		$("input[oldname='oldname']").each(function(){
    			var labelTxt=$(this).parent().prev().text().trim();
    			labelTxt = labelTxt.split(":");
    			labelTxt.pop();
    			labelTxt = labelTxt.join(":");
				$(this).removeAttr('validationMessage',labelTxt+"不能为空");
    			$(this).removeAttr('required','required');
    		});
    	}
    	return state;
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = viewModel.get("customer.orthercountrylist");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
    //日本纳税人认证码
    jpAuthenticatorCode:function(){
    	/*var jpAuthenticatorCode = viewModel.get("customer.taxpayerauthenticat");
        var state = jpAuthenticatorCode ? jpAuthenticatorCode.length > 0 : false;
        return state;*/
    	///return viewModel.get("customer.taxpayerauthenticat");
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
    	
    	/*var a=viewModel.get("customer.taxpayerauthenticat.id");
    	if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(taxpayerauthenticatNum<4){
    			taxpayerauthenticatNum++;
    			return false;
    		}else {return true};
    	}
    	return false;*/
    	
    },
    //通信地址与家庭地址是否一致
    usaCommunicaHomeAddress:function(){
    	/*var usaCommunicaHomeAddress = viewModel.get("customer.communicahomeaddress");
        var state = usaCommunicaHomeAddress ? usaCommunicaHomeAddress.length > 0 : false;
        return state;*/
    	///return viewModel.get("customer.communicahomeaddress");
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
		|| viewModel.get("customer.commhomeaddress.issuingCity")
		|| passportType
    	|| issuingCountry;
    	return state;
    }
});
kendo.bind($(document.body), viewModel);//数据绑定结束

//护照是否遗失过或被偷过
$("#pp_lost").change(function () {
	//viewModel.set("customer.passportlose", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.passportlose.sendcountry", "CHN");
    viewModel.set("customer.passportlose.passport", value);
    viewModel.set("customer.passportlose.reason", value);
    viewModel.set("customer.passportlose.reasonen", value);
});
//曾用名
$("#has_used_name").change(function () {
//	viewModel.set("customer.oldname", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.oldnameJp.oldname", value);
    viewModel.set("customer.oldnameJp.oldnameen", value);
    viewModel.set("customer.oldnameJp.oldxing", value);
    viewModel.set("customer.oldnameJp.oldxingen", value);
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
//日本纳税人认证码
$("#jp_authenticator_code").change(function () {
	//viewModel.set("customer.taxpayerauthenticat", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.taxpayerauthenticat.city", value);
    viewModel.set("customer.taxpayerauthenticat.country", "CHN");
    viewModel.set("customer.taxpayerauthenticat.homeAddress", value);
    viewModel.set("customer.taxpayerauthenticat.postCode", value);
    viewModel.set("customer.taxpayerauthenticat.province", value);
    
    /*var a={"city":"","country":"CHN","createTime":null,"customerId":0,"homeAddress":"","id":0,"postCode":"","province":"","remark":"","status":0,"updateTime":null};
	var b={"city":"","country":"CHN","createTime":null,"customerId":0,"homeAddress":"","id":-1,"postCode":"","province":"","remark":"","status":0,"updateTime":null};
	viewModel.set("customer.taxpayerauthenticat", $(this).is(':checked') ? a : b);*/
});
//通信地址与家庭地址是否一致
$("#communica_home_address").change(function () {
	//viewModel.set("customer.communicahomeaddress", $(this).is(':checked') ? " " : "");
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
*编辑保存日本基本信息
**********************************************/
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];
//护照信息编辑保存
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
function updateBaseJPInfoData(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: "/visa/basicinfo/updateBaseJPInfoData",
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				layer.msg("保存成功",{time:2000});
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
	            layer.msg('保存失败',{time:2000});
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
}
//点击下一步时跳转至日本签证信息
$("#nextStepBtn").click(function(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: "/visa/basicinfo/updateBaseJPInfoData",
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				layer.msg("操作成功",{time:2000});
				window.location.href='/personal/visaInfo/JPvisaInfoList.html?firstPartJP='
					  +escape(JSON.stringify(firstPartJP))+"&secondPartJP="
					  +escape(JSON.stringify(secondPartJP))+"&thirdPartJP="
					  +escape(JSON.stringify(thirdPartJP))+"&typeId=1"+"&country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
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
			 if(index!=null){
				layer.close(index);
			 }
			 /*显示 预览 按钮*/
		    viewModel.set("customer.phoneurl",data);
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