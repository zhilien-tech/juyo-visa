var ImgPath = "c:\\test.jpg", HeadPath = "c:\\test_head.jpg";
//加载核心
function LoadRecogKenal() {
    if (Object.prototype.hasOwnProperty.call(objIDCard, "IsLoaded")) {
        if (!objIDCard.IsLoaded()) {
            var nRet = objIDCard.InitIDCard("53174805228526970341", 1);
            if (nRet == 0) {
                $("#scanner").show();
                //console.log("识别核心加载成功");
            } else {
                if (nRet == 1) console.log("(无效的UserID)");
                //console.log("识别核心加载失败\r\n返回值：" + nRet);
            }
        } else {
            $("#scanner").show();
            //console.log("核心已经加载");
        }
    } else {
        //console.log("判断objIDCard是否存在IsLoaded属性");
    }
}
//释放核心
function FreeRecogKenal() {
    if (Object.prototype.hasOwnProperty.call(objIDCard, "FreeIdcard")) {
        objIDCard.FreeIdcard();
    }
}
//识别
function RecognizeImg() {
    if (Object.prototype.hasOwnProperty.call(objIDCard, "IsLoaded")) {
        if (objIDCard.IsLoaded()) {
            $("#scanner i").toggleClass("fa-pulse fa-spinner fa-lightbulb-o");
            //采集图像
            var nResult = objIDCard.AcquireImage(3);
            if (nResult != 0) {
                $.layer.alert("采集图像失败\r\n返回值：" + nResult);
                return;
            }
            //调用识别接口识别
            //设置要识别的证件类型
            //0表示添加次主类型的所有子模板
            objIDCard.SetIDCardType(13, 0);
            //识别
            objIDCard.ProcessImage(2);
            nResult = objIDCard.RecogIDCard();
            if (nResult <= 0) {
                $.layer.alert("识别失败\r\n返回值：" + nResult);
                return;
            }
            //显示识别结果
            DisplayResult();
            //保存图像
            objIDCard.SaveImage(ImgPath);
            //保存头像
            objIDCard.SaveHeadImage(HeadPath);
            ShowImage();
        } else {
            $.layer.alert("识别核心加载失败扫描暂不可用！");
        }
    } else {
        $.layer.alert("扫描暂不可用！");
    }
}

function showScanResult(key, value) {
    if ("本国姓名" === key) {
        model.set("customer.lastName", value.substring(0, 1));
        model.set("customer.firstName", value.substring(1, value.length));
    }
    if ("英文姓" === key) {
        model.set("customer.lastNameEN", value);
    }
    if ("英文名" === key) {
        model.set("customer.firstNameEN", value);
    }
    if ("护照号码MRZ" === key) {
        model.set("customer.passport", value);
    }
    if ("性别" === key) {
        model.set("customer.gender", value);
    }
    if ("签发日期" === key) {
        model.set("customer.issueDate", value);
    }
    if ("有效期至" === key) {
        model.set("customer.expiryDate", value);
    }
    if (document.URL.indexOf("japan.html") > 0) {
        if ("签发地点" === key) {
            model.set("customer.issueProvince", value);
        }
        if ("签发机关OCR" === key) {
            model.set("customer.issueCity", value);
        }
    } else {
        if ("签发地点拼音" === key) {
            model.set("customer.issueProvince", value);
            model.set("customer.issueCity", value);
        }
    }
    // if ("签发地点拼音" === key) {
    //     if ("p" === value.toLowerCase()) {
    //         model.set("customer.type", "普通");
    //     } else if ("w" === value.toLowerCase()) {
    //         model.set("customer.type", "外交");
    //     } else if ("g" === value.toLowerCase()) {
    //         model.set("customer.type", "公务");
    //     }
    // }
}
//显示识别结果
function DisplayResult() {
    $("#scanner i").toggleClass("fa-pulse fa-spinner fa-lightbulb-o");
    var strResult = "识别成功\r\n";
    var nFieldNum = objIDCard.GetRecogFieldNum();
    if (nFieldNum > 0) {
        for (var i = 1; i <= nFieldNum; ++i) {
            strResult += objIDCard.GetFieldName(i);
            strResult += ":";
            strResult += objIDCard.GetRecogResult(i);
            strResult += "\r\n";
            showScanResult(objIDCard.GetFieldName(i), objIDCard.GetRecogResult(i));
            console.log(strResult);
        }
    }
}
function ShowImage() {
    //直接使用控件显示图片，但是这样不能隐藏控件始终有一个占位
    //objIDCard.EncodeToBase64("c:\\test.jpg");
    //将图片转换为base64供其他控件显示
    if (Object.prototype.hasOwnProperty.call(objIDCard, "EncodeToBase64")) {
        $("#passport").attr("src", "data:image/png;base64," + objIDCard.EncodeToBase64(ImgPath));
        $("#passport").show();
    }
}
$(function () {
    LoadRecogKenal();
    $(window).unload(function () {
        //响应事件
        FreeRecogKenal();
    });
});
