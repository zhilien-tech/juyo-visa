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
	alert(JSON.stringify(viewModel.customer));
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
        	
        });
    }
});
