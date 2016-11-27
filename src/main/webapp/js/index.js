$(function(){
    $("aside section ul li a").click(function () {
        $(".content").html("");
        var selection_href = $(this).attr("href");
        if(selection_href) {
            $(".content").load(selection_href);
        }
        $(".active").removeClass("active");
        $(this).parent().addClass("active");
        return false;
    })
});