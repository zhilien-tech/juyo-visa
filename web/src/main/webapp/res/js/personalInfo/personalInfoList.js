$(function(){
	//列表页
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
		$("#fullname").text(fullname);
		$("#telephone").text(telephone);
		$("#landline").text(landline);
		$("#qq").text(qq);
		$("#email").text(resp.email);
		$("#deptname").text(deptname);
		$("#jobname").text(jobname);
		
		viewModel.set("personalInfo", $.extend(true, dafaults, resp));
		//设置值
		console.log(JSON.stringify(resp));
	});
	
	
});
var dafaults = {},
/*****************************************************
 * 数据绑定
 ****************************************************/
viewModel = kendo.observable({
	personalInfo : dafaults
});
kendo.bind($(document.body), viewModel);

//编辑页
$('.editBtn').click(function(){
	layer.open({
		type: 2,
		title: '编辑个人信息',
		area: ['750px', '430px'],
		shadeClose: true,
		content: '/personalInfo/personalInfoEdit.html'
	});
});
