//客户来源
var customersourceEnum=[
    {text:"线上",value:1},
    {text:"OTS",value:2},
    {text:"直客",value:3},
    {text:"线下",value:4}
    
  ];
function translateZhToEn(from, to) {
    $.getJSON("/translate/google", {q: $(from).val()}, function (result) {
        $("#" + to).val(result.data).change();
    });
}









var countries = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/country.json",
                dataType: "json"
            }
        }
    }),
    states = new kendo.data.DataSource({
        transport: {
            read: {
                url: "/res/json/usa_states.json",
                dataType: "json"
            }
        }
    }),
    dafaults = {
			passportlose:{},
			oldname:{},
			orthercountrylist:[],
			father:{},
			mother:{},
			relation:[],
			spouse:{},
			usainfo:{},
			teachinfo:[],
			recentlyintousalist:[],
			workinfo:{},
			oldworkslist:[],
			languagelist:[],
			visitedcountrylist:[],
			workedplacelist:[],
			army:{}
		
		
    },
    keys = {
		"customer.orthercountrylist":{},
		"customer.recentlyintousalist":{},
		"customer.oldworkslist":{},
		"customer.languagelist":{},
		"customer.visitedcountrylist":{},
		"customer.workedplacelist":{},
		"customer.relation":{},
		"customer.teachinfo":{}
		
	}
;
/*****************************************************
 * 数据绑定
 ****************************************************/
var viewModel = kendo.observable({
    customer: dafaults,
    countries:countries,
    customersourceEnum:customersourceEnum,
    states:states,
    hasTogether: function () {
        var togethers = viewModel.get("customer.travel.togethers");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;
    },
    addOne: function (e) {
        var key = $.isString(e) ? e : $(e.target).data('params');
        console.log(key);
       /* viewModel.get(key).push(
        		{
	          		peerxing: "",
	            	peerxingen: "",
	            	peernameen: "",
	            	peername: "",
	            	tripid: "",
	            	relationme: ""
	          	}
        );*/
        viewModel.get(key).push(keys[key]);
    },
    delOne: function (e) {
        var key = $(e.target).data('params');
        var all = viewModel.get(key);
        all.splice(all.indexOf(e.data), 1);
    },
    // 是否有同行人
    hasTogether: function () {
    	var togethers = viewModel.get("customer.peerList");
        var state = false;
        if (togethers) state = togethers.length > 0;
        return state;
        /*viewModel.set("customer.ordernumber", $(this).is(':checked') ? " " : "");*/
    },
    clearAll: function (key) {
        var all = viewModel.get(key);
        if (all) all.splice(0, all.length);
    },
 // 支付人
    payType: function (type) {
        return viewModel.get("customer.trip.paypersion") === type;
    },
    
});
kendo.bind($(document.body), viewModel);

