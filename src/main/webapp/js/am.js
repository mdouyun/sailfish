$(function() {
    var miniHeight = $("#container").height();
    var footerHeight = $("footer").height() * 2;
    var maxHeight = document.body.scrollHeight - 5;

    if(maxHeight > (miniHeight + footerHeight)) {
        var height = maxHeight - footerHeight;

        $("#container").css("*height", "auto !important");
        $("#container").css("*height", height + "px");
        $("#container").css("min-height", height + "px");
    }

    var thisUrl = window.location.href;
    $("#moduleNav li a").each(function() {
        var href = $(this).attr("href");
        if(matchUrl(href, thisUrl)) {
            $(this).parent("li").addClass("active");
            return false;
        }
    });

    $("a[href=#]").attr("href", "javascript:void(0);");
});

function matchUrl(pUrl, cUrl) {

    if(cUrl.indexOf(pUrl) > 0) {
        return true;
    }

    var reg = new RegExp("(^|&)module=([^&]*)(&|$)","i");
    var module = cUrl.match(reg);
    if(pUrl.indexOf("/" + module + "/index") >= 0) {
        return true;
    }

    return false;
}