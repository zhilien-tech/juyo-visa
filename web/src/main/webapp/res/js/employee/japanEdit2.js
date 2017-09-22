/**
 * 
 * 
 * 日本 一级 编辑页面 js
 * 
 * 
 */
var sixnum=[];
var threenum=[];
var arricity = null;
//客户来源
var customersourceEnum=[

                        {text:"线上",value:1},
                        {text:"OTS",value:2},
                        {text:"直客",value:3},
                        {text:"线下",value:4}
                        ];
var arricity = null;
var proposersnew=[];
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
               {text:"上海",value:2},
               {text:"东京",value:3},
               {text:"广州",value:4},
               {text:"沈阳",value:5},
               {text:"天津",value:6},
               {text:"石家庄",value:7},
               {text:"青岛",value:8},
               {text:"成都",value:9},
               {text:"重庆",value:10},
               {text:"西安",value:11},
               {text:"郑州",value:12},
               {text:"昆明",value:13},
               {text:"厦门",value:14},
               {text:"深圳",value:15},
               {text:"札幌",value:16},
               {text:"青森",value:17},
               {text:"盛冈",value:18},
               {text:"仙台",value:19},
               {text:"秋田",value:20},
               {text:"山形",value:21},
               {text:"福岛",value:22},
               {text:"水户",value:23},
               {text:"宇都宫",value:24},
               {text:"前桥",value:25},
               {text:"埼玉",value:26},
               {text:"千叶",value:27},
               {text:"新宿",value:28},
               {text:"横滨",value:29},
               {text:"箱根",value:30},
               {text:"新潟",value:31},
               {text:"富山",value:32},
               {text:"金泽",value:33},
               {text:"福井",value:34},
               {text:"甲府",value:35},
               {text:"长野",value:36},
               {text:"岐阜",value:37},
               {text:"静冈",value:38},
               {text:"名古屋",value:39},
               {text:"津市",value:40},
               {text:"大津",value:41},
               {text:"大阪",value:43},
               {text:"神户",value:44},
               {text:"奈良",value:45},
               {text:"和歌山",value:46},
               {text:"鸟取",value:47},
               {text:"松江",value:48},
               {text:"冈山",value:49},
               {text:"广岛",value:50},
               {text:"山口",value:51},
               {text:"徳岛",value:52},
               {text:"高松",value:53},
               {text:"松山",value:54},
               {text:"高知",value:55},
               {text:"福冈",value:56},
               {text:"佐贺",value:57},
               {text:"长崎",value:58},
               {text:"熊本",value:59},
               {text:"大分",value:60},
               {text:"宫崎",value:61},
               {text:"鹿儿岛",value:62},
               {text:"那霸",value:63},
               {text:"兵库",value:64}

               ];

var defaults = {
		visatype:0,
		area:0,
		customermanage:{},
		tripJp:{
			oneormore:0,
			trippurpose:"旅游",
			gofilght:{},
			returnfilght:{}
		},
		dateplanJpList:[],
		tripplanJpList:[],
		fastMail:{},
		customerresourceJp:{},
		proposerInfoJpList:[]
		
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
			scenicIds:"",
			viewid:"",	
			hotelid:"",
			hometype:"",
			homenum:"",
			homeday:"",
			intime:"",
			outtime:"",
			breakfast:"",
			dinner:"",
			scenics:[]
		},
		"customer.proposerInfoJpList":{}
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
				if(options.filter.filters[0]==null||options.filter.filters[0]==''||options.filter.filters[0]==undefined){
					return null;
				}
				return {filter: options.filter.filters[0].value};
			}
		},
	}

});

//计算时区差
function gmtZone(){
	var now = new Date();
	var chinaZone = -8;
	var gmtHours = now.getTimezoneOffset()/60;
	console.log("与东八区的时区差："+(chinaZone-gmtHours));
	var zoneSub = chinaZone-gmtHours;
	return zoneSub;
}


var viewModel = kendo.observable({
	customersourceEnum:customersourceEnum,
	startcitynew:startcity,
	proposers:proposersnew,
	flights:flights,
	addOne: function (e) {
		var key = $.isString(e) ? e : $(e.target).data('params'); //customer.tripplanJpList
		//viewModel.get(key).push(keys[key]);
		var tripplanJpNum = viewModel.customer.tripplanJpList.length + 1;
		viewModel.customer.tripplanJpList.push({
			daynum:tripplanJpNum,
			nowdate:"",
			city:"",
			scenicIds:"",
			viewid:"",	
			hotelid:"",
			hometype:"",
			homenum:"",
			homeday:"",
			intime:"",
			outtime:"",
			breakfast:"",
			dinner:"",
			scenics:[]
		});
	},
	addOneDatePlan: function (e) {
		viewModel.customer.dateplanJpList.push({
				startdate:"",
				startcity:"",
				arrivecity:"",
				flightnum:"",
				returndate:"",
				returnstartcity:"",
				returnarrivecity:"",
				returnflightnum:""
		});
	},
	delOne: function (e) {
		var key = $(e.target).data('params');
		var all = viewModel.get(key);
		var currentDaynum = e.data.daynum;
		//手动更新界面
		$("input#traveler_last_daynum_cn").each(function(){			
			var daynum = this.value ;
			if(daynum > currentDaynum){
				$(this).val(daynum - 1);
			}
		});
		
		$(all).each(function(index,element){
			var daynum = element.daynum;
			if(daynum > currentDaynum){
				element.daynum =  daynum - 1 ;
			}
		});
		
		all.splice(all.indexOf(e.data), 1);
		console.log(e.data.daynum);
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
	togetherClick:function(e){
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
	},
	flightChange: function(e){
		var dataUid = e.data.uid;
		var flightNum = $("#flightMore_select_"+dataUid).val();
		e.data.flightnum = flightNum;
	},
	scenicChange: function(e){
		//景区
		var dataUid = e.data.uid; 
		arricity = $("#arricity_"+dataUid).val();
		viewModel.set("customer.tripplanJpList.city",arricity);

		//景区
		scenicDs = new kendo.data.DataSource({
			serverFiltering: true,
			transport: {
				read: {
					dataType: "json",
					url: "/visa/scenic/arricity",
					data:{
						arricity:arricity
					}
				}
			}
		});
		//viewModel.set("scenic",scenicDs);
		var multiSelect = $("#scenic_select_"+dataUid).data("kendoMultiSelect");
		//console.log(JSON.stringify(scenicDs));

		//默认值
		multiSelect.dataSource.filter({}); //clear applied filter before setting value
		multiSelect.setDataSource(scenicDs);
		//scenic.data([]) ;
		
		var scenicList = [] ;
		var viewIdStr = "";
		$.ajax({
			type: 'GET',
			async: false,
			url: "/visa/scenic/arricity?arricity="+arricity,
			contentType: "application/json",
			dataType: 'json',
			success: function (result) {
				var selectList = [];
				for(var i=0; i<result.length && i<4; i++){
					selectList.push(result[i].id);
					viewIdStr += result[i].id +",";
					
					var scenic = {} ;
					scenic.id=result[i].id;
					scenicList.push(scenic) ;
				}
				multiSelect.value(selectList);
				
				var tripPlanId = $("#tripPlanId_"+dataUid).val();
				var tripPlanDayNum = $("#tripPlanDayNum_"+dataUid).val();
				var tripplanJpList = viewModel.customer.tripplanJpList;
	
//				$(tripplanJpList).each(function(index,element){
//					var eleId = element.id;
//					if(tripPlanId <= 0){
//						//添加
//						var tripIndex = index+1;
//						if( tripIndex == tripPlanDayNum){
//							element.viewid = viewIdStr;
//							element.scenics = scenicList;
//						}
//					}else{
//						//编辑
//						if( tripPlanId == eleId){
//							element.viewid = viewIdStr;
//							element.scenics = scenicList;
//						}
//					}
//					
//				});
				

			}
		});
		e.data.scenics = scenicList;
		e.data.viewid = viewIdStr;

		//酒店
		hotelDs = new kendo.data.DataSource({
			/*serverFiltering: true,
			transport: {
				read: {
					dataType: "json",
					url: "/visa/hotel/arricity",
					data:{
						arricity:arricity
					}
				},
				parameterMap: function (options, type) {
					if (options.filter) {
						if(options.filter.filters[0]==null||options.filter.filters[0]==''||options.filter.filters[0]==undefined){
							return null;
						}
						return {filter: options.filter.filters[0].value};
					}
				}
			}*/
			serverFiltering: true,
			filter:"contains",
			transport: {
				read: {
					type: "POST",
					dataType: "json",
					url: "/visa/hotel/arricityfilter"
				},
				parameterMap:function(data,type){
					if (data.filter) {
						if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
							return { toCity:arricity};
						}
						return { toCity:arricity,filterValue:data.filter.filters[0].value};
					}						
				}
				
			}
		});
		var multiSelect_hotel = $("#hotel_select_"+dataUid).data("kendoDropDownList");

		//默认值
		multiSelect_hotel.dataSource.filter({}); //clear applied filter before setting value
		multiSelect_hotel.setDataSource(hotelDs);

		var hotelId_add = 0;
		$.ajax({
			type: 'GET',
			async: false,
			url: "/visa/hotel/arricity?arricity="+arricity,
			contentType: "application/json",
			dataType: 'json',
			success: function (result) {
				var selectList = [];
				if(result.length > 0){
					selectList.push(result[0].id);
					multiSelect_hotel.value(selectList);
					
					var tripPlanId = $("#tripPlanId_"+dataUid).val();
					var tripPlanDayNum = $("#tripPlanDayNum_"+dataUid).val();
					var tripplanJpList = viewModel.customer.tripplanJpList;
					hotelId_add = result[0].id;
					/*$(tripplanJpList).each(function(index,element){
						var eleId = element.id;
						if(tripPlanId <= 0){
							//添加
							var tripIndex = index+1;
							if( tripIndex == tripPlanDayNum){
								element.hotelid = result[0].id;
							}
						}else{
							if( tripPlanId == eleId){
								element.hotelid = result[0].id;
							}
						}
					});*/
					
				}
			}
		});
		e.data.hotelid = hotelId_add;
	},
	changeismainproposer:function(e){
		///console.log(e.data.id);
		///console.log(e.data);
		///console.log(e.data.fullname);
		var person=new Object();
		person.text=e.data.xing+e.data.name;
		person.value=e.data.id;
		if(!e.data.ismainproposer){
			for(var i=0;i<proposersnew.length;i++){
				if(proposersnew[i].value+""==(e.data.id)+""){
					proposersnew.splice(i, 1);
				}
			}
			proposersnew.push(person);
			viewModel.set("proposers",proposersnew);
			var proposerInfoJpList=viewModel.get("customer.proposerInfoJpList");
			///console.log(proposerInfoJpList);
			/*for(var m=0;m<proposerInfoJpList.length;m++){
    				if(proposerInfoJpList[m].id==e.data.id){
    					viewModel.set("customer.proposerInfoJpList["+m+"].ismainproposer",true);
					}
    			}*/
			viewModel.set("customer.proposerInfoJpList",viewModel.get("customer.proposerInfoJpList"));
			$("#ismainproposer_"+e.data.id).text("主申请人("+e.data.xing+e.data.name+")");
		}else{
			for(var i=0;i<proposersnew.length;i++){
				/*if(proposersnew[i].text+""==(e.data.xing+e.data.name)+""){
    				proposersnew.splice(i, 1);
    			}*/
				if(proposersnew[i].value+""==(e.data.id)+""){
					proposersnew.splice(i, 1);
				}
			}
			viewModel.set("proposers",proposersnew);
			$("#ismainproposer_"+e.data.id).text("副申请人("+e.data.xing+e.data.name+")");
		}
	},
	updateData:function(e){
		var person=new Object();
		person.text=(e.data.xing+''+e.data.name);
		person.value=e.data.id;
		//alert(e.data.ismainproposer);
		if(e.data.ismainproposer){
			for(var i=0;i<proposersnew.length;i++){
				if(proposersnew[i].value+""==(e.data.id)+""){
					proposersnew.splice(i, 1);
				}
			}
			proposersnew.push(person);
			viewModel.set("proposers",proposersnew);
		}else{
			for(var i=0;i<proposersnew.length;i++){
				if(proposersnew[i].value+""==(e.data.id)+""){
					proposersnew.splice(i, 1);
				}
			}
			viewModel.set("proposers",proposersnew);
		}

	},
	updateData1:function(e){
		var person=new Object();
		person.text=(e.data.xing+''+e.data.name);
		person.value=e.data.id;
		/*alert(e.data.ismainproposer);*/
		if(e.data.ismainproposer){
			for(var i=0;i<proposersnew.length;i++){
				if(proposersnew[i].value+""==(e.data.id)+""){
					proposersnew.splice(i, 1);
				}
			}
			proposersnew.push(person);
			viewModel.set("proposers",proposersnew);
		}else{
			for(var i=0;i<proposersnew.length;i++){
				if(proposersnew[i].value+""==(e.data.id)+""){
					proposersnew.splice(i, 1);
				}
			}
			viewModel.set("proposers",proposersnew);
		}
	},
	travelFromCitySingle_G:function(e){
		viewModel.set("customer.tripJp.returnarrivecity",viewModel.get("customer.tripJp.startcity"));
		//往返 去程航班
		goFlightByCity(e,"fromCitySingle_go", "toCitySingle_go", "flight_select_go_single", "singleGo");
		//往返 返程航班
		goFlightByCity(e,"fromCitySingle_return", "toCitySingle_return", "flight_select_return_single", "singleReturn");
		
	},
	travelToCitySingle_G:function(e){
		viewModel.set("customer.tripJp.returnstartcity",viewModel.get("customer.tripJp.arrivecity"));
		viewModel.set("customer.tripplanJpList.city",viewModel.get("customer.tripJp.arrivecity"));
		//往返 去程航班
		goFlightByCity(e,"fromCitySingle_go", "toCitySingle_go", "flight_select_go_single", "singleGo");
		//往返 返程航班
		goFlightByCity(e,"fromCitySingle_return", "toCitySingle_return", "flight_select_return_single", "singleReturn");
		
	},
	travelFromCitySingle_R:function(e){
		//往返 返程航班
		goFlightByCity(e,"fromCitySingle_return", "toCitySingle_return", "flight_select_return_single", "singleReturn");
		
	},
	travelToCitySingle_R:function(e){
		//往返 返程航班
		goFlightByCity(e,"fromCitySingle_retrun", "toCitySingle_return", "flight_select_return_single", "singleReturn");
		
	},
	travelFromCityMore:function(e){
		var dataUid = e.data.uid;
		goFlightByCity(e,"fromCityMore_select_"+dataUid, "toCityMore_select_"+dataUid, "flightMore_select_"+dataUid, "moreType");
	},
	travelToCityMore:function(e){
		//出行信息， 多程抵达城市
		//航班联动
		var dataUid = e.data.uid;
		goFlightByCity(e,"fromCityMore_select_"+dataUid, "toCityMore_select_"+dataUid, "flightMore_select_"+dataUid, "moreType");
		//出行信息 多程抵达城市
		var dataPlanList =viewModel.customer.dateplanJpList;
		
		var index = dataPlanList.indexOf(e.data);
		
		var b=index+1;
		if(dataPlanList.length > b){
			var dataUid = viewModel.get("customer.dateplanJpList["+b+"]").uid;
			//改变页面显示数据
			viewModel.set("customer.dateplanJpList["+b+"].startcity",e.data.arrivecity);
			viewModel.set("customer.dateplanJpList["+b+"].flightnum","");
			
			//处理传到后台的数据
			var datePlan = dataPlanList[b];
			datePlan.startcity=e.data.arrivecity;
			
			var toCity = $("#toCityMore_select_"+dataUid).val();
			if(null==toCity || undefined==toCity || ""==toCity){
				toCity = "北京";
			}
			datePlan.arrivecity = toCity;
			datePlan.flightnum = $("#flightMore_select_"+dataUid).val(); //暂未获取到
			
			var startCity = viewModel.get("customer.dateplanJpList["+b+"].startcity");
			if(undefined != startCity){
				//出行信息 多程 下一个
				nextFlightByCity(e,"fromCityMore_select_"+dataUid, "toCityMore_select_"+dataUid, "flightMore_select_"+dataUid,datePlan);
			}
		}
		
	},
	
});
kendo.bind($(document.body), viewModel);


//加载航班数据源
/*$(function () {
	var fromCity = $("#fromCitySingle_go").val();
	var toCity = $("#toCitySingle_go").val();
	console.log("页面加载："+ fromCity+"---"+ toCity);
	var flightNew = new kendo.data.DataSource({
		serverFiltering: true,
		transport: {
			read: {
				dataType: "json",
				url: "/visa/flight/filghtByCity",
				data:{
					fromCity: fromCity,
					toCity: toCity
				}
			}
		}
	});
	var singleSelect_flight = $("#flight_select_go_single").data("kendoDropDownList");
	singleSelect_flight.setDataSource(flightNew);
	//singleSelect_flight.value();
});*/

function comsource(){
	viewModel.set("customer.customermanage.fullComName",'');
	viewModel.set("customer.customermanage.linkman",'');
	viewModel.set("customer.customermanage.email",'');
	viewModel.set("customer.customermanage.telephone",'');
	viewModel.set("customer.customerresourceJp.linkman",'');
	viewModel.set("customer.customerresourceJp.fullComName",'');
	viewModel.set("customer.customerresourceJp.email",'');
	viewModel.set("customer.customerresourceJp.telephone",'');
	var flag=$("#customerSource").val();
	if(flag==3){//直客
		$("#select").hide();
		$("#selectno").show();
		$(".companyFullName").hide();
		$('.ZKcompanyFullName').removeClass("hide");// 显示 直客状态下的  公司全称


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
	}else{//其他的...
		$("#select").show();
		$("#selectno").hide();
		$(".companyFullName").show();
		$('.ZKcompanyFullName').addClass("hide");// 隐藏 直客状态下的  公司全称

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
	/*var df=new SimpleDateFormat();
	df.applyPattern("HH:mm");
	var date=new Date();
	var str=df.format(date);*/
	comsource();//页面加载时，初始化客户来源字段 

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

	//订单信息 签证类型
	$('.dongliuXian').hide();
	$('.dongSanXian').hide();
	$('select[name="visatype"]').change(function(){
		var selVal=$(this).val();
		/*if(selVal==2){//状态为 东三县
    		$('.dongSanXian').show();
    		$('.dongliuXian').hide();
    	}else if(selVal==3){//状态为 新三县
    		$('.dongSanXian').hide();
    		$('.dongliuXian').show();
    	}else{
    		$('.dongSanXian').hide();
    		$('.dongliuXian').hide();
    	}*/
		$('.dongxiancheck input').each(function(){
			$(this).attr('checked',false);
		});
		if(selVal==2 || selVal==3 || selVal==4){//东三县，新三县，冲绳，选择时，下方均出现7个县，允许多选
			$('.dongSanXian').hide();
			$('.dongliuXian').show();
		}else{
			$('.dongSanXian').hide();
			$('.dongliuXian').hide();
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
					console.log("~~~~~~~~~~~"+viewModel.set("customer.customermanage.id",data.id));
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
});
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];


var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
//信息保存
function orderJpsave(){
	
	//只要有一个被选中
	var isMainFlag = false;
	$(".isMainProposerClass").each(function(index,element){
		if($(element).is(':checked')){
			isMainFlag = true;
		}
	});
	
	//主申请人非空校验
	$(".divMainRelationRow").each(function(index,element){
		var divMainRelationRowId = element.id;
		
		var dataUid = divMainRelationRowId.split("_")[1];
		var xing = $("#xing_"+dataUid).val();
		var name = $("#name_"+dataUid).val();
		var main = $("#main_"+dataUid).is(':checked');
		var relationproposer = $("#relationproposer_"+dataUid);
		var relation = $("#relation_"+dataUid);
		
		
		if(isMainFlag){
			if(!main){
				//不是主申请人
				relationproposer.attr("required","true");
				relationproposer.attr("validationMessage","关联的主申请人不能为空");
				relationproposer.nextAll().remove();
				relationproposer.after('<span class="k-invalid-msg" data-for="relationproposer_'+dataUid+'"></span>');
				
				relation.attr("required","true");
				relation.attr("validationMessage","与主申请人关系不能为空");
				relation.nextAll().remove();
				relation.after('<span class="k-invalid-msg" data-for="relation_'+dataUid+'"></span>');
			}else{
				relationproposer.removeAttr("required");
				relationproposer.removeAttr("validationMessage");
				relationproposer.nextAll().remove();
				relation.removeAttr("required");
				relation.removeAttr("validationMessage");
				relation.nextAll().remove();
			}
		}else{
			if( (xing!=null && xing!="" && xing!=undefined) || (name!=null && name!="" && name!=undefined) || main){
				relationproposer.attr("required","true");
				relationproposer.attr("validationMessage","关联的主申请人不能为空");
				relationproposer.nextAll().remove();
				relationproposer.after('<span class="k-invalid-msg" data-for="relationproposer_'+dataUid+'"></span>');
				
				relation.attr("required","true");
				relation.attr("validationMessage","与主申请人关系不能为空");
				relation.nextAll().remove();
				relation.after('<span class="k-invalid-msg" data-for="relation_'+dataUid+'"></span>');
				
			}else{
				relationproposer.removeAttr("required");
				relationproposer.removeAttr("validationMessage");
				relationproposer.nextAll().remove();
				relation.removeAttr("required");
				relation.removeAttr("validationMessage");
				relation.nextAll().remove();
			}
		}
		
	});
	
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		//判断是否为日本地接社添加的
		var island=$.queryString("island");
		if(island==1){
			viewModel.set("customer.island",1);
		}

		//对东三县东六县的值进行处理
		var visatype=viewModel.get("customer.visatype");
		if(visatype==2 || visatype==3 || visatype==4){
			var sixnumstr="";
			var a=sixnum.length;	
			if(sixnum.length>0){
				for(var i=0;i<sixnum.length;i++){
					sixnumstr+=sixnum[i]+",";
				}
				var sixa=viewModel.get("customer.sixnum");
				if(sixa!=null&&sixa!=''&&sixa!=undefined){
					viewModel.set("customer.threenum","");
					viewModel.set("customer.sixnum",viewModel.get("customer.sixnum")+sixnumstr);
				}else{
					viewModel.set("customer.threenum","");
					viewModel.set("customer.sixnum",sixnumstr);
				}
			}else{
				$('.dongxiancheck input').each(function(){
					if($(this).is(':checked')){
						sixnumstr+=$(this).attr("id")+",";
					}
				});
				viewModel.set("customer.threenum","");
				viewModel.set("customer.sixnum",sixnumstr);
			}
		}else{
			viewModel.set("customer.sixnum","");
			viewModel.set("customer.threenum","");

		}
		var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 

		$.ajax({
			type: "POST",
			url: "/visa/neworderjp/orderJpsave",
			contentType: "application/json",
			dataType: "json",
			data: JSON.stringify(viewModel.customer),
			success: function (result) {
				//console.log(result.code);
				if(result.code=="SUCCESS"){
					if(indexnew!=null){
						layer.close(indexnew);
					}

					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
					//$.layer.closeAll();
					parent.layer.close(index);
					window.parent.successCallback('1');

				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				if(indexnew!=null){

					layer.close(indexnew);
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
			var flag = 1;
			var relationFlag = 1;
			for(var i=0;i<emptyNum.length;i++){
				var emptyStr = emptyNum[i].text+",";
				if(emptyNum[i].text == "关联的主申请人"){
					if(flag == 0){
						emptyStr = "";
					}
					flag = 0;
				}
				if(emptyNum[i].text == "与主申请人关系"){
					if(relationFlag == 0){
						emptyStr = "";
					}
					relationFlag = 0;
				}
				str += emptyStr;
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


//编辑订单信息
$(function () {
	//如果有传递ID就是修改
	var oid = $.queryString("cid");
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
	if (oid) {
		$.getJSON("/visa/neworderjp/showDetail?orderid=" + oid, function (resp) {
			//console.log(JSON.stringify(resp));
			viewModel.set("customer", $.extend(true, defaults, resp));

			//单程 动态设置航班数据源
			var tripJp = viewModel.customer.tripJp;
			//去程
			var fromCityGo = tripJp.startcity;
			if(fromCityGo == null || fromCityGo == undefined || fromCityGo == ""){
				fromCityGo = "北京";
			}
			var toCityGo = tripJp.arrivecity;
			if(toCityGo == null || toCityGo == undefined || toCityGo == ""){
				toCityGo = "北京";
			}
			//航班
			var flightGoDS = new kendo.data.DataSource({
				/*serverFiltering: true,
				transport: {
					read: {
						dataType: "json",
						url: "/visa/flight/filghtByCity",
						data:{
							fromCity:fromCityGo, 
							toCity:toCityGo
						}
					}
				}*/
				serverFiltering: true,
				filter:"contains",
				transport: {
					read: {
						type: "POST",
						dataType: "json",
						url: "/visa/flight/filghtByCityFilter"
					},
					parameterMap:function(data,type){
						if (data.filter) {
							if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
								return { fromCity:fromCityGo, toCity:toCityGo};
							}
							return { fromCity:fromCityGo, toCity:toCityGo,filterValue:data.filter.filters[0].value};
						}						
					}
				}
			});
			var singleSelect_flight_Go = $("#flight_select_go_single").data("kendoDropDownList");
			singleSelect_flight_Go.setDataSource(flightGoDS);
			singleSelect_flight_Go.value(tripJp.flightnum);
			//返程
			var fromCityRetrun = tripJp.returnstartcity;
			if(fromCityRetrun == null || fromCityRetrun == undefined || fromCityRetrun == ""){
				fromCityRetrun = "北京";
			}
			var toCityReturn = tripJp.returnarrivecity;
			if(toCityReturn == null  || toCityReturn == undefined || toCityReturn == ""){
				toCityReturn = "北京";
			}
			//航班
			var flightRetrunDS = new kendo.data.DataSource({
				/*serverFiltering: true,
				transport: {
					read: {
						dataType: "json",
						url: "/visa/flight/filghtByCity",
						data:{
							fromCity:fromCityRetrun, 
							toCity:toCityReturn
						}
					}
				}*/
				serverFiltering: true,
				filter:"contains",
				transport: {
					read: {
						type: "POST",
						dataType: "json",
						url: "/visa/flight/filghtByCityFilter"
					},
					parameterMap:function(data,type){
						if (data.filter) {
							if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
								return { fromCity:fromCityRetrun, toCity:toCityReturn};
							}
							return { fromCity:fromCityRetrun, toCity:toCityReturn,filterValue:data.filter.filters[0].value};
						}						
					}
					
				}
			});
			var singleSelect_flight_Return = $("#flight_select_return_single").data("kendoDropDownList");
			singleSelect_flight_Return.setDataSource(flightRetrunDS);
			singleSelect_flight_Return.value(tripJp.returnflightnum);
			
			
			//多程  动态设置航班数据源
			var dateplanJpList =  viewModel.customer.dateplanJpList;
			$(dateplanJpList).each(function(index, element){
				var dataUid = element.uid;
				var fromCity = element.startcity;
				var toCity = element.arrivecity;
				if(fromCity == null || fromCity == undefined || fromCity == ""){
					fromCity = "北京";
				}
				if(toCity == null  || toCity == undefined || toCity == ""){
					toCity = "北京";
				}
				//航班
				var flightDS = new kendo.data.DataSource({
					/*serverFiltering: true,
					transport: {
						read: {
							dataType: "json",
							url: "/visa/flight/filghtByCity",
							data:{
								fromCity:fromCity, 
								toCity:toCity
							}
						}
					}*/
					serverFiltering: true,
					filter:"contains",
					transport: {
						read: {
							type: "POST",
							dataType: "json",
							url: "/visa/flight/filghtByCityFilter",
						},
						parameterMap:function(data,type){
							if (data.filter) {
								if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
									return { fromCity:fromCity, toCity:toCity};
								}
								return { fromCity:fromCity, toCity:toCity,filterValue:data.filter.filters[0].value};
							}						
						}
					}
				});
				var singleSelect_flight = $("#flightMore_select_"+dataUid).data("kendoDropDownList");
				singleSelect_flight.setDataSource(flightDS);
				singleSelect_flight.value(element.flightnum);
			});
			
			
			var visatype=viewModel.get("customer.visatype");
			if(visatype==2 || visatype==3 || visatype==4){ //东三县，新三县，冲绳，选择时，下方均出现7个县，允许多选
				var sixnumstr=viewModel.get("customer.sixnum");
				$('.dongSanXian').hide();
				$('.dongliuXian').show();
				if(sixnumstr!=null&&sixnumstr!=''){

					var result=sixnumstr.split(",");
					for(var i=0;i<result.length;i++){
						$("#"+result[i]).attr("checked", true);
					}
				}

			}/*else if(visatype==2){
    			var threenumstr=viewModel.get("customer.threenum");
    			$('.dongSanXian').show();
        		$('.dongliuXian').hide();
    			if(threenumstr!=null&&threenumstr!=''){

    				var result=threenumstr.split(",");
    				for(var i=0;i<result.length;i++){
    					$("#"+result[i]+"t").attr("checked", true);
    				}
    			}
    		}*/else{
    			$('.dongSanXian').hide();
    			$('.dongliuXian').hide();
    		}






			/*----小灯泡 回显----*/
			var reason=viewModel.get("customer.errorinfo");
			var map=new Map();
			map=eval("("+reason+")");
			///console.log("map的值为："+map);
			for (var key in map){
				var a = map[key];//获取到 错误信息 数据
				for(var i=0;i<a.length;i++){
					var reasonnew=a[i].key+",";//获取到  错误信息 字段名称
					///console.log("reasonnew的值为："+reasonnew);
				}
			}
			/*----end 小灯泡 回显----*/

			var proposerInfoJpList=viewModel.get("customer.proposerInfoJpList");
			for(var i=0;i<proposerInfoJpList.length;i++){
				var ismain=proposerInfoJpList[i].isMainProposer;
				if(ismain){
					//viewModel.set("customer.proposerInfoJpList["+i+ "].ismainproposer",true);
					var person=new Object();
					person.text=proposerInfoJpList[i].fullname;
					person.value=proposerInfoJpList[i].id;

					proposersnew.push(person);
					//alert(JSON.stringify(proposersnew));
				}
			}
			viewModel.set("proposers",proposersnew);
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
			// comsource();
			var flag=$("#customerSource").val();
			if(flag==3){//直客
				$("#select").hide();
				$("#selectno").show();
				$(".companyFullName").hide();
				$('.ZKcompanyFullName').removeClass("hide");// 显示 直客状态下的  公司全称

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
			}else{//其他的...
				$("#select").show();
				$("#selectno").hide();
				$(".companyFullName").show();
				$('.ZKcompanyFullName').addClass("hide");// 隐藏 直客状态下的  公司全称
				//验证需要设置相关的值
				$("#cus_fullComName").val(defaults.customermanage.fullComName);
				$("#cus_linkman").val(defaults.customermanage.linkman);
				$("#cus_phone").val(defaults.customermanage.telephone);
				$("#cus_email").val(defaults.customermanage.email);
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
	//comsource();//客户来源 状态 模块加载
});

$("#DuoCheng_WangFan").change(function(){
	if($(this).is(':checked')){
		viewModel.set("customer.tripJp.oneormore", true);
		$('.WangFan').addClass('hide');
		$('.DuoCheng').removeClass('hide');
		var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
		$.ajax({
			type: "POST",
			url: "/visa/neworderjp/autothree",
			contentType: "application/json",
			dataType: "json",
			data: JSON.stringify(viewModel.customer),
			success: function (result) {
				viewModel.set("customer", $.extend(true, defaults, result));
				if(indexnew!=null){
					layer.close(indexnew);
				}
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
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
	$.ajax({
		type: "POST",
		url: "/visa/neworderjp/autogenerate",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(viewModel.customer),
		success: function (result) {
			$("#trip_div").removeClass("hide");
			viewModel.set("customer", $.extend(true, defaults, result));
			viewModel.set("customer.tripplanJpList",result.tripplanJpList);
			//获取所有的景区下拉列表jquery元素，遍历获取每一个的id，得到此id对应城市
			$('[name="scenic_name"]').each(function(index, element){
				var scenicId = $(this).attr("id");
				var dataUid = scenicId.split("scenic_select_")[1];
				var cityId = "arricity_" + dataUid;
				var scenicInput = $("#scenic_input_"+dataUid).val();
				var hotelId = "hotel_select_" + dataUid;
				var hotelInput = $("#hotel_input_"+dataUid).val();
				var arricity = $("#"+cityId).val();
				
				//scenic
				var scenicNew = new kendo.data.DataSource({
					serverFiltering: true,
					transport: {
						read: {
							dataType: "json",
							url: "/visa/scenic/arricity",
							data:{
								arricity:arricity
							}
						}
					}
				})
				var scenicSelect = $("#"+scenicId).data("kendoMultiSelect");
				scenicSelect.setDataSource(scenicNew);
				scenicSelect.value(scenicInput.split(","));
				$(scenicInput).each(function(index, element){
					console.log(element.id);
				});

				//酒店
				var hotelNew = new kendo.data.DataSource({
					/*serverFiltering: true,
					transport: {
						read: {
							dataType: "json",
							url: "/visa/hotel/arricity",
							data:{
								arricity:arricity
							}
						}
					},
					parameterMap: function (options, type) {
						if (options.filter) {
							if(options.filter.filters[0]==null||options.filter.filters[0]==''||options.filter.filters[0]==undefined){
								return null;
							}
							return {filter: options.filter.filters[0].value};
						}
					},*/
					ignoreCase:true,
					serverFiltering: true,
					filter:"contains",
					transport: {
						read: {
							type: "POST",
							dataType: "json",
							url: "/visa/hotel/arricityfilter"
						},
						parameterMap:function(data,type){
							if (data.filter) {
								if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
									return { toCity:arricity};
								}
								return { toCity:arricity,filterValue:data.filter.filters[0].value};
							}						
						}
						
					}
				});
				var multiSelect_hotel = $("#"+hotelId).data("kendoDropDownList");
				multiSelect_hotel.setDataSource(hotelNew);
				//回显
				//console.log(hotelInput);
				multiSelect_hotel.value(hotelInput);
				
				//最后一天 不生成酒店
				if(index == $('[name="scenic_name"]').length-1){
					multiSelect_hotel.value("");
				}

			});



			if(viewModel.get("customer.tripJp.oneormore")==1){//多程
				$('.WangFan').addClass('hide');
				$('.DuoCheng').removeClass('hide');
			}else{//单程
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
			if(indexnew!=null){
				layer.close(indexnew);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$("#trip_div").addClass("hide");
			if(indexnew!=null){
				layer.close(indexnew);
			}
			//console.log(XMLHttpRequest);
			//console.log(textStatus);
		//	console.log(errorThrown);
			           /* layer.msg('失败!',{time:2000});*/
			     }
	});
}


function addporposer(){
	var renShu=$('#usa_arrival_date').val();
	if(renShu!=""){//人数 不等于null时
		$('.mainApplicant').siblings('li').find('.k-content').css("display","none");
		var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
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
				if(indexnew!=null){

					layer.close(indexnew);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				if(indexnew!=null){

					layer.close(indexnew);
				}

				//console.log(XMLHttpRequest);
				//console.log(textStatus);
				//console.log(errorThrown);
				            layer.msg('失败!',{time:2000});
				         }
		});
		$(".mainApplicant").hide();//隐藏 span标签的 主申请人
	}
}

function togetherlinkman(){
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

function linkcityone(){
//	alert(viewModel.get("customer.tripJp.arrivecity"));
	viewModel.set("customer.tripJp.returnstartcity",viewModel.get("customer.tripJp.arrivecity"));
}
Array.prototype.removeByValue = function(val) {
	for(var i=0; i<this.length; i++) {
		if(this[i] == val) {
			this.splice(i, 1);
			break;
		}
	}
}

function addSix(a,num){
	if($(a).is(':checked')){
		sixnum.push(num);
	}else{
		sixnum.removeByValue(sixnum);
	}
}
function addthree(a,num){
	if($(a).is(':checked')){
		threenum.push(num);
	}else{
		threenum.removeByValue(threenum);
	}
}

//出行城市的航班
function goFlightByCity(e,fromCityEle, toCityEle, flightSelect, tripType){
	var eData = e.data;
	if("singleGo" == tripType){
		//往返 去程
		e.data.customer.tripJp.gofilght= "";
		e.data.customer.tripJp.flightnum= "";
	}else if ("singleReturn" == tripType){
		//往返 返程
		e.data.customer.tripJp.returnfilght= "";
		e.data.customer.tripJp.returnflightnum= "";
	}else if("moreType" == tripType){
		e.data.flightnum="";
	}
	
	var fromCity = $("#"+fromCityEle).val();
	if(fromCity == null){
		fromCity = "北京";
	}
	var toCity = $("#"+toCityEle).val();
	if(toCity == null){
		toCity = "北京";
	}
	//航班
	var flightDS = new kendo.data.DataSource({
		/*serverFiltering: true,
		transport: {
			read: {
				dataType: "json",
				url: "/visa/flight/filghtByCity",
				data:{
					fromCity:fromCity, 
					toCity:toCity
				}
			}
		}*/
		serverFiltering: true,
		filter:"contains",
		transport: {
			read: {
				type: "POST",
				dataType: "json",
				url: "/visa/flight/filghtByCityFilter"
			},
			parameterMap:function(data,type){
				if (data.filter) {
					if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
						return { fromCity:fromCity, toCity:toCity};
					}
					return { fromCity:fromCity, toCity:toCity,filterValue:data.filter.filters[0].value};
				}						
			}
			
		}
	});
	var singleSelect_flight = $("#"+flightSelect).data("kendoDropDownList");
	singleSelect_flight.setDataSource(flightDS);
	
	
	//设置默认选中
	/*var moreFilght = null;
	$.ajax({
		type: 'GET',
		async: false,
		url: "/visa/flight/filghtByCity?fromCity="+fromCity+"&toCity="+toCity,
		contentType: "application/json",
		dataType: 'json',
		success: function (result) {
			if(result.length > 0){
				singleSelect_flight.value(result[0].id) ;
				
				if("singleGo" == tripType){
					//往返 去程
					viewModel.customer.tripJp.gofilght.id = result[0].id;
				}else if ("singleReturn" == tripType){
					//往返 返程
					viewModel.customer.tripJp.returnfilght.id = result[0].id;
				}else if("moreType" == tripType){
					//多程
					moreFilght = result[0].id;
					eData.startcity = fromCity;
					eData.arrivecity = toCity;
					eData.flightnum = moreFilght;
					console.log("1级："+moreFilght);
				}else{
					
					
					
				}
				
			}
		},
		error: function (error){
			console.log(error);
		}
	});
*/	
}


//出行城市的航班
function nextFlightByCity(e,fromCityEle, toCityEle, flightSelect, datePlan){
	var eData = e.data;
	var fromCity = $("#"+fromCityEle).val();
	if(fromCity == null){
		fromCity = "北京";
	}
	var toCity = $("#"+toCityEle).val();
	if(toCity == null){
		toCity = "北京";
	}
	//航班
	var flightDS = new kendo.data.DataSource({
		/*serverFiltering: true,
		transport: {
			read: {
				dataType: "json",
				url: "/visa/flight/filghtByCity",
				data:{
					fromCity:fromCity, 
					toCity:toCity
				}
			}
		}*/
		serverFiltering: true,
		filter:"contains",
		transport: {
			read: {
				type: "POST",
				dataType: "json",
				url: "/visa/flight/filghtByCityFilter"
			},
			parameterMap:function(data,type){
				if (data.filter) {
					if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
						return { fromCity:fromCity, toCity:toCity};
					}
					return { fromCity:fromCity, toCity:toCity,filterValue:data.filter.filters[0].value};
				}						
			}
			
		}
	});
	var singleSelect_flight = $("#"+flightSelect).data("kendoDropDownList");
	singleSelect_flight.setDataSource(flightDS);
	
	
	//设置默认选中
	/*var moreFilght = null;
	var flightnum = 0;
	$.ajax({
		type: 'GET',
		async: false,
		url: "/visa/flight/filghtByCity?fromCity="+fromCity+"&toCity="+toCity,
		contentType: "application/json",
		dataType: 'json',
		success: function (result) {
			if(result.length > 0){
				singleSelect_flight.value(result[0].id) ;
				flightnum = result[0].id;
			}
		},
		error: function (error){
			console.log(error);
		}
	});
	datePlan.flightnum = flightnum;*/
}

$("#dataPlanSpan").parent().click(function(e){
	//多程  动态设置航班数据源
	var dateplanJpList =  viewModel.customer.dateplanJpList;
	$(dateplanJpList).each(function(index, element){
		var dataUid = element.uid;
		var fromCity = element.startcity;
		var toCity = element.arrivecity;
		if(fromCity == null || fromCity == undefined || fromCity == ""){
			fromCity = "北京";
		}
		if(toCity == null  || toCity == undefined || toCity == ""){
			toCity = "北京";
		}
		//航班
		var flightDS = new kendo.data.DataSource({
			/*serverFiltering: true,
			transport: {
				read: {
					dataType: "json",
					url: "/visa/flight/filghtByCity",
					data:{
						fromCity:fromCity, 
						toCity:toCity
					}
				}
			}*/
			serverFiltering: true,
			filter:"contains",
			transport: {
				read: {
					type: "POST",
					dataType: "json",
					url: "/visa/flight/filghtByCityFilter",
				},
				parameterMap:function(data,type){
					if (data.filter) {
						if(data.filter.filters[0]==null||data.filter.filters[0]==''||data.filter.filters[0]==undefined){
							return { fromCity:fromCity, toCity:toCity};
						}
						return { fromCity:fromCity, toCity:toCity,filterValue:data.filter.filters[0].value};
					}						
				}
			}
		});
		var singleSelect_flight = $("#flightMore_select_"+dataUid).data("kendoDropDownList");
		singleSelect_flight.setDataSource(flightDS);
		singleSelect_flight.value(element.flightnum);
	});
});


