
//客户来源
var customersourceEnum=[
    {text:"线上",value:1},
    {text:"OTS",value:2},
    {text:"直客",value:3},
    {text:"线下",value:4}
  ];

//出发城市
var startcity=[
                        {text:"北京",value:1},
                        {text:"东京",value:2},
                        {text:"名古屋",value:3},
                        {text:"大阪",value:4},
                        {text:"札幌",value:5},
                        {text:"那霸",value:6}
                        ];
var defaults = {
		customermanage:{},
		tripJp:{
			oneormore:0,
			trippurpose:"旅游"
		},
		dateplanJpList:[],
		tripplanJpList:[],
		fastMail:{},
		customerresourceJp:{}
}, 
keys = {
		"customer.dateplanJpList":{
			startdate:"",
			startcity:"",
			arrivecity:"",
			flightnum:"",
			returndate:"",
			returnstartcity:"",
			returnarrivecity:"",
			returnflightnum:""
		},
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
},
flights = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/flight/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
}), hotels = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/hotel/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
}), scenic = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/scenic/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});


var viewModel = kendo.observable({
	 customersourceEnum:customersourceEnum,
	 startcitynew:startcity,
    flights: flights,
    hotels: hotels,
    scenic: scenic,
    addOne: function (e) {
    	var key = $.isString(e) ? e : $(e.target).data('params');
        viewModel.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    onDateChange: function (e) {
        var target = e.sender.element.attr("id");
        var start = $("#start").data("kendoDatePicker");
        var end = $("#end").data("kendoDatePicker");
        if (target === "start") {
            end.min(start.value());
        } else {
            start.max(end.value());
        }
    },
    onSpotChange: function (e) {
        console.log(e);
    },
    getSpot: function (data) {
        if (data.spot) {
            return data.spot.split(",");
        }
    },
    customer: defaults,
});
kendo.bind($(document.body), viewModel);
$(function () {
	
	/*var df=new SimpleDateFormat();
	df.applyPattern("HH:mm");
	var date=new Date();
	var str=df.format(date);*/
	
	
	
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
    		/*		viewModel.set("customer.customermanage.customerSource",data.customerSource);*/
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
    				/*viewModel.set("customer.customermanage.customerSource",data.customerSource);*/
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
    			/*	viewModel.set("customer.customermanage.customerSource",data.customerSource);*/
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
    				/*viewModel.set("customer.customermanage.customerSource",data.customerSource);
    				*/viewModel.set("customer.customermanage.id",data.id);
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
		
			 console.log(JSON.stringify(viewModel.customer));

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
        	console.log(JSON.stringify(resp));
        	viewModel.set("customer", $.extend(true, defaults, resp));
        	
        	if(viewModel.get("customer.tripJp.oneormore")==1){
        		$('.WangFan').addClass('hide');
        		$('.DuoCheng').removeClass('hide');
        	}else{
        		$('.WangFan').removeClass('hide');
        		$('.DuoCheng').addClass('hide');
        	}
        	defaults.customermanage.telephone=resp.customermanage.telephone;
        	defaults.customermanage.email=resp.customermanage.email;
        	var color = $("#cus_phone").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
        	var color = $("#cus_email").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
			var color = $("#cus_fullComName").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
			var color = $("#cus_linkman").data("kendoMultiSelect");
			color.value(resp.customermanage.id);
			//客户来源加载显示判断
			 comsource();
			
			//时间格式
			/*var intimename=document.getEelementsByName("intimename");
			for(var i in intimername){
				alert(i);
			}*/
			
		/*	$("[name='intimename']").each(function(){
				var mm = $(this).val();
			});*/
			
        });
    }
    
    comsource();
   
});
/*$(function(){
	
	if(viewModel.get("customer.tripJp.oneormore")==1){
		$('.WangFan').addClass('hide');
		$('.DuoCheng').removeClass('hide');
	}else{
		$('.WangFan').removeClass('hide');
		$('.DuoCheng').addClass('hide');
	}
});*/
   $("#DuoCheng_WangFan").change(function(){
    	if($(this).is(':checked')){
    		viewModel.set("customer.tripJp.oneormore", true);
    		$('.WangFan').addClass('hide');
    		$('.DuoCheng').removeClass('hide');
    		
    		$.ajax({
   			 type: "POST",
   			 url: "/visa/neworderjp/autothree",
   			 contentType: "application/json",
   			 dataType: "json",
   			 data: JSON.stringify(viewModel.customer),
   			 success: function (result) {
   					viewModel.set("customer", $.extend(true, defaults, result));
   		        	
   		        	if(viewModel.get("customer.tripJp.oneormore")==1){
   		        		$('.WangFan').addClass('hide');
   		        		$('.DuoCheng').removeClass('hide');
   		        	}else{
   		        		$('.WangFan').removeClass('hide');
   		        		$('.DuoCheng').addClass('hide');
   		        	}
   		        	defaults.customermanage.telephone=resp.customermanage.telephone;
   		        	defaults.customermanage.email=resp.customermanage.email;
   		        	var color = $("#cus_phone").data("kendoMultiSelect");
   					color.value(resp.customermanage.id);
   		        	var color = $("#cus_email").data("kendoMultiSelect");
   					color.value(resp.customermanage.id);
   					var color = $("#cus_fullComName").data("kendoMultiSelect");
   					color.value(resp.customermanage.id);
   					var color = $("#cus_linkman").data("kendoMultiSelect");
   					color.value(resp.customermanage.id);
   					
   			 }
   		 });
    		
    		
    		
    		
    	}else{
    		viewModel.set("customer.tripJp.oneormore", false);
    		$('.WangFan').removeClass('hide');
    		$('.DuoCheng').addClass('hide');
    		
    		
    		$.ajax({
      			 type: "POST",
      			 url: "/visa/neworderjp/autothree",
      			 contentType: "application/json",
      			 dataType: "json",
      			 data: JSON.stringify(viewModel.customer),
      			 success: function (result) {
      					viewModel.set("customer", $.extend(true, defaults, result));
      		        	
      		        	if(viewModel.get("customer.tripJp.oneormore")==1){
      		        		$('.WangFan').addClass('hide');
      		        		$('.DuoCheng').removeClass('hide');
      		        	}else{
      		        		$('.WangFan').removeClass('hide');
      		        		$('.DuoCheng').addClass('hide');
      		        	}
      		        	defaults.customermanage.telephone=resp.customermanage.telephone;
      		        	defaults.customermanage.email=resp.customermanage.email;
      		        	var color = $("#cus_phone").data("kendoMultiSelect");
      					color.value(resp.customermanage.id);
      		        	var color = $("#cus_email").data("kendoMultiSelect");
      					color.value(resp.customermanage.id);
      					var color = $("#cus_fullComName").data("kendoMultiSelect");
      					color.value(resp.customermanage.id);
      					var color = $("#cus_linkman").data("kendoMultiSelect");
      					color.value(resp.customermanage.id);
      					
      			 }
      		 });
       		
    		
    		
    		
    	}
    });

   	function autogenerate(){
	   	 $.ajax({
			 type: "POST",
			 url: "/visa/neworderjp/autogenerate",
			 contentType: "application/json",
			 dataType: "json",
			 data: JSON.stringify(viewModel.customer),
			 success: function (result) {
					viewModel.set("customer", $.extend(true, defaults, result));
		        	
		        	if(viewModel.get("customer.tripJp.oneormore")==1){
		        		$('.WangFan').addClass('hide');
		        		$('.DuoCheng').removeClass('hide');
		        	}else{
		        		$('.WangFan').removeClass('hide');
		        		$('.DuoCheng').addClass('hide');
		        	}
		        	defaults.customermanage.telephone=resp.customermanage.telephone;
		        	defaults.customermanage.email=resp.customermanage.email;
		        	var color = $("#cus_phone").data("kendoMultiSelect");
					color.value(resp.customermanage.id);
		        	var color = $("#cus_email").data("kendoMultiSelect");
					color.value(resp.customermanage.id);
					var color = $("#cus_fullComName").data("kendoMultiSelect");
					color.value(resp.customermanage.id);
					var color = $("#cus_linkman").data("kendoMultiSelect");
					color.value(resp.customermanage.id);
					
			 }
		 });

   	}
   	
   	
   	function comsource(){
   		var flag=$("#customerSource").val();
   		if(flag==3){
   			$("#select").hide();
   			$("#selectno").show();
   		}else{
   			$("#select").show();
   			$("#selectno").hide();
   			
   		}
   	}
   	
   	