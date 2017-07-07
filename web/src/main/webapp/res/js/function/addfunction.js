//页面加载时查询出上级功能
$(function(){
	var select = function (e) {
        var data = grid.dataItem($(e.currentTarget).closest("tr"));
        alert(data);
        return data;
    };
});