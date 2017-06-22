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
	customermanage:{},
	tripJp:{},
	dateplanJpList:[],
	tripplanJpList:[],
	fastMail:{}
    },
    keys = {
		"customer.dateplanJpList":{},
		"customer.tripplanJpList":{
			daynum:"",
			nowdate:"",
			city:"",
			viewid:"",	
			hotelid:"",
			hometype:"",
			homenum:"",
			homeday:"",
			intime:"",
			outtime:"",
			breakfast:"",
			dinner:""

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
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
        viewModel.get(key).push(keys[key]);
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
    }
    
});
kendo.bind($(document.body), viewModel);


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
    				viewModel.set("customer.customermanage.customerSource",data.customerSource);
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
    				viewModel.set("customer.customermanage.customerSource",data.customerSource);
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
    				viewModel.set("customer.customermanage.customerSource",data.customerSource);
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
    				viewModel.set("customer.customermanage.customerSource",data.customerSource);
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

function orderJpsave(){
		
			 
			 $.ajax({
				 type: "POST",
				 url: "/visa/neworderjp/orderJpsave",
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
        $.getJSON("/visa/neworderjp/showDetail?orderid=" + oid, function (resp) {
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
    }
   
});



