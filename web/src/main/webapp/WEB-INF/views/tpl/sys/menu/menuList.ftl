<#include "../include/layout.ftl"/>
<@html>
    <@head>
        <@script src="res/sys/js/admin/menuList.js"/>
    </@head>
    <@body style='overflow-y: hidden;'>
    <div class="container-fluid content-height"">
    <div id="grid" class="row"></div>
    </div>
    </@body>
    <#include "./menuForm.ftl"/>
    <#include "./menuIcon.ftl"/>
    <#include "../include/treeIcon.ftl"/>
</@html>