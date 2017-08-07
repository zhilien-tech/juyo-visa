//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//获取系统当前日期
var myDate = new Date();
//页面加载时回显护照信息
window.onload = function(){
	var logintype1=$.queryString("logintype");
	var after;
	if(logintype1==5){
		var country = JSON.parse(unescape($.queryString("country")));
		after=localhostPaht +'/visa/passportinfo/listPassport?logintype=5&customerId='+country.id;
		$.getJSON(after, function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
			//设置默认值
	     	var nowcountry=viewModel.get("customer.nowcountry");
			if(nowcountry!=null&&nowcountry!=''){
			}else{
				viewModel.set("customer.nowcountry","CHN");
			}
			viewModel.set("customer.countrynum", "CHN");
			viewModel.set("customer.passporttype", 1);
			viewModel.set("customer.visaoffice", "出入境管理局");
			
			
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
		 		$('.effectiveDate').append("<span class='k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照已过期,请及时更换</span>");
			}else if(dateDifference>180 && dateDifference<240){
		 		$('.effectiveDate').append("<span class='k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照即将过期，请及时更换</span>");
			}else if(dateDifference>240){}
			
			
			//预览 按钮
			var photoname= '<a href="#">'
				+ viewModel.get("customer.photoname")
				+ '</a>'
				var phoneurl=viewModel.get("customer.phoneurl");
			if(phoneurl!=null&&phoneurl!=''){
				$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
				$("#photoname").html(photoname);
			}
			$(document).on('click','#preview',function(){
				$('#light').css('display','block');
				$('#fade').css('display','block');
				var phoneurl=viewModel.get("customer.phoneurl");
				if(phoneurl!=null&&phoneurl!=''){
					$("#imgId").attr('src',phoneurl);
				}
			});
		});
	}else{
		after=localhostPaht +'/visa/passportinfo/listPassport';
		$.getJSON(after, function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
			//设置默认值
	     	var nowcountry=viewModel.get("customer.nowcountry");
			if(nowcountry!=null&&nowcountry!=''){
			}else{
				viewModel.set("customer.nowcountry","CHN");
			}
			viewModel.set("customer.countrynum", "CHN");
			viewModel.set("customer.passporttype", 1);
			viewModel.set("customer.visaoffice", "出入境管理局");
			
			//得到当前用户签证有效日期
	     	var passporteffectdate = viewModel.get("customer.passporteffectdate");
	     	if(passporteffectdate != "" && passporteffectdate != null && passporteffectdate != undefined){
	     		var passporteffectdate=passporteffectdate.substring(0,10);
	     	}
	     	//日期差
	     	var dateDifference =null;
			var a=myDate.getTime();//系统日期
			var b=new Date(Date.parse((passporteffectdate+"").replace(/-/g, "/"))).getTime();//签证有效日期
			var daynum=(b-a)/(1000*3600*24);
			
			if(daynum<0){
				dateDifference = 0;
			}else if(daynum>0){
				dateDifference = Math.ceil(daynum);
			}
			//当前系统时间和签证有效日期对比
			if(dateDifference<180){
		 		$('.effectiveDate').append("<span class='k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照已过期,请及时更换</span>");
			}else if(dateDifference>180 && dateDifference<240){
		 		$('.effectiveDate').append("<span class='k-widget k-tooltip k-tooltip-validation k-invalid-msg'><span class='k-icon k-i-warning'> </span>您的护照即将过期，请及时更换</span>");
			}else if(dateDifference>240){}
			
			
			//预览 按钮
			var photoname= '<a href="#">'
				+ viewModel.get("customer.photoname")
				+ '</a>'
				var phoneurl=viewModel.get("customer.phoneurl");
			if(phoneurl!=null&&phoneurl!=''){
				$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
				$("#photoname").html(photoname);
			}
			$(document).on('click','#preview',function(){
				$('#light').css('display','block');
				$('#fade').css('display','block');
				var phoneurl=viewModel.get("customer.phoneurl");
				if(phoneurl!=null&&phoneurl!=''){
					$("#imgId").attr('src',phoneurl);
				}
			});
		});
	}
}

var firstPart ;
var secondPart ;
var thirdPart ;
var country;
var countrystatus;
//页面加载时初始化各个组件
$(function(){
	//初始化上传按钮
	uploadFile();
	var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
		$("#gender").kendoDropDownList({enable:false});//性别 状态 下拉框初始化 不可编辑
		$("#home_nationality").kendoDropDownList({enable:false});//国籍下拉框初始化 不可编辑
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期至
		$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
		$("#passporttype").kendoDropDownList({enable:false});//类型 状态为 不可编辑
		$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
		$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
		$(".input-group input").attr("disabled");//添加 不可编辑的属性
		//操作 编辑 按钮时
		$(".editBtn").click(function(){
			$(this).addClass("hide");//编辑 按钮隐藏
			$(".cancelBtn").removeClass("hide");//取消 按钮显示
			$(".saveBtn").removeClass("hide");//保存 按钮显示
			$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
			$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
			$("#gender").data("kendoDropDownList").enable(true);//性别 状态为 可编辑
			$("#home_nationality").data("kendoDropDownList").enable(true);//国籍下拉框为可编辑
			$("#passporttype").data("kendoDropDownList").enable(true);//类型 状态为可编辑
			$("#signedDate").data("kendoDatePicker").enable(true);//签发日期 可编辑
			$("#validDate").data("kendoDatePicker").enable(true);//有效期至 可编辑 
		});
		
		//操作 取消 按钮时
		$(".cancelBtn").click(function(){
			$(this).addClass("hide");//取消 按钮隐藏
			$(".saveBtn").addClass("hide");//保存 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			$("#gender").kendoDropDownList({enable:false});//性别 状态为 不可编辑
			$("#home_nationality").kendoDropDownList({enable:false});//国籍 状态为 不可编辑
			$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
			$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
			$("#passporttype").data("kendoDropDownList").enable(false);//类型 状态为 不可编辑
		});
		
		//操作 保存 按钮时
		$(".saveBtn").click(function(){
			$(this).addClass("hide");//保存 按钮隐藏
			$(".cancelBtn").addClass("hide");//取消 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			$("#gender").kendoDropDownList({enable:false});//性别 状态为 不可编辑
			$("#home_nationality").kendoDropDownList({enable:false});//国籍状态为 不可编辑
			$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
			$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
		});
	}else if(aa == 1){//表示从签证进度跳转至此页面
		$("#gender").kendoDropDownList({enable:true});//性别 状态 下拉框初始化可编辑
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期至
		//隐藏编辑按钮
		$(".editBtn").hide();
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
	country = JSON.parse(unescape($.queryString("country")));
    countrystatus=$.queryString("countrystatus");
	/*-------------------------小灯泡 效果--------------------------*/
	firstPart = JSON.parse(unescape($.queryString("firstPart")));//获取 错误 信息
	secondPart = JSON.parse(unescape($.queryString("secondPart")));//获取 错误 信息
	thirdPart = JSON.parse(unescape($.queryString("thirdPart")));//获取 错误 信息
	$('label').each(function(){
			var labelText=$(this).text();//获取 页面上所有的字段 名称
			labelText = labelText.split(":");
			labelText.pop();
			labelText = labelText.join(":");//截取 :之前的信息
			if(firstPart!=null&&firstPart!=''){
				
				for(var i=0;i<firstPart.length;i++){
					if(labelText==firstPart[i]){
						$(this).next().find('input').css('border-color','#f17474');
						$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
						$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
					}
				}
			}
	});
	/*-------------------------end 小灯泡 效果--------------------------*/
});

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
		countrynum:"CHN",
		passporttype:1,
		passportlose:{},
		oldname:{},
		orthercountrylist:[],
		father:{},
		mother:{},
		relation:[],
		spouse:{},
		usainfo:{},
		teachinfo:[],
		recentlyintousalist:[],
		workinfo:{},
		oldworkslist:[],
		languagelist:[],
		visitedcountrylist:[],
		workedplacelist:[],
		army:{}
},
keys = {
	"customer.orthercountrylist":{},
	"customer.recentlyintousalist":{},
	"customer.oldworkslist":{},
	"customer.languagelist":{},
	"customer.visitedcountrylist":{},
	"customer.workedplacelist":{},
	"customer.relation":{
		xing:""
	},
	"customer.teachinfo":{}
};
//护照类型
var passportTypeEnum=[
    {text:"旅游护照",value:1}
  ];
//数据绑定
var viewModel = kendo.observable({
	customer: dafaults,
	countries:countries,
	passportTypeEnum:passportTypeEnum,
	states:states
});
kendo.bind($(document.body), viewModel);

//事件提示
function successCallback(id){
	grid.dataSource.read();
	if(id == '1'){
		layer.msg("添加成功",{time:2000});
	}else if(id == '2'){
		layer.msg("修改成功",{time:2000});
	}else if(id == '3'){
		layer.msg("删除成功",{time:2000});
	}
}

//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];

//护照信息编辑保存
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#updatePassportSave").on("click",function(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: "/visa/passportinfo/updatePassportSave",
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				 layer.msg("修改成功",{time:2000});
				 location.reload();
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
	             layer.msg('保存失败!',{time:2000});
	         }
		});
	}else{
		//验证————————————————————————————————————
	    $('.k-tooltip-validation').each(function(){
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
	    	console.log("-获取验证的文字信息是："+verificationText+"                -获取验证信息 对应的label名称是："+labelVal);
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
//点击下一步时跳转至基本信息
var logintype;

$("#nextStepBtn").click(function(){
	logintype=$.queryString("logintype");
	var after;
	var before;
	if(logintype==5){
		after="/visa/passportinfo/updatePassportSave?logintype=5";
		before='/personal/basicInfo/basicInfoList.html?logintype=5&typeId=1&firstPart='
			  +escape(JSON.stringify(firstPart))+"&secondPart="
			  +escape(JSON.stringify(secondPart))+"&thirdPart="
			  +escape(JSON.stringify(thirdPart))+"&country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus+"&orderId="+$.queryString('orderId');
	}else{
		after="/visa/passportinfo/updatePassportSave";
		before='/personal/basicInfo/basicInfoList.html?typeId=1&firstPart='
			+escape(JSON.stringify(firstPart))+"&secondPart="
			+escape(JSON.stringify(secondPart))+"&thirdPart="
			+escape(JSON.stringify(thirdPart))+"&country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
		
	}
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: after,
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				 layer.msg("操作成功",{time:2000});
				 window.location.href=before;
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
	             layer.msg('保存失败!',{time:2000});
	         }
		});
	}else{
		//验证————————————————————————————————————
	    $('.k-tooltip-validation').each(function(){
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
	    	console.log("-获取验证的文字信息是："+verificationText+"                -获取验证信息 对应的label名称是："+labelVal);
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
//文件上传
function uploadFile(){
	var index=null;
	$.fileupload1 = $('#uploadFile').uploadify({
		'auto' : true,//选择文件后自动上传
		'formData' : {
			'fcharset' : 'uft-8'
		},
		'buttonText' : '上传',//按钮显示的文字
		'fileSizeLimit' : '3000MB',
		'fileTypeDesc' : '文件',//在浏览窗口底部的文件类型下拉菜单中显示的文本
		 'fileTypeExts' : '*.png;*.jpg; *.jpeg; *.gif;',//上传文件的类型
		'swf' : '/res/upload/uploadify.swf',//指定swf文件
		'multi' : false,//multi设置为true将允许多文件上传
		'successTimeout' : 1800, 
		'uploader' : localhostPaht+'/visa/passportinfo/uploadFile',
		//下面的例子演示如何获取到vid
		'onUploadStart':function(file){
			index = layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
		},
		 'onUploadSuccess': function(file, data, response) {
			 var fileName = file.name;//文件名称
			 var photoname= '<a id="downloadA"  href="#">'
		            + viewModel.get("customer.photoname")
		            + '</a>'
			 if(index!=null){
				layer.close(index);
			 }
			 /*显示 预览 按钮*/
		    viewModel.set("customer.phoneurl",data);
		    viewModel.set("customer.photoname",photoname);
            $("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
            $("#photoname").html(photoname);
            $(document).on('click','#preview',function(){
	           	$('#light').css('display','block');
	           	$('#fade').css('display','block');
	           	var phoneurl=viewModel.get("customer.phoneurl");
	           	if(phoneurl!=null&&phoneurl!=''){
	           		$("#imgId").attr('src',phoneurl);
	           	}
           });
		},
       //加上此句会重写onSelectError方法【需要重写的事件】
       'overrideEvents': ['onSelectError', 'onDialogClose'],
       //返回一个错误，选择文件的时候触发
       'onSelectError':function(file, errorCode, errorMsg){
       	//console.log(errorMsg);
           switch(errorCode) {
               case -110:
                   alert("文件 ["+file.name+"] 大小超出系统限制！");
                   break;
               case -120:
                   alert("文件 ["+file.name+"] 大小异常！");
                   break;
               case -130:
                   alert("文件 ["+file.name+"] 类型不正确！");
                   break;
           }
       },
       'onUploadError':function(file, errorCode, errorMsg, errorString){
       }
	});
}
