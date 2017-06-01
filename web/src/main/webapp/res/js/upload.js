$.getJSON("/visa/photo/options", {range:$.queryString("range")}, function (result) {
    if (result.code != "SUCCESS") {
        $.layer.alert(result.msg);
        return;
    }
    var model = kendo.observable({
        options: result.data,
        mobile: !device.mobile(),
        qrcode: "/visa/customer/qrcode?cid=" + $.queryString("cid")
    });
    kendo.bind($(document.body), model);
    WebUploader.create({
        pick: '.picker',
        fileVal: "file",
        auto: true,// 选完文件后，是否自动上传。
        server: '/upload',
        swf: 'res/plugin/webuploader/Uploader.swf',// swf文件路径
        accept: {
            title: '选择图片',
            extensions: ["jpg", "jpeg", "png"],
            mimeTypes: 'image/jpg,image/jpeg,image/png'
        },
        fileSingleSizeLimit: 1024 * 1024,//1M
    }).on('fileQueued', function (file) {
        this.makeThumb(file, function (error, src) {
            if (error) return;
            $("#rt_" + file.source.ruid).closest(".form-group").prev().attr('src', src);
        }, 1, 1);
    }).on('uploadSuccess', function (file, resp) {
        if (resp.code === "SUCCESS") {
            $("#rt_" + file.source.ruid).closest(".form-group").prev().attr('src', resp.data);
        }
    }).on('error', function (code) {
        $.layer.alert("只能是图片类型,且大小不能超过1M(推荐JPG图片)!");
    });
});
$("#save").kendoButton({
    click: function (e) {
        var cid = $.queryString("cid"), photos = [];
        $(".photo").each(function (i, e) {
            var img = $(e);
            photos.push({
                value: img.attr("src"),
                id: {
                    option: {
                        id: img.data("id"),
                        key: img.data("key"),
                    },
                    customer: {
                        id: $.queryString("cid")
                    },
                }
            });
        });
        $.ajax({
            type: "POST",
            url: "/visa/photo/save",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(photos),
            success: function (result) {
                if (parent.refresh) parent.refresh();
                $.layer.closeAll();
                $.layer.msg(result.msg);
            }
        });
    }
});