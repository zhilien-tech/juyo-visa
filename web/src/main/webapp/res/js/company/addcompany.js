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

/*function checkEmail(){
	$("#aaaa").kendoValidator({
		//validateOnBlur: true,
		validateOnInput: true,
		rules: {
	      customRule1: function(input) {
	    	  //alert(11);
	    	  if(input.is("[name=email]")) {
	    		  var email = $("#emailId").val();
	    		  if(email==null || email==""){
		    		  return false;
	    		  }
	    	  }
	    	  return true;
	      },
	      customRule2: function(input) {
	    	  if(input.is("[name=email]")) {
	    		  var companySource = $("#emailId").val();
	    		  //邮箱验证正则表达式
	    		    var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	    		    if (companySource.match(pattern)) {
	    		        return false;
	    		    }
	    		    else {
	    		        return true;
	    		    }
	    	  }
	    	  return true;
	      }
	},
		//自定义验证消息
		messages: {
			customRule1: "邮箱不能为空aaa",
			customRule2: "邮箱格式不正确"
		  }
	});
}*/





//添加保存
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#companySave").click(function(){
	if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
		$.ajax({
			type : "POST",
			url : '/visa/company/addcompany',
			data : $('#addForm').serialize(),// 你的formid
			success : function(data) {
				layer.load(1, {
					 shade: [0.1,'#fff'] //0.1透明度的白色背景
				});
				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			    parent.layer.close(index);
			    window.parent.successCallback('1');
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