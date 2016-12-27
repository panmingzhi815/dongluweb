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
                $("span[name='userLoginName']").html(r.data.accountName);
            }else{
                $.jGrowl(r.meta.message, { life: 1000,position:"center",beforeClose: function(e,m) {
                    window.location = "login.html";
                }});
            }
        }
    })

    var active = $("aside section ul li .active");
    if (active) {
        var selection_href = active.attr("href");
        var selection_target = active.attr("target");
        $(".content").load(selection_href);
    }
});
