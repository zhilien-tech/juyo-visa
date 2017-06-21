//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显护照信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/passportinfo/listPassport', function (resp) {
     	viewModel.set("customer", $.extend(true, dafaults, resp));
     });
}
//页面加载时初始化各个组件
$(function(){
	$("#sex").kendoDropDownList();//性别 状态 下拉框初始化
	$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
	$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
	$("#validDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//有效期至
	
	//操作 编辑 按钮时
	$(".editBtn").click(function(){
		$(this).addClass("hide");//编辑 按钮隐藏
		$(".cancelBtn").removeClass("hide");//取消 按钮显示
		$(".saveBtn").removeClass("hide");//保存 按钮显示
		$(".input-group .k-textbox").removeClass("k-state-disabled");//删除 不可编辑的边框颜色
		$(".input-group input").removeAttr("disabled");//删除 不可编辑的属性
	});
	
	//操作 取消 按钮时
	$(".cancelBtn").click(function(){
		$(this).addClass("hide");//取消 按钮隐藏
		$(".saveBtn").addClass("hide");//保存 按钮隐藏
		$(".editBtn").removeClass("hide");//编辑 按钮显示
		$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
		$(".input-group input").attr("disabled");//添加 不可编辑的属性
	});
	
	//操作 保存 按钮时
	$(".saveBtn").click(function(){
		$(this).addClass("hide");//保存 按钮隐藏
		$(".cancelBtn").addClass("hide");//取消 按钮隐藏
		$(".editBtn").removeClass("hide");//编辑 按钮显示
		$(".input-group .k-textbox").addClass("k-state-disabled");//添加 不可编辑的边框颜色
		$(".input-group input").attr("disabled");//添加 不可编辑的属性
	});
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
    {text:"外交护照",value:1},
    {text:"公务护照",value:2},
    {text:"旅游护照",value:3},
    {text:"海员护照",value:4},
    {text:"特区护照",value:5},
    {text:"难民护照",value:6}
  ];
//数据绑定
var viewModel = kendo.observable({
	customer: dafaults,
	countries:countries,
	passportTypeEnum:passportTypeEnum,
	states:states
});
kendo.bind($(document.body), viewModel);
//护照信息编辑保存
$("#updatePassportSave").on("click",function(){
	console.log(JSON.stringify(viewModel.customer));
	$.ajax({
		 type: "POST",
		 url: "/visa/passportinfo/updatePassportSave",
		 contentType:"application/json",
		 data: JSON.stringify(viewModel.customer)+"",
		 success: function (result){
			 console.log(result);
			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			 parent.layer.close(index);
			 window.parent.successCallback('2');
		 },
		 error: function(XMLHttpRequest, textStatus, errorThrown) {
			 console.log(XMLHttpRequest);
			 console.log(textStatus);
			 console.log(errorThrown);
            layer.msg('保存失败!',{time:2000});
         }
	});
});
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
