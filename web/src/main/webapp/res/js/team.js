/**
 * Created by Chaly on 2017/3/20.
 */
var oid = $.queryString("oid");
var model = kendo.observable({
    click: function (e) {
        var data = {
            oid: oid, cid: $(e.target).closest(".row").data("params"),
        };
        $.layer.closeAll();
        $.layer.open({
            type: 2,
            title: '信息收集表',
            maxmin: true, //开启最大化最小化按钮
            area: ['700px', '600px'],
            content: '/m/' + $.queryString("to") + '.html?' + $.param(data)
        });
    },
    customers: []
});
kendo.bind($(document.body), model);
//加载订单数据
if (oid) {
    $.getJSON("/visa/order/team/" + oid, function (result) {
        model.set("customers", result);
    });
}
