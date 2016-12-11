var times;
$(function(){
    $("aside section ul li a").click(function () {
        var selection_href = $(this).attr("href");
        var selection_target = $(this).attr("target");
        if (selection_target){
            return true;
        }
        $(".content").html("");
        if(selection_href) {
            window.clearInterval(times);
            times = null;
            $(".content").load(selection_href);
        }
        $(".active").removeClass("active");
        $(this).parent().addClass("active");
        return false;
    })

    $.ajax({
        url:"admin/login",
        type:"json",
        method:"GET",
        success:function (r) {
            if (r.meta.success){
                $("#loginName1").html(r.data.accountName);
                $("#loginName2").html(r.data.accountName);
                $("#loginDescribe").html(r.data.accountDescript);
            }else{
                alert("该会话己结束，请重新登录");
                window.location = "login.html";
            }
        }
    })
});

var datatable_language_cn = {
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
            alert("加载数据失败");
        }
    });
}