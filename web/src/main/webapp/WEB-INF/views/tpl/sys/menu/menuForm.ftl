<script id="popup_editor" type="text/x-kendo-template">
    <div class="k-edit-label"><label for="name">上级菜单</label></div>
    <div data-container-for="name" class="k-edit-field">
        <input id="pid" name="pid" data-bind="value:pid"/>
    </div>
    <div class="k-edit-label"><label for="name">名称</label></div>
    <div data-container-for="name" class="k-edit-field">
        <input type="text" class="k-input k-textbox" name="name" data-bind="value:name"/>
    </div>
    <div class="k-edit-label"><label for="name">图标</label></div>
    <div data-container-for="name" class="k-edit-field">
        <input type="text" name="icon" data-bind="value:icon"/>
        <input type="text" name="color" data-bind="value:color"/>
    </div>
    <div class="k-edit-label"><label for="url">资源路径</label></div>
    <div data-container-for="url" class="k-edit-field">
        <input type="text" class="k-input k-textbox" name="url" data-bind="value:url"/>
    </div>
    <div class="k-edit-label"><label for="sort">排序</label></div>
    <div data-container-for="sort" class="k-edit-field">
        <input type="text" class="k-input k-textbox" name="sort" data-bind="value:sort"/>
    </div>
    <#--<div class="k-edit-label"><label for="description">描述</label></div>-->
    <#--<div data-container-for="description" class="k-edit-field" style="height: 90px;">-->
        <#--<textarea class="k-input k-textbox" data-bind="value: description" style="line-height:1.5" rows="4"/>-->
    <#--</div>-->
</script>
