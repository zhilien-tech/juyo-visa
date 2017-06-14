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
			
		}
		
		
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
	}
;
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
    customer: dafaults,
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
    				$("#cus_fullComName").val(data.fullComName);
    				viewModel.set("customer.customermanage.fullComName",data.fullComName);
    				$("#cus_linkman").val(data.linkman);
    				viewModel.set("customer.customermanage.linkman",data.linkman);
    				viewModel.set("customer.customermanage.telephone",data.telephone);
    				viewModel.set("customer.customermanage.email",data.email);
    				viewModel.set("customer.customermanage.id",data.id);
    				//$("#cus_fullComName").val(data.fullComName);
    				var color = $("#cus_email").data("kendoMultiSelect");
    				color.value(data.id);
    				viewModel.set("customer.customermanage.email",data.email);
    				viewModel.set("customer.customermanage.customerSource",data.customerSource);
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
    				$("#cus_fullComName").val(data.telephone);
    				viewModel.set("customer.customermanage.fullComName",data.fullComName);
    				$("#cus_linkman").val(data.linkman);
    				viewModel.set("customer.customermanage.linkman",data.linkman);
    				viewModel.set("customer.customermanage.id",data.id);
    				$("#cus_fullComName").val(data.fullComName);
    				viewModel.set("customer.customermanage.telephone",data.telephone);
    				viewModel.set("customer.customermanage.customerSource",data.customerSource);
    				var color = $("#cus_phone").data("kendoMultiSelect");
    				color.value(data.id);
    				viewModel.set("customer.customermanage.email",data.email);
    				viewModel.set("customer.customermanage.email",data.email);
    			},
    			error : function(xhr) {
    			}
    		});
        	
        	
        	
        	
        	
        }
    });
});

//信息保存

function ordersave(){
	   console.log(JSON.stringify(viewModel.customer));
    $.ajax({
        type: "POST",
        url: "/visa/order/orderSave",
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




