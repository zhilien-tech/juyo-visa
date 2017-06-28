var japan;
var usa;
$(function(){
	 $.ajax({
		 type: "POST",
		 url: "/visa/progress/country",
		 contentType: "application/json",
		 dataType: "json",
		 success: function (result) {
			 console.log(result);
			japan=result.japan;
			usa=result.usa;
			if(japan!=null&&japan!=''){
				$("#usa").hide();
				if(japan.status!=13){
					$("#japanstatus").text("未完成");
				}
			}
			if(usa!=null&&usa!=''){
				$("#japan").hide();
				if(japan.status!=13){
					$("#usastatus").text("未完成");
				}
			}
		 }
	 });
});




function intopersion(country){
	if(country=='japan'){
		
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?country="+JSON.stringify(japan);
	}
	if(country=='usa'){
		
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?country="+JSON.stringify(usa);
	}
}