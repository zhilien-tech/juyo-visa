<form id="mainform" action="/sys/schedule/add" method="post">
    <table class="formTable">
        <tr>
            <td>任务名：</td>
            <td>
                <input type="hidden" name="id" value="${(schedule.id)!}"/>
                <input id="name" name="name" value="${(schedule.name)!}" class="easyui-validatebox"
                       data-options="width: 307,required:'required'">
            </td>
        </tr>
        <tr>
            <td>任务组：</td>
            <td><input id="group" name="group" value="${(schedule.group)!}" type="text" class="easyui-validatebox"
                       data-options="width: 307,required:'required'"/></td>
        </tr>
        <tr>
            <td>表达式：</td>
            <td><input id="cronExpression" name="cronExpression" value="${(schedule.cronExpression)!}" type="text"
                       class="easyui-validatebox"
                       data-options="width: 307,required:'required'"/></td>
        </tr>
        <tr>
            <td>任务类：</td>
            <td><input name="className" type="text" value="${(schedule.className)!}" class="easyui-validatebox"
                       data-options="width: 307,required:'required'"/></td>
        </tr>
    </table>
    <button type="submit">提交</button>
</form>