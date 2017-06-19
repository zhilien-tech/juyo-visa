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
			army:{}
		
		
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
		"customer.teachinfo":{}
		
	}
;
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
    customer: dafaults,
    countries:countries,
    customersourceEnum:customersourceEnum,
    states:states,
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
        console.log(key);
       /* viewModel.get(key).push(
        		{
	          		peerxing: "",
	            	peerxingen: "",
	            	peernameen: "",
	            	peername: "",
	            	tripid: "",
	            	relationme: ""
	          	}
        );*/
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
        /*viewModel.set("customer.ordernumber", $(this).is(':checked') ? " " : "");*/
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
        return state.indexOf(viewModel.get("customer.spouse.marrystatus")) > -1;
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
   /* joinArmy: function () {
        var state = viewModel.get("customer.army");
    	var schools = viewModel.get("customer.army");
        var state = schools ? schools.length > 0 : false;
        return state;
    },*///工作信息详情
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
    }
    
});
kendo.bind($(document.body), viewModel);




//丢过护照
$("#pp_lost").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.passportlose");
    } else {
    	viewModel.clear("customer.passportlose");
    }
});
//曾用名
$("#has_used_name").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.oldname");
    } else {
    	viewModel.clear("customer.oldname");
    }
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
	viewModel.set("customer.relation.indirect", $(this).is(':checked'));
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
	viewModel.set("customer.usainfo.sameaslast", $(this).is(':checked'));
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
$("#join_army").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.army");
    } else {
    	viewModel.clear("customer.army");
    }
});





//信息保存

function customersave(){
	console.log(JSON.stringify(viewModel.customer));
    $.ajax({
        type: "POST",
        url: "/visa/newcustomer/customerSave",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(viewModel.customer),
        success: function (result) {
        	  console.log(result.code);
            if(result.code=="SUCCESS"){
            	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
            	//$.layer.closeAll();
            	parent.layer.close(index);
            	window.parent.successCallback('1');
            	
            }
        }
    });
}
$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("cid");
    if (oid) {
        $.getJSON("/visa/newcustomer/showDetail?customerid=" + oid, function (resp) {
        	viewModel.set("customer", $.extend(true, dafaults, resp));
        /*	dafaults.customermanage.telephone=resp.customermanage.telephone;
        	dafaults.customermanage.email=resp.customermanage.email;
        	var color = $("#cus_phone").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
        	var color = $("#cus_email").data("kendoMultiSelect");
			color.value(resp.customermanage.id);*/
        });
    }
   /* console.log(JSON.stringify(viewModel.customer));*/
    //折叠面板的显隐切换
    /*$(document).on("click", ".k-link", function () {
        $(this).find(".k-icon").toggleClass("k-i-arrow-60-down k-i-arrow-n");
        $(this).next().toggle();
    });*/
   
});


