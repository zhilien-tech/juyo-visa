$(function(){
	$("#sex").kendoDropDownList();//性别 状态 下拉框初始化
	$("#birthDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//出生日期
	$("#signedDate").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});//签发日期
	
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
	 