var country;
$(function(){
	 country=JSON.parse($.queryString("country"));
	 if(country.status!=13){
			$("#status").text("未完成");
		}
	 $("#name").text(country.chinesexing+country.chinesename);
});




function intoprogressImg(){
	window.location.href="/myvisa/transactVisa/visaProgressImg.html?country="+JSON.stringify(country);
}