//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时查询出本公司下的部门
loadSelectDept();
function loadSelectDept(){
	$.ajax({
		url : localhostPaht+'/visa/employeemanage/queryDeptName',
		success : function(data) {
			var data = JSON.parse(data);
			var aa = data.queryList;
			var str = '<option value="">--请选择--</option>';
			for (var i = 0; i < aa.length; i++) {
				str += '<option value="'+aa[i].id+'">'+aa[i].deptname+'</option>';
			}
			$('#deptId').html(str);
		},
		error : function(request) {
			
		}
	});
}
function loadSelectJob(){
	var deptId = $("#deptId").val();
	$.ajax({
		url : localhostPaht+'/visa/employeemanage/selectJobName?deptId='+deptId,
		success : function(data) {
			var data = JSON.parse(data);
			var str = '<option value="">--请选择--</option>';
			for (var i = 0; i < data.length; i++) {
				str += '<option value="'+data[i].id+'">'+data[i].jobName+'</option>';
			}
			$('#jobId').html(str);
		},
		error : function(request) {
			
		}
	});
}