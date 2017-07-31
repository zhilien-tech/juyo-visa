//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//遍历得到的对象
var zNodes =[
	 {id:"0", pId:"0", name:"职位权限设置", open:true}
];
var treeIndex = 0 ;
//功能表的list
var functionlist;
$(function () {
	var indexnew= layer.load(1, {shade: [0.1,'#fff']});//菊花加载效果
	//ztree初始配置
    var setting = {
			check: {
				enable: true,//显示复选框
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	 };
	//页面加载时得到对应的数据
	var deptId = $.queryString("deptId");
	var deptName = unescape($.queryString("deptName"));
	var jobName = unescape($.queryString("jobName"));
	var funName = unescape($.queryString("funName"));
	var jobId = $.queryString("jobId");
	//设置值
	$("#deptId").val(deptId);
	$("#deptNameId").val(deptName);
	$("#jobName").val(jobName);
	//页面加载时查询出本公司下所有的功能
	$.ajax({
		url : localhostPaht+'/visa/authority/updateAuthority',
		data:{
			deptId:deptId
		},
		success : function(data) {
			data=JSON.parse(data);
			var deptname = data.dept;
			var jobnamelist = data.list;
			var jobfunctionlist = data.zNodes;
			for(var i=0;i<jobfunctionlist.length;i++){
				var fun=new Object();
				fun.id=jobfunctionlist[i].id;
				fun.pId=jobfunctionlist[i].parentId;
				fun.name=jobfunctionlist[i].funName;
				fun.open=true;
				fun.checked=jobfunctionlist[i].checked;
				zNodes.push(fun);
			}
			var root =  {id:"0", pId:"0", name:"职位权限设置", open:true};
			for (var i = 0; i < jobnamelist.length; i++) {
				var objaa = jobnamelist[i];//得到对象
				bb = JSON.parse(objaa.znodes);//将json转换为Object
				$(".jobName").append('<div class="job_container"><ul class="addDepartment marHei"><li><label class="text-right">职位名称：</label></li><li class="li-input inpPadd"><input id="jobName" name="jobName" type="text" class="k-textbox inputText" value="'+jobnamelist[i].jobName+'"><input name="jobId" type="hidden" value="'+jobnamelist[i].jobId+'"></li><li><button type="button" class="btn btn-primary btn-sm btnPadding" id="settingsPermis">设置权限</button><button type="button" class="btn btn-primary btn-sm btnPadding" id="deleteBtn">删除</button></li></ul><div class="ztree"><ul id="tree_'+treeIndex+'"></ul></div><input type="hidden" class="znodes" value="'+jobnamelist[i].znodes+'"/></div>');
				treeIndex++;
				var ztree_container = $(".job_container:last").find("div.ztree").find("ul:first");
		        var treeId = ztree_container.attr("id") ;
		        var treeObj = $.fn.zTree.getZTreeObj(treeId);
		        if(null == treeObj || undefined == treeObj){
		        	var nodes = eval("(" +jobnamelist[i].znodes+")");
		        	nodes.push(root);
			      	//初始化ztree
				    $.fn.zTree.init(ztree_container, setting, nodes );
		      	}
			}
			var dptid = deptname.id;
			$("#deptId").val(dptid);
		},
		error : function(request) {
			
		}
	});
	//菊花加载完毕
	if(indexnew!=null){
		layer.close(indexnew);
	}
	//部门职位 添加职位
    $('#addJob').click(function(){
    	$(".job_container .ztree").hide();
	       $('.jobName').append('<div class="job_container"><ul class="addDepartment marHei"><li><label class="text-right">职位名称：</label></li><li class="li-input inpPadd"><input id="jobName" name="jobName" type="text" class="k-textbox inputText" placeholder="请输入职位名称"></li><li><button type="button" class="btn btn-primary btn-sm btnPadding" id="settingsPermis">设置权限</button><button type="button" class="btn btn-primary btn-sm btnPadding" id="deleteBtn1" >删除</button></li></ul>'
	       +'<div class="ztree"><ul id="tree_'+treeIndex+'"></ul></div></div>');
	       treeIndex++;
	   var ztree_container = $(".job_container:last").find("div.ztree").find("ul:first");
       var treeId = ztree_container.attr("id") ;
       var treeObj = $.fn.zTree.getZTreeObj(treeId);
       if(null == treeObj || undefined == treeObj){
	      	//初始化ztree
		    $.fn.zTree.init(ztree_container, setting, zNodes);
      	}
    });
    //删除按钮
    $(document).on("click","#deleteBtn",function(jobId){
    	var dele= this;
		layer.confirm("您确认删除信息吗？", {
		    btn: ["是","否"], //按钮
		    shade: false //不显示遮罩
		},function(index){
		     layer.close(index);
		     $(dele).parent().parent().next().remove();
		     $(dele).closest('.job_container').remove();
		},function(index){
			//点击否
			layer.close(index);
		});
    });
   
  //删除不提示
    $(document).on("click","#deleteBtn1",function(jobId){
    	var dele= this;
    	//删除按钮
        $(dele).parent().parent().next().remove();
        $(dele).closest('.job_container').remove();
    });	
    
    //设置权限 按钮
    $('.jobName').on("click","#settingsPermis",function() {
    	$(this).parents('.marHei').next().toggle('500');
        $(this).parents(".job_container").siblings().children('.ztree').hide();
      	var ztree_container = $(this).parents(".marHei").next("div.ztree").find("ul:first");
      	var treeId = ztree_container.attr("id") ;
      	
      	var treeObj = $.fn.zTree.getZTreeObj(treeId);
      	if(null == treeObj || undefined == treeObj){
      	//初始化ztree
	    	$.fn.zTree.init(ztree_container, setting, zNodes);
      	}
    });
});  //$.function结束

//设置功能
function setFunc(){
   var jobInfos = [];
   //取所有树
   $(".job_container").each(function(index,container){
	   var jobName = $(container).find("input[name='jobName']").val();
	   var jobId = $(container).find("input[name='jobId']").val();
	   var treeObj = $.fn.zTree.getZTreeObj("tree_" + index);
	   var nodes =  treeObj.getCheckedNodes(true);
	   var funcIds = "" ;
	   $(nodes).each(function(i,node){
		  funcIds += node.id + ",";
	   });
	   var job = new Object();
	   job.jobName=jobName;
	   job.jobId=jobId;
	   job.functionIds=funcIds;
	   jobInfos.push(job);
   });
   var jobJson = JSON.stringify(jobInfos) ;
   $("#jobJson").val(jobJson) ;
}
//修改保存
//存放空的数组
var emptyNum=[];
//存放格式错误的数组
var errorNum=[];
var validatable = $("#aaaa").kendoValidator().data("kendoValidator");
$("#saveDeptJob").click(function(){
	setFunc();//设置功能
	var _deptName = $("input#deptNameId").val();
	var _jobJson = $("input#jobJson").val();
	var _dptid = $("#deptId").val();
	try{
		$("input[name='jobName']").each(function(index,element){
			var eachJobName = $(element).val();
			if(null == eachJobName || undefined == eachJobName || "" == eachJobName || "" == $.trim(eachJobName)){
				throw "职位名称不能为空";
			}
		}) ;
	}catch(e){
		layer.msg("职位不能为空且至少存在一个") ;
		return false ;
	}
	if(validatable.validate()){
		$.ajax({
			type : "POST",
			url : localhostPaht +'/visa/authority/updateAuthoritySave',
			dataType: "json",
			data : {
				deptName:_deptName,
				jobJson:_jobJson,
				deptId:_dptid
			},
			success : function(data) {
				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
				parent.layer.close(index);
				window.parent.successCallback('2');
			},
			error : function(request) {
				layer.msg("职位不能为空且至少存在一个");
			}
		});
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
	    });
	    //end 验证————————————————————————————————
		var str="";
		if(emptyNum.length>0){
			for(var i=0;i<emptyNum.length;i++){
				alert(emptyNum[i].text);
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
});
