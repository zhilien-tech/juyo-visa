$(document).ready(function() {
	//var aa = $('#jobId').kendoDropDownList().data("kendoDropDownList");//用户职位 初始化
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
			//部门职位信息
			/*var deptjobinfo = data.userDeptList;
			var str = '<option value="">--请选择--</option>';
			for(var i=0;i<deptjobinfo.length;i++){
				var deptId = deptjobinfo[i].id;
				var deptName = deptjobinfo[i].deptname;
				if(deptId==userinfo.deptid){
					str += '<option value="'+deptId+'" selected="selected">'+deptName+'</option>';
				}else{
					str += '<option value="'+deptId+'">'+deptName+'</option>';
				}
			}
				$('#depts').html(str);*/
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
});

$(function(){
	var jbname = unescape($.queryString("jobName"));
	alert(jbname);
	$("#jobId").val(jbname);
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
});


