<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head>
    <base href="${base}"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html />
    <![endif]-->
    <title>${(site.siteName)!}后台管理系统</title>
    <link href="/res/css/login.css" rel="stylesheet" type="text/css"/>
</head>
<body class="loginBg">
<div id="mainBody">
    <div id="cloud1" class="cloud"></div>
    <div id="cloud2" class="cloud"></div>
</div>
<div class="logintop">
    <span>欢迎登录${(site.siteName)!''}管理后台</span>
    <ul>
        <li><a>前台首页</a></li>
        <li><a>帮助</a></li>
        <li><a>关于</a></li>
    </ul>
</div>
<div class="loginbody">
    <span class="systemlogo"></span>
    <div class="loginbox loginbox2">
        <form id="loginForm" action="auth" method="post">
            <input type="hidden" name="to" value="/a"/>
            <input type="hidden" name="login" value="/a/login"/>
            <ul>
                <div class="login_main_errortip">${error!'&nbsp;'}</div>
                <li><input name="username" type="text" class="loginuser" value="${username!}"/></li>
                <li><input name="password" type="password" class="loginpwd" value="${password!}"/></li>
                <li class="yzm">
                    <span>
                        <input name="captcha" type="text" value="验证码"/>
                    </span>
                    <cite>
                        <img alt="验证码" src="res/img/kaptcha.jpg" title="点击刷新"/>
                    </cite>
                </li>
                <li>
                    <input type="submit" class="loginbtn" value="登录"/>
                    <label>
                        <input name="rememberMe" type="checkbox" ${(remember!true)?string('checked','')}/>记住密码
                    </label>
                    <label><a>忘记密码？</a></label>
                </li>
            </ul>
        </form>
    </div>
</div>
<div class="loginbm">
    Copyright © 2017 <a href="${(site.siteUrl)!}">${(site.siteName)!'站点'}</a> All Rights Reserved. ${(site.certtext)!}
</div>
</body>
<script src="/res/plugin/jquery.min.js"></script>
<script src="/res/plugin/jquery.base64.js"></script>
<script src="/res/plugin/jquery.querystring.js"></script>
<script src="/res/js/login.js"></script>
</html>