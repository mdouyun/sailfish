(function($) {
    $.utils = {
        isEmpty : function(str) {
            if (str == null || str == "" || str == undefined) {
                return true;
            } else {
                return false;
            }
        },
        toString : function(domer) {
            return $($('<div></div>').html(domer.clone())).html();
        },
        refreshPage : function() {
            window.location.reload(true);
        },
        reloadPage : function() {
            window.location.replace(window.location.href);
        },
        toUrl : function(url) {
            window.location.href = url;
        }
    }

})(jQuery);