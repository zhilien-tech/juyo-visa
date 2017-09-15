/**
 * Created by Chaly on 2017/4/27.
 */
//确保登录界面不在iframe框架中显示
if (window != top) top.location.href = location.href;
$(function () { 

	/*$(".VerificatCode").click(function(){
		 alert("vccc");
	});*/	
	var e = $.Querystring("e");
	if(e){
		var j = JSON.parse($.base64.atob(decodeURI(e),true));
		$(".login_main_errortip").html(j.error);
		$(".loginuser").val(j.username);
		$(".loginpwd").val(j.loginuser);
		$("input[name=rememberMe]").val(j.remember);
	}
	var $cloud1 = $("#cloud1"), $cloud2 = $("#cloud2");
	var mainwidth = $("#mainBody").outerWidth(), offset1 = 450, offset2 = 0;
	// 飘动
	setInterval(function flutter() {
		if (offset1 >= mainwidth) offset1 = -580;
		if (offset2 >= mainwidth) offset2 = -580;
		offset1 += 1.1;
		offset2 += 1;
		$cloud1.css("background-position", offset1 + "px 100px")
		$cloud2.css("background-position", offset2 + "px 460px")
	}, 100);

	$(".yzm cite img").on('click', function () {
		$(this).attr("src", "/login/captcha-image.html?t=" + Math.random());
	});

	$("input[name=username],input[name=password]").on('click', function () {
		$(this).val("");
	});
	$("input[name=captcha]").focus(function () {
		if ($(this).val() === "验证码") {
			$(this).val("");
		}
	}).blur(function () {
		if ($(this).val() === "") {
			$(this).val("验证码");
		}
	});
	//document.getElementById("VerificatCode1").click();
	$("#VerificatCode1").click();
	
	//记住密码
	var str = getCookie("loginInfo");
	//str = str.replace(/(^\s*)|(\s*$)/g, "");
	if(str == undefined || str == null || str == ""){
		$("input[type='checkbox']").attr("checked", false);
		$("#remFlag").val("0");
		$("#pass").val("");
	}else{
		str = str.substring(1,str.length-1);
		var name = str.split(",")[0];
		var password = str.split(",")[1];
		var remFlag = str.split(",")[2];
		if(remFlag == 1){
			$("#remFlag").attr("checked", true);
			$("#remFlag").val("1");
			//自动填充密码
			$("#pass").val(password);
			
		}else{
			$("#remFlag").attr("checked", false);
			$("#remFlag").val("0");
			$("#pass").val("");
		}
		$("#uname").val(name);
	}
	
	//游客用户名密码
	var ykStr = getCookie("loginYkInfo");
	if(ykStr == undefined || ykStr == null || ykStr == ""){
		$("input[type='checkbox']").attr("checked", false);
		$("#remFlagYk").val("0");
		$("#ykpass").val("");
	}else{
		ykStr = ykStr.substring(1,ykStr.length-1);
		var nameYk = ykStr.split(",")[0];
		var passwordYk = ykStr.split(",")[1];
		var remFlagYk = ykStr.split(",")[2];
		//游客
		if(remFlagYk == 1){
			$("#remFlagYk").attr("checked", true);
			$("#remFlagYk").val("1");
			//自动填充密码
			$("#ykpass").val(passwordYk);
		}else{
			$("#remFlagYk").attr("checked", false);
			$("#remFlagYk").val("0");
			$("#ykpass").val("");
		}
		$("#ykname").val(nameYk);
	}
	
	
});



//获取cookie
function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1);
		if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	}
	return "";
}

//记住密码功能
function rememberPass(){
	var remFlag = $("#remFlag").attr("checked");
	if(remFlag == "checked"){ //如果选中设置remFlag为0		
		$("#remFlag").val("0");
		$("#remFlag").attr("checked", false);	
	}else{ //如果没选中设置remFlag为1
		//cookie存用户名和密码,回显的是真实的用户名和密码,存在安全问题.
		/*var conFlag = confirm("记录密码功能不宜在公共场所(如网吧等)使用,以防密码泄露.您确定要使用此功能吗?");
		if(conFlag){ //确认标志
			$("#remFlag").val("1");
			$("input[type='checkbox']").attr("checked", true);
		}else{
			$("input[type='checkbox']").attr("checked", false);
			$("#remFlag").val("0");
		}*/
		$("#remFlag").val("1");
		$("#remFlag").attr("checked", true);
	}
}

function rememberPassYk(){
	var remFlag = $("#remFlagYk").attr("checked");
	if(remFlag == "checked"){ //如果选中设置remFlag为0		
		$("#remFlagYk").val("0");
		$("#remFlagYk").attr("checked", false);	
	}else{ //如果没选中设置remFlag为1
		$("#remFlagYk").val("1");
		$("#remFlagYk").attr("checked", true);
	}
}

function userClick(){
	$("#VerificatCode1").click();
}
function visitorclick(){
	$("#VerificatCode2").click();
}
