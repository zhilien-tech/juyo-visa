/**
 * Created by Chaly on 16/5/27.
 */
require(['jquery', 'template'], function ($, template) {
    // 确保登录界面不在iframe框架中显示
    if (window != top) top.location.href = location.href;
    $(function () {
        var $cloud1 = $("#cloud1"), $cloud2 = $("#cloud2");
        var mainwidth = $("#mainBody").outerWidth(), offset1 = 450, offset2 = 0;
        // 飘动
        setInterval(function flutter() {
            if (offset1 >= mainwidth) offset1 = -580;
            if (offset2 >= mainwidth) offset2 = -580;
            offset1 += 1.1;
            offset2 += 1;
            $cloud1.css("background-position", offset1 + "px 100px")
            $cloud2.css("background-position", offset2 + "px 460px")
        }, 100);

        $(".yzm cite img").on('click', function () {
            $(this).attr("src", "res/img/kaptcha.jpg?t=" + Math.random());
        });
        $("input[name=username],input[name=password]").on('click', function () {
            $(this).val("");
        });
        $("input[name=captcha]").focus(function () {
            if ($(this).val() === "验证码") {
                $(this).val("");
            }
        }).blur(function () {
            if ($(this).val() === "") {
                $(this).val("验证码");
            }
        });
    });
});
