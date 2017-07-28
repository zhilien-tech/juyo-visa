$(document).ready(function() {
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
        change:onChange
    }).data("kendoDropDownList");
	
	var jobs = $("#jobId").kendoDropDownList({
		optionLabel: "--请选择职位--"
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
//添加保存
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
			url : '/visa/employeemanage/addUserData',
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























/*$(function () {
    $("#deptId").kendoDropDownList({
        optionLabel: "--请选择部门--",
        dataTextField: "deptName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "/visa/employeemanage/queryDeptName"
                }
            }
        },
        change: onChange
    });
    $("#jobId").kendoDropDownList({
        optionLabel: "--请选择职位--"
    });
});
function onChange() {
    $("#jobId").kendoDropDownList({
        optionLabel: "--请选择职位--",
        dataTextField: "jobName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: '/visa/employeemanage/selectJobName',
                    data: { deptId: $("#deptId").data("kendoDropDownList").value() }
                }
            }
        }
    });
}*/