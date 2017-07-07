$.ajax({
		type : "POST",
		url : '/visa/function/selectparentname',
		data : {},
		success : function(data) {
			data=JSON.parse(data);//将字符串转为json
			for(var i=0;i<data.length;i++){
				$("#parentId").append("<option value='"+data[i].id+"'>"+data[i].funName+"</option>");
			}
		},
		error : function(request) {
			layer.msg('操作有误');
		}
	});
