//根据拒绝原因判断是哪个页面的，每个页面一个数组
//日本
var firstAllJP=['类型','国家码','中文姓','拼音','中文名','拼音','护照号码','性别','国籍','出生地点（省份）',
                '签发日期','签发地点（省份）','有效期至','签发机关','护照本号码','护照机读码'];
var secondAllJP=['姓名（中文）','姓名（拼音）','姓名电报码','性别','曾用姓','拼音','曾用名','拼音','婚姻状况',
                 '出生日期','出生地点（省份）','国籍','身份证号码','家庭地址','城市','省份','邮编','国家',
                 '主要电话号码','次要电话号码','工作电话号码','电子邮箱','护照类型','护照号码','护照本号码',
                 '签发国家','签发地城市','签发地省份','签发日期','有效期限','签发国','护照号','原因（中文）',
                 '原因（英文），填写中文自动翻译'];
var thirdAllJP=['我的职业','单位名称','单位电话','公司地址'];
var firstPartJP=[];
var secondPartJP=[];
var thirdPartJP=[];
//美国
var firstAll=['类型','国家码','中文姓','拼音','中文名','拼音','护照号码','性别','国籍','出生地点（省份）',
              '签发日期','签发地点（省份）','有效期至','签发机关','护照本号码','护照机读码'];
var secondAll=['姓名（中文）','姓名（拼音）','姓名电报码','性别','曾用姓','拼音','曾用名','拼音','婚姻状况',
               '出生日期','出生地点（省份）','国籍','身份证号码','家庭地址','城市','省份','邮编','国家',
               '主要电话号码','次要电话号码','工作电话号码','电子邮箱','护照类型','护照号码','护照本号码',
               '签发国家','签发地城市','签发地省份','签发日期','有效期限','签发国','护照号','原因（中文）',
               '原因（英文），填写中文自动翻译'];
var thirdAll=['赴美国旅行目的','赴美国旅行具体目的','预计抵达美国日期','预计停留时间','在美国停留地点的地址',
              '支付旅行费用的个人或实体','同行人姓','拼音','同行人名','拼音','和我的关系','美国联系地址',
              '电话号码','电子邮件','父亲姓','父亲名','父亲是否在美国','母亲姓','母亲名','母亲是否在美国',
              '配偶全称','配偶出生日期','配偶国籍','配偶出生城市','配偶出生国家','配偶地址','主要职业',
              '现在单位或学校名称','地址','城市','省份','邮编','国家','工作电话号码','月薪','简要描述你的职责',
              '学校名称','学校地址','城市','省份','邮编','所学专业','参加学习日期','结束学习日期','服役开始日期',
              '服役结束日期','国家','军种','军衔','军事专业','提交申请的地点','目前的地点','制作者姓','制作者名',
              '组织名称','详细地址','城市','省份','邮编','国家','与你的关系'];
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
    	$('#writeResourceJump').attr('href','/personal/passportInfo/passportJPInfoList.html?typeId=1&country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus); 

    }else{
    	$('#writeResourceJump').attr('href','/personal/passportInfo/passportInfoList.html?typeId=1&country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus); 

    }
    //alert(unescape($.queryString("country")));
    if(country!=null&&country!=''){
		//alert($.queryString("country"));S
		 ///console.log($.queryString("country"));
		var a=country.status;
		//alert(a);
		var customerid=country.id;
		//获取ordernum
		var orderstatus=0;
		
		$.ajax({
			 type: "POST",
			 url: "/visa/progress/ordernumber?customerid="+customerid+"&countrystatus="+countrystatus,
			 contentType: "application/json",
			 dataType: "json",
			 success: function (result) {
				$("#ordernum").text("订单号:"+result.ordernumber);
				orderstatus=result.status;
			 }
		 });
		var updateTime=country.updatetime;
		$("#reason").hide();
		$("#waitReview").hide();
		if(orderstatus==3){
			$("#waitReview").show();
			$("#writeResourceJump").hide();
		}
		if(a!=0){
			//alert($("#writeResource"));
			$("#writeResource").addClass("a-active");
		}
		if(a>=4){
			$("#approvel").addClass("a-active");
		}
		if(a==4){
			$("#approvel").text("初审通过");
			$("#writeResourceJump").hide();
		}
		//准备提交到使馆的时间计算
		if(a==8){
			if(updateTime!=null&&updateTime!=''){
				var val = Date.parse(updateTime);
				var newDate = new Date(val);
				var newtime=new Date((newDate/1000+86400)*1000);
				var year = newtime.getFullYear();

				var month=newtime.getMonth()+1;

				var day = newtime.getDate();
				$("#readyDate").html('<label>预计&nbsp;&nbsp;'+year+"-"+month+"-"+day+'&nbsp;提交使馆</label>');
			}
			
		}
		//提交到使馆的结果时间计算
		if(a==9){
			if(updateTime!=null&&updateTime!=''){
				var val = Date.parse(updateTime);
				var newDate = new Date(val);
				var newtimethree=new Date((newDate/1000+86400*3)*1000);
				var yearthree = newtimethree.getFullYear();
				
				var monththree=newtimethree.getMonth()+1;
				
				var daythree = newtimethree.getDate();
				var newtimefive=new Date((newDate/1000+86400*5)*1000);
				var yearfive = newtimefive.getFullYear();
				
				var monthfive=newtimefive.getMonth()+1;
				
				var dayfive = newtimefive.getDate();
				$("#resultDate").html('<label>预计&nbsp;&nbsp;'+yearthree+"-"+monththree+"-"+daythree+'&nbsp;——————'+yearfive+"-"+monthfive+"-"+dayfive+'&nbsp'+'返回结果</label>');
			}
			
		}
		if(a>=8){
			$("#approvel").text("初审通过");
			$("#readysubmit").addClass("a-active");
			$("#writeResourceJump").hide();
			
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
	//alert(JSON.stringify(map));
	///console.log(JSON.stringify(map));
	var reasonnew="";
	for (var key in map) {  
		var a = map[key];//获取到 错误信息 数据
		//alert(JSON.stringify(a));
		for(var i=0;i<a.length;i++){
			//alert(a[i].key);
			reasonnew+=a[i].key+",";//获取到  错误信息 字段名称
			
			
			//美国
			if(countrystatus==0){
				
					//产生第一个页面的新数组      ___护照信息 页面
					for(var m=0;m<firstAll.length;m++){
						if(a[i].key==firstAll[m]){
							firstPart.push(firstAll[m]);
						}
					}
					
					//产生第二个页面的新数组      ___基本信息 页面
					for(var m=0;m<secondAll.length;m++){
						if(a[i].key==secondAll[m]){
							secondPart.push(secondAll[m]);
						}
					}
					
					//产生第三个页面的新数组      ___签证信息 页面
					for(var m=0;m<thirdAll.length;m++){
						if(a[i].key==thirdAll[m]){
							thirdPart.push(thirdAll[m]);
						}
					}
					
					if(firstPart.length>0){
							window.location.href='/personal/passportInfo/passportInfoList.html?firstPart='
								  +escape(JSON.stringify(firstPart))+"&secondPart="
								  +escape(JSON.stringify(secondPart))+"&thirdPart="
								  +escape(JSON.stringify(thirdPart))+"&typeId=1";
							
						
						///console.log(JSON.stringify(firstPart));
					}else if(secondPart.length>0){
						///console.log("_____________________"+secondPart);
							window.location.href='/personal/basicInfo/basicInfoList.html?firstPart='
								  +escape(JSON.stringify(firstPart))+"&secondPart="
								  +escape(JSON.stringify(secondPart))+"&thirdPart="
								  +escape(JSON.stringify(thirdPart))+"&typeId=1";
	
					}else if(thirdPart.length>0){

							window.location.href='/personal/visaInfo/visaInfoList.html?firstPart='
												  +escape(JSON.stringify(firstPart))+"&secondPart="
												  +escape(JSON.stringify(secondPart))+"&thirdPart="
												  +escape(JSON.stringify(thirdPart))+"&typeId=1";
					}
				}
			
			
				//日本
				if(countrystatus==1){
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
						///console.log("_______"+firstPartJP);
						window.location.href='/personal/passportInfo/passportInfoList.html?firstPart='
											  +escape(JSON.stringify(firstPartJP))+"&secondPart="
											  +escape(JSON.stringify(secondPartJP))+"&thirdPart="
											  +escape(JSON.stringify(thirdPartJP))+"&typeId=1";
					}else if(secondPartJP.length>0){
						window.location.href='/personal/basicInfo/basicJPInfoList.html?firstPart='
											  +escape(JSON.stringify(firstPartJP))+"&secondPart="
											  +escape(JSON.stringify(secondPartJP))+"&thirdPart="
											  +escape(JSON.stringify(thirdPartJP))+"&typeId=1";
					}else if(thirdPartJP.length>0){
						window.location.href='/personal/visaInfo/JPvisaInfoList.html?firstPart='
							                  +escape(JSON.stringify(firstPartJP))+"&secondPart="
							                  +escape(JSON.stringify(secondPartJP))+"&thirdPart="
							                  +escape(JSON.stringify(thirdPartJP))+"&typeId=1";
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
    	 $.layer.closeAll();
     });
}

//点击 改签时间申请
function timeapply(){
	 /*$.layer.prompt({
         formType: 2,
         value: '',
         title: '时间申请',
     }, function (value, index, elem) {
    	// alert(value+"=="+index+"=="+elem);value为输入的值
    	 $.layer.closeAll();
     });*/
	
	 layer.open({
		    type: 2,
		    title:false,
		    closeBtn:true,
		    fix: false,
		    maxmin: true,
		    shadeClose: false,
		    title: '改签时间申请',
		    area: ['400px', '470px'],
		    content: '/myvisa/transactVisa/changeTimeApply.html',
		    /*end: function(){//添加完页面点击返回的时候自动加载表格数据
		    	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				parent.layer.close(index);
		    }*/
		 });
}

function jump(){
	window.location.href='/myvisa/transactVisa/visaNationPersonal.html?country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
}
