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
 // 支付人
    payType: function (type) {
        return viewModel.get("customer.trip.paypersion") === type;
    },
    
});
kendo.bind($(document.body), viewModel);
/*****************************************************
 * 开始个人信息
 ****************************************************/

/*****************************************************
 * 开始出行信息
 ****************************************************/
//是否参团
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



function comsource(){
	var flag=$("#customerSource").val();
	if(flag==3){//直客 
		$("#select").hide();
		$("#selectno").show();
		$('.companyFullName').addClass('hide');//隐藏 默认显示的 其他状态下的 公司全称
		$('.ZKcompanyFullName').removeClass('hide');//显示   默认显示的  直客  公司全称
	}else{//其他
		$("#select").show();
		$("#selectno").hide();
		$('.companyFullName').removeClass('hide');//显示 默认显示的 其他状态下的 公司全称
		$('.ZKcompanyFullName').addClass('hide');//隐藏   默认显示的  直客  公司全称
	}
}


$(function () {
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
    			},
    			error : function(xhr) {
    			}
    		});
        	
        }
    });
    
});

$(function () {
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
    				
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
});
//联系人

$(function () {
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
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
});


//公司全称

$(function () {
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
    			},
    			error : function(xhr) {
    			}
    		});
        }
    });
});

//信息保存
//var validator = $("#orderForm").kendoValidator().data("kendoValidator");
function ordersave(){
	 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
			 $.ajax({
				 type: "POST",
				 url: "/visa/order/orderSave",
				 contentType: "application/json",
				 dataType: "json",
				 data: JSON.stringify(viewModel.customer),
				 success: function (result) {
					 if(indexnew!=null){
							
							layer.close(index);
							}
					 
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
        });
        comsource();
    }
    
    comsource();
});

	
	function comsource(){
		var flag=$("#customerSource").val();
		if(flag==3){//直客 
			$("#select").hide();
			$("#selectno").show();
			$('.companyFullName').addClass('hide');//隐藏 默认显示的 其他状态下的 公司全称
			$('.ZKcompanyFullName').removeClass('hide');//显示   默认显示的  直客  公司全称
		}else{//其他
			$("#select").show();
			$("#selectno").hide();
			$('.companyFullName').removeClass('hide');//显示 默认显示的 其他状态下的 公司全称
			$('.ZKcompanyFullName').addClass('hide');//隐藏   默认显示的  直客  公司全称
		}
	}

