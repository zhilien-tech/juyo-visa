/*//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载的时候从session中拿到功能
$(function(){
	var url = '/login/loginfunctions';
	$.ajax({
		url : url,
		type : "POST",
		datatype: 'JSON', 
		success : function(data) {
			var emp = JSON.parse(data);
			
		},
		error : function(request) {
			layer.msg('获取失败');
		}
	});
});
*/