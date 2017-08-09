
//信息保存
function orderJpsave(){
			 $.ajax({
				 type: "POST",
				 url: "/visa/neworderjp/orderJpsave",
				 contentType: "application/json",
				 dataType: "json",
				 data: JSON.stringify(viewModel.customer),
				 success: function (result) {
					 console.log(result.code);
					 if(result.code=="SUCCESS"){
						 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						 //$.layer.closeAll();
						 parent.layer.close(index);
						 window.parent.successCallback('1');
						 
					 }
				 }
			 });
	 
}
String.prototype.replaceAll = function (FindText, RepText) { 
	regExp = new RegExp(FindText, "g"); 
	return this.replace(regExp, RepText); 
	}
$(function () {
	 $.ajax({
		 type: "POST",
		 url: "/visa/newlog/list",
		 /*contentType: "application/json",*/
		dataType: "json",
		 /* data: JSON.stringify(viewModel.customer),*/
		 success: function (result) {
			 for(var i=0;i<result.length;i++){
				 var context=result[i].context;
				 context= context.replaceAll("\r\n","<br/>");
				 var str='<div class="log-all"><p class="log-time">'+result[i].createTime.substring(0,result[i].createTime.indexOf(" "))+'</p><ul class="log-content">'
				       		+context+
			       		'</ul>';
				 $("#add").append(str);
				 
				 
				 
			 }

		 }
	 });
   
});



