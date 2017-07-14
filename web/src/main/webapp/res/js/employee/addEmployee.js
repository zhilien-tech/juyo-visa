//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时查询出本公司下的部门
$(function(){
	//部门职位联动查询
	function selectDeptName(){
		$.ajax({
			type : "POST",
			url : localhostPaht+'/visa/employeemanage/selectDeptName',
			data : {
				deptId:$('#deptId').val()
			},
			success : function(data) {
				console.log(typeof data);
				console.log(data);
				console.log(JSON.stringify(data));
				var str = '<option>--请选择--</option>';
				for(var i=0;i< data.length;i++){
					str += '<option value="'+data[i].id+'">'+data[i].jobName+'</option>';
				}
				document.getElementById("jobId").innerHTML=str;
			},
			error : function(request) {
				
			}
		});
	}
});