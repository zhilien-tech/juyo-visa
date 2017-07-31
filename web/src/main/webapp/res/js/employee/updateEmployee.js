$(document).ready(function() {
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
	//回显数据
	function GetQueryString(name)
	{
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	//是否禁用用户回显
	function display(optionID){
	   var all_options = document.getElementById("disableUserStatusId").options;
	   for (i=0; i<all_options.length; i++){
	      if (all_options[i].value == optionID)  // 根据option标签的ID来进行判断  测试的代码这里是两个等号
	      {
	         all_options[i].selected = true;
	      }
	   }
	}
	//调用方法
	var uid = GetQueryString("uid");
	var dptid = GetQueryString("deptId");
	var jbId = GetQueryString("jobId");
	$("#uid").val(uid);
	$("#depts").val(dptid);
	
	$.ajax({
		type : "POST",
		url : '/visa/employeemanage/updateData',
		data : {
			uid:uid
		},
		success : function(data) {
			var data=JSON.parse(data);
			var userinfo = data.one;
			//用户信息
			$('#fullNameId').val(userinfo.fullname);
			$('#telephoneId').val(userinfo.telephone);
			$('#qqId').val(userinfo.qq);
			$('#landlineId').val(userinfo.landline);
			$('#emailId').val(userinfo.email);
			display(userinfo.disableuserstatus);
		},
		error : function(request) {
			layer.msg('更新失败');
		}
	});
	
	var deptDropDown = $("#depts").kendoDropDownList({
        optionLabel: "--请选择部门--",
        dataTextField: "deptName",
        dataValueField: "id",
        dataSource:{
        	serverFiltering: true,
            transport: {
                read:{
                	dataType:"json",
                	url:"/visa/employeemanage/queryDeptName"
                }
            }
        },
        change: onChange
    }).data("kendoDropDownList");
	
	//联动出职位信息
	function onChange() {
		var jobs = $("#jobId").kendoDropDownList({
	        optionLabel: "--请选择职位--",
	        dataTextField: "jobName",
	        dataValueField: "id",
	        dataSource: {
	            serverFiltering: true,
	            transport: {
	            	read:{
	                	dataType:"json",
	                	url:"/visa/employeemanage/selectJobName",
	                	data: { deptId: $("#depts").data("kendoDropDownList").value() }
	                } 
	            }
	        }
	    }).data("kendoDropDownList");
	}
	//菊花加载完毕
	if(indexnew!=null){
		layer.close(indexnew);
	}
});

$(function(){
	var jobs = $("#jobId").kendoDropDownList({
        optionLabel: "--请选择职位--",
        dataTextField: "jobName",
        dataValueField: "id",
        dataSource: {
            serverFiltering: true,
            transport: {
            	read:{
                	dataType:"json",
                	url:"/visa/employeemanage/selectJobName",
                	data: { deptId: $("#depts").data("kendoDropDownList").value() }
                } 
            }
        }
    }).data("kendoDropDownList");
	jobs.value($.queryString("jobId"));//设置选中职位名称
});
//验证手机号唯一性
function checktelephone(){
	var telephone = $("#telephoneId").val();
	$.ajax({
		type : "POST",
		url : '/visa/employeemanage/checktelephone',
		data : {
			telephone:telephone
		},
		success : function(data) {
			if(data=="false"){
				layer.msg('此号码已被使用');
				$("#telephoneId").css("border","solid 1px red");
				return false;
			}
			$("#telephoneId").css("border","solid 1px #bababa");
		},
		error : function(request) {
			layer.msg('手机号码格式有误');
		}
	});
}
//编辑保存
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#saveBtn").click(function(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			type : "POST",
			url : '/visa/employeemanage/updateUserData',
			data : $('#updateForm').serialize(),// 你的formid
			success : function(data) {
				layer.load(1, {
					 shade: [0.1,'#fff'] //0.1透明度的白色背景
				});
				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			    parent.layer.close(index);
			    window.parent.successCallback('2');
			},
			error : function(request) {
				layer.msg('编辑失败');
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
//删除数据
$("#deleteFlagId").click(function(){
	var userId =  $("#uid").val();
	layer.confirm("您确认删除信息吗？", {
	    btn: ["是","否"], //按钮
	    shade: false //不显示遮罩
	}, function(){
		// 点击确定之后
		var url = '/visa/employeemanage/deleteUserData';
		$.ajax({
			type : 'POST',
			data : {
				userId : userId
			},
			url : url,
			success : function(data) {
				if(data=="true"){
					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				     parent.layer.close(index);
				     window.parent.successCallback('3');
				}else{
					layer.msg("删除失败", "", 3000);
				}
			},
			error : function(xhr) {
				layer.msg("操作失败", "", 3000);
			}
		});
	}, function(){
	    // 取消之后不用处理
	});
});
//初始化密码
$("#initPasswordId").click(function(){
	var userId =  $("#uid").val();
	layer.confirm("您确认初始化密码吗？", {
	    btn: ["是","否"], //按钮
	    shade: false //不显示遮罩
	}, function(){
		// 点击确定之后
		var url = '/visa/employeemanage/initpassword';
		$.ajax({
			type : 'POST',
			data : {
				userId : userId
			},
			url : url,
			success : function(data) {
				if(data=="true"){
					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				     parent.layer.close(index);
				     window.parent.successCallback('4');
				}else{
					layer.msg("初始化密码失败", "", 3000);
				}
			},
			error : function(xhr) {
				layer.msg("操作失败", "", 3000);
			}
		});
	}, function(){
	    // 取消之后不用处理
	});
});
