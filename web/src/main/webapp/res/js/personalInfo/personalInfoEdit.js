$(function(){
	//列表页
	$.getJSON("/visa/personalinfo/personallist", function (resp) {
		viewModel.set("personalInfo", $.extend(true, dafaults, resp));
	});
	
	
});
var dafaults = {};
/*****************************************************
 * 数据绑定
 ****************************************************/
viewModel = kendo.observable({
	personalInfo : dafaults
});
kendo.bind($(document.body), viewModel);


//编辑个人信息
$("#updateCusSave").on("click",function(){
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
	$.ajax({
		 type: "POST",
		 url: "/visa/personalinfo/personalUpdate",
		 data: $('#updateForm').serialize(),
		 success: function (result){
			 if(indexnew!=null){
				 layer.close(indexnew);
			 }
			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			 parent.layer.close(index);
			 window.parent.successCallback('2');
			 personalInfo();
			 
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown){
			 if(indexnew!=null){
				 layer.close(indexnew);
			 }
             layer.msg('编辑失败!',{time:2000});
         }
	});
});

//刷新个人信息
function personalInfo(){
	$.getJSON("/visa/personalinfo/personallist", function (resp) {
		var fullname = resp.fullname;
		if(null == fullname){
			fullname = "";
		}
		var telephone = resp.telephone;
		if(null == telephone){
			telephone = "";
		}
		var landline = resp.landline;
		if(null == landline){
			landline = "";
		}
		var qq = resp.qq;
		if(null == qq){
			qq = "";
		}
		var email = resp.email;
		if(null == email){
			email = "";
		}
		var deptname = resp.deptname;
		if(null == deptname){
			deptname = "";
		}
		var jobname = resp.jobname;
		if(null == jobname){
			jobname = "";
		}
		parent.$("#fullname").text(fullname);
		parent.$("#telephone").text(telephone);
		parent.$("#landline").text(landline);
		parent.$("#qq").text(qq);
		parent.$("#email").text(resp.email);
		parent.$("#deptname").text(deptname);
		parent.$("#jobname").text(jobname);
	});
}
//校验邮箱格式
/*function checkEmail(){
	$("input[checkname='checkname']").each(function(){
		alert(11);
		var labelTxt=$(this).parent().prev().text().trim();
		labelTxt = labelTxt.split(":");
		labelTxt.pop();
		labelTxt = labelTxt.join(":");
		$(this).attr('pattern','/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/');
		$(this).attr('validationMessage',labelTxt+"格式不正确");
		$(this).attr('required','required');
	});
}*/



