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
			gender:0,
			birthcountry:"CHN",
			nowcountry:"CHN",
			passportlose:{
				sendcountry:"CHN"
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
		"customer.relation":{},
		"customer.teachinfo":{}
	};
/*****************************************************
 * 数据绑定
 ****************************************************/
var passportnum=0;
var oldNameEnableNum=0;
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
    	//var joinArmy = viewModel.get("customer.army");
    	var state = viewModel.get("customer.army.armydo")|| viewModel.get("customer.army.armyname")
    			 || viewModel.get("customer.army.armytype")|| viewModel.get("customer.army.country")
    			 || viewModel.get("customer.army.enddate")|| viewModel.get("customer.army.startdate");
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
    	var a=viewModel.get("customer.passportlose.id");
    	if(a>0) return true;
    	else if(a<0) return false;
    	else{
    		if(passportnum<4){
    			passportnum++;
    			return false;
    		}else {return true};
    	}
    	return false;
       //return viewModel.get("customer.passportlose");
    },
    // 曾用名
    oldNameEnable: function () {
    	var state = viewModel.get("customer.oldname.oldname")
    			 || viewModel.get("customer.oldname.oldnameen") 
    			 || viewModel.get("customer.oldname.oldxing") 
    			 || viewModel.get("customer.oldname.oldxingen");
    	if(state){
    		$("input[oldname='oldname']").each(function(){
    			var labelTxt=$(this).parent().prev().text().trim();
    			labelTxt = labelTxt.split(":");
    			labelTxt.pop();
    			labelTxt = labelTxt.join(":");
    			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
    			$(this).attr('validationMessage',labelTxt+"不能为空");
    			$(this).attr('required','required');
    		});
    	}else{
    		$("input[oldname='oldname']").each(function(){
    			var labelTxt=$(this).parent().prev().text().trim();
    			labelTxt = labelTxt.split(":");
    			labelTxt.pop();
    			labelTxt = labelTxt.join(":");
    			//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
    			$(this).removeAttr('validationMessage',labelTxt+"不能为空");
    			$(this).removeAttr('required','required');
    		});
    		
    	}
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
	///console.log("==1==="+JSON.stringify(viewModel.customer.passportlose));
	var a={"sendcountry":"CHN","customerid":0,"id":0,"passport":"","reason":"","reasonen":""};
	var b={"sendcountry":"CHN","customerid":0,"id":-1,"passport":"","reason":"","reasonen":""};
	viewModel.set("customer.passportlose", $(this).is(':checked') ? a : b);
	///console.log("===2=="+JSON.stringify(viewModel.customer.passportlose));
});

//曾用名
$("#has_used_name").change(function () {
	/*var a={"customerid":0,"id":0,"oldname":"","oldnameen":"","oldxing":"","oldxingen":""};
	var b={"customerid":0,"id":-1,"oldname":"","oldnameen":"","oldxing":"","oldxingen":""};
	viewModel.set("customer.oldname", $(this).is(':checked') ? a : b);*/
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
	//viewModel.set("customer.army", $(this).is(':checked') ? " " : "");
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.army.armydo", value);
    viewModel.set("customer.army.armyname", value);
    viewModel.set("customer.army.armytype", value);
    viewModel.set("customer.army.country", value);
    viewModel.set("customer.army.enddate", value);
    viewModel.set("customer.army.startdate", value);
});
/*$("#join_army").change(function () {
    if ($(this).is(':checked')) {
    	viewModel.add("customer.army");
    } else {
    	viewModel.clear("customer.army");
    }
});*/
//=====================kendoui自定义验证开始====================
/*$(function(){
	$("#aaaa").kendoValidator({
		validateOnBlur: true,
		rules: {
	      customRule1: function(input) {
	    	  if(input.is("[name=idcard]")) {
	    		  var fullComName = $("#card_id").val();
	    		  if(fullComName==null || fullComName==""){
		    		  return false;
	    		  }
	    	  }
	    	  return true;
	      },
	      customRule2: function(input) {
	    	  if(input.is("[name=idcard]")) {
	    		  var customerSource = $("#card_id").val();
	    		  //15位数身份证正则表达式
	    		    var arg1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
	    		    //18位数身份证正则表达式
	    		    var arg2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/;
	    		    if (customerSource.match(arg1) == null && customerSource.match(arg2) == null) {
	    		        return false;
	    		    }
	    		    else {
	    		        return true;
	    		    }
	    	  }
	    	  return true;
	      }
	},
		//自定义验证消息
		messages: {
			customRule1: "身份证不能为空",
			customRule2: "身份证格式不正确"
			
		  }
			
	});

});*/








//=====================kendoui自定义验证结束====================
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];

//信息保存
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#saveCustomerData").on("click",function(){
 
	
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
		 viewModel.set("customer.relation.indirect",viewModel.get("customer.relation.indirect"));
	/*var error=JSON.stringify(map);
	if(error.length>15){
		
		viewModel.set("customer.errorinfo",JSON.stringify(map));
		map.clear();
	}*/
		 //console.log("====="+JSON.stringify(viewModel.customer));
	$.ajax({
		 type: "POST",
		 url: "/visa/newcustomer/customerSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			 if(indexnew!=null){
				 layer.close(indexnew);
			 }
			 //console.log(result);
			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			 parent.layer.close(index);
			 window.parent.successCallback('1');
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown){
			 if(indexnew!=null){
				 layer.close(indexnew);
			 }
             layer.msg('保存失败!',{time:2000});
         }
	});
	}else{
		   //验证————————————————————————————————————
	    $('.k-tooltip-validation').each(function(){
	    	var none=$(this).css("display")=="none";//获取 判断验证提示隐藏
	    	if(!none){
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
	 /*viewModel.set("customer.relation.indirect",viewModel.get("customer.relation.indirect"));
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
        	//设置默认值
        	var birthcountry=viewModel.get("customer.birthcountry");
			if(birthcountry!=null&&birthcountry!=''){
			}else{
				viewModel.set("customer.birthcountry","CHN");
				
			}
			var nowcountry=viewModel.get("customer.nowcountry");
			if(nowcountry!=null&&nowcountry!=''){
			}else{
				viewModel.set("customer.nowcountry","CHN");
				
			}
			var sendcountry=viewModel.get("customer.passportlose.sendcountry");
			if(sendcountry!=null&&sendcountry!=''){
			}else{
				
				viewModel.set("customer.passportlose.sendcountry","CHN");
			}
			
			var photoname= "<a id='downloadA' href='#'>"
	             + viewModel.get("customer.photoname")
	             + "</a>"
        	var phoneurl=viewModel.get("customer.phoneurl");
        	if(phoneurl!=null&&phoneurl!=''){
        		$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
        		$("#photoname").html(photoname);
        	}

        	/*预览 按钮*/
            $(document).on('click','#preview',function(){
            	$('#light').css('display','block');
            	$('#fade').css('display','block');
            	var phoneurl=viewModel.get("customer.phoneurl");
            	if(phoneurl!=null&&phoneurl!=''){
            		console.log("1111111====="+phoneurl);
            		$("#imgId").attr('src',phoneurl);
            	}
            });
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
            	map1=eval('(' +reason+ ')');
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
            					if(labelText==reasonnew){
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
