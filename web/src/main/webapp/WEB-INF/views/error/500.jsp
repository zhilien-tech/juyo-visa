<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
%>
<!DOCTYPE html>
<html>
<head>
    <title>系统发生内部错误</title>
</head>
<style>
    .u_button{
        display: inline-block;
        padding: 0 20px;
        height: 30px;
        border-radius: 3px;
        -webkit-border-radius: 3px;
        -moz-border-radius: 3px;
        border: none;
        vertical-align: middle;
        text-align: center;
        text-decoration: none;
        font-size: 12px;
        line-height: 30px;
        cursor: pointer;
    }
    .u_button_gray {
        background-color: #BDC3C7;
    }
    a.u_button_gray {
        color: #FFF;
    }
    .u_button_blue {
        background-color: #3498DB;
        color: #FFF;
    }
</style>
<body style="background-color: #E4E7EA;padding-top:5%">
<center>
    <img src="/res/sys/img/500_img.png"/>
    <div>
        <h3 style="  clear: both;  line-height: 40px;  font-size: 40px;  color: #333;">很抱歉，您访问的页面存在异常！</h3>
        <p style="  clear: both;  margin: 30px auto 20px auto;  line-height: 35px;  font-size: 20px;  color: #999;">你访问的页面发生系统内部错误,请稍候再试。如一直存在请联系管理员。</p>
        <a href="javascript:location.reload();" class="u_button u_button_gray">刷新该页</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;or&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="u_button u_button_blue" href="/">返回首页</a>
    </div>
</center>
</body>
</html>

