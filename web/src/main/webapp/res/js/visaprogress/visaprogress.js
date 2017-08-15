var japan;
var usa;
var tourist;
var ordersid;
var secretMsg;
$(function(){
	 secretMsg = $.queryString('secretMsg');
	 $.ajax({
		 type: "POST",
		 url: "/visa/progress/country?logintype="+$.queryString('logintype')+"&secretMsg="+secretMsg,
		 contentType: "application/json",
		 dataType: "json",
		 success: function (result) {
			 console.log(result);
			japan=result.japan;
			usa=result.usa;
			tourist=result.tourist;
			ordersid = result.ordersid;
			$("#usa").hide();
			$("#japan").hide();
			if(japan!=null&&japan!=''){
				$("#japan").show();
				if(japan.status!=13){
					$("#japanstatus").text("未完成");
				}
			}
			if(usa!=null&&usa!=''){
				$("#usa").show();
				if(usa.status!=13){
					$("#usastatus").text("未完成");
				}
			}/*else{
				window.location.href= "/login/logout";
			}*/
		 }
	 });
});




function intopersion(country){
	if(tourist==1&&country=='usa'){
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?countrystatus=0&tourist=1&logintype=5&secretMsg="+secretMsg;
		return;
	}
	if(country=='japan'){
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?country="+escape(JSON.stringify(japan))+"&countrystatus=1";
	}
	if(country=='usa'){
		window.location.href="/myvisa/transactVisa/visaNationPersonal.html?country="+escape(JSON.stringify(usa))+"&countrystatus=0";
		return;
	}
}