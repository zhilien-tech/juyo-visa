//获取路径
var curWwwPath = window.document.location.href;  
var pathName =  window.document.location.pathname;  
var pos = curWwwPath.indexOf(pathName);  
var localhostPaht = curWwwPath.substring(0,pos);  
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//页面加载时查询出本公司下的部门
var proposers=new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url:'/visa/employeemanage/queryDeptName',
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        }
    }
});


var proposersjob= [];
//全部职位
var allJob =[];
//该部门下需要的职位
var singleJob=[];
function loadSelectJob(){
	//联动出职位
	var deptId = $("#deptId").val();
	$.ajax({
		url : localhostPaht+'/visa/employeemanage/selectJobName?deptId='+deptId,
		success : function(data) {
			var data = JSON.parse(data);
			for (var i = 0; i < data.length; i++) {
				var job = new Object();
				job.id=data[i].id;
				job.jobName=data[i].jobName;
				proposersjob.push(job);
			}
			console.log(JSON.stringify(proposersjob));
		},
		error : function(request) {
			
		}
	});
}

/*var proposersjob=new kendo.data.DataSource({
	serverFiltering: true,
	transport: {
		read: {
			dataType: "json",
			url:'/visa/employeemanage/selectJobName?deptId='+deptId,
		},
		parameterMap: function (options, type) {
			if (options.filter) {
				return {filter: options.filter.filters[0].value};
			}
		}
	}
});*/

var viewModel = kendo.observable({
	proposers:proposers,
	proposersjob:proposersjob
});
kendo.bind($(document.body), viewModel);



	




/*loadSelectDept();
function loadSelectDept(){
	$.ajax({
		url : localhostPaht+'/visa/employeemanage/queryDeptName',
		success : function(data) {
			var data = JSON.parse(data);
			var aa = data.queryList;
			var str = '<option value="">--请选择--</option>';
			for (var i = 0; i < aa.length; i++) {
				str += '<option value="'+aa[i].id+'">'+aa[i].deptname+'</option>';
			}
			$('#deptId').html(str);
		},
		error : function(request) {
			
		}
	});
}
function loadSelectJob(){
	var deptId = $("#deptId").val();
	$.ajax({
		url : localhostPaht+'/visa/employeemanage/selectJobName?deptId='+deptId,
		success : function(data) {
			var data = JSON.parse(data);
			var str = '<option value="">--请选择--</option>';
			for (var i = 0; i < data.length; i++) {
				str += '<option value="'+data[i].id+'">'+data[i].jobName+'</option>';
			}
			$('#jobId').html(str);
		},
		error : function(request) {
			
		}
	});
}*/