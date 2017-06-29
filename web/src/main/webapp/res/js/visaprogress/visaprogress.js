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
			$("#usa").hide();
			$("#japan").hide();
			if(japan!=null&&japan!=''){
				alert(111);
				$("#japan").show();
				
				if(japan.status!=13){
					$("#japanstatus").text("未完成");
				}
			}
			if(usa!=null&&usa!=''){
				alert(222);
				$("#usa").show();
				
				if(usa.status!=13){
					$("#usastatus").text("未完成");
				}
			}
		 }
	 });
});




function intopersion(country){
	
	if(country=='japan'){
		
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?country="+escape(JSON.stringify(japan));
	}
	if(country=='usa'){
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?country="+escape(JSON.stringify(usa));
	}
}