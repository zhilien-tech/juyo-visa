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
        travel: {
            payer: "我自己",
            // 同行人
            togethers: []
        },
        customermanage:{
        	fullComName: "",
        	customerSource: "",
        	linkman: "",
        	telephone: "",
        	email: ""
        },
		trip:{
			teamname: "",
        	intostate: "",
        	intocity: "",
        	usahotel: "",
        	linkaddress: "",
        	zipcode: "",
        	linkxing: "",
        	linkname: "",
        	linkxingen: "",
        	linknameen: "",
        	linkstate: "",
        	linkcity: "",
        	detailaddress: "",
        	phone: "",
        	email: "",
        	paypersion: "",
        	linkzipcode: "",
        	staytime: "",
        	orderid: "",
        	staytype: "",
        	linkrelation: "",
        	arrivedate: ""
		},
		peerList:[],
		fastMail:{
			datasource: 0,
        	fastmailnum: "",
        	mailmethod: 0,
        	mailaddress: "",
        	linkpeople: "",
        	phone: "",
        	invoicecontent: "",
        	invoicehead: "",
        	remaker: "",
        	orderid: ""
		},
		payPersion:{
			
		},
		payCompany:{
			
		},
		customerresource:{}
    },
    keys = {
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
    /*hasTogether: function () {
        var togethers = viewModel.get("customer.travel.togethers");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;
    },*/
    addOne: function (e) {
    	 var key = $.isString(e) ? e : $(e.target).data('params');
         console.log(key);
         console.log(keys[key]);
         viewModel.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    // 是否有同行人
    hasTogether: function () {
    	/*var togethers = viewModel.get("customer.peerList");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;*/
        /*viewModel.set("customer.ordernumber", $(this).is(':checked') ? " " : "");*/
    	
    	var togethers = viewModel.get("customer.peerList");
        var state = togethers ? togethers.length > 0 : false;
        return state;
        
    },
    clearAll: function (key) {
        var all = viewModel.get(key);
        if (all) all.splice(0, all.length);
    },
    //支付人
    payType: function (type) {
        return viewModel.get("customer.trip.paypersion") === type;
    },
    //您是否作为一个团队或组织去旅行
    joinTeam: function (type) {
    	var state = viewModel.get("customer.trip.teamname");
    	return state;
    },
});
kendo.bind($(document.body), viewModel);
/*****************************************************
 * 开始个人信息
 ****************************************************/

/*****************************************************
 * 开始出行信息
 ****************************************************/
//您是否作为一个团队或组织去旅行
$("#join_group").change(function () {
	viewModel.set("customer.trip.teamname", $(this).is(':checked') ? " " : "");
});
//同行人
$("#has_other_travelers").change(function () {
    var key = "customer.peerList";
    if ($(this).is(':checked')) {
    	viewModel.addOne(key);
    } else {
    	viewModel.clearAll(key);
    }
});

//客户来源
function comsource(){
	viewModel.set("customer.customermanage.fullComName",'');
	viewModel.set("customer.customermanage.linkman",'');
	viewModel.set("customer.customermanage.email",'');
	viewModel.set("customer.customermanage.telephone",'');
	viewModel.set("customer.customerresource.linkman",'');
	viewModel.set("customer.customerresource.fullComName",'');
	viewModel.set("customer.customerresource.email",'');
	viewModel.set("customer.customerresource.telephone",'');
	var flag=$("#customerSource").val();
	if(flag==3){//直客 
		$("#select").hide();
		$("#selectno").show();
		$('.companyFullName').addClass('hide');//隐藏 默认显示的 其他状态下的 公司全称
		$('.ZKcompanyFullName').removeClass('hide');//显示   默认显示的  直客  公司全称
		
		$("input[comResource='comResource']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
			$(this).attr('validationMessage',labelTxt+"不能为空");
			$(this).attr('required','required');
		});
		$("input[comManage='comManage']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
			$(this).removeAttr('validationMessage',labelTxt+"不能为空");
			$(this).removeAttr('required','required');
		});

	}else{//其他
		$("#select").show();
		$("#selectno").hide();
		$('.companyFullName').removeClass('hide');//显示 默认显示的 其他状态下的 公司全称
		$('.ZKcompanyFullName').addClass('hide');//隐藏   默认显示的  直客  公司全称
		$("input[comManage='comManage']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
			$(this).attr('validationMessage',labelTxt+"不能为空");
			$(this).attr('required','required');
		});
		$("input[comResource='comResource']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
			$(this).removeAttr('validationMessage',labelTxt+"不能为空");
			$(this).removeAttr('required','required');
		});
	}
}

$(function () {
	comsource();//页面加载时，初始化客户来源字段 
	//手机--------------------------------------------------------------------------
	$("#cus_phone").kendoMultiSelect({
   		placeholder:"请选择手机号",
        dataTextField: "telephone",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "/visa/order/custominfo"
                }
            }
        },
        maxSelectedItems:1,
        change:function(data){
        	var cobVal = $("#cus_phone").data("kendoMultiSelect").value();
        	var falg='telephone';
        	$.ajax({
    			type : 'POST',
    			data : {
    				id:cobVal
    			},
    			dataType : 'json',
    			url : "/visa/order/custominfowrite",
    			success : function(data) {
    				//联系人
    				viewModel.set("customer.customermanage.linkman",data.linkman);
    				var color = $("#cus_linkman").data("kendoMultiSelect");
    				color.value(data.id);
    				//公司全称
    				viewModel.set("customer.customermanage.fullComName",data.fullComName);
    				var color = $("#cus_fullComName").data("kendoMultiSelect");
    				color.value(data.id);
    				//客户来源
    				viewModel.set("customer.customerSource",data.customerSource);
    				viewModel.set("customer.customermanage.id",data.id);
    				//电话
    				viewModel.set("customer.customermanage.telephone",data.telephone);
    				var color = $("#cus_phone").data("kendoMultiSelect");
    				color.value(data.id);
    				//邮箱
    				viewModel.set("customer.customermanage.email",data.email);
    				var color = $("#cus_email").data("kendoMultiSelect");
    				color.value(data.id);
    				//验证需要设置相关的值
    				$("#cus_fullComName").val(data.fullComName);
    				$("#cus_linkman").val(data.linkman);
    				$("#cus_phone").val(data.telephone);
    				$("#cus_email").val(data.email);
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
	//end 手机--------------------------------------------------------------------------
	
	//邮箱--------------------------------------------------------------------------
	$("#cus_email").kendoMultiSelect({
    	placeholder:"请选择邮箱",
        dataTextField: "email",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "/visa/order/custominfo"
                }
            }
        },
        maxSelectedItems:1,
        change:function(data){
        	var cobVal = $("#cus_email").data("kendoMultiSelect").value();
        	$.ajax({
    			type : 'POST',
    			data : {
    				id:cobVal
    			},
    			dataType : 'json',
    			url : "/visa/order/custominfowrite",
    			success : function(data) {
    				//联系人
    				viewModel.set("customer.customermanage.linkman",data.linkman);
    				var color = $("#cus_linkman").data("kendoMultiSelect");
    				color.value(data.id);
    				//公司全称
    				viewModel.set("customer.customermanage.fullComName",data.fullComName);
    				var color = $("#cus_fullComName").data("kendoMultiSelect");
    				color.value(data.id);
    				//客户来源
    				viewModel.set("customer.customerSource",data.customerSource);
    				viewModel.set("customer.customermanage.id",data.id);
    				//电话
    				viewModel.set("customer.customermanage.telephone",data.telephone);
    				var color = $("#cus_phone").data("kendoMultiSelect");
    				color.value(data.id);
    				//邮箱
    				viewModel.set("customer.customermanage.email",data.email);
    				var color = $("#cus_email").data("kendoMultiSelect");
    				color.value(data.id);
    				//验证需要设置相关的值
    				$("#cus_fullComName").val(data.fullComName);
    				$("#cus_linkman").val(data.linkman);
    				$("#cus_phone").val(data.telephone);
    				$("#cus_email").val(data.email);
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
	//end 邮箱--------------------------------------------------------------------------
	
	
	//联系人--------------------------------------------------------------------------
	$("#cus_linkman").kendoMultiSelect({
    	placeholder:"请选择联系人",
        dataTextField: "linkman",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "/visa/order/custominfo"
                }
            }
        },
        maxSelectedItems:1,
        change:function(data){
        	var cobVal = $("#cus_linkman").data("kendoMultiSelect").value();
        	$.ajax({
    			type : 'POST',
    			data : {
    				id:cobVal
    			},
    			dataType : 'json',
    			url : "/visa/order/custominfowrite",
    			success : function(data) {
    				//联系人
    				viewModel.set("customer.customermanage.linkman",data.linkman);
    				var color = $("#cus_linkman").data("kendoMultiSelect");
    				color.value(data.id);
    				//公司全称
    				viewModel.set("customer.customermanage.fullComName",data.fullComName);
    				var color = $("#cus_fullComName").data("kendoMultiSelect");
    				color.value(data.id);
    				//客户来源
    				viewModel.set("customer.customerSource",data.customerSource);
    				viewModel.set("customer.customermanage.id",data.id);
    				//电话
    				viewModel.set("customer.customermanage.telephone",data.telephone);
    				var color = $("#cus_phone").data("kendoMultiSelect");
    				color.value(data.id);
    				//邮箱
    				viewModel.set("customer.customermanage.email",data.email);
    				var color = $("#cus_email").data("kendoMultiSelect");
    				color.value(data.id);
    				//验证需要设置相关的值
    				$("#cus_fullComName").val(data.fullComName);
    				$("#cus_linkman").val(data.linkman);
    				$("#cus_phone").val(data.telephone);
    				$("#cus_email").val(data.email);
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
	//end 联系人--------------------------------------------------------------------------
	
	
	//公司全称--------------------------------------------------------------------------
	$("#cus_fullComName").kendoMultiSelect({
    	placeholder:"请选择公司全称",
        dataTextField: "fullComName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "/visa/order/custominfo"
                }
            }
        },
        maxSelectedItems:1,
        change:function(data){
        	var cobVal = $("#cus_fullComName").data("kendoMultiSelect").value();
        	$.ajax({
    			type : 'POST',
    			data : {
    				id:cobVal
    			},
    			dataType : 'json',
    			url : "/visa/order/custominfowrite",
    			success : function(data) {
    				//联系人
    				viewModel.set("customer.customermanage.linkman",data.linkman);
    				var color = $("#cus_linkman").data("kendoMultiSelect");
    				color.value(data.id);
    				//公司全称
    				viewModel.set("customer.customermanage.fullComName",data.fullComName);
    				var color = $("#cus_fullComName").data("kendoMultiSelect");
    				color.value(data.id);
    				//客户来源
    				viewModel.set("customer.customerSource",data.customerSource);
    				viewModel.set("customer.customermanage.id",data.id);
    				//电话
    				viewModel.set("customer.customermanage.telephone",data.telephone);
    				var color = $("#cus_phone").data("kendoMultiSelect");
    				color.value(data.id);
    				//邮箱
    				viewModel.set("customer.customermanage.email",data.email);
    				var color = $("#cus_email").data("kendoMultiSelect");
    				color.value(data.id);
    				//验证需要设置相关的值
    				$("#cus_fullComName").val(data.fullComName);
    				$("#cus_linkman").val(data.linkman);
    				$("#cus_phone").val(data.telephone);
    				$("#cus_email").val(data.email);
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
	//end 公司全称--------------------------------------------------------------------------
});

//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];

//信息保存
//var validator = $("#orderForm").kendoValidator().data("kendoValidator");
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
function ordersave(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		
	 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
			 $.ajax({
				 type: "POST",
				 url: "/visa/order/orderSave",
				 contentType: "application/json",
				 dataType: "json",
				 data: JSON.stringify(viewModel.customer),
				 success: function (result) {
					 if(indexnew!=null){
							layer.close(indexnew);
					 }
					 //console.log(result.code);
					 if(result.code=="SUCCESS"){
						 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						 //$.layer.closeAll();
						 parent.layer.close(index);
						 window.parent.successCallback('1');
					 }
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
}

$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("cid");
    var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
    if (oid) {
        $.getJSON("/visa/order/showDetail?orderid=" + oid, function (resp) {
        	viewModel.set("customer", $.extend(true, dafaults, resp));
        	dafaults.customermanage.telephone=resp.customermanage.telephone;
        	dafaults.customermanage.email=resp.customermanage.email;
        	var color = $("#cus_phone").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
        	var color = $("#cus_email").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
			var color = $("#cus_fullComName").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
			var color = $("#cus_linkman").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
	
			var flag=viewModel.get("customer.customerSource");
        	//comsource();//判断客户来源 状态 
			var flag=$("#customerSource").val();
			if(flag==3){//直客 
				$("#select").hide();
				$("#selectno").show();
				$('.companyFullName').addClass('hide');//隐藏 默认显示的 其他状态下的 公司全称
				$('.ZKcompanyFullName').removeClass('hide');//显示   默认显示的  直客  公司全称
				$("input[comResource='comResource']").each(function(){
					var labelTxt=$(this).parent().prev().text().trim();
					labelTxt = labelTxt.split(":");
					labelTxt.pop();
					labelTxt = labelTxt.join(":");
					//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
					$(this).attr('validationMessage',labelTxt+"不能为空");
					$(this).attr('required','required');
				});
				$("input[comManage='comManage']").each(function(){
					var labelTxt=$(this).parent().prev().text().trim();
					labelTxt = labelTxt.split(":");
					labelTxt.pop();
					labelTxt = labelTxt.join(":");
					//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
					$(this).removeAttr('validationMessage',labelTxt+"不能为空");
					$(this).removeAttr('required','required');
				});
				//验证需要设置相关的值
		/*		$("#cus_fullComName1").val(dafaults.customerresource.fullComName);
				$("#cus_linkman1").val(dafaults.customerresource.linkman);
				$("#cus_phone1").val(dafaults.customerresource.telephone);
				$("#cus_email1").val(dafaults.customerresource.email);*/
			}else{//其他
				$("#select").show();
				$("#selectno").hide();
				$('.companyFullName').removeClass('hide');//显示 默认显示的 其他状态下的 公司全称
				$('.ZKcompanyFullName').addClass('hide');//隐藏   默认显示的  直客  公司全称
				//验证需要设置相关的值
				$("#cus_fullComName").val(dafaults.customermanage.fullComName);
				$("#cus_linkman").val(dafaults.customermanage.linkman);
				$("#cus_phone").val(dafaults.customermanage.telephone);
				$("#cus_email").val(dafaults.customermanage.email);
				
				
				$("input[comManage='comManage']").each(function(){
					var labelTxt=$(this).parent().prev().text().trim();
					labelTxt = labelTxt.split(":");
					labelTxt.pop();
					labelTxt = labelTxt.join(":");
					//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
					$(this).attr('validationMessage',labelTxt+"不能为空");
					$(this).attr('required','required');
				});
				$("input[comResource='comResource']").each(function(){
					var labelTxt=$(this).parent().prev().text().trim();
					labelTxt = labelTxt.split(":");
					labelTxt.pop();
					labelTxt = labelTxt.join(":");
					//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
					$(this).removeAttr('validationMessage',labelTxt+"不能为空");
					$(this).removeAttr('required','required');
				});
			}
			if(indexnew!=null){
				layer.close(indexnew);
			}
        });
    }else{
    	if(indexnew!=null){
			layer.close(indexnew);
		}
    }
});
