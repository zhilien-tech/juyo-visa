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
})(jQuery)