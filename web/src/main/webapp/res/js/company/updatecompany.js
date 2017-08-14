$(function(){
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
	//回显数据
	function GetQueryString(name)
	{
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	// 调用方法
	var comId = GetQueryString("comId");
	var adminId = GetQueryString("adminId");
	$("#comId").val(comId);
	$("#adminId").val(adminId);
	$.ajax({
		type : "POST",
		url : '/visa/company/updatecompany',
		data : {
			comId:comId,
			adminId:adminId
		},
		success : function(data) {
			var data=JSON.parse(data);
			$('#comNameId').val(data.updatecompany.comName);
			$('#adminNameId').val(data.updatecompany.adminName);
			$('#connectId').val(data.updatecompany.connect);
			$('#mobileId').val(data.updatecompany.mobile);
			$('#emailId').val(data.updatecompany.email);
			$('#landLineId').val(data.updatecompany.landLine);
			$('#addressId').val(data.updatecompany.address);
			var companytype = data.companytype;
			var str = '<option value="">--请选择--</option>';
			var sendname ="日本送签社";
			var landname ="日本地接社";
			for(var i=0;i<companytype.length;i++){
				var comtypeId = companytype[i].comType;
				if(comtypeId==data.updatecompany.comType){
					str += '<option value="'+comtypeId+'" selected="selected">'+sendname+'</option>';
					str += '<option value="'+comtypeId+'" selected="selected">'+landname+'</option>';
				}else{
					str += '<option value="'+comtypeId+'">'+sendname+'</option>';
					str += '<option value="'+comtypeId+'">'+landname+'</option>';
				}
			}
				$('#comTypeId').html(str);
		},
		error : function(request) {
			layer.msg('添加失败');
		}
	});
	//菊花加载完毕
	if(indexnew!=null){
		layer.close(indexnew);
	}
});
//手机号唯一性校验
function checkTelephone(){
	var mobile = $("#mobileId").val();
	$.ajax({
		type : "POST",
		url : '/visa/company/checkTelephone',
		data : {
			mobile:mobile
		},
		success : function(data) {
			if(data=="false"){
				layer.msg('此号码已被注册');
				$("#mobileId").css("border","solid 1px red");
				return false;
			}
			$("#mobileId").css("border","solid 1px #bababa");
		},
		error : function(request) {
			layer.msg('手机号码格式有误');
		}
	});
}

//更新保存
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#updateCompanySave").click(function(){
	alert($('#updateForm').serialize());
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			type : "POST",
			url : '/visa/company/updateCompanySave',
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
				layer.msg('添加失败');
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
	var comId =  $("#comId").val();
	layer.confirm("您确认删除信息吗？", {
	    btn: ["是","否"], //按钮
	    shade: false //不显示遮罩
	}, function(){
		// 点击确定之后
		var url = '/visa/company/deleteCompany';
		$.ajax({
			type : 'POST',
			data : {
				comId : comId
			},
			url : url,
			success : function(data) {
				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			    parent.layer.close(index);
			    window.parent.successCallback('3');
			},
			error : function(xhr) {
				layer.msg("操作失败", "", 3000);
			}
		});
	}, function(){
	    // 取消之后不用处理
	});
});
function changetype(){
	var typename = $("#comTypeId option:selected").text();
	if(typename=="日本送签社"){
		$("#comTypeId option:selected").val(1);
	}else if(typename=="日本地接社"){
		$("#comTypeId option:selected").val(2);
	}
}