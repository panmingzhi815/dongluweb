var table;
$(function () {
    $("#visitorTime").datepicker({
        autoclose: true,
        format: "yyyy-mm-dd"
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
            {data: "id", searchable: false},
            {data: "visitorName"},
            {data: "companyName"},
            {data: "IDCard"},
            {data: "sex"},
            {data: "phone"},
            {data: "visitorTime", searchable: false},
            {data: "reason"}
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
})

function changeAction(modalTitle, type) {
    $("#modalTitle").html(modalTitle);
    $("#visitorBookForm").attr("type", type);
    $("#id").val("");
    $("#visitorBookForm")[0].reset();

    $("#visitorBookForm").attr("action", "../visitorController/visitor");

    if (type == 'post') {
        return;
    }

    var selected = table.row('.info').data();
    if (selected) {
        deserialize("visitorBookForm", selected);
    }

    if (type == 'delete') {
        $("#visitorBookForm").attr("action", "../visitorController/visitor/" + $("#id").val());
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