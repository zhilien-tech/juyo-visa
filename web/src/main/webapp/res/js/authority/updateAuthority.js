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
		url : localhostPaht+'/visa/authority/queryComAllFunction',
		success : function(data) {
			data=JSON.parse(data);
			functionlist=data.dept;
			for(var i=0;i<functionlist.length;i++){
				var person=new Object();
				person.id=functionlist[i].id;
				person.pId=functionlist[i].parentId;
				person.name=functionlist[i].funName;
				person.open=true;
				person.checked=functionlist[i].checked;
				zNodes.push(person);
			}
		},
		error : function(request) {
		}
	});
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
   
	//部门职位 添加职位
    $('#addJob').click(function(){
       $(".job_container .ztree").hide();
       $('.jobName').append('<div class="job_container form-group"><ul class="addDepartment marHei"><li><label class="text-right">职位名称：</label></li><li class="li-input inpPadd"><input id="jobName" name="jobName'+(treeIndex)+'" type="text" class="form-control input-sm inputText" style="float:left;width:655px;" placeholder="请输入职位名称"></li><li><button type="button" class="btn btn-primary btn-sm btnPadding" id="settingsPermis">设置权限</button><button type="button" class="btn btn-primary btn-sm btnPadding" id="deleteBtn" >删除</button></li></ul>'
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
    $('.jobName').on("click","#deleteBtn",function() {
      $(this).parent().parent().next().remove();
      $(this).closest('.job_container').remove();

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
});
//设置功能
function setFunc(){
   var jobInfos = [];
   //取所有树
   $(".job_container").each(function(index,container){
	   var jobName = $(container).find("input[id='jobName']").val();
	   var treeObj = $.fn.zTree.getZTreeObj("tree_" + index);
	   var nodes =  treeObj.getCheckedNodes(true);
	   var funcIds = "" ;
		$(nodes).each(function(i,node){
			funcIds += node.id + ",";
		});
	   var job = new Object();
	   job.jobName=jobName;
	   job.functionIds=funcIds;
	   jobInfos.push(job);
   });
   var jobJson = JSON.stringify(jobInfos) ;
   alert(jobJson);
   $("#jobJson").val(jobJson) ;
}
//添加保存
$("#saveDeptJob").click(function(){
	setFunc();//设置功能
	var _deptName = $("input#deptNameId").val();
	var _jobJson = $("input#jobJson").val();
	alert(_deptName);
	alert(_jobJson);
	$.ajax({
		type : "POST",
		url : localhostPaht +'/visa/authority/addDeptJob',
		data : {
			deptName:_deptName,
			jobJson:_jobJson
		},
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
	});
});
