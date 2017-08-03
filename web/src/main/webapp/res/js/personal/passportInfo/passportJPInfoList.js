//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//获取系统当前日期
var myDate = new Date();
var year = myDate.getFullYear();//获取完整的年份(4位,1970-????)
var month = myDate.getMonth();//获取当前月份(0-11,0代表1月)
var day = myDate.getDate();//获取当前日(1-31)
var seperator = "-";//日期分隔符
if (month >= 1 && month <= 9) {
month = "0" + month;
}
if (day >= 0 && day <= 9) {
	day = "0" + day;
}
var currentdate = year+seperator+month+seperator+day;
//页面加载时回显护照信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/passportinfo/listJPPassport', function (resp) {
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     	//设置默认值
     	var nowcountry=viewModel.get("customer.docountry");
		if(nowcountry!=null&&nowcountry!=''){
		}else{
			viewModel.set("customer.docountry","CHN");
		}
		viewModel.set("customer.countrynum", "CHN");
		viewModel.set("customer.passportsendoffice", "出入境管理局");
     	viewModel.set("customer.passporttype", 1);
     	//得到当前用户签证有效日期
     	var passporteffectdate = viewModel.get("customer.passporteffectdate");
     	if(passporteffectdate != "" && passporteffectdate != null && passporteffectdate != undefined){
     		var passporteffectdate=passporteffectdate.substring(0,10);
     	}
     	
     	//日期差
		passporteffectdate = passporteffectdate.split(seperator);
		currentdate = currentdate.split(seperator);
		var strDateS = new Date(passporteffectdate[0] + "/" + passporteffectdate[1] + "/" + passporteffectdate[2]);
		//alert(strDateS);
		var strDateE = new Date(currentdate[0] + "/" + currentdate[1] + "/" + currentdate[2]);
		var intDay = (strDateE-strDateS)/(1000*3600*24);
//		alert(intDay);
     	//当前系统时间和签证有效日期对比
     	/*if(){
     		
     	}*/
     	//预览 按钮
   	   /*var phoneurl=viewModel.get("customer.phoneurl");
    	 if(phoneurl!=null&&phoneurl!=''){
    		$("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
    	 }
        $(document).on('click','#preview',function(){
        	$('#light').css('display','block');
        	$('#fade').css('display','block');
        	var phoneurl=viewModel.get("customer.phoneurl");
        	if(phoneurl!=null&&phoneurl!=''){
        		$("#imgId").attr('src',phoneurl);
        	}
        });*/
     });
}
var firstPartJP;
var secondPartJP ;
var thirdPartJP ;
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
		$("#gender").kendoDropDownList({enable:false});//性别状态下拉框不可编辑
		$("#passporttype").kendoDropDownList({enable:false});//护照类型下拉框不可编辑
		$("#home_docountry").kendoDropDownList({enable:false});//国籍下拉框不可编辑
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期至
		$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
		$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
		$(".input-group input").attr("disabled");//添加 不可编辑的属性
		//操作 编辑 按钮时
		$(".editBtn").click(function(){
			$(this).addClass("hide");//编辑 按钮隐藏
			$("#gender").data("kendoDropDownList").enable(true);//性别 状态为 可编辑
			$("#passporttype").data("kendoDropDownList").enable(true);//护照类型下拉框可编辑
			$("#home_docountry").data("kendoDropDownList").enable(true);//国籍下拉框可编辑
			$(".cancelBtn").removeClass("hide");//取消 按钮显示
			$(".saveBtn").removeClass("hide");//保存 按钮显示
			$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
			$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
			$("#signedDate").data("kendoDatePicker").enable(true);//签发日期 不可编辑
			$("#validDate").data("kendoDatePicker").enable(true);//有效期至 不可编辑 
		});
		
		//操作 取消 按钮时
		$(".cancelBtn").click(function(){
			$(this).addClass("hide");//取消 按钮隐藏
			$(".saveBtn").addClass("hide");//保存 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
			$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
			$("#passporttype").kendoDropDownList({enable:false});//护照类型下拉框不可编辑
			$("#home_docountry").kendoDropDownList({enable:false});//国籍下拉框不可编辑
			$("#gender").kendoDropDownList({enable:false});//性别状态下拉框不可编辑
		});
		
		//操作 保存 按钮时
		$(".saveBtn").click(function(){
			$(this).addClass("hide");//保存 按钮隐藏
			$(".cancelBtn").addClass("hide");//取消 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
		});
	}else if(aa == 1){//表示从签证进度跳转至此页面
		$("#sex").kendoDropDownList({enable:true});//性别 状态 下拉框初始化可编辑
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
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
	/*firstPartJP = JSON.parse(unescape($.queryString("firstPartJP")));//获取 错误 信息
	secondPartJP = JSON.parse(unescape($.queryString("secondPartJP")));//获取 错误 信息
	thirdPartJP = JSON.parse(unescape($.queryString("thirdPartJP")));//获取 错误 信息
	//console.log("html____"+firstPart);
	$('label').each(function(){
			
			var labelText=$(this).text();//获取 页面上所有的字段 名称
			labelText = labelText.split(":");
			labelText.pop();
			labelText = labelText.join(":");//截取 :之前的信息
			
			for(var i=0;i<firstPartJP.length;i++){
				if(labelText==firstPartJP[i]){
					$(this).next().find('input').css('border-color','#f17474');
					$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
					$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
				}
			}
	}); */
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
//护照信息编辑保存
$("#updateJPPassportSave").on("click",function(){
//	console.log(JSON.stringify(viewModel.customer));
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: "/visa/passportinfo/updateJPPassportSave",
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				 layer.msg("修改成功",{time:2000});
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
//点击下一步时跳转至日本客户基本信息
$("#nextStepBtn").click(function(){
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			 type: "POST",
			 url: "/visa/passportinfo/updateJPPassportSave",
			 contentType:"application/json",
			 data: JSON.stringify(viewModel.customer)+"",
			 success: function (result){
				 layer.msg("操作成功",{time:2000});
				 window.location.href='/personal/basicInfo/basicJPInfoList.html?firstPartJP='
					  +escape(JSON.stringify(firstPartJP))+"&secondPartJP="
					  +escape(JSON.stringify(secondPartJP))+"&thirdPartJP="
					  +escape(JSON.stringify(thirdPartJP))+"&typeId=1"+"&country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
			 },
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
	             layer.msg('保存失败!',{time:2000});
	         }
		});
		//菊花加载完毕
		if(indexnew!=null){
			layer.close(indexnew);
		}
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
	    });
	    //菊花加载完毕
		if(indexnew!=null){
			layer.close(indexnew);
		}
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
			 if(index!=null){
				layer.close(index);
			 }
			 /*显示 预览 按钮*/
		    viewModel.set("customer.phoneurl",data);
            $("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
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