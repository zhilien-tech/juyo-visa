//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显签证信息
/*window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/visainfo/listvisainfo', function (resp) {
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     });
}*/
var firstPart ;
var secondPart ;
var thirdPart ;
var country;
var countrystatus;

//美国签证信息同行人员根据中文自动转为拼音
/*function getPinYin(zz){
	var divParent = $(zz).parent().parent().parent();
	var divBrother = divParent.next();
	var pinYinInput = divBrother.find('input');
	var pinYinName = pinYinInput.attr("pinYinName");
	var chinesexing = $(zz).val();
	var chinesexingen = pinyinUtil.getPinyin(chinesexing, '', false, false);
	//得到list对象
	var listobj = viewModel.get("customer.peerList");
	var aa = $(zz).parent().parent().parent().next().find("input");
	var val1=$(zz).val();
	if(val1!=''&&val1!=null){
		var bb = pinyinUtil.getPinyin(val1, '', false, false);
		var val2=bb.toUpperCase();
		aa.val(val2);
		for(var i=0;i<listobj.length;i++){
			console.log("aa:"+aa.val());
			alert(val1);
			alert(listobj[i].peerxing);
			console.log("listobj:"+listobj[i].peerxing);
			if(aa.val() == listobj[i].peerxing){
				viewModel.set("customer.peerList["+i+"].peerxingen",val2);
			}
		}
		console.log(JSON.stringify(viewModel.get("customer.peerList")));
	}else{
		aa.val("");
		for(var i=0;i<listobj.length;i++){
			if(aa.peerxing == listobj[i].peerxing){
				viewModel.set("customer.peerList["+i+"].peerxingen","");
			}
		}
	}
}*/

//初始化页面组件
$(function () {
	//页面加载时回显签证信息
	var logintype1=$.queryString("logintype");
	var after;
	if(logintype1==5){
		var country = JSON.parse(unescape($.queryString("country")));
		/*$.ajax({
			 type: "POST",
			 url: '/visa/visainfo/listvisainfo?customerId1='+country.id,
			 contentType:"application/json",
			 data: "",
			 success: function (result){
				layer.msg("保存成功",{time:2000});
				 window.location.href=before;
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
	             layer.msg('保存失败',{time:2000});
	         }
		});*/
		$.getJSON(localhostPaht +'/visa/visainfo/listvisainfo?logintype=5&customerId1='+country.id, function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
		});
	}else{
		$.getJSON(localhostPaht +'/visa/visainfo/listvisainfo', function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
		});
	}
	//折叠板 效果初始化
    $("#panelbar").kendoPanelBar({
         expandMode: "single" //设置展开模式只能展开单个
    });
    
	var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
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
	}else if(aa == 1){//表示从签证进度跳转至此页面
		//隐藏编辑按钮
		$(".editBtn").hide();
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
	country = JSON.parse(unescape($.queryString("country")));
    countrystatus=$.queryString("countrystatus");
	/*-------------------------小灯泡 效果--------------------------*/
	firstPart = JSON.parse(unescape($.queryString("secondPart")));//获取 错误 信息
	secondPart = JSON.parse(unescape($.queryString("secondPart")));//获取 错误 信息
	thirdPart = JSON.parse(unescape($.queryString("thirdPart")));//获取 错误 信息
	$('label').each(function(){
		var labelText=$(this).text();//获取 页面上所有的字段 名称
		labelText = labelText.split(":");
		labelText.pop();
		labelText = labelText.join(":");//截取 :之前的信息
		if(thirdPart!=null&&thirdPart!=''){
			
			for(var i=0;i<thirdPart.length;i++){
				if(labelText==thirdPart[i]){
					$(this).next().find('input').css('border-color','#f17474');
					$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
					$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
				}
			}
		}
	});
	/*-------------------------end 小灯泡 效果--------------------------*/	
	
	
});
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
			visatype:0,
			area:0,
			paytype:0,
			passportlose:{},
			oldname:{},
			orthercountrylist:[],
			father:{},
			mother:{},
			relation:[],
			spouse:{
				marrystatus:0
			},
			usainfo:{},
			teachinfo:[],
			recentlyintousalist:[],
			workinfo:{
				jobstatus:'S'
			},
			oldworkslist:[],
			languagelist:[],
			visitedcountrylist:[],
			workedplacelist:[],
			army:{},
	        order:{},
	        customermanage:{},
	        trip:{
	        	paypersion:"我自己"
	        },
	        payPersion:{},
	        payCompany:{},
	        fastMail:{},
	        peerList:{},
	        travelpurpose:{},//赴美国旅行目的列表
	        travelplan:{},//是否制定了具体旅行计划
	        relationship:{},//与你的关系
	        travel: {
	            payer: "我自己",
	            // 同行人
	            togethers:[]
	        },
	        placeinformation:{},//地点信息
	        applicantproducer:{}//申请的制作者
    },
    keys = {
		"customer.orthercountrylist":{},
		"customer.recentlyintousalist":{},
		"customer.oldworkslist":{},
		"customer.languagelist":{},
		"customer.visitedcountrylist":{},
		"customer.workedplacelist":{},
		"customer.relation":{},
		"customer.teachinfo":{},
		"customer.peerList":{
			peerxing: "",
	    	peerxingen: "",
	    	peernameen: "",
	    	peername: "",
	    	tripid: "",
	    	relationme: ""
		}
	};
/*****************************************************
 * 数据绑定
 ****************************************************/
travelpurposeNum=0;
var viewModel = kendo.observable({
    customer: dafaults,
    countries:countries,
    customersourceEnum:customersourceEnum,
    states:states,
    onCheckpeer:function(e){//同行人姓和拼音
    	//得到list对象
    	var listobj = viewModel.get("customer.peerList");
    	var peerxingobj = $("#peerxing"+e.data.uid).val();
    	var aa = $("#peerxing"+e.data.uid).parent().parent().parent().next().find("input");
    	var val1=peerxingobj;
    	if(val1!=''&&val1!=null){
    		var bb = pinyinUtil.getPinyin(val1, '', false, false);
    		var val2=bb.toUpperCase();
    		aa.val(val2);
    		for(var i=0;i<listobj.length;i++){
    			if(e.data.peerxing == listobj[i].peerxing){
    				viewModel.set("customer.peerList["+i+"].peerxingen",val2);
    				return;
    			}
    		}
    	}else{
    		aa.val("");
    		for(var i=0;i<listobj.length;i++){
    			if(e.data.peerxing == listobj[i].peerxing){
    				viewModel.set("customer.peerList["+i+"].peerxingen","");
    				return;
    			}
    		}
    	}
    },
    onCheckPeername:function(e){//同行人名和拼音
    	//得到list对象
    	var listobj = viewModel.get("customer.peerList");
    	var peernameobj = $("#peername"+e.data.uid).val();
    	var aa = $("#peername"+e.data.uid).parent().parent().parent().next().find("input");
    	var val1=peernameobj;
    	if(val1!=''&&val1!=null){
    		var bb = pinyinUtil.getPinyin(val1, '', false, false);
    		var val2=bb.toUpperCase();
    		aa.val(val2);
    		for(var i=0;i<listobj.length;i++){
    			if(e.data.peername == listobj[i].peername){
    				viewModel.set("customer.peerList["+i+"].peernameen",val2);
    				return;
    			}
    		}
    	}else{
    		aa.val("");
    		for(var i=0;i<listobj.length;i++){
    			if(e.data.peername == listobj[i].peername){
    				viewModel.set("customer.peerList["+i+"].peernameen","");
    				return;
    			}
    		}
    	}
    },
    hasTogether: function () {
        var togethers = viewModel.get("customer.travel.togethers");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;
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
    	var aa = viewModel.get("customer.trip[0].paypersion");
    	if(aa==type){
    		return true;
    	}
    	return  false;
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
        //console.log(families.length);
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
        //console.log(JSON.stringify(schools));
        var state = schools ? schools.length > 0 : false;
        return state;
    },
    //参过军
    joinArmy: function () {
        /*var joinArmy = viewModel.get("customer.army");
        console.log(JSON.stringify(joinArmy));
        var state = joinArmy ? joinArmy.length > 0 : false;
        return state;*/
        var state = viewModel.get("customer.army.startdate") 
        		 || viewModel.get("customer.army.enddate")
        		 || viewModel.get("customer.army.country")
        		 || viewModel.get("customer.army.armydo")
        		 || viewModel.get("customer.army.armyname")
        		 || viewModel.get("customer.army.armytype");
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
    //赴美国旅行目的列表
    travelPurposeEnable: function () {
    	/*var objectiveList = viewModel.get("customer.travelpurpose");
    	console.log(JSON.stringify(objectiveList));
    	var state = objectiveList ? objectiveList.length > 0 : false;
        return state;*/
        /*var a=viewModel.get("customer.travelpurpose.id");
    	if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(travelpurposeNum<4){
    			travelpurposeNum++;
    			return false;
    		}else {return true};
    	}
    	return false;*/
    	var travelPurpose=viewModel.get("customer.travelpurpose.travelPurpose");
    	if(travelPurpose=="旅游"){
    		travelPurpose = "";
    	}else{
    		travelPurpose = viewModel.get("customer.travelpurpose.travelPurpose");
    	}
    	var state = travelPurpose
    			 || viewModel.get("customer.travelpurpose.travelSpecificPurpose");
    	return state;
    },
    //是否制定了具体旅行计划
    travelPlanEnable: function () {
    	/*var travelPlan = viewModel.get("customer.travelplan");
    	console.log(JSON.stringify(travelPlan));
    	var state = travelPlan ? travelPlan.length > 0 : false;
    	return state;*/
    	
    	var state = viewModel.get("customer.travelplan.arrivaledDate")
    				|| viewModel.get("customer.travelplan.pauseTime")
    				|| viewModel.get("customer.travelplan.pausePlace");
    	return state;
    	
    },
    //是否加入一个团队或组织旅行
    joinTeamEnable: function () {
    	return viewModel.get("customer.trip[0].teamname");
    },
    //你是否有其他亲属在美国
    relationEnable:function(){
    	return viewModel.get("customer.spouse");
    },
    //申请的制作者
    assistApplyEnable: function () {
    	/*var assistApplyEnable = viewModel.get("customer.applicantproducer");
    	console.log(JSON.stringify(assistApplyEnable));
    	var state = assistApplyEnable ? assistApplyEnable.length > 0 : false;
        return state;*/
        
        var state = viewModel.get("customer.applicantproducer.producerName")
					|| viewModel.get("customer.applicantproducer.producerXing")
					|| viewModel.get("customer.applicantproducer.organizeName")
					|| viewModel.get("customer.applicantproducer.addressDetails")
					|| viewModel.get("customer.applicantproducer.city")
					|| viewModel.get("customer.applicantproducer.province")
					|| viewModel.get("customer.applicantproducer.postCode")
					|| viewModel.get("customer.applicantproducer.country")
					|| viewModel.get("customer.applicantproducer.relation");
        return state;
    }
});
kendo.bind($(document.body), viewModel);

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
//同行人员
$("#has_other_travelers").change(function () {
    var key = "customer.peerList";
    if ($(this).is(':checked')) {
    	viewModel.addOne(key);
    } else {
    	viewModel.clearAll(key);
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
/*****************************************************
 * 亲属信息
 ****************************************************/
$("#has_immediate_relatives").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.addOne("customer.relation");
    } else {
    	viewModel.clearAll("customer.relation");
    }
});
//我有非直系亲属在美国
$("#other_relatives").change(function () {
	viewModel.set("customer.relation.indirect", $(this).is(':checked') ? " " : "");
});
/*****************************************************
 * 美国相关信息
 ****************************************************/

//最近5次进入美国的信息
$("#has_usa_aboard").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.addOne("customer.recentlyintousalist");
    } else {
    	viewModel.clearAll("customer.recentlyintousalist");
    }
});
//美国驾照
$("#have_usa_dl").change(function () {
	viewModel.set("customer.usainfo.usadriveport", $(this).is(':checked') ? " " : "");
});
//申请移民过
$("#old_apply").change(function () {
    var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.usainfo.instruction", value);
    viewModel.set("customer.usainfo.instructionen", value);
});
//申请过美国签证
$("#old_apply2").change(function () {
	viewModel.set("customer.usainfo.visaport", $(this).is(':checked') ? " " : "");
});
//本次签证和上次的一样
$("#type_same_as_previous").change(function () {
	viewModel.set("customer.usainfo.sameaslast", $(this).is(':checked') ? " " : "");
});
/*****************************************************
 * 教育信息
 ****************************************************/
$("#has_never_educated").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.addOne("customer.teachinfo");
    } else {
    	viewModel.clearAll("customer.teachinfo");
    }
});
/*****************************************************
 * 其他信息
 ****************************************************/
/*我曾参军*/
$("#join_army").change(function () {
	//viewModel.set("customer.army", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.army.armydo", value);
    viewModel.set("customer.army.armyname", value);
    viewModel.set("customer.army.armytype", value);
    viewModel.set("customer.army.country", value);
    viewModel.set("customer.army.enddate", value);
    viewModel.set("customer.army.startdate", value);
});

//赴美国旅行目的列表
$("#travel_purpose").change(function () {
	//viewModel.set("customer.travelpurpose", $(this).is(':checked') ? " " : "");
	/*var a={"createTime":null,"customerId":0,"id":0,"orderId":0,"remark":"","status":0,"travelPurpose":"旅游","travelSpecificPurpose":"","updateTime":null};
	var b={"createTime":null,"customerId":0,"id":-1,"orderId":0,"remark":"","status":0,"travelPurpose":"旅游","travelSpecificPurpose":"","updateTime":null};
	viewModel.set("customer.travelpurpose", $(this).is(':checked') ? a : b);*/
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.travelpurpose.travelPurpose", "旅游");
    viewModel.set("customer.travelpurpose.travelSpecificPurpose", value);
});
//是否制定了具体旅行计划
$("#if_formulate_plan").change(function () {
	//viewModel.set("customer.travelplan", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.travelplan.arrivaledDate", value);
    viewModel.set("customer.travelplan.pauseTime", value);
    viewModel.set("customer.travelplan.pausePlace", value);
});
//是否加入一个团队或组织旅行
$("#join_group").change(function () {
	viewModel.set("customer.trip[0].teamname", $(this).is(':checked') ? " " : "");
});
//申请的制作者
$("#has_assist_apply").change(function () {
	//viewModel.set("customer.applicantproducer", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.applicantproducer.producerName", value);
    viewModel.set("customer.applicantproducer.producerXing", value);
    viewModel.set("customer.applicantproducer.organizeName", value);
    viewModel.set("customer.applicantproducer.addressDetails", value);
    viewModel.set("customer.applicantproducer.city", value);
    viewModel.set("customer.applicantproducer.province", value);
    viewModel.set("customer.applicantproducer.postCode", value);
    viewModel.set("customer.applicantproducer.country", value);
    viewModel.set("customer.applicantproducer.relation", value);
});

//签证信息保存
$("#updatePassportSave").on("click",function(){
	viewModel.set("customer.relation.indirect",viewModel.get("customer.relation.indirect"));
	$.ajax({
		 type: "POST",
		 url: "/visa/visainfo/updatePassportSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			layer.msg('保存成功',{time:2000});
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('保存失败',{time:2000});
         }
	});
});
//点击保存时
var logintype;
$("#nextStepBtn").click(function(){
	var country = JSON.parse(unescape($.queryString("country")));
	
	logintype=$.queryString("logintype");
	var after;
	var before;
	if(logintype==5){
		after="/visa/visainfo/updatePassportSave?logintype=5";
		before='/myvisa/transactVisa/visaNationList.html?logintype=5&country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus+"&orderId="+$.queryString('orderId');
	}else{
		after="/visa/visainfo/updatePassportSave";
		before='/myvisa/transactVisa/visaProgressImg.html?country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
		
	}
	
	
	
	
	$.ajax({
		 type: "POST",
		 url: after,
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			layer.msg("保存成功",{time:2000});
			 window.location.href=before;
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
             layer.msg('保存失败',{time:2000});
         }
	});
});
