var country;
$(function(){
	 //country=JSON.parse($.queryString("country"));
    country = JSON.parse($.queryString("country"));
    alert(country);
    if(country!=null&&country!=''){
		//alert($.queryString("country"));
		 console.log($.queryString("country"));
		var a=country.status;
			alert(a);
		if(a!=0){
			alert($("#writeResource"));
			$("#writeResource").addClass("a-active");
		}
		if(a>=4){
			$("#approvel").addClass("a-active");
		}
		if(a==4){
			$("#reason").hide();
		}
	}
	/* $("#name").text(country.chinesexing+country.chinesename);*/
});


function reasion(){
	 $.layer.prompt({
         formType: 2,
         value: '',
         title: '拒绝原因',
     }, function (value, index, elem) {
//         alert(value+index+elem);
     });
}
