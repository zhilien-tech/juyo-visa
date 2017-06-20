//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时回显护照信息
window.onload = function(){
	 $.getJSON(localhostPaht +'/visa/passportinfo/listPassport', function (resp) {
		 alert(JSON.stringify(resp));
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
//数据绑定
var viewModel = kendo.observable({
	customer: dafaults,
	countries:countries,
	states:states,
	addOne: function (e) {
		
	}
});
kendo.bind($(document.body), viewModel);
//护照信息编辑保存
function editPassPortInfo(){
	/*$.ajax({
		type : "POST",
		url : localhostPaht +'/visa/passportinfo/listPassport',
		data : $('#addForm').serialize(),// 你的formid
		success : function(data) {
			layer.load(1, {
				 shade: [0.1,'#fff'] //0.1透明度的白色背景
			});
			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
		    parent.layer.close(index);
		    window.parent.successCallback('1');
		},
		error : function(request) {
			layer.msg('添加失败');
		}
	});*/
}