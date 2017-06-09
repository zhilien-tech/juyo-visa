<#macro html>
<!DOCTYPE html>
<html><#nested/></html>
</#macro>
<#macro body class="" style="">
<body class="${class}" style="${style}"><#nested/></body>
</#macro>
<#macro head title="${(site.siteName)!}后台管理系统${((site.showPowered)!true)?string(' - Powered By ZNZ.IO','')}">
<head>
    <base href="${base}"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>${title!}</title>
    <#--<!-- Theme style &ndash;&gt;-->
    <#--<link rel="stylesheet" href="res/plugins/lte/css/AdminLTE.css"/>-->
    <#--<!-- Version Skin &ndash;&gt;-->
    <#--<link rel="stylesheet" href="res/plugins/lte/css/skins/_all-skins.min.css"/>-->
    <#--<#nested/>-->
    <#--<link href="res/plugins/kendoui/styles/kendo.common.min.css" rel="stylesheet"/>-->
    <#--<link href="res/plugins/kendoui/styles/kendo.office365.min.css" rel="stylesheet"/>-->
    <#--<link href="res/sys/css/common.min.css" rel="stylesheet"/>-->
    <#--<!-- Normalize &ndash;&gt;-->
    <#--<link rel="stylesheet" href="res/plugins/lte/css/normalize.css"/>-->
    <#--<!-- Bootstrap 3.3.6 &ndash;&gt;-->
    <#--<link rel="stylesheet" href="res/plugins/bootstrap/css/bootstrap.min.css"/>-->
    <#--<!-- Font Awesome &ndash;&gt;-->
    <#--<link rel="stylesheet" href="res/plugins/font-awesome/css/font-awesome.min.css"/>-->

    <link rel="stylesheet" href="/res/plugin/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/res/plugin/kendoui/styles/kendo.common.min.css"/>
    <link rel="stylesheet" href="/res/plugin/kendoui/styles/kendo.office365.min.css"/>
    <link rel="stylesheet" href="/res/plugin/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/res/css/colors.min.css"/>
    <link rel="stylesheet" href="/res/css/common.min.css?v=1"/>
</head>
</#macro>
<#macro script src>
    <script src="/res/plugin/jquery.min.js"></script>
    <script src="/res/plugin/layer/layer.js"></script>
    <script src="/res/plugin/kendoui/js/kendo.web.min.js"></script>
    <script src="/res/plugin/kendoui/js/cultures/kendo.culture.zh-CN.min.js"></script>
    <script src="/res/plugin/kendoui/js/messages/kendo.messages.zh-CN.min.js"></script>
    <script src="/res/js/common.js"></script>
    <script src="${src}"></script>
    <#nested/>
</#macro>
