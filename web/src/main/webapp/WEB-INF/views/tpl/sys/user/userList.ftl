<#include "../include/layout.ftl"/>
<@html>
    <@head/>
    <@body style='overflow-y: hidden;'>
    <div class="container-fluid content-height">
        <div id="grid" class="row"></div>
    </div>
    </@body>
    <#include "./userPassword.ftl"/>
    <#include "./userRole.ftl"/>
    <#include "./userOrg.ftl"/>
    <@script src="/res/js/admin/userList.js"/></script>
</@html>