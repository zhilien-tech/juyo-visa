var country;
var countrystatus;
var tourist;
$(function(){
	tourist=$.queryString("tourist");
	 /*alert(unescape($.queryString("country")));*/
	 if(tourist==1){
		 //console.log(JSON.stringify(country));
	/*	 for(var i=0;i<country.length;i++){
			 console.log(JSON.stringify(country[i]));
			 var html='<a href="javascript:void(0)" onclick="intoprogressImgnew('+country[i].id+');" id="single'+country[i].id+'">'+
				 '<span class="nation" id="name">'+country[i].chinesefullname+'</span>'+
				 '<span class="state" id="status">';
			 if(country[i].status!=13){
				 html+='未完成</span></a>';
			 }else{
				 html+='已完成</span></a>';*/
				 
		 $.ajax({
			 type: "POST",
			 url: "/visa/progress/country?logintype="+$.queryString('logintype')+"&secretMsg="+$.queryString('secretMsg'),
			 contentType: "application/json",
			 dataType: "json",
			 success: function (result) {
				// console.log(result);
				tourist=result.tourist;
				 //country=JSON.parse(unescape($.queryString("country")));
				country=result.usa;
				 countrystatus=$.queryString("countrystatus");
				 tourist=$.queryString("tourist");
				 $("#single").hide();
				 /*alert(unescape($.queryString("country")));*/
				 if(tourist==1){
					 console.log(JSON.stringify(country));
					 for(var i=0;i<country.length;i++){
						 console.log(JSON.stringify(country[i]));
						 var html='<a href="javascript:void(0)" onclick="intoprogressImgnew('+country[i].id+');" id="single'+country[i].id+'">'+
							 '<span class="nation" id="name">'+country[i].chinesefullname+'</span>'+
							 '<span class="state" id="status">';
						 if(country[i].status!=13){
							 html+='未完成</span></a>';
						 }else{
							 html+='已完成</span></a>';
							 
						 }
						 $("#div1").append(html);
					 }
				 }else{
					 $("#single").show();
					 
					 if(country.status!=13){
						 $("#status").text("未完成");
					 }
					 $("#name").text(country.chinesexing+country.chinesename);
				 }
			
			 }
		 });
	 }else{
		 
		 country=JSON.parse(unescape($.queryString("country")));
		 countrystatus=$.queryString("countrystatus");
		 
		 $("#single").hide();
		 $("#single").show();
		 
		 if(country.status!=13){
			 $("#status").text("未完成");
		 }
		 $("#name").text(country.chinesexing+country.chinesename);
	 }
});




function intoprogressImg(){
	if($.queryString('logintype')==5){
		
	}else{
		
		window.location.href="/myvisa/transactVisa/visaProgressImg.html?country="+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
	}
}

function intoprogressImgnew(id){
	
	
	 for(var i=0;i<country.length;i++){
		 if(country[i].id==id){
			 window.location.href="/myvisa/transactVisa/visaProgressImg.html?logintype=5&country="
				 +escape(JSON.stringify(country[i]))
			 +"&countrystatus="+countrystatus+"&orderId="+$.queryString('orderId');
			 return;
		 }
	 }
	
	
	
}



function jump(){
	if($.queryString('logintype')==5){
		
		window.location.href="/myvisa/transactVisa/visaNationList.html?logintype=5&orderId="+$.queryString('orderId');
	}else{
		
		window.location.href='/myvisa/transactVisa/visaNationList.html?country='+escape(JSON.stringify(country))+"&countrystatus="+countrystatus;
	}
}
