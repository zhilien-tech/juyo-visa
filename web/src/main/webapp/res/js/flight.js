/**
 * Created by Chaly on 2017/4/11.
 */
function timeEditor(container, options) {
    $('<input data-role="timepicker" data-bind="value:' + options.field + '"/>').appendTo(container);
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    height: "100%",
    selectable: "row",
    sortable: true,
    resizable: true,
    filterable: true,
    pageable: {
        refresh: true,
        pageSizes: true,
        buttonCount: 5
    },
    toolbar: [
        {name: "create", imageClass: "base fa-plus green", text: "新增"},
        {name: "importExcel", id:"importExcel", imageClass: "base fa-plus green", text: "导入", type:"file"},
    ],
    editable: {
        confirmation: true,
        mode: "popup",
        window: {title: "编辑"}
    },
    dataSource: {
        transport: {
            read: {
                url: "visa/flight/list",
                dataType: "json",
                type: "POST",
                contentType: 'application/json;charset=UTF-8',
            },
            create: {
                url: "visa/flight/save",
                dataType: "json",
                type: "POST",
            },
            update: {
                url: "visa/flight/save",
                dataType: "json",
                type: "POST",
            },
            destroy: {
                url: function (data) {
                    return "visa/flight/del/" + data.id;
                },
                dataType: "json",
                type: "POST",
            },
            parameterMap: function (options, type) {
                if (type === "read") {
                    options = JSON.stringify(options);
                }
                return options;
            }
        },
        schema: {
            model: {
                id: "id",
            },
            data: function (response) {
                return response.content;
            },
            total: function (response) {
                return response.totalElements;
            },
            errors: function (response) {
                if (response.code == "EXCEPTION") {
                    return response.msg;
                }
            }
        },
        error: function (e) {
            $.layer.alert(e.errors);
        },
        pageSize: 20,
        serverPaging: true,
        serverFiltering: true,
        serverSorting: true
    },
    columns: [
        {field: 'from', title: '起飞机场'},
        {field: 'to', title: '降落机场'},
        {field: 'line', title: '航班号'},
        {field: 'departure', title: '起飞时间', format: "{0:HH:mm}", editor: timeEditor},
        {field: 'landing', title: '着陆时间', format: "{0:HH:mm}", editor: timeEditor},
        {field: 'fromTerminal', title: '起飞航站楼'},
        {field: 'fromCity', title: '起飞城市'},
        {field: 'toTerminal', title: '降落航站楼'},
        {field: 'toCity', title: '降落城市'},
        {
            title: "操作",
            width: 156,
            command: [
                {name: "edit", imageClass: "base fa-pencil orange", text: " 编辑"},
                {name: "destroy", imageClass: "base fa-trash red", text: "删除", type:"file"},
            ],
        },
    ]
}).data("kendoGrid");


$(".k-grid-importExcel").click(function(){
	
	$("#excelFile-button").click();
	
});
//选中后开始导入
function onfileChange() {
	uploadFile();
}
//导入Excel
/*function importfile() {
		var filepath = document.getElementById("excelFile").value;
		var extStart = filepath.lastIndexOf(".");
		var ext = filepath.substring(extStart, filepath.length).toUpperCase();
		if (ext != ".XLS" && ext != ".XLSX") {
			layer.msg("请选择正确的Excel文件");
			return;
		}
		alert(filepath);
		document.getElementById("uploadExcelForm").submit();
		layer.load(1);
	}*/

//文件上传
function uploadFile(){
	var index=null;
	$.fileupload1 = $('#excelFile').uploadify({
		'auto' : true,//选择文件后自动上传
		'formData' : {
			'fcharset' : 'uft-8'/* ,
			'action' : 'uploadimage' */
		},
		'buttonText' : '导入',//按钮显示的文字
		'fileSizeLimit' : '3000MB',
		'fileTypeDesc' : '文件',//在浏览窗口底部的文件类型下拉菜单中显示的文本
		 'fileTypeExts' : '*.XLS; *.XLSX;',//上传文件的类型
		'swf' : '/res/upload/uploadify.swf',//指定swf文件
		'multi' : false,//multi设置为true将允许多文件上传
		'successTimeout' : 1800, 
		/* 'queueSizeLimit' : 100, */
		'uploader' : localhostPaht+'/visa/flight/importExcel',
		//下面的例子演示如何获取到vid
		'onUploadStart':function(file){
			index = layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
		},
		 'onUploadSuccess': function(file, data, response) {
			 if(index!=null){
					layer.close(index);
			 }
		},
       //加上此句会重写onSelectError方法【需要重写的事件】
       'overrideEvents': ['onSelectError', 'onDialogClose'],
       //返回一个错误，选择文件的时候触发
       'onSelectError':function(file, errorCode, errorMsg){
       	///console.log(errorMsg);
           switch(errorCode) {
               case -110:
                   alert("文件 ["+file.name+"] 大小超出系统限制！");
                   break;
               case -120:
                   alert("文件 ["+file.name+"] 大小异常！");
                   break;
               case -130:
                   alert("文件 ["+file.name+"] 类型不正确！");
                   break;
           }
       },
       'onUploadError':function(file, errorCode, errorMsg, errorString){
       	/* alert('The file ' + file.name + ' could not be uploaded: ' + errorString); */
       }
	});
}

$(function(){
    uploadFile();
});
