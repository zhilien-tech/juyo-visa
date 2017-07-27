$(document).ready(function() {
	var deptDropDown = $("#depts").kendoDropDownList({
        optionLabel: "--请选择部门--",
        dataTextField: "deptName",
        dataValueField: "id",
        dataSource:{
        	serverFiltering: true,
            transport: {
                read:{
                	dataType:"json",
                	url:"/visa/employeemanage/queryDeptName"
                }
            }
        },
        change: onChange
    }).data("kendoDropDownList");
	//联动出职位信息
	function onChange() {
		var jobs = $("#jobId").kendoDropDownList({
	        optionLabel: "--请选择职位--",
	        dataTextField: "jobName",
	        dataValueField: "id",
	        dataSource: {
	            serverFiltering: true,
	            transport: {
	            	read:{
	                	dataType:"json",
	                	url:"/visa/employeemanage/selectJobName",
	                	data: { deptId: $("#depts").data("kendoDropDownList").value() }
	                } 
	            }
	        }
	    }).data("kendoDropDownList");
	}
});



























/*$(function () {
    $("#deptId").kendoDropDownList({
        optionLabel: "--请选择部门--",
        dataTextField: "deptName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "/visa/employeemanage/queryDeptName"
                }
            }
        },
        change: onChange
    });
    $("#jobId").kendoDropDownList({
        optionLabel: "--请选择职位--"
    });
});
function onChange() {
    $("#jobId").kendoDropDownList({
        optionLabel: "--请选择职位--",
        dataTextField: "jobName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: '/visa/employeemanage/selectJobName',
                    data: { deptId: $("#deptId").data("kendoDropDownList").value() }
                }
            }
        }
    });
}*/