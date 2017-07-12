//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显护照信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/passportinfo/listJPPassport', function (resp) {
		 console.log(JSON.stringify(resp));
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     });
}
//页面加载时初始化各个组件
$(function(){
	var aa = $.queryString("typeId");//得到签证进度在填写资料时跳转页面传来的参数
	if(aa == null || aa == "" || aa == undefined){//表示不是从签证进度跳转而来
		$("#nextStepBtn").hide();//隐藏下一步按钮
		$("#back").hide();//隐藏返回按钮
		$("#gender").kendoDropDownList({enable:false});//性别 状态 下拉框初始化
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期至
		$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
		//操作 编辑 按钮时
		$(".editBtn").click(function(){
			$(this).addClass("hide");//编辑 按钮隐藏
			$("#gender").data("kendoDropDownList").enable(true);//性别 状态为 可编辑
			$(".cancelBtn").removeClass("hide");//取消 按钮显示
			$(".saveBtn").removeClass("hide");//保存 按钮显示
			$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
			$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
			$("#signedDate").data("kendoDatePicker").enable(true);//签发日期 不可编辑
			$("#validDate").data("kendoDatePicker").enable(true);//有效期至 不可编辑 
		});
		
		//操作 取消 按钮时
		$(".cancelBtn").click(function(){
			$(this).addClass("hide");//取消 按钮隐藏
			$(".saveBtn").addClass("hide");//保存 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
			$("#signedDate").data("kendoDatePicker").enable(false);//签发日期 不可编辑
			$("#validDate").data("kendoDatePicker").enable(false);//有效期至 不可编辑 
		});
		
		//操作 保存 按钮时
		$(".saveBtn").click(function(){
			$(this).addClass("hide");//保存 按钮隐藏
			$(".cancelBtn").addClass("hide");//取消 按钮隐藏
			$(".editBtn").removeClass("hide");//编辑 按钮显示
			$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
			$(".input-group input").attr("disabled");//添加 不可编辑的属性
		});
	}else if(aa == 1){//表示从签证进度跳转至此页面
		$("#sex").kendoDropDownList({enable:true});//性别 状态 下拉框初始化可编辑
		$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
		$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
		$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期至
		//隐藏编辑按钮
		$(".editBtn").hide();
		$(".input-group input").removeAttr("disabled"); //去掉所有input框的不可编辑属性
		$(".input-group input").removeClass("k-state-disabled");//去掉不可编辑样式
	}
	
	/*-------------------------小灯泡 效果--------------------------*/
	var firstPart = JSON.parse(unescape($.queryString("firstPartJP")));//获取 错误 信息
	//console.log("html____"+firstPart);
	$('label').each(function(){
			
			var labelText=$(this).text();//获取 页面上所有的字段 名称
			labelText = labelText.split(":");
			labelText.pop();
			labelText = labelText.join(":");//截取 :之前的信息
			
			for(var i=0;i<firstPart.length;i++){
				if(labelText==firstPart[i]){
					$(this).next().find('input').css('border-color','#f17474');
					$(this).next().find('.k-state-default').css('border-color','#f17474');//select(span)
					$(this).next().find('.input-group-addon').addClass('yellow');//小灯泡
				}
			}
	}); 
	/*-------------------------end 小灯泡 效果--------------------------*/
	
	
});

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
		passportlose:{},
		oldname:{},
		orthercountrylist:[],
		father:{},
		mother:{},
		relation:[],
		spouse:{},
		usainfo:{},
		teachinfo:[],
		recentlyintousalist:[],
		workinfo:{},
		oldworkslist:[],
		languagelist:[],
		visitedcountrylist:[],
		workedplacelist:[],
		army:{}
},
keys = {
	"customer.orthercountrylist":{},
	"customer.recentlyintousalist":{},
	"customer.oldworkslist":{},
	"customer.languagelist":{},
	"customer.visitedcountrylist":{},
	"customer.workedplacelist":{},
	"customer.relation":{
		xing:""
	},
	"customer.teachinfo":{}
};
//护照类型
var passportTypeEnum=[
    {text:"旅游护照",value:1}
  ];
//数据绑定
var viewModel = kendo.observable({
	customer: dafaults,
	countries:countries,
	passportTypeEnum:passportTypeEnum,
	states:states
});
kendo.bind($(document.body), viewModel);
//事件提示
function successCallback(id){
	grid.dataSource.read();
	if(id == '1'){
		layer.msg("添加成功",{time:2000});
	}else if(id == '2'){
		layer.msg("修改成功",{time:2000});
	}else if(id == '3'){
		layer.msg("删除成功",{time:2000});
	}
}
//护照信息编辑保存
$("#updateJPPassportSave").on("click",function(){
	console.log(JSON.stringify(viewModel.customer));
	$.ajax({
		 type: "POST",
		 url: "/visa/passportinfo/updateJPPassportSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			 layer.msg("修改成功",{time:2000});
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
			 console.log(XMLHttpRequest);
			 console.log(textStatus);
			 console.log(errorThrown);
             layer.msg('保存失败!',{time:2000});
         }
	});
});
//点击下一步时跳转至日本客户基本信息
$("#nextStepBtn").click(function(){
	$.ajax({
		 type: "POST",
		 url: "/visa/passportinfo/updateJPPassportSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			 layer.msg("操作成功",{time:2000});
			 window.location.href='/personal/basicInfo/basicJPInfoList.html?typeId=1';
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
			 console.log(XMLHttpRequest);
			 console.log(textStatus);
			 console.log(errorThrown);
             layer.msg('保存失败!',{time:2000});
         }
	});
});