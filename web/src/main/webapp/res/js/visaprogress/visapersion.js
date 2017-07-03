var country;
var countrystatus;
$(function(){
	 country=JSON.parse(unescape($.queryString("country")));
	 countrystatus=$.queryString("countrystatus");
	 /*alert(unescape($.queryString("country")));*/
	 if(country.status!=13){
			$("#status").text("未完成");
		}
	 $("#name").text(country.chinesexing+country.chinesename);
});




function intoprogressImg(){
	window.location.href="/myvisa/transactVisa/visaProgressImg.html?country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
}


/*
function jump(){
	window.location.href='/myvisa/transactVisa/visaNationList.html?country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
}*/