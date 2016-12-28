var table;
$(function () {

    $("#visitorTime").datepicker({
        language: 'zh-CN',
        autoclose: true,
        format: "yyyy-mm-dd"
    });

    $("#visitorTime_search").daterangepicker({
        startDate: moment().subtract('months', 1),
        endDate: moment().add('months',3),
        ranges : {
            '今日': [moment().startOf('day'), moment()],
            '明日': [moment().add('days', 1), moment().add('days', 1)],
            '未来1周': [moment().add('days', 1), moment().add('days', 8)],
            '过去1周': [moment().subtract('days', 8), moment().subtract('days', 1)],
            '过去3月': [moment().subtract('months', 3).subtract('days', 1), moment().subtract('days', 1)]
        },
        separator : ' 至 ',
        locale: {
            format: 'YYYY-MM-DD', applyLabel: '确定',
            cancelLabel: '取消',
            fromLabel: '起始时间',
            toLabel: '结束时间',
            customRangeLabel: '自定义',
            daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
                '七月', '八月', '九月', '十月', '十一月', '十二月'],
            firstDay: 1
        }
    });

    table = $("#example1").DataTable({
        "paging": true,
        "lengthChange": false,
        "searching": false,
        "ordering": true,
        "fnServerData": retrieveData,
        "autoWidth": true,
        "language": datatable_language_cn,
        "serverSide": true,
        sAjaxSource: "../visitorController/visitor",
        columns: [
            {data: "id"},
            {data: "visitorName"},
            {data: "companyName"},
            {data: "IDCard"},
            {data: "sex"},
            {data: "phone"},
            {data: "visitorTime"},
            {data: "reason"},
            {data: "status"}
        ]
    });

    $.ajax({
        url:"../visitorController/login",
        type:"json",
        method:"GET",
        success:function (r) {
            if (r.meta.success){
                $("span[name='userLoginName']").html(r.data.userName);
            }else{
                $.jGrowl(r.meta.message, { life: 1000,position:"center",beforeClose: function(e,m) {
                    window.location = "login.html";
                }});
            }
        }
    })

    $("#refresh").on("click",function () {
        window.location.reload();
    })

    $("#search").on("click",function () {
        table.ajax.reload();
    })
})

function changeAction(modalTitle, type) {
    $("#modalTitle").html(modalTitle);
    $("#visitorBookForm").attr("type", type);

    $("#visitorBookForm")[0].reset();

    $("#visitorBookForm").attr("action", "../visitorController/visitor");

    var selected = table.row('.info').data();
    if (selected) {
        deserialize("visitorBookForm", selected);
    }

    if (type == 'delete') {
        $("#visitorBookForm").attr("action", "../visitorController/visitor/" + $("#id").val());
    }

    if (type == 'post'){
        $("#id").val("");
    }
}

function saveVisitorBooking() {
    var $visitorBookForm = $("#visitorBookForm");
    var data = $visitorBookForm.serializeArray();
    var action = $visitorBookForm.attr("action");
    var type = $visitorBookForm.attr("type");
    $.ajax({
        url: action,
        type: type,
        data: data,
        success: function (data) {
            if (data.meta.success) {
                $.jGrowl(data.meta.message, {
                    life: 100, position: "center", beforeClose: function (e, m) {
                        window.location.reload();
                    }
                });
            } else {
                $.jGrowl(data.meta.message, {life: 5000, position: "center"});
            }
        }
    });
}

function midifyPassword() {
    var modifyPasswordForm = $("#modifyPasswordForm");
    var data = modifyPasswordForm.serializeArray();
    var action = "../visitorController/login";
    $.ajax({
        url: action,
        type: "put",
        data: data,
        success: function (data) {
            if (data.meta.success) {
                $.jGrowl(data.meta.message, {
                    life: 100, position: "center", beforeClose: function (e, m) {
                        window.location = "login.html";
                    }
                });
            } else {
                $.jGrowl(data.meta.message, {life: 5000, position: "center"});
            }
        }
    });
}