//客户来源
var customersourceEnum=[
    {text:"线上",value:1},
    {text:"OTS",value:2},
    {text:"直客",value:3},
    {text:"线下",value:4}
  ];
function translateZhToEn(from, to) {
    $.getJSON("/translate/google", {q: $(from).val()}, function (result) {
        $("#" + to).val(result.data).change();
    });
}

var countries = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/country.json",
                dataType: "json"
            }
        }
    }),
    states = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/usa_states.json",
                dataType: "json"
            }
        }
    }),
    dafaults = {
	comType:1
};
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
    customer: dafaults,
    addOne: function (e) {
    	 var key = $.isString(e) ? e : $(e.target).data('params');
         console.log(key);
         console.log(keys[key]);
         viewModel.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    clearAll: function (key) {
        var all = viewModel.get(key);
        if (all) all.splice(0, all.length);
    },
});
kendo.bind($(document.body), viewModel);

//信息保存
function ordersave(){
	 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
			 $.ajax({
				 type: "POST",
				 url: "/visa/comebaby/comesave",
				 contentType: "application/json",
				 dataType: "json",
				 data: JSON.stringify(viewModel.customer),
				 success: function (result) {
					 if(indexnew!=null){
							
							layer.close(indexnew);
							}
					 
					 console.log(result.code);
					 if(result.code=="SUCCESS"){
						 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						 //$.layer.closeAll();
						 parent.layer.close(index);
						 window.parent.successCallback('1');
						 
					 }
				 }
			 });
}

$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("cid");
    if (oid) {
        $.getJSON("/visa/comebaby/comefetch?comeid=" + oid, function (resp) {
        	viewModel.set("customer", $.extend(true, dafaults, resp));
        	var a=viewModel.get("customer.comType");
        	if(a!=null&&a!=''){
        		$("#state").val(a);
        		if(a==1){
        			$("#3").hide();
        			$("#4").hide();
        			$('#1').show();
        			$('#2').show();
        			$('.songqianshe-div').show();//显示 送签社：担当者/携带电话/TEL/FAX
        			$('.companyChopDiv').hide();//隐藏 上传公司公章部分
        			$('.dijieshe-div').hide();//隐藏  地接社：担当者/TEL
        		}else if(a==2){
        			$('.dijieshe-div').show();//显示 地接社：担当者/TEL 
        			$('.companyChopDiv').show();//显示 上传公司公章部分
        			$('.songqianshe-div').hide();//隐藏 送签社：担当者/携带电话/TEL/FAX
        			$('#1').hide();
        			$('#2').hide();
        			$("#3").show();
        			$("#4").show();
        			var sealUrl=viewModel.get("customer.sealUrl");
        			if(sealUrl!=null&&sealUrl!=''){
        				
        				$("#img").attr('src',sealUrl);
        			}
        		}
        	}
        	
        	
        });
    }else{
    	var a=1
    	$("#state").val(a);
		if(a==1){
			$("#3").hide();
			$("#4").hide();
			$('#1').show();
			$('#2').show();
			$('.songqianshe-div').show();//显示 送签社：担当者/携带电话/TEL/FAX
			$('.companyChopDiv').hide();//隐藏 上传公司公章部分
			$('.dijieshe-div').hide();//隐藏  地接社：担当者/TEL
		}else if(a==2){
			$('.dijieshe-div').show();//显示 地接社：担当者/TEL 
			$('.companyChopDiv').show();//显示 上传公司公章部分
			$('.songqianshe-div').hide();//隐藏 送签社：担当者/携带电话/TEL/FAX
			$('#1').hide();
			$('#2').hide();
			$("#3").show();
			$("#4").show();
		}
    }
});
