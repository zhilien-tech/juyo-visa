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
var passporttype=[
                 {text:"外交",value:0},
                 {text:"普通",value:1},
                 {text:"公务",value:3},
                 {text:"其他",value:4},
               ];
function translateZhToEn(from, to) {
    $.getJSON("/translate/google", {q: $(from).val()}, function (result) {
        $("#" + to).val(result.data).change();
    });
}

//日本订单用户信息 根据中文自动转为拼音
function getPinYin(){
	var chinesexing = $("#last_name_cn").val();
	var chinesexingen = pinyinUtil.getPinyin(chinesexing, '', false, false);
	viewModel.set("customer.chinesexingen",chinesexingen.toUpperCase());
	
	var chinesename = $("#first_name_cn").val();
	var chinesenameen = pinyinUtil.getPinyin(chinesename, '', false, false);
	viewModel.set("customer.chinesenameen",chinesenameen.toUpperCase());
	
	//曾用名
	var oldname = $("#old_usrname_cn").val();
	var oldxingen = pinyinUtil.getPinyin(oldname, '', false, false);
	viewModel.set("customer.oldnameJp.oldxingen",oldxingen.toUpperCase());
	
	var oldnamePin = $("#old_given_name_cn").val();
	var oldnameen = pinyinUtil.getPinyin(oldnamePin, '', false, false);
	viewModel.set("customer.oldnameJp.oldnameen",oldnameen.toUpperCase());
	
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
	
	docountry:"CHN",
	passporttype:1,
	workinfoJp: {},
	financeJpList:[],
	oldpassportJp: {},
	oldnameJp: {},
	recentlyintojp: {},
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
        "customer.financeJpList": [],
        "customer.orthercountryJpList": [],
        "customer.recentlyintojpJpList": []
    }
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
	customer: dafaults,
	passporttype:passporttype,
    countries: countries,
    states: states,
    onRecentChange:function (e) {
    	var target = e.sender.element.attr("id");
    	var start = $("#in_date").data("kendoDatePicker");
        var end = $("#out_date").data("kendoDatePicker");
        /*if (target == "in_date") {
            end.min(start.value());
        } else {
            start.max(end.value());
        }*/
        //入境时间
        var intoDate = viewModel.get("customer.recentlyintojp.intoDate")+"";
        //出境时间
        var outofDate = viewModel.get("customer.recentlyintojp.outofDate")+"";
    	//日期差
        var dateDifference = 0;
    	var a=new Date(Date.parse((intoDate))).getTime();
    	var b=new Date(Date.parse((outofDate))).getTime();
    	var daynum=(b-a)/(1000*3600*24);
    	if(daynum>=0){
    		dateDifference = Math.ceil(daynum)+1;
    	}else{
    		dateDifference = 0;
    	}
    	viewModel.set("customer.recentlyintojp.stayDays", dateDifference);
        
    },
    onDateChange: function (e) {
        var target = e.sender.element.attr("id");
        var start = $("#signed_at").data("kendoDatePicker");
        var end = $("#expire_at").data("kendoDatePicker");
        if (target == "signed_at") {
            end.min(start.value());
        } else {
            start.max(end.value());
        }
      //获取系统当前日期
        var myDate = new Date();
      //得到当前用户签证有效日期
     	var passporteffectdate = viewModel.get("customer.passporteffectdate");
     /*	alert(typeof passporteffectdate);
     	if(passporteffectdate != "" && passporteffectdate != null && passporteffectdate != undefined){
     		var passporteffectdate=JSON.stringify(passporteffectdate).substring(0,10);
     	}*/
     	//日期差
     	if(passporteffectdate!=null&&passporteffectdate!=''&&passporteffectdate!=undefined){
     		var dateDifference =null;
    		var a=myDate.getTime();//系统日期
    		var b=passporteffectdate.getTime();//签证有效日期
    		var daynum=(b-a)/(1000*3600*24);
    		if(daynum<0){
    			dateDifference = 0;
    		}else if(daynum>0){
    			dateDifference = Math.ceil(daynum);
    		}
    		//当前系统时间和签证有效日期对比
    		if(dateDifference<180){
    			$('.msgg').remove();
    	 		$('#div11 .k-datepicker').append("<span class='msgg k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照已过期,请及时更换</span>");
    		}else if(dateDifference>180 && dateDifference<240){
    			$('.msgg').remove();
    	 		$('#div11 .k-datepicker').append("<span class='msgg k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照即将过期，请及时更换</span>");
    		}else if(dateDifference>240){
    			$('.msgg').remove();
    			
    		}
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
        var cusId = viewModel.customer.id;
        viewModel.customer.financeJpList.push({});
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
    	///console.log("旧护照:"+JSON.stringify(viewModel.get("customer.oldpassportJp")));
    	var state = viewModel.get("customer.oldpassportJp.passport") || viewModel.get("customer.oldpassportJp.reason")
		 || viewModel.get("customer.oldpassportJp.reasonen") || viewModel.get("customer.oldpassportJp.sendcountry");
    	return state;
    },
    // 曾用名
    oldNameEnable: function () {
    	var state = viewModel.get("customer.oldnameJp.oldname") || viewModel.get("customer.oldnameJp.oldnameen")
        		 || viewModel.get("customer.oldnameJp.oldxing") || viewModel.get("customer.oldnameJp.oldxingen");
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
    //最近1次入境日本的信息
    jprecentlyEnable:function(){
    	var state = viewModel.get("customer.recentlyintojp.intoDate")
		|| viewModel.get("customer.recentlyintojp.outofDate")
		|| viewModel.get("customer.recentlyintojp.stayDays");
    	return state;
    }
});
kendo.bind($(document.body), viewModel);
/*****************************************************
 * 开始个人信息
 ****************************************************/

//丢过护照
$("#pp_lost").change(function () {
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.oldpassportJp.passport", value);
    viewModel.set("customer.oldpassportJp.reason", value);
    viewModel.set("customer.oldpassportJp.reasonen", value);
    viewModel.set("customer.oldpassportJp.sendcountry", value);
});
//曾用名
$("#has_used_name").change(function () {
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.oldnameJp.oldname", value);
    viewModel.set("customer.oldnameJp.oldnameen", value);
    viewModel.set("customer.oldnameJp.oldxing", value);
    viewModel.set("customer.oldnameJp.oldxingen", value);
});
//其他国家居民
$("#has_pr").change(function () {
    if ($(this).is(':checked')) {
        viewModel.addOne("customer.orthercountryJpList");
    } else {
        viewModel.clearAll("customer.orthercountryJpList");
    }
});
//最近1次入境日本的信息
$("#has_usa_aboard").change(function () {
	var value = $(this).is(':checked') ? " " : "";
    viewModel.set("customer.recentlyintojp.intoDate", value);
    viewModel.set("customer.recentlyintojp.outofDate", value);
    viewModel.set("customer.recentlyintojp.stayDays", value);
});


//自定义校验规则
var validatable = $("#customer_form").kendoValidator({
    rules: {
      reg: function(input) {
    	  if (input.is("[id=mobile]")) {
    		  var regex = /^\d{11}$/;
    		  var flag = regex.test(input.val());
        	  return flag;
          }
          return true;
      },
      card: function(input){//身份证
    	  if (input.is("[name=card]")) {
    		  var regex = /^.{18}$/;
    		  var flag = regex.test(input.val());
        	  return flag;
          }
    	  return true;
      },
      passport: function(input){//护照
    	  if (input.is("[name=passport]")) {
    		  var regex = /^.{9}$/;
    		  var flag = regex.test(input.val());
        	  return flag;
          }
          return true;
      }
    },
    messages:{
    	reg:"手机号码必须为11位数字",
    	card:"身份证号必须为18位",
    	passport:"护照号必须为9位"
    }
}).data("kendoValidator");


//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];


//信息保存
$("#saveCustomerData").on("click",function(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		///console.log(JSON.stringify(viewModel.customer));
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
						layer.close(indexnew);
				 }
				 
				 ///console.log(result);
				 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				 parent.layer.close(index);
				 window.parent.successCallback('1');
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
				 if(indexnew!=null){
					layer.close(indexnew);
				 }
				  
				 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				 parent.layer.close(index);
				 window.parent.successCallback('1');
				 //console.log(XMLHttpRequest);
				 //console.log(textStatus);
				 //console.log(errorThrown);
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
		    	//console.log("-获取验证的文字信息是："+verificationText+"                -获取验证信息 对应的label名称是："+labelVal);
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
			 ///console.log(result);
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
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
    var oid = $.queryString("cid");
    if (oid) {
        $.getJSON("/visa/newcustomerjp/showDetail?customerid=" + oid, function (resp){
        	viewModel.set("customer", $.extend(true, dafaults, resp));
        	var passporttype=viewModel.get("customer.passporttype");
        	if(passporttype!=""&&passporttype!=null){
        		
        	}else{
        		viewModel.set("customer.passporttype", 1);
        	}
        	//护照签发机关默认值处理
        	var passportsendoffice=viewModel.get("customer.passportsendoffice");
        	if(passportsendoffice!=""&&passportsendoffice!=null){
        		
        	}else{
        		viewModel.set("customer.passportsendoffice", "公安部出入境管理局");
        	}
        	var docountry=viewModel.get("customer.docountry");
        	if(docountry!=""&&docountry!=null){
        		
        	}else{
        		viewModel.set("customer.docountry", "CHN");
        	}
        	
        	//获取系统当前日期
        	/*var myDate = new Date();
        	//得到当前用户签证有效日期
         	var passporteffectdate = viewModel.get("customer.passporteffectdate");
         	if(passporteffectdate != "" && passporteffectdate != null && passporteffectdate != undefined){
         		var passporteffectdate=passporteffectdate.substring(0,10);
         	}
         	//日期差
         	var dateDifference =null;
    		var a=myDate.getTime();//系统日期
    		var b=new Date(Date.parse((passporteffectdate+"").replace(/-/g, "/"))).getTime();//签证有效日期
    		var daynum=(a-b)/(1000*3600*24);
    		if(daynum<0){
    			dateDifference = Math.ceil(-daynum);
    		}else if(daynum>0){
    			dateDifference = -(Math.floor(-daynum));
    		}
         	//当前系统时间和签证有效日期对比
         	if(dateDifference<180){
         		$('.effectiveDate .k-datepicker').append("<span class='k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照已过期,请及时更换</span>");
         	}else if(180<dateDifference<240){
         		$('.effectiveDate .k-datepicker').append("<span class='k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照即将过期，请及时更换</span>");
         	}*/
        	
        	
        	/*var reason=viewModel.get("customer.errorinfo");
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
        	if(reason!=null&&reason!=''){
        		
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
        	}
        	
        	if(indexnew!=null){
    			layer.close(indexnew);
        	}
        	/*--------------------------end 小灯泡 回显----------------------*/
        	
        });
    }
	 
});
