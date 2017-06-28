$(function(){
	 $.ajax({
		 type: "POST",
		 url: "/visa/progress/country",
		 contentType: "application/json",
		 dataType: "json",
		 success: function (result) {
			 console.log(result);
			 if(result.code=="SUCCESS"){
				 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				 //$.layer.closeAll();
				 parent.layer.close(index);
				 window.parent.successCallback('1');
				 
			 }
		 }
	 });
});