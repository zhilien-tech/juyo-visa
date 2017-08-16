
var sendCom=new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/neworderjp/downloadselectfind?a="+1,
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});
var landCom=new kendo.data.DataSource({
	serverFiltering: true,
	transport: {
		read: {
			dataType: "json",
			url: "/visa/neworderjp/downloadselectfind?a="+2,
		},
		parameterMap: function (options, type) {
			if (options.filter) {
				return {filter: options.filter.filters[0].value};
			}
		},
	}
});

var defaults = {
		
};


var viewModel = kendo.observable({
	sendCom:sendCom,
	landCom:landCom,
    addOne: function (e) {
    	var key = $.isString(e) ? e : $(e.target).data('params');
        //viewModel.get(key).push(keys[key]);
    	viewModel.get(key).push({
             lastName: "",
             firstName: "",
             passport: "",
             main: false,
             depositSource: "",
             depositMethod: "",
             depositSum: 0,
             depositCount: 1,
             receipt: false,
             insurance: false,
             outDistrict: false,
         });
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    
    delOneApplicant: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        if (all.length == 1) {
            $.layer.alert("至少要有一个申请人");
            return;
        }
        all.splice(all.indexOf(e.data), 1);
        if (all.length == 1) {//如果剩一个申请人则置为主申请人
            var item = all.pop();
            item.main = true;
            all.unshift(item);
        }
    },
    master: function () {
        var result = [];
        $.each(viewModel.customer.customers, function (index, item) {
            if(item.main){
                var data = {text: item.lastName + item.firstName};
                data.value = $.hashCode(data.text);
                result.push(data);
            }
        });
        return result;
    },
    customer: defaults
    
});
kendo.bind($(document.body), viewModel);



var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
var emptyNum=[];
var errorNum=[];
//信息保存
function downloadsave(){
	var sendComId=viewModel.customer.sendComId;
	var landComId=viewModel.customer.landComId;
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
	 $.fileDownload("/visa/neworderjp/export?orderid=" + $.queryString("cid")+"&sendComId="+sendComId+"&landComId="+landComId, {
         successCallback: function (url) {
        		if(indexnew!=null){
            		layer.close(indexnew);
            	}
         },
         failCallback: function (html, url) {
            /* if (html.indexOf('<') >= 0) {
                 html = $(html).text();
             }
             var json = JSON.parse(html);
             $.layer.alert(json.msg);*/
        		if(indexnew!=null){
            		layer.close(indexnew);
            	}
        		$.layer.alert("下载失败");
        		
         }
     });
/*	 var iframebody = layer.getChildFrame('body', index); //获取窗口索引
	 parent.layer.close(iframebody);*/
/*	 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	 layer.closeAll(type);*/
//	 parent.layer.close();
//	 window.parent.successCallback('4');
	/*if(validatable.validate()){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
				 console.log(JSON.stringify(viewModel.customer));
				 var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
				
				 $.ajax({
					 type: "POST",
					 url: "/visa/neworderjp/downloadselectsave",
					 contentType: "application/json",
					 dataType: "json",
					 data: JSON.stringify(viewModel.customer),
					 success: function (result) {
						 console.log(result.code);
						 if(result.code=="SUCCESS"){
							 if(indexnew!=null){
									
									layer.close(indexnew);
									}
							 
							 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
							 //$.layer.closeAll();
							 parent.layer.close(index);
							 window.parent.successCallback('4');
							 
							 
							 
						 }
					 },
					 error: function(XMLHttpRequest, textStatus, errorThrown){
						 if(indexnew!=null){
								
								layer.close(indexnew);
								}
						 
							 console.log(XMLHttpRequest);
							 console.log(textStatus);
							 console.log(errorThrown);
				            layer.msg('失败!',{time:2000});
				         }
				 });
		//	}
	}else{
		  //验证————————————————————————————————————
	    $('.k-tooltip-validation').each(function(){
	    	var verificationText=$(this).text().trim();//获取验证的文字信息
	    	var labelVal=$(this).parents('.form-group').find('label').text();//获取验证信息 对应的label名称
	    	labelVal = labelVal.split(":");
	    	labelVal.pop();
	    	labelVal = labelVal.join(":");//截取 :之前的信息
	    	var person=new Object();
	    	person.text=labelVal;
	    	person.error="";
	    	if(verificationText.indexOf("不能为空")>0){
	    		emptyNum.push(person);
	    	}else{
	    		errorNum.push(person);
	    		
	    	}
	    	console.log("-获取验证的文字信息是："+verificationText+"                -获取验证信息 对应的label名称是："+labelVal);
	    });
	    //end 验证————————————————————————————————
		
		
		
		
		var str="";
		if(emptyNum.length>0){
			
			for(var i=0;i<emptyNum.length;i++){
				str+=emptyNum[i].text+",";
			}
			str+="不能为空！"
		}
		if(errorNum.length>0){
			
			for(var i=0;i<errorNum.length;i++){
				str+=errorNum[i].text+",";
			}
			str+="格式不正确！";
		}
		$.layer.alert(str);
		//用完清空
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
	}*/
	 
}


$(function () {
    //如果有传递ID就是修改
    var oid = $.queryString("cid");
    var indexnew= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
    if (oid) {
        $.getJSON("/visa/neworderjp/downloadselect?orderid=" + oid, function (resp) {
        	//console.log(JSON.stringify(resp));
        	viewModel.set("customer", $.extend(true, defaults, resp));
        	
        	if(indexnew!=null){
        		
        		layer.close(indexnew);
        	}
        });
    }
    
   //comsource();//客户来源 状态 模块加载
   
});

  
