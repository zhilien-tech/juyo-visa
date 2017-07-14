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
			birthcountry:"CHIN",
			nowcountry:"CHIN",
			passportlose:{
				sendcountry:"CHIN"
			},
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
			
		},
		"customer.teachinfo":{}
		
	};
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
        ///var state = viewModel.get("customer.army");
    	var joinArmy = viewModel.get("customer.army");
        var state = joinArmy ? joinArmy.length > 0 : false;
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
    	var oldPassportEnable = viewModel.get("customer.passportlose");
    	var state = oldPassportEnable ? oldPassportEnable.length > 0 : false;
        return state;
       ///return viewModel.get("customer.passportlose");
    },
    // 曾用名
    oldNameEnable: function () {
    	var oldNameEnable = viewModel.get("customer.oldname");
    	var state = oldNameEnable ? oldNameEnable.length > 0 : false;
        return state;
        ///return viewModel.get("customer.oldname");
    },
    // 其他国家公民
    otherCountryEnable: function () {
        var otherCountry = viewModel.get("customer.orthercountrylist");
        var state = otherCountry ? otherCountry.length > 0 : false;
        return state;
    },
});
kendo.bind($(document.body), viewModel);




//丢过护照
$("#pp_lost").change(function () {
	viewModel.set("customer.passportlose", $(this).is(':checked') ? " " : "");
});
/*$("#pp_lost").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.passportlose");
    } else {
    	viewModel.clear("customer.passportlose");
    }
});*/

//曾用名
$("#has_used_name").change(function () {
	viewModel.set("customer.oldname", $(this).is(':checked') ? " " : "");
});
/*$("#has_used_name").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.oldname");
    } else {
    	viewModel.clear("customer.oldname");
    }
});*/

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
/*我曾参军*/
$("#join_army").change(function () {
	viewModel.set("customer.army", $(this).is(':checked') ? " " : "");
});
/*$("#join_army").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.army");
    } else {
    	viewModel.clear("customer.army");
    }
});*/





//信息保存
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#saveCustomerData").on("click",function(){
	if(validatable.validate()){
		 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
	viewModel.set("customer.relation.indirect",viewModel.get("customer.relation.indirect"));
	/*var error=JSON.stringify(map);
	if(error.length>15){
		
		viewModel.set("customer.errorinfo",JSON.stringify(map));
		map.clear();
	}*/
	 //console.log(JSON.stringify(viewModel.customer));
	$.ajax({
		 type: "POST",
		 url: "/visa/newcustomer/customerSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			 if(indexnew!=null){
					
					layer.close(indexnew);
					}
			 
			 console.log(result);
			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			 parent.layer.close(index);
			 window.parent.successCallback('1');
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown){
		 if(indexnew!=null){
				
				layer.close(indexnew);
				}
		 
			 console.log(XMLHttpRequest);
			 console.log(textStatus);
			 console.log(errorThrown);
            layer.msg('保存失败!',{time:2000});
         }
	});
	}
});


/*$.ajax({
	cache : false,
	type : "POST",
	url : '${base}/admin/airlinepolicy/add.html',
	data : $('#addFileInfoForm').serialize(),// 你的formid
	error : function(request) {
		layer.msg('添加失败!');
	},
	success : function(data) {
		layer.close(index);
		 formValidator();  
		var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	    parent.layer.close(index);
	   window.parent.successCallback('1');
	  
	    
	    
	}
});
*/
//通过或者拒绝的方法
function agreeOrRefuse(flag){
	
/*	
	viewModel.set("customer.relation.indirect",viewModel.get("customer.relation.indirect"));
	 viewModel.set("customer.errorinfo",JSON.stringify(map));*/
	 var error=JSON.stringify(map);
	 map.clear();
	var id=viewModel.get("customer.id");
	$.ajax({
		 type: "POST",
		 url: "/visa/newcustomer/agreeOrRefuse?flag="+flag+"&customerid="+id+"&error="+error,
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
        $.getJSON("/visa/newcustomer/showDetail?customerid=" + oid, function (resp) {
        	viewModel.set("customer", $.extend(true, dafaults, resp));
        	//console.log(JSON.stringify(viewModel.customer.errorinfo));
        	
        	   /*var reason=viewModel.get("customer.errorinfo");
        		var map=new Map();
        		map=eval("("+reason+")");
        		var reasonnew="";
        		
        		for (var key in map) {
        			var a = map[key];
        			for (var k in a){ 
        				var b=1;
        				for(var i in a[k]){
        					if(b%2!=0){
        						reasonnew=(a[k])[i];//label 名称
        						//console.log(reasonnew);
        					}
        					b++;
        				}
        			} 
        		}*/
        		
        		/*----小灯泡 回显----*/
            	var reason=viewModel.get("customer.errorinfo");
            	var map1=new Map();
            	map1=eval("("+reason+")");
            	for (var key in map1){
            		var a = map1[key];//获取到 错误信息 数据
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
            	
            	/*----end 小灯泡 回显----*/
        	
        });
    }
    
});
