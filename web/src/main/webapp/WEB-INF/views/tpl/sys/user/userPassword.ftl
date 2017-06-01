<script id="popup_password" type="text/html">
    <div id="container" class="k-popup-edit-form k-window-content k-content" data-role="window">
        <div class="k-edit-form-container">
            <div class="k-edit-label"><label for="password">密码</label></div>
            <div data-container-for="password" class="k-edit-field">
                <input type="password" class="k-input k-textbox"
                       name="password" required="required"
                       data-required-msg="密码不能为空!"
                       data-bind="value:password"/>
            </div>
            <div class="k-edit-label" style="margin-top: 18px;"><label for="repassword">重复密码</label></div>
            <div data-container-for="repassword" class="k-edit-field" style="margin-top: 18px;margin-bottom: 17px;">
                <input type="password" class="k-input k-textbox"
                       name="repassword" confirm="true"
                       data-confirm-msg="两次输入的密码不一致!"
                       data-bind="value:repassword"/>
            </div>
            <div class="k-edit-buttons k-state-default">
                <button class="k-button k-button-icontext k-primary k-grid-update">
                    <span class="k-icon k-i-update"></span>更新
                </button>
                <button class="k-button k-button-icontext k-grid-cancel">
                    <span class="k-icon k-i-cancel"></span>取消
                </button>
            </div>
        </div>
    </div>
</script>