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
//初始化页面组件
$(function () {
	$.getJSON(localhostPaht +'/visa/visainfo/listvisainfo', function (resp) {
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     });
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
	        trip:{},
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
var viewModel = kendo.observable({
    customer: dafaults,
    countries:countries,
    customersourceEnum:customersourceEnum,
    states:states,
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
    	console.log(type);
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
    //参过军
    joinArmy: function () {
        var state = viewModel.get("customer.army");
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
        return viewModel.get("customer.travelpurpose");
    },
    //是否制定了具体旅行计划
    travelPlanEnable: function () {
    	return viewModel.get("customer.travelplan");
    },
    //是否加入一个团队或组织旅行
    joinTeamEnable: function () {
    	return viewModel.get("customer.trip");
    },
    //你是否有其他亲属在美国
    relationEnable:function(){
    	return viewModel.get("customer.spouse");
    },
    //申请的制作者
    assistApplyEnable: function () {
        return viewModel.get("customer.applicantproducer");
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
	viewModel.set("customer.army", $(this).is(':checked') ? " " : "");
});

//赴美国旅行目的列表
$("#travel_purpose").change(function () {
	viewModel.set("customer.travelpurpose", $(this).is(':checked') ? " " : "");
});
//是否制定了具体旅行计划
$("#if_formulate_plan").change(function () {
	viewModel.set("customer.travelplan", $(this).is(':checked') ? " " : "");
});
//是否加入一个团队或组织旅行
$("#join_group").change(function () {
	viewModel.set("customer.trip", $(this).is(':checked') ? " " : "");
});
//是否加入一个团队或组织旅行
$("#other_relatives_aa").change(function () {
	viewModel.set("customer.spouse", $(this).is(':checked') ? " " : "");
});
//申请的制作者
$("#has_assist_apply").change(function () {
	viewModel.set("customer.applicantproducer", $(this).is(':checked') ? " " : "");
});

//签证信息保存
$("#updatePassportSave").on("click",function(){
	console.log(JSON.stringify(viewModel.customer));
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