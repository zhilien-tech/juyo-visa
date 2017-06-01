<#include "./include/layout.ftl"/>
<@html>
    <@head>
        <@script src="res/sys/js/admin/main.js"/>
    </@head>
    <@body style="padding: 10px;">
        <div class="row">
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box bg-aqua">
                    <span class="info-box-icon"><i class="fa fa-users"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">用户量</span>
                        <span class="info-box-number">${userTotal?string(',###')}人</span>
                        <div class="progress">
                            <div class="progress-bar" style="width:${userIncrease*100/userTotal}%"></div>
                        </div>
                        <span class="progress-description">
                            最近30天增加${(userIncrease*100/userTotal)?string("0.##")}%
                        </span>
                    </div>
                    <!-- /.info-box-content -->
                </div>
                <!-- /.info-box -->
            </div>
            <!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box bg-green">
                    <span class="info-box-icon"><i class="fa fa-floppy-o"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">内存</span>
                        <span class="info-box-number">${formatByte(memoryUsed,'####')}
                            /${formatByte(memoryTotal,'####')}</span>
                        <div class="progress">
                            <div class="progress-bar" style="width:${memoryUsed*100/memoryTotal}%"></div>
                        </div>
                        <span class="progress-description">
                          内存利用率${memoryUsed*100/memoryTotal}%
                        </span>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box bg-yellow">
                    <span class="info-box-icon"><i class="fa fa-calendar"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">Events</span>
                        <span class="info-box-number">41,410</span>
                        <div class="progress">
                            <div class="progress-bar" style="width: 70%"></div>
                        </div>
                        <span class="progress-description">
                            p 70% Increase in 30 Days
                        </span>
                    </div>
                    <!-- /.info-box-content -->
                </div>
                <!-- /.info-box -->
            </div>
            <!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box bg-red">
                    <span class="info-box-icon"><i class="fa fa-hdd-o"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">硬盘</span>
                        <span class="info-box-number">
                            ${formatByte(diskUsed,'####')}/${formatByte(diskTotal,'####')}
                        </span>
                        <div class="progress">
                            <div class="progress-bar" style="width: ${diskUsed*100/diskTotal}%"></div>
                        </div>
                        <span class="progress-description">
                          硬盘利用率${diskUsed*100/diskTotal}%
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">一周注册</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="box-body no-padding">
                        <div id="chart" class="col-md-12" style="height: 260px;"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
        <div class="col-md-12">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">系统信息</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse">
                            <i class="fa fa-minus"></i>
                        </button>
                    </div>
                    <!-- /.box-tools -->
                </div>
                <!-- /.box-header -->
                <div class="box-body no-padding">
                    <table class="table">
                        <tbody>
                        <tr>
                            <th class="info">系统名称:</th>
                            <td>${(site.name)!}</td>
                            <th class="info">系统版本:</th>
                            <td>${(site.version)!}</td>
                        </tr>
                        <tr>
                            <th class="info">JAVA版本:</th>
                            <td>${javaVersion}</td>
                            <th class="info">JAVA路径:</th>
                            <td>
                                <#if javaHome?length lt 20>
                                ${javaHome}
                                <#else>
                                ${javaHome[0..21]}...
                                </#if>
                            </td>
                        </tr>
                        <tr>
                            <th class="info">操作系统名称:</th>
                            <td>${osName}</td>
                            <th class="info">操作系统构架:</th>
                            <td>${osArch}</td>
                        </tr>
                        <tr>
                            <th class="info">SERVLET信息:</th>
                            <td>${serverInfo}</td>
                            <th class="info">SERVLET版本:</th>
                            <td>${servletVersion}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
        </div>
    </div>
    </@body>
</@html>