$(function(){
	//编辑页面
	$('.editBtn').click(function(){
		layer.open({
    		type: 2,
    		title: '编辑个人信息',
    		area: ['750px', '430px'],
    		shadeClose: true,
    		content: '/personalInfo/personalInfoEdit.html'
    	});
	});
	
});



