/*function Map() {

    this.elements = new Array();

    this.size = function() {
        return this.elements.length;
    }

    this.isEmpty = function() {
        return (this.elements.length < 1);
    }

    this.clear = function() {
        this.elements = new Array();
    }

    this.put = function(_key, _value) {
        if (!this.containsKey(_key))
            this.elements.push({
                key : _key,
                value : _value
            });
    }

    this.remove = function(_key) {
        var bln = false;

        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    }

    this.get = function(_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return null;
        }
    }

    this.element = function(_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return null;
        }
        return this.elements[_index];
    }

    this.containsKey = function(_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    }

    this.containsValue = function(_value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    }

    this.values = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    }

    this.keys = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    }
}
*/



var country;
$(function(){
	 //country=JSON.parse($.queryString("country"));
    country = JSON.parse(unescape($.queryString("country")));
//    alert(unescape($.queryString("country")));
    if(country!=null&&country!=''){
		//alert($.queryString("country"));S
		 console.log($.queryString("country"));
		var a=country.status;
//			alert(a);
		var customerid=country.id;
		//获取ordernum
		$.ajax({
			 type: "POST",
			 url: "/visa/progress/ordernumber?customerid="+customerid,
			 contentType: "application/json",
			 dataType: "json",
			 success: function (result) {
				$("#ordernum").text("订单号:"+result.ordernumber);
			 }
		 });
		
		
		if(a!=0){
//			alert($("#writeResource"));
			$("#writeResource").addClass("a-active");
		}
		if(a>=4){
			$("#approvel").addClass("a-active");
		}
		if(a==4){
			$("#reason").hide();
			$("#approvel").text("初审通过");
		}
		if(a!=0&&a!=5){
			$("#readysubmit").addClass("a-active");
			
		}
		if(a==5){
			$("#approvel").text("初审拒绝");
		}
		if(a==10){
			$("#yueVisa").addClass("a-active");
			
		}
		if(a>=9){
			$("#alreadysubmit").addClass("a-active");
			
		}
	}
	/* $("#name").text(country.chinesexing+country.chinesename);*/
});


function reasion(){
	var reason=country.errorinfo;
	var map=new Map();
	map=eval("("+reason+")");
//	alert(JSON.stringify(map));
	var reasonnew="";
	for (var key in map) {  
		var a = map[key];
		for (var k in a) {  
//			alert(k+'---'+JSON.stringify(a[k]));
			var b=1;
			for(var i in a[k]){
//				alert(i);
				if(b%2!=0){
					
//					alert("=="+(a[k])[i]);
					reasonnew+=(a[k])[i]+",";
				}
				b++;
			}
		} 
	}
	reasonnew+="有问题！请修改！";
	 $.layer.prompt({
         formType: 2,
         value: reasonnew,
         title: '拒绝原因',
     }, function (value, index, elem) {
//         
    	 $.layer.closeAll();
     });
}


function timeapply(){
	 $.layer.prompt({
         formType: 2,
         value: '',
         title: '时间申请',
     }, function (value, index, elem) {
    	 alert(value+"=="+index+"=="+elem);
    	 $.layer.closeAll();
     });
}
