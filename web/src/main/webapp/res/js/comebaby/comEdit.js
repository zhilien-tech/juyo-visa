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
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];

//信息保存
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
//信息保存
function ordersave(){

	var flag=true;
	var imgurl=viewModel.get("customer.sealUrl");
	var sqImgurl=viewModel.get("customer.sealSQUrl");
	var comType=viewModel.get("customer.comType");

	if(comType==2){
		if(imgurl==null||imgurl==''){
			flag=false;
		}
	}else{
		if(sqImgurl==null||sqImgurl==''){
			flag=false;
		}
	}
	if(validatable.validate()&&flag){
		//清空验证的数组
		emptyNum.splice(0,emptyNum.length);
		errorNum.splice(0,errorNum.length);
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


	}else{
		//验证————————————————————————————————————
		$('.k-tooltip-validation').each(function(){
			var none=$(this).css("display")=="none";//获取 判断验证提示隐藏
			if(!none){
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
				///console.log("-获取验证的文字信息是："+verificationText+"                -获取验证信息 对应的label名称是："+labelVal);
			}
		});
		//end 验证————————————————————————————————



		var  comType=viewModel.get("customer.comType");


		var str="";
		if(comType==2){
			if(imgurl==null||imgurl==''){
				str+="公司公章,";
				if(validatable.validate()){
					str+="不能为空！";
				}
			}
		}else{
			if(sqImgurl==null||sqImgurl==''){
				str+="公司公章,";
				if(validatable.validate()){
					str+="不能为空！";
				}
			}
		}
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
	}
}

$(function () {
	//如果有传递ID就是修改
	var oid = $.queryString("cid");
	var a=viewModel.get("customer.comType");
	if (oid) {
		//招宝信息 编辑页面
		$.getJSON("/visa/comebaby/comefetch?comeid=" + oid, function (resp) {
			viewModel.set("customer", $.extend(true, dafaults, resp));
			if(a!=null&&a!=''){
				$("#state").val(a);
				if(a==1){
					$("#3").hide();
					$("#4").hide();
					$('#1').show();
					$('#2').show();
					$('.songqianshe-div').show();//显示 送签社：担当者/携带电话/TEL/FAX
					//$('.companyChopDiv').hide();//隐藏 上传公司公章部分
					$('.dijieshe-div').hide();//隐藏  地接社：担当者/TEL
					var sealSQUrl=viewModel.get("customer.sealSQUrl");
					if(sealSQUrl!=null&&sealSQUrl!=''){
						$("#sqImg").attr('src',sealSQUrl);
					}

					$("input[send='send']").each(function(){
						var labelTxt=$(this).parent().prev().text().trim();
						labelTxt = labelTxt.split(":");
						labelTxt.pop();
						labelTxt = labelTxt.join(":");
						//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
						$(this).attr('validationMessage',labelTxt+"不能为空");
						$(this).attr('required','required');
					});
					$("input[land='land']").each(function(){
						var labelTxt=$(this).parent().prev().text().trim();
						labelTxt = labelTxt.split(":");
						labelTxt.pop();
						labelTxt = labelTxt.join(":");
						//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
						$(this).removeAttr('validationMessage',labelTxt+"不能为空");
						$(this).removeAttr('required','required');
					});
				}else if(a==2){
					$('.dijieshe-div').show();//显示 地接社：担当者/TEL 
					//$('.companyChopDiv').show();//显示 上传公司公章部分
					$('.songqianshe-div').hide();//隐藏 送签社：担当者/携带电话/TEL/FAX
					$('#1').hide();
					$('#2').hide();
					$("#3").show();
					$("#4").show();
					var sealUrl=viewModel.get("customer.sealUrl");
					if(sealUrl!=null&&sealUrl!=''){

						$("#img").attr('src',sealUrl);
					}
					$("input[land='land']").each(function(){
						var labelTxt=$(this).parent().prev().text().trim();
						labelTxt = labelTxt.split(":");
						labelTxt.pop();
						labelTxt = labelTxt.join(":");
						//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
						$(this).attr('validationMessage',labelTxt+"不能为空");
						$(this).attr('required','required');
					});
					$("input[send='send']").each(function(){
						var labelTxt=$(this).parent().prev().text().trim();
						labelTxt = labelTxt.split(":");
						labelTxt.pop();
						labelTxt = labelTxt.join(":");
						//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
						$(this).removeAttr('validationMessage',labelTxt+"不能为空");
						$(this).removeAttr('required','required');
					});

				}
			}


		});
	}else{
		//招宝信息 添加页面
		$("#deleteBtn").hide();
		$('#saveBtn').parent().parent().attr('class','col-xs-12');
		var a=1;
		$("#state").val(a);
		if(a==1){
			$('#1').show();	//保证会社、指定番号
			$('#2').show(); //住所
			$("#3").hide(); //公司全称
			$("#4").hide(); //住所
			$('.songqianshe-div').show();//显示 送签社：担当者/携带电话/TEL/FAX
			$('.companyChopDiv').hide();//隐藏 上传公司公章部分
			$('.dijieshe-div').hide();//隐藏  地接社：担当者/TEL

			$("input[send='send']").each(function(){
				var labelTxt=$(this).parent().prev().text().trim();
				labelTxt = labelTxt.split(":");
				labelTxt.pop();
				labelTxt = labelTxt.join(":");
				//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
				$(this).attr('validationMessage',labelTxt+"不能为空");
				$(this).attr('required','required');
			});
			$("input[land='land']").each(function(){
				var labelTxt=$(this).parent().prev().text().trim();
				labelTxt = labelTxt.split(":");
				labelTxt.pop();
				labelTxt = labelTxt.join(":");
				//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
				$(this).removeAttr('validationMessage',labelTxt+"不能为空");
				$(this).removeAttr('required','required');
			});
		}else if(a==2){
			$('.dijieshe-div').show();//显示 地接社：担当者/TEL 
			$('.companyChopDiv').show();//显示 上传公司公章部分
			$('.songqianshe-div').hide();//隐藏 送签社：担当者/携带电话/TEL/FAX
			$('#1').hide();
			$('#2').hide();
			$("#3").show();
			$("#4").show();
			$("input[land='land']").each(function(){
				var labelTxt=$(this).parent().prev().text().trim();
				labelTxt = labelTxt.split(":");
				labelTxt.pop();
				labelTxt = labelTxt.join(":");
				//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
				$(this).attr('validationMessage',labelTxt+"不能为空");
				$(this).attr('required','required');
			});
			$("input[send='send']").each(function(){
				var labelTxt=$(this).parent().prev().text().trim();
				labelTxt = labelTxt.split(":");
				labelTxt.pop();
				labelTxt = labelTxt.join(":");
				//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
				$(this).removeAttr('validationMessage',labelTxt+"不能为空");
				$(this).removeAttr('required','required');
			});

		}
	}
});


function checknull(){
	var a=viewModel.get("customer.comType");
	if(a==2){
		$("input[send='send']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
			$(this).attr('validationMessage',labelTxt+"不能为空");
			$(this).attr('required','required');
		});
		$("input[land='land']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
			$(this).removeAttr('validationMessage',labelTxt+"不能为空");
			$(this).removeAttr('required','required');
		});
	}else{
		$("input[land='land']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).attr({requested:'requested',validationMessage:"不能为空"} );
			$(this).attr('validationMessage',labelTxt+"不能为空");
			$(this).attr('required','required');
		});
		$("input[send='send']").each(function(){
			var labelTxt=$(this).parent().prev().text().trim();
			labelTxt = labelTxt.split(":");
			labelTxt.pop();
			labelTxt = labelTxt.join(":");
			//$(this).removeAttr({requested:'requested',validationMessage:"不能为空"} );
			$(this).removeAttr('validationMessage',labelTxt+"不能为空");
			$(this).removeAttr('required','required');
		});

	}
}

//删除方法
function deletecome(){
	var oid = $.queryString("cid");
	layer.confirm("您确认删除吗？", {
		btn: ["是","否"], //按钮
		shade: false //不显示遮罩
	}, function(){
		// 点击确定之后
		var url = "/visa/comebaby/delete?comeid="+oid;

		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : url,
			success : function(data) {
				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				//$.layer.closeAll();
				parent.layer.close(index);
				window.parent.successCallback('4');
			},
			error : function(xhr) {
				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				//$.layer.closeAll();
				parent.layer.close(index);
				window.parent.successCallback('5');
			}
		});
	}, function(){
		// 取消之后不用处理
	});
}
