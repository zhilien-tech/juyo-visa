//国家
var countrylist=[
    {text:"美国",value:0},
    {text:"日本",value:1},
  ];
//性别
var genderlist=[
                 {text:"男",value:1},
                 {text:"女",value:0},
                 ];
//签证类型
var visatypelist=[
                {text:"东三县",value:2},
                {text:"单次",value:0},
                {text:"新三县",value:3},
                {text:"冲绳",value:4},
                {text:"普通三年",value:5}
                ];
//状态listorder
var statuslist=[
     
                {text:"下单",value:15},
                {text:"已分享",value:1},
                {text:"资料填写",value:2},
                {text:"初审",value:3},
                {text:"初审通过",value:4},
                {text:"初审拒绝",value:5},
                {text:"递送",value:17},
                {text:"发招宝中",value:18},
                {text:"已发招宝",value:19},
                {text:"发招宝失败",value:20},
                {text:"归国报告",value:21},
                {text:"归国报告失败",value:22}
              ];
//注册命令
function regCmd(command) {
	var select = function (e) {
        var data = $(e.delegateTarget).data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
        if (!data) $.layer.alert("请先选择需要操作的数据行");
        return data;
    };
    return {
        name: command,
        className: "k-grid-" + command,
        attr: "style='display:none;'",
        click: function (e) {
            e.preventDefault();
            var data;
            switch (command) {
                case "share":
                    if (!(data = select(e))) return;
                    var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                    $.getJSON("/visa/neworderjp/share?type=customer&customerid=" + data.id, {}, function (resp) {
                        if (resp.code === "SUCCESS") {
                			if(index!=null){
        						
            					layer.close(index);
            					}
                           
                        	layer.msg("分享成功",{time: 2000});
                        } else {
                            $.layer.alert(resp.msg);
                        }
                    });
                    break;
                case "delivery":
                	if (!(data = select(e))) return;
                	window.open("/delivery/deliveryJapan.html?oid="+data.id);
                	break;
                case "notice":
                	if (!(data = select(e))) return;
                	 var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	$.getJSON("/visa/order/notice?type=customer&customerid=" + data.id, {}, function (resp) {
                		if (resp.code === "SUCCESS") {
                			if(index!=null){
        						
            					layer.close(index);
            					}
                			/* $.layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open(resp.data);
                            }, function (index) {
                                $.layer.closeAll();
                            });*/
                			layer.msg("通知成功",{time: 2000});
                		} else {
                			$.layer.alert(resp.msg);
                		}
                	});
                	break;
                case "shareall":
                	if (!(data = select(e))) return;
                	 var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	$.getJSON("/visa/neworderjp/shareall?type=order&orderid=" + data.id, {}, function (resp) {
                		if (resp.code === "SUCCESS") {
                			if(index!=null){
        						
            					layer.close(index);
            					}
                		/*	layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open("/delivery/deliveryJapan.html?oid="+data.id);
                            }, function (index) {
                                $.layer.closeAll();
                            });*/
                			
                			 layer.msg("分享成功",{time: 2000});
                		} else if(resp.code === "FAIL"){
                				if(index!=null){
        						
            					layer.close(index);
            					}
                			$.layer.alert(resp.msg);
                		}else{
                			$.layer.alert(resp.msg);
                			
                		}
                	});
                	break;
                case "validate":
                	if (!(data = select(e))) return;
                	var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	$.getJSON("/visa/neworderjp/validate?type=order&orderid=" + data.id, {}, function (resp) {
                		if (resp.code === "SUCCESS") {
                			if(index!=null){
                				
                				layer.close(index);
                			}
                			/*	layer.confirm('发送成功，打开预览？', {
                                btn: ['预览', '关闭']
                            }, function (index, layero) {
                                window.open("/delivery/deliveryJapan.html?oid="+data.id);
                            }, function (index) {
                                $.layer.closeAll();
                            });*/
                			
                			layer.msg("等待递送！",{time: 2000});
                		} else if(resp.code === "FAIL"){
                			if(index!=null){
                				
                				layer.close(index);
                			}
                			$.layer.alert(resp.msg);
                		}else{
                			$.layer.alert(resp.msg);
                			
                		}
                	});
                	break;
                case "noticeall":
                	if (!(data = select(e))) return;
                	 var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	$.getJSON("/visa/order/noticeall?type=order&orderid=" + data.id, {}, function (resp) {
                		if (resp.code === "SUCCESS") {
                			if(index!=null){
        						
            					layer.close(index);
            					}
                			/*$.layer.confirm('发送成功，打开预览？', {
                				btn: ['预览', '关闭']
                			}, function (index, layero) {
                				window.open(resp.data);
                			}, function (index) {
                				$.layer.closeAll();
                			});*/
                			layer.msg("通知成功",{time: 2000});
                		} else {
                			$.layer.alert(resp.msg);
                		}
                	});
                	break;
                case "upload":
                	
                	
                	
                	
                	
                	
                	/*if (!(data = select(e))) return;
                	 WebUploader.create({
             	        pick: '.k-button k-button-icontext k-grid-upload',
             	        fileVal: "file",
             	        auto: true,// 选完文件后，是否自动上传。
             	        server: "visa/neworderjp/uploadFile?type=order",
             	        swf: 'res/plugin/webuploader/Uploader.swf',// swf文件路径
             	        accept: {
             	            title: '选择图片',
             	            extensions: ["jpg", "jpeg", "png"],
             	            mimeTypes: 'image/jpg,image/jpeg,image/png'
             	        },
             	        fileSingleSizeLimit: 1024 * 1024,//1M
                             	    }).on('fileQueued', function (file) {
             	        this.makeThumb(file, function (error, src) {
             	            if (error) return;
             	            $("#rt_" + file.source.ruid).closest(".form-group").prev().attr('src', sr;;c);
             	        }, 1, 1);
             	    }).on('uploadSuccess', function (file, resp) {
             	        if (resp.code === "SUCCESS") {
             	            $("#rt_" + file.source.ruid).closest(".form-group").prev().attr('src', resp.data);
             	        }
             	    }).on('error', function (code) {
             	        $.layer.alert("只能是图片类型,且大小不能超过1M(推荐JPG图片)!");
             	    });
                	var index= layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
                	
               
                	$.getJSON("/visa/neworderjp/uploadFile?type=order&orderid=" + data.id, {}, function (resp) {
                		if (resp.code === "SUCCESS") {
                			if(index!=null){
                				
                				layer.close(index);
                			}
                			$.layer.confirm('发送成功，打开预览？', {
                				btn: ['预览', '关闭']
                			}, function (index, layero) {
                				window.open(resp.data);
                			}, function (index) {
                				$.layer.closeAll();
                			});
                			layer.msg("上传成功",{time: 2000});
                		} else {
                			$.layer.alert(resp.msg);
                		}
                	});
                	break;
                case "customerEdit1":
                	
		              	if (!(data = select(e))) return;
		              	layer.open({
		              		type: 2,
		              		title: '编辑客户信息',
		              		area: ['950px', '600px'],
		              		shadeClose: true,
		              		content: '/japan/japancustomerEdit.html?cid=' + data.id + "&check=true"
		              	});*/
		              	break;
                case "modify":
                    var data = grid.dataItem($(e.currentTarget).closest("tr"));
                    layer.open({
                        type: 2,
                        title: '编辑订单',
                        area: ['950px', '600px'],
                        shadeClose: true,
                        content: '/japan/japanEdit.html?cid=' + data.id + "&check=true"
                    });
                    break;
                case "download":
                	 if (!(data = select(e))) return;
                   /*  $.fileDownload("/visa/neworderjp/export?orderid=" + data.id, {
                         successCallback: function (url) {
                             $.layer.alert('文件不存在 :' + url);
                         },
                         failCallback: function (html, url) {
                             if (html.indexOf('<') >= 0) {
                                 html = $(html).text();
                             }
                             var json = JSON.parse(html);
                             $.layer.alert(json.msg);
                         }
                     });*/
                	 layer.open({
         	            type: 2,
         	            title: '下载',
         	            area: ['450px', '300px'],
         	            shadeClose: true,
         	            content: '/order/download.html?cid='+data.id,
         	           end: function(){//添加完页面点击返回的时候自动加载表格数据
         	    	    	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
         	    			parent.layer.close(index);
         	    	    }
         	        });
                     break;
             
                default:
                    $.layer.alert(command);
                    break;
            }
        }
    };
}
//上传
function uploadfile(e) {
	var index=null;
	var data = grid.dataItem($(e.currentTarget).closest("tr"));
	 var index=null;
		$.fileupload1 = $('#fileupload').uploadify({
			'auto' : true,//选择文件后自动上传
			'formData' : {
				'fcharset' : 'uft-8'/* ,
				'action' : 'uploadimage' */
			},
			'buttonText' : '上传',//按钮显示的文字
			'fileSizeLimit' : '3000MB',
			'fileTypeDesc' : '文件',//在浏览窗口底部的文件类型下拉菜单中显示的文本
			 'fileTypeExts' : '*.png;*.jpg; *.jpeg; *.gif;',//上传文件的类型
			'swf' : '/res/upload/uploadify.swf',//指定swf文件
			'multi' : false,//multi设置为true将允许多文件上传
			'successTimeout' : 1800, 
			/* 'queueSizeLimit' : 100, */
			'uploader' : "/visa/neworderjp/uploadFile?type=order&orderid=" + data.id,
			//下面的例子演示如何获取到vid
			'onUploadStart':function(file){
				index = layer.load(1, {shade: [0.1,'#fff']});//0.1透明度的白色背景 
			},
			 'onUploadSuccess': function(file, data, response) {
				 var fileName = file.name;//文件名称
				var photoname= "<a id='downloadA' href='#'>"
	             + file.name
	             + "</a>"
				 ///console.log(file);
				/* 	var jsonobj = eval('(' + data + ')');
					var url  = jsonobj;//地址 */
					///console.log(data);
				 ///console.log(response);
				/*  alert(typeof data); */
				 if(index!=null){
						layer.close(index);
				 }
				 /*显示 预览 按钮*/
				viewModel.set("customer.phoneurl",data);
				viewModel.set("customer.photoname",fileName);
				/*$("#yvlan").html('<a href="'+data+'">预览</a>'); */
	            $("#yvlan").html('<a href="javascript:;" id="preview">预览</a>');
				$("#photoname").html(photoname);
	            $(document).on('click','#preview',function(){
	            	$('#light').css('display','block');
	            	$('#fade').css('display','block');
	            	var phoneurl=viewModel.get("customer.phoneurl");
	            	if(phoneurl!=null&&phoneurl!=''){
	            		///console.log("1111111====="+phoneurl);
	            		$("#imgId").attr('src',phoneurl);
	            	}
	            });
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

function download(cid) {
    $.getJSON("/visa/photo/list", {cid: cid}, function (result) {
        if (result.code === "SUCCESS") {
            if (result.data.length > 0) {
                var array = [];
                $.each(result.data, function (i, e) {
                    array.push({
                        subHtml: "<h4>" + e.id.option.label + "</h4>",
                        src: e.value,
                    });
                });
                $.photos(array);
            } else {
                $.layer.alert("暂无照片");
            }
        } else {
            $.layer.alert(result.msg);
        }
    });
}
var a;
function detailInit(e) {
    a=$("<div/>").appendTo(e.detailCell).kendoGrid({
        scrollable: false,
        dataSource: {
            transport: {
                read: {
                    /*type: "GET",*/
                    dataType: "json",
                    url: "/visa/neworderjpland/childList?type=customer&orderId=" + e.data.id ,
                }
            }
        },
        schema: {
//          data: "content",
//          total: "totalElements",
        	data : function(d) {
	              return d.list;  //响应到页面的数据
	          },
	          total : function(d) {
	              return d.recordCount;   //总条数
	          }/*,
	          model: {
	                id: "id",
	                fields: {
	                	birthday: {type: "date"},
	          			email: {type: "string"},
	                    user: {defaultValue: {id: "1", name: "管理员"}},
	                    useFor: {defaultValue: "美国"},
	                    amount: {type: "number", defaultValue: 1, validation: {min: 1, required: true}}
	                }
	            }*/
      },
        columns: [
            {field: 'chinesefullname',title: '氏名'},
            {field: 'fullnameen',title: 'ピンイン'},
            {field: 'gender',title: '性別',values:genderlist},
            {field: 'nowcity',title: '居住地域'},
            {field: 'birthday',title: '生年月日',format: "{0: yyyy-MM-dd}",template: "<span class='ellipsis' title='#=data.birthday#'>#=data.birthday?(kendo.toString(data.birthday, 'yyyy-MM-dd')).substring(0,11):''#</span>"},
            {field: 'passport',title: '旅券番号'},
            
          /*  {field: 'phone', title: '电话'},
            {field: 'passport', title: '护照号'},
            {field: 'gender', title: '性别',values:genderlist},
            {field: 'sendtime', title: '送签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'outtime', title: '出签时间',format: "{0: yyyy-MM-dd}"},
            {field: 'status', title: ' 状态',values:statuslist},*/
            { 
                title: "操作",
                command: [

                    {name: "customerEdit1", imageClass:false, text: "编辑"},
                /*    {name: " ", imageClass: "base fa-send", text: "递送"},
                    {name: "share", imageClass: "base fa-share-alt", text: "分享"	
                    },//,template: "<span class='ellipsis' title='#=data.sharecount#'>#=data.chinesefullname#</span>"
                    {name: "notice", imageClass: "base fa-bell-o", text: "通知"},*/
                    regCmd("customerEdit1"),
                   /* regCmd("share"),
                    regCmd("notice"),*/
                /*    regCmd(" "),*/
                ],
            },
        ]
    });
}

//初始化上部的表格布局
var grid = $("#grid").kendoGrid({
    height: "93%",
    selectable: "row",
    sortable: true,
    resizable: true,
    filterable: true,
    reorderable: true,
    columnMenu: true,
    scrollable: true,
    pageable: {
        refresh: true,
        pageSizes: true,
        buttonCount: 5
    },
   detailInit: detailInit,
    sortable: {
        mode: "multiple",
    },
    toolbar: false,
    dataSource: {
        transport: {
            read: {
                type: "POST",
                dataType: "json",
                url: "/visa/neworderjpland/list",
                contentType: 'application/json;charset=UTF-8',
            },
            parameterMap: function (options, type) {
                /*return JSON.stringify(options);*/
            	var parameter = {
                        pageNumber : options.page,    //当前页
                        pageSize : options.pageSize,//每页显示个数
                        start_time:$("#start_time").val(),
                        end_time:$("#end_time").val(),
                        keywords:$("#keywords").val(),
                        state:$("#state").val()
                    };
               return kendo.stringify(parameter);
            },
        },
        schema: {
           /* data: "content",
            total: "totalElements",*/
        	data : function(d) {
                return d.list;  //响应到页面的数据
            },
            total : function(d) {
                return d.recordCount;   //总条数
            },
            model: {
                id: "id",
                fields: {
                	senddate: {type: "date"},
                	outdate: {type: "date"},
                	startdate: {type: "date"},
                    email: {type: "string"}
                   /* user: {defaultValue: {id: "1", name: "管理员"}},
                    useFor: {defaultValue: "美国"},
                    amount: {type: "number", defaultValue: 1, validation: {min: 1, required: true}}*/
                }
            }
        },
        pageSize: 20,
        serverPaging: true,
        serverFiltering: true
    },
    columns: [
 /*     {
	    field: 'rowNumber',
      	title: '序号',
      	template: "<span class='row-number'></span>",
      	width:75
      },*/
        {
            field: 'ordernumber', title: ' 订单号', width: 130,
            template: "<span class='ellipsis' title='#=ordernumber#'>#=ordernumber#</span>",
            editor: function (container, options) {
                container.hide().prev().hide();
            }
        },
        {field: 'completednumber', title: '受付番号', width: 110,template: "<span class='ellipsis' title='#=data.completednumber#'>#=data.completednumber?data.completednumber:''#</span>"},
        {field: 'fullcomname', title: '送签社', width: 88,template: "<span class='ellipsis' title='#=data.fullcomname#'>#=data.fullcomname?data.fullcomname:''#</span>"},
        {field: 'fullname', title: '操作人',  width: 88,},
        {field: 'mainporposer', title: '主申请人',  width:100,},
        {field: 'headnum', title: '人数',  width: 75,},
        {field: 'visatype', title: '签证类型', values:visatypelist, width:100,},
        {field: 'senddate', title: '送签时间',format: "{0: yyyy-MM-dd}",template: "<span class='ellipsis' title='#=data.senddate#'>#=data.senddate?kendo.toString(data.senddate, 'yyyy-MM-dd'):''#</span>"},
        {field: 'startdate', title: '出发时间',format: "{0: yyyy-MM-dd}",template: "<span class='ellipsis' title='#=data.startdate#'>#=data.startdate?kendo.toString(data.startdate, 'yyyy-MM-dd'):''#</span>"},
        {field: 'outdate', title: '返回时间',format: "{0: yyyy-MM-dd}",template: "<span class='ellipsis' title='#=data.outdate#'>#=data.outdate?kendo.toString(data.outdate, 'yyyy-MM-dd'):''#</span>"},
/*        {field: 'countrytype', title: '签证类型', width: 80,values:countrylist},*/
        {field: 'status', title: '状态',values:statuslist, width: 75,},
        {
            title: "操作", width:180,
            command: [
                {name: "modify", imageClass:false, text: "编辑"},
                {name: "validate", imageClass:false, text: "发招宝"},
                {name: "download", imageClass:false, text: "下载"},
                {name: "upload", imageClass:false, text: "上传",click:uploadfile},
                regCmd("modify"),
                regCmd("shareall"),
                regCmd("download"),
                /*regCmd("delivery"),*/
                regCmd("validate"),
                regCmd("upload"),
            ]
        }
    ]/*,
    dataBound: function () {  
        var rows = this.items();  
        $(rows).each(function () {  
            var index = $(this).index() + 1;  
            var rowLabel = $(this).find(".row-number");  
            $(rowLabel).html(index);  
        });  
    }*/
}).data("kendoGrid");
//页面刷新
function successCallback(id){
	//grid.GetJQuery().refresh();
	grid.dataSource.read();
	
	  if(id == '1'){
		  layer.msg("添加成功",{time: 2000});
	  }else if(id == '2'){
		  layer.msg("修改成功",{time: 2000});
	  }else if(id == '3'){
		  layer.msg("操作成功",{time: 2000});
	  }else if(id == '4'){
	layer.msg("准备下载,请稍候",{time: 2000});
}
  }
//页面加载时加载日历
$(function(){
	$("#start_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	$("#end_time").kendoDatePicker({culture:"zh-CN",format:"yyyy-MM-dd"});
	// 点击 下载 按钮时 触发
/*	$(document).on('click','.k-grid-download',function(){
			
	});*/
	
	
$("#fileupload").hide();
	
});
/*//点击触发日历
$().click(function(
		
));*/



