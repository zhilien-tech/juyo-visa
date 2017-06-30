//客户来源
var customersourceEnum=[
    {text:"线上",value:1},
    {text:"OTS",value:2},
    {text:"直客",value:3},
    {text:"线下",value:4}
  ];
var defaults = {
		customermanage:{},
		tripJp:{
			oneormore:0
		},
		dateplanJpList:[],
		tripplanJpList:[],
		fastMail:{}
}, 
keys = {
		"customer.dateplanJpList":{
			startdate:"",
			startcity:"",
			arrivecity:"",
			flightnum:"",
			returndate:"",
			returnstartcity:"",
			returnarrivecity:"",
			returnflightnum:""
		},
		"customer.tripplanJpList":{
			daynum:"",
			nowdate:"",
			city:"",
			viewid:"",	
			hotelid:"",
			hometype:"",
			homenum:"",
			homeday:"",
			intime:"",
			outtime:"",
			breakfast:"",
			dinner:""

		}
},
flights = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/flight/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
}), hotels = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/hotel/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
}), scenic = new kendo.data.DataSource({
    serverFiltering: true,
    transport: {
        read: {
            dataType: "json",
            url: "/visa/scenic/json",
        },
        parameterMap: function (options, type) {
            if (options.filter) {
                return {filter: options.filter.filters[0].value};
            }
        },
    }
});
var viewModel = kendo.observable({
	 customersourceEnum:customersourceEnum,
	 customer: defaults,
});
kendo.bind($(document.body), viewModel);