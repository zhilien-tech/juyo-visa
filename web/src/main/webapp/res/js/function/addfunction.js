//页面加载时查询出上级功能
$(function(){
	/*var parentname =new kendo.data.DataSource({
	    serverFiltering: true,
	    transport: {
	        read: {
	            url: "/visa/function/selectparentname"
	        },
	        parameterMap: function (options, type) {
	        	alert(JSON.stringify(options.filter));
	            if (options.filter) {
	              //  return {filter: options.filter.filters[0].value};
	            return null;
	            }
	        }
	    }
	});
	//数据绑定
	var viewModel = kendo.observable({
		parentname:parentname
	});
	kendo.bind($(document.body), viewModel);*/
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
});