<#include "../../sys/include/layout.ftl"/>
<@html>
    <@head>
        <@script src="res/sect/js/sectList.js"/>
    </@head>
    <@body style='overflow-y: hidden;'>
    <div class="container-fluid content-height"">
    <div id="grid" class="row"></div>
    </div>
    </@body>
    <#include "./sectForm.ftl"/>
    <#include "./sectIcon.ftl"/>
    <#include "../../sys/include/treeIcon.ftl"/>
</@html>