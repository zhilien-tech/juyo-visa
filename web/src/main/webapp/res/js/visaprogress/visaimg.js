//根据拒绝原因判断是哪个页面的，每个页面一个数组
//日本
var firstAllJP=[];
var secondAllJP=[];
var thirdAllJP=[];
var firstPartJP=[];
var secondPartJP=[];
var thirdPartJP=[];
//美国
var firstAll=[];
var secondAll=[];
var thirdAll=[];
var firstPart=[];
var secondPart=[];
var thirdPart=[];




var country;
var countrystatus;
$(function(){
	 //country=JSON.parse($.queryString("country"));
    country = JSON.parse(unescape($.queryString("country")));
    countrystatus=$.queryString("countrystatus");
    if(countrystatus != "" && countrystatus != null && countrystatus == 1){//1表示进入日本的签证状态
    	$('#writeResource').attr('href','/personal/passportInfo/passportJPInfoList.html?typeId=1'); 
    }else{
    	$('#writeResource').attr('href','/personal/passportInfo/passportInfoList.html?typeId=1'); 
    }
//    alert(unescape($.queryString("country")));
    if(country!=null&&country!=''){
		//alert($.queryString("country"));S
		 console.log($.queryString("country"));
		var a=country.status;
//			alert(a);
		var customerid=country.id;
		//获取ordernum
		$.ajax({
			 type: "POST",
			 url: "/visa/progress/ordernumber?customerid="+customerid+"&countrystatus="+countrystatus,
			 contentType: "application/json",
			 dataType: "json",
			 success: function (result) {
				$("#ordernum").text("订单号:"+result.ordernumber);
			 }
		 });
		
		
		$("#reason").hide();
		if(a!=0){
//			alert($("#writeResource"));
			$("#writeResource").addClass("a-active");
		}
		if(a>=4){
			$("#approvel").addClass("a-active");
		}
		if(a==4){
			$("#approvel").text("初审通过");
		}
		if(a>=8){
			$("#approvel").text("初审通过");
			$("#readysubmit").addClass("a-active");
			
		}
		if(a==5){
			$("#reason").show();
			$("#approvel").text("初审拒绝");
		}
		if(a==10){
			$("#yueVisa").addClass("a-active");
			
		}
		if(a>=9){
			$("#alreadysubmit").addClass("a-active");
			
		}
	}
	/* $("#name").text(country.chinesexing+country.chinesename);*/
});


function reasion(){
	var reason=country.errorinfo;
	var map=new Map();
	map=eval("("+reason+")");
//	alert(JSON.stringify(map));
	var reasonnew="";
	for (var key in map) {  
		var a = map[key];
//		alert(JSON.stringify(a));
		console.log(JSON.stringify(a));
		for(var i=0;i<a.length;i++){
//			alert(a[i].key);
			reasonnew+=a[i].key+",";
			//日本
			if(countrystatus==0){
				
					//产生第一个页面的新数组
					for(var m=0;m<firstAll.length;m++){
						if(a[i].key==firstAll[m]){
							firstPart.push(firstAll[m]);
						}
					}
					
					
					
					
					//产生第二个页面的新数组
					for(var m=0;m<secondAll.length;m++){
						if(a[i].key==secondAll[m]){
							secondPart.push(secondAll[m]);
						}
					}
					
					
					
					
					//产生第三个页面的新数组
					for(var m=0;m<thirdAll.length;m++){
						if(a[i].key==thirdAll[m]){
							thirdPart.push(thirdAll[m]);
						}
					}
					
					
					if(firstPart.length>0){
						window.location.href='/personal/basicInfo/basicInfoList.html?firstPart='+escape(JSON.stringify(firstPart))+"&secondPart="+escape(JSON.stringify(secondPart))+"&thirdPart="+escape(JSON.stringify(thirdPart));
					}else if(secondPart.length>0){
						window.location.href='/personal/passportInfo/passportInfoList.html?firstPart='+escape(JSON.stringify(firstPart))+"&secondPart="+escape(JSON.stringify(secondPart))+"&thirdPart="+escape(JSON.stringify(thirdPart));
					}else if(thirdPart.length>0){
						window.location.href='/personal/visaInfo/visaInfoList.html?firstPart='+escape(JSON.stringify(firstPart))+"&secondPart="+escape(JSON.stringify(secondPart))+"&thirdPart="+escape(JSON.stringify(thirdPart));
					}
				}
			
				//美国
				if(countrystatus==0){
					
					//产生第一个页面的新数组
					for(var m=0;m<firstAllJP.length;m++){
						if(a[i].key==firstAllJP[m]){
							firstPartJP.push(firstAllJP[m]);
						}
					}
					
					
					
					
					//产生第二个页面的新数组
					for(var m=0;m<secondAllJP.length;m++){
						if(a[i].key==secondAllJP[m]){
							secondPartJP.push(secondAllJP[m]);
						}
					}
					
					
					
					
					//产生第三个页面的新数组
					for(var m=0;m<thirdAllJP.length;m++){
						if(a[i].key==thirdAllJP[m]){
							thirdPartJP.push(thirdAllJP[m]);
						}
					}
					
					
					
					if(firstPartJP.length>0){
						window.location.href='/personal/basicInfo/basicJPInfoList.html?firstPart='+escape(JSON.stringify(firstPart))+"&secondPart="+escape(JSON.stringify(secondPart))+"&thirdPart="+escape(JSON.stringify(thirdPart));
					}else if(secondPartJP.length>0){
						window.location.href='/personal/passportInfo/passportJPInfoList.html?firstPart='+escape(JSON.stringify(firstPart))+"&secondPart="+escape(JSON.stringify(secondPart))+"&thirdPart="+escape(JSON.stringify(thirdPart));
					}else if(thirdPartJP.length>0){
						window.location.href='/personal/visaInfo/JPvisaInfoList.html?firstPart='+escape(JSON.stringify(firstPart))+"&secondPart="+escape(JSON.stringify(secondPart))+"&thirdPart="+escape(JSON.stringify(thirdPart));
					}
				}
		
			
				
			}
		
	
	}
	reasonnew+="有问题！请修改！";
	 $.layer.prompt({
         formType: 2,
         value: reasonnew,
         title: '拒绝原因',
     }, function (value, index, elem) {
//         
    	 $.layer.closeAll();
     });
}


function timeapply(){
	 $.layer.prompt({
         formType: 2,
         value: '',
         title: '时间申请',
     }, function (value, index, elem) {
    	// alert(value+"=="+index+"=="+elem);value为输入的值
    	 $.layer.closeAll();
     });
}



function jump(){
	window.location.href='/myvisa/transactVisa/visaNationPersonal.html?country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
}
