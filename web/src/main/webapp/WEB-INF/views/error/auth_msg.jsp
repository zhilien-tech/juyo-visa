<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" errorPage="/WEB-INF/common/500.jsp"%>
<%@include file="/WEB-INF/common/tld.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>提示信息</title>
        <script src="${base}/public/plugins/jQuery/jquery-2.2.3.min.js"></script>
        <script src="${base}/common/js/layer/layer.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
            	layer.confirm('对不起，你没有访问该功能的权限！', {
            		  btn: ['确定','取消'] //按钮
            		}, function(){
            			window.history.back(); 
            		}, function(){
            		  });
            });
        </script>
    </head>
    <body>
    </body>
</html>