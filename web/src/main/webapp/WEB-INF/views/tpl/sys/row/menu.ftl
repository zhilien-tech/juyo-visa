<script id="menu" type="text/html">
    <ul class="{{root ? 'sidebar-menu':''}} treeview{{root ? '':'-menu'}}">
        {{if root}}
        <li class="header">主导航</li>
        {{/if}}
        {{each list as item index}}
        <li class="treeview" style="cursor:pointer" data-id="{{item.id}}" data-href="{{item.url}}"
            data-leaf="{{item.state}}" data-name="{{item.name}}" data-info="{{item.description}}">
        <#--如果是叶子节点则为a标签添加跳转连接-->
            <a {{(item.state !='closed' )? "data-href="+item.url :""}}>
                <i class="fa fa-fw fa-{{item.icon}}" style="color:{{item.color}}"></i>
                <span>{{item.name}}</span>
                {{if item.state == 'closed'}}
                <i class="fa fa-angle-right pull-right"></i>
                {{/if}}
            </a>
            {{if item.state == 'closed'}}
            <ul class="treeview-menu">
                <li>
                    <a>
                        <i class="fa fa-fw fa-spinner fa-spin"></i> 加载中...
                    </a>
                </li>
            </ul>
            {{/if}}
        </li>
        {{/each}}
    </ul>
</script>