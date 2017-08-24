//session过期之后跳到登录页面
/*$(document).ajaxComplete(function(event, xhr, settings) {  
    if(xhr.getResponseHeader("sessionstatus")=="timeOut"){  
        if(xhr.getResponseHeader("loginPath")){
            alert("会话过期，请重新登陆!");
            window.location.replace(xhr.getResponseHeader("/index.html"));  
        }else{  
            alert("请求超时请重新登陆 !");  
        }  
    }  
}); */ 
//全局的AJAX访问，处理AJAX清求时SESSION超时
$.ajaxSetup({
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
    complete:function(XMLHttpRequest,textStatus){
          //通过XMLHttpRequest取得响应头，sessionstatus         
          var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");
          if(sessionstatus=="timeout"){
               //这里跳转的登录页面
        	  window.location.href="/index.html";
               //window.location.replace(PlanEap.getActionURI("/index.html"));
       }
    }
});
