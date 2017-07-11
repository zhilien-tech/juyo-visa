/**
 * 
 * 
 * 日本 二级 编辑页面 js
 * 
 * 
 */



//客户来源
var customersourceEnum=[
    {text:"线上",value:1},
    {text:"OTS",value:2},
    {text:"直客",value:3},
    {text:"线下",value:4}
  ];
var proposers=new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/neworderjp/porposerdatasource?orderid="+$.queryString("cid"),
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});

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
		customerresourceJp:{},
		proposerInfoJpList:[]/*,
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
		        ]*/
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

		},
		"customer.proposerInfoJpList":{
			
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
	 proposers:proposers,
    flights: flights,
    hotels: hotels,
    scenic: scenic,
    addOne: function (e) {
    	var key = $.isString(e) ? e : $(e.target).data('params');
        //viewModel.get(key).push(keys[key]);
    	viewModel.get(key).push({
             lastName: "",
             firstName: "",
             passport: "",
             main: false,
             depositSource: "",
             depositMethod: "",
             depositSum: 0,
             depositCount: 1,
             receipt: false,
             insurance: false,
             outDistrict: false,
         });
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    
    delOneApplicant: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        if (all.length == 1) {
            $.layer.alert("至少要有一个申请人");
            return;
        }
        all.splice(all.indexOf(e.data), 1);
        if (all.length == 1) {//如果剩一个申请人则置为主申请人
            var item = all.pop();
            item.main = true;
            all.unshift(item);
        }
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
    customer: defaults,
    aaa:function(e){
    	 /* console.log(e.data.id);
    	  console.log(e.data.istogetherlinkman);*/
    	  //$("#main1_"+e.data.id).is(':checked')
   		if(!e.data.istogetherlinkman){
   			
   			var proposerInfoJpList=viewModel.get("customer.proposerInfoJpList");
   			///console.log(JSON.stringify(proposerInfoJpList));
   			var porposernow;
   			for(var i=0;i<proposerInfoJpList.length;i++){
   				var proposer=proposerInfoJpList[i];
   				//alert(proposer.istogetherlinkman);
   				if(proposer.istogetherlinkman==1&&proposer.id!=e.data.id){
   					porposernow=proposer;
   				}
   				//console.log(JSON.stringify(proposer));
   			}
   			if(porposernow!=null&&porposernow!=''){
   				
   				layer.confirm("原来的统一联系人为："+porposernow.fullname+"，您确认修改吗？", {
   					btn: ["是","否"], //按钮
   					shade: false //不显示遮罩
   				}, function(index){
   					
   					for(var i=0;i<proposerInfoJpList.length;i++){
   		   				var proposer=proposerInfoJpList[i];
   		   				//alert(proposer.istogetherlinkman);
   		   				if(proposer.id==porposernow.id){
   		   			//	proposer.istogetherlinkman=false;
   		   			viewModel.set("customer.proposerInfoJpList["+i+"].istogetherlinkman",false);
   		   				}
   		   				//console.log(JSON.stringify(proposer));
   		   			}
   					viewModel.set("customer.proposerInfoJpList",proposerInfoJpList);
   					///console.log(JSON.stringify(proposerInfoJpList));
   					layer.close(index);
   				},function(){
   					for(var i=0;i<proposerInfoJpList.length;i++){
   		   				var proposer=proposerInfoJpList[i];
   		   				//alert(proposer.istogetherlinkman);
   		   				if(proposer.id==e.data.id){
   		   				//proposer.istogetherlinkman=false;
   		   			viewModel.set("customer.proposerInfoJpList["+i+"].istogetherlinkman",false);

   		   				}
   		   				//console.log(JSON.stringify(proposer));
   		   			}
   					viewModel.set("customer.proposerInfoJpList",proposerInfoJpList);
   					///console.log(JSON.stringify(proposerInfoJpList));
   					
   				});
   				
   			}
   			
   		}
    	  
    	  
    }
});
kendo.bind($(document.body), viewModel);


	function comsource(){
		var flag=$("#customerSource").val();
		if(flag==3){//直客
			$("#select").hide();
			$("#selectno").show();
			$(".companyFullName").hide();
			$('.ZKcompanyFullName').removeClass("hide");// 显示 直客状态下的  公司全称
		}else{//其他的...
			$("#select").show();
			$("#selectno").hide();
			$(".companyFullName").show();
			$('.ZKcompanyFullName').addClass("hide");// 隐藏 直客状态下的  公司全称
		}
	}


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
    
    //折叠面板的显隐切换
    $(document).on("click", ".span-Title", function () {
        $(this).find(".k-icon").toggleClass("k-i-arrow-60-down k-i-arrow-n");
        $(this).addClass("k-state-selected");
        $(this).next().toggle();
        //$(this).next().addClass('div-context');//显示 主申请人的 样式
        //$(this).next().css('border',"solid 1px red");
        $(this).parents('.k-panelbar').siblings().find('.k-link').removeClass('k-state-selected');
        $(this).parents('.k-panelbar').siblings().find('.container-fluid').hide();
        
    });
    
    $(".k-header").click(function(){
    	$('.k-content').removeClass("div-context");
    	$(this).parent().siblings().find('.span-Title').removeClass('k-state-selected');
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
    				/*viewModel.set("customer.customermanage.customerSource",data.customerSource);*/
    				viewModel.set("customer.customermanage.id",data.id);
    				console.log("~~~~~~~~~~~"+viewModel.set("customer.customermanage.id",data.id));
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
		     ///console.log(JSON.stringify(viewModel.customer));
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
        	//console.log(JSON.stringify(resp));
        	viewModel.set("customer", $.extend(true, defaults, resp));
        	/*----小灯泡 回显----*/
        	var reason=viewModel.get("customer.errorinfo");
        	var map=new Map();
        	map=eval("("+reason+")");
        	console.log("map的值为："+map);
        	for (var key in map){
        		var a = map[key];//获取到 错误信息 数据
        		for(var i=0;i<a.length;i++){
        			var reasonnew=a[i].key+",";//获取到  错误信息 字段名称
        			console.log("reasonnew的值为："+reasonnew);
        		}
        	}
        	
        	/*----end 小灯泡 回显----*/
        	var proposerInfoJpList=viewModel.get("customer.proposerInfoJpList");
        	if(proposerInfoJpList.length>0){
        		
        		$(".mainApplicant").hide();
        	}
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
    
    comsource();//客户来源 状态 模块加载
   
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
   		        	defaults.customermanage.telephone=result.customermanage.telephone;
   		        	defaults.customermanage.email=result.customermanage.email;
   		        	var color = $("#cus_phone").data("kendoMultiSelect");
   					color.value(result.customermanage.id);
   		        	var color = $("#cus_email").data("kendoMultiSelect");
   					color.value(result.customermanage.id);
   					var color = $("#cus_fullComName").data("kendoMultiSelect");
   					color.value(result.customermanage.id);
   					var color = $("#cus_linkman").data("kendoMultiSelect");
   					color.value(result.customermanage.id);
   					
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
      		        	defaults.customermanage.telephone=result.customermanage.telephone;
      		        	defaults.customermanage.email=result.customermanage.email;
      		        	var color = $("#cus_phone").data("kendoMultiSelect");
      					color.value(result.customermanage.id);
      		        	var color = $("#cus_email").data("kendoMultiSelect");
      					color.value(result.customermanage.id);
      					var color = $("#cus_fullComName").data("kendoMultiSelect");
      					color.value(result.customermanage.id);
      					var color = $("#cus_linkman").data("kendoMultiSelect");
      					color.value(result.customermanage.id);
      					
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
		        	defaults.customermanage.telephone=result.customermanage.telephone;
		        	defaults.customermanage.email=result.customermanage.email;
		        	var color = $("#cus_phone").data("kendoMultiSelect");
					color.value(result.customermanage.id);
		        	var color = $("#cus_email").data("kendoMultiSelect");
					color.value(result.customermanage.id);
					var color = $("#cus_fullComName").data("kendoMultiSelect");
					color.value(result.customermanage.id);
					var color = $("#cus_linkman").data("kendoMultiSelect");

					color.value(result.customermanage.id);
					
			 }
		 });
   	}
   	
   	
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
   	
 
   	function addporposer(){
   		var renShu=$('#usa_arrival_date').val();
   		if(renShu!=""){//人数 不等于null时
   			$('.mainApplicant').siblings('li').find('.k-content').css("display","none");
   			$.ajax({
     			 type: "POST",
     			 url: "/visa/neworderjp/autoporposer",
     			 contentType: "application/json",
     			 dataType: "json",
     			 data: JSON.stringify(viewModel.customer),
     			 success: function (result) {
     				    //console.log(JSON.stringify(result));
     					viewModel.set("customer", $.extend(true, defaults, result));
     		        	if(viewModel.get("customer.tripJp.oneormore")==1){
     		        		$('.WangFan').addClass('hide');
     		        		$('.DuoCheng').removeClass('hide');
     		        	}else{
     		        		$('.WangFan').removeClass('hide');
     		        		$('.DuoCheng').addClass('hide');
     		        	}
     		        	defaults.customermanage.telephone=result.customermanage.telephone;
     		        	defaults.customermanage.email=result.customermanage.email;
     		        	var color = $("#cus_phone").data("kendoMultiSelect");
     					color.value(result.customermanage.id);
     		        	var color = $("#cus_email").data("kendoMultiSelect");
     					color.value(result.customermanage.id);
     					var color = $("#cus_fullComName").data("kendoMultiSelect");
     					color.value(result.customermanage.id);
     					var color = $("#cus_linkman").data("kendoMultiSelect");
     					color.value(result.customermanage.id);
     			 }
     		 });
   			 $(".mainApplicant").hide();//隐藏 span标签的 主申请人
   		}
   		
   		
   	}
   	
   	
   	function togetherlinkman(){
   		alert($(this).is(':checked'));
   		if(true){
   			
   			var proposerInfoJpList=viewModel.get("customer.proposerInfoJpList");
   			/*console.log(JSON.stringify(proposerInfoJpList));*/
   			var porposernow;
   			for(var i=0;i<proposerInfoJpList.length;i++){
   				var proposer=proposerInfoJpList[i];
   				//alert(proposer.istogetherlinkman);
   				if(proposer.istogetherlinkman==1){
   					porposernow=proposer;
   				}
   				//console.log(JSON.stringify(proposer));
   			}
   			if(porposernow!=null&&porposernow!=''){
   				
   				layer.confirm("原来的统一联系人为："+porposernow.fullname+"，您确认修改吗？", {
   					btn: ["是","否"], //按钮
   					shade: false //不显示遮罩
   				}, function(){
   					alert(111);
   				},function(){
   					alert(222);
   					
   				});
   				
   			}
   			
   		}
   		
   		
   		
   	}
