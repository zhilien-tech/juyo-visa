<#include "../include/layout.ftl"/>
<@html>
    <@head>
        <@script src="res/sys/js/admin/roleList.js"/>
    </@head>
    <@body style='overflow-y: hidden;'>
    <div class="content-height">
        <div id="horizontal" style="height: 100%;">
            <div class="clear-scroll">
                <div id="role_grid"></div>
            </div>
            <div class="clear-scroll">
                <div id="permission_grid"></div>
            </div>
        </div>
    </div>
    </@body>
    <#include "./roleIcon.ftl"/>
</@html>