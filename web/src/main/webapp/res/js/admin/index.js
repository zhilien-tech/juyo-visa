/**
 * Created by Chaly on 16/5/27.
 */
require(
    ['res/sys/js/admin/menu.js', 'jsite'],
    function (menu) {
        //初始化左侧菜单
        menu.init(".sidebar-menu", true);
    }
);