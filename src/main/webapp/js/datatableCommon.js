var datatable_language_cn = {
    "sSearch":"搜索",
    "sLengthMenu": "每页显示 _MENU_ 条记录",
    "sZeroRecords": "抱歉， 没有找到",
    "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
    "sInfoEmpty": "没有数据",
    "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "前一页",
        "sNext": "后一页",
        "sLast": "尾页"
    }
}

function retrieveData( sSource, aoData, fnCallback ) {
    //查询条件称加入参数数组
    var searchJson = $("form").serializeArray();
    $.ajax( {
        type: "GET",
        url: sSource,
        dataType:"json",
        data: "tableParam="+JSON.stringify(aoData) + "&searchParam=" + JSON.stringify(searchJson),
        success: function(data) {
            if (data.meta.success == true){
                fnCallback(data);
            }else{
                alert(data.meta.message);
            }
        },
        error:function () {
            $.jGrowl("加载数据失败", { life: 5000,position:"center"});
        }
    });
}


function deserialize(form,data){
    var rcheck = /^(?:radio|checkbox)$/i,
        rselect = /^(?:option|select-one|select-multiple)$/i,
        rvalue = /^(?:button|color|date|datetime|datetime-local|email|hidden|month|number|password|range|reset|search|submit|tel|text|textarea|time|url|week)$/i;

    var $form = (typeof(form)=="string" ? $("#"+form) : form);

    //得到所有表单元素
    function getElements( elements ) {
        //此处elements为jquery对象。这个map函数使用来便利elements数组的.如存在多个form表单，则便利多个form表单
        return elements.map(function(index, domElemen) {
            //this代表form表单，this.elements获取表单中的DOM数组. jQuery.makeArray 转换一个类似数组的对象成为真正的JavaScript数组。
            return this.elements ? jQuery.makeArray( this.elements ) : this;
            //过滤不启用的选项
        }).filter( ":input:not(:disabled)" ).get();
    }
    //把表单元素转为json对象
    function elementsToJson( elements ) {
        var current,elementsByName = {};
        //elementsByName对象为{控件名：控件元素或控件元素数组}
        jQuery.each( elements, function( i, element ) {
            current = elementsByName[ element.name ];
            elementsByName[ element.name ] = current === undefined ? element :
                //如果已经是一个数组了，那么就添加，否则构造一个数组
                ( jQuery.isArray( current ) ? current.concat( element ) : [ current, element ] );
        });
        return elementsByName;
    }

    var elementsJson = elementsToJson(getElements($form));
    //data是一个对象
    for(var key in data){
        var val = data[key];
        var dataArr = [];//更具数据直接构造一个jQUery序列化后的数组形式。
        //判断值是否为数组
        if( $.isArray(val)){
            for(var i= 0,v;v=val[i++];){
                //是数组那么就变成多个对象形式
                dataArr.push({name:key,value:v});
            }
        } else{
            //不是数组直接构造
            dataArr.push({name:key,value:val});
        }

        //根据数据构造的这个数组进行操作
        for(var m= 0,vObj;vObj=dataArr[m++];){
            var element;
            //如果表单中无元素则跳过
            if ( !( element = elementsJson[vObj.name] ) ) {
                continue;
            }
            //判断元素是否为数组,暂时获取第一个元素，后面会有迭代赋值。
            var type = element.length?element[0]:element;
            //元素类型
            type = ( type.type || type.nodeName ).toLowerCase();

            var property = null;
            //是单值类型
            if ( rvalue.test( type ) ) {
                element.value = (typeof(vObj.value)=="undefined" || vObj.value==null)?"":vObj.value;
                //checkbox
            } else if ( rcheck.test( type ) ) {
                property = "checked";
                //select
            } else if ( rselect.test( type ) ) {
                property = "selected";
            }
            //判断类型是否为多选
            if(property) {
                //如果是，则迭代多选的元素赋值
                for(var n= 0,e;e=element[n++];){
                    if(e.value==vObj.value){
                        //设置选中
                        e[property] = true;
                    }
                }
            }
        }
    }
};