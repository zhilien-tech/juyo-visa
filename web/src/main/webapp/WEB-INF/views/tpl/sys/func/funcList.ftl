<#include "../include/layout.ftl"/>
<@html>
    <@head>
        <@script src="res/sys/js/admin/funcList.js"/>
    </@head>
    <@body style='overflow-y: hidden;'>
    <div class="content-height">
        <div id="horizontal" style="height: 100%;">
            <div class="clear-scroll">
                <div id="menu_grid"></div>
            </div>
            <div class="clear-scroll">
                <div id="func_grid"></div>
            </div>
        </div>
    </div>
    </@body>
    <#include "../include/treeIcon.ftl"/>
</@html>