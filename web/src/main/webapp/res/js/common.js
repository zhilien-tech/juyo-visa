(function ($) {
    $.ajaxSettings.traditional = true;//设置ajax请求防止数组参数多个中括号
    kendo.culture("zh-CN");//切换语言
    if (parent.layer) {
        $.extend({layer: parent.layer});
    } else {
        $.extend({layer: layer});
    }
    $.photos = function (array) {
        if (parent.hasOwnProperty("photos")) {
            parent.photos(array);
        } else {
            $("body").lightGallery({
                dynamic: true,
                dynamicEl: array
            });
        }
    }
    $.queryString = function (name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null)return unescape(r[2]);
        return null;
    }
    $.isString = function (str) {
        return (typeof str == 'string') && str.constructor == String;
    }
    $.hashCode = function (str) {
        var hash = 0, i, chr;
        if (str.length === 0) return hash;
        for (i = 0; i < str.length; i++) {
            chr = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + chr;
            hash |= 0; // Convert to 32bit integer
        }
        return hash;
    };
}
)(jQuery)


/*****************************************************
 * 加载数据   小灯泡
 ****************************************************/
$(function () {
   /* var data = {cid: $.queryString("cid")};
    if (data.cid) {
        $.getJSON("/visa/customer/show", data, function (result) {
            bind(result);
        });
    }*/
    $(document).on('click', '.input-group-addon', function () {
        var tip = $(this);
        
        var labelName=tip.parents('.form-group').find("label").text();//获取对应的字段名称
        console.log(labelName);
        
        tip.find("i").toggleClass("fa-pulse fa-spinner fa-lightbulb-o");//变转圈圈效果~
        var node = tip.parent().find(":text,select");
        $.each(node.data("bind").split(","), function (i, e) {
            if (e.startsWith("value:")) {
                var source = node.closest(".row").find("*[data-params]").data("params");
                var bind = e.substring(e.indexOf("value:") + 6, e.length);
                var key = (source ? source + "." + bind : bind) + ":" + node.val();
                $.getJSON("visa/help/get", {key: key, cid: $.queryString("cid")}, function (resp) {
                    if ($.queryString("check")) {
                        layer.prompt({
                            formType: 2,
                            title: "问题描述",
                            value: (resp.data) ? resp.data.msg : ''
                        }, function (msg, index, elem) {
                            var data = {
                                "id.key": key, msg: msg,
                                "id.cid": $.queryString("cid"),
                            };
                            $.post("visa/help/add", data, function (result) {
                                layer.closeAll();
                                layer.msg(result.msg);
                            }, "JSON");
                        });
                    } else if (resp.data.msg) {
                        layer.tips(resp.data.msg, tip);
                    }
                    tip.find("i").toggleClass("fa-pulse fa-spinner fa-lightbulb-o");//变灯泡效果~
                });
            }
        });
        /*--  --*/
        if(tip.find("i").hasClass('fa-lightbulb-o')){
        	alert("灯泡亮~~");
        }else{
        	alert("转圈圈~~");
        }
    });
});