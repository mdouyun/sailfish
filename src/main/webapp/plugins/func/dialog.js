(function ($) {

    function Dialog(options) {

        var defaults = {
            id: "",
            title: "",
            body: "",
            foot: "",
            width: "",
            height: "",
            cls: ""
        };

        this.options = $.extend(defaults, options);

        if ($("#" + options.id).length <= 0) {
            var dialog = $('<div id="' + options.id + '" tabindex="-1" class="modal fade" aria-hidden="true" role="dialog">' +
                '<div class="modal-dialog"><div class="modal-content"></div></div></div>');

            dialog = dialog.find(".modal-content");

            dialog.append('<div class="modal-header">' +
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
                '<h4 class="modal-title">' + options.title + '</h4></div>');
            dialog.append($('<div class="modal-body"></div>').append(options.body));
            dialog.append($('<div class="modal-footer"></div>').append(options.foot));

            dialog.parents("#" + options.id).appendTo("body");

            $("#" + options.id).modal({
                backdrop: 'static',
                keyboard: false,
                show: false
            });
            $("#" + options.id).modal("hide");
        } else {
            // reset
            $("#" + options.id + "  .modal-header > h4").html(options.title);
            $("#" + options.id + "  .modal-body").empty();
            $("#" + options.id + "  .modal-body").append(options.body);
            $("#" + options.id + "  .modal-footer").empty();
            $("#" + options.id + "  .modal-footer").append(options.foot);
        }

        if (!$.utils.isEmpty(options.width)) {
            $("#" + options.id + " > .modal-dialog").css("width", options.width);
        } else {
            $("#" + options.id + " > .modal-dialog").css("width", "");
        }

        var _modalBody = $("#" + options.id + "  .modal-body");

        if (!$.utils.isEmpty(options.height)) {
            _modalBody.css("height", options.height);
        } else {
            _modalBody.css("height", "");
        }

        _modalBody.removeClass();
        _modalBody.addClass("modal-body");

        if (!$.utils.isEmpty(options.cls)) {
            _modalBody.addClass(options.cls);
        }
    };

    Dialog.prototype.show = function () {
        $("#" + this.options.id).modal("show");
    };

    Dialog.prototype.hide = function () {
        $("#" + this.options.id).modal("hide");
    };

    Dialog.prototype.setTitle = function (title) {

        if (!$.utils.isEmpty(title)) {
            title = this.options.title;
        }
        $("#" + this.options.id + " > .modal-header > h4").html(title);
    };

    $.fn.formDialog = function (options) {

        var _formId = $(this).attr("id");
        var _dialogId = "_formDialog_" + _formId;
        var _form = $(this);

        var _method = {
            show: function (title) {
                if (!$.utils.isEmpty(title)) {
                    $("#" + _dialogId + " > .modal-header > h4").html(title);
                }
                $("#" + _dialogId).modal("show");
            },
            close: function () {
                $("#" + _dialogId).modal("hide");
            },
            init: function (options) {
                var defaults = {
                    title: "",
                    buttons: [
                        {
                            text: "关闭",
                            cls: "",
                            closed: true,
                            click: function () {
                                $("#" + _dialogId).modal("hide");
                            }
                        }
                    ]
                };

                var options = $.extend(defaults, options);

                var _body = $.utils.toString($("#" + _formId));
                _form.remove();

                var _foot = $("<div></div>");
                $.each(options.buttons, function(i, n) {
                    var _btn = $('<button type="button" class="btn"></button>');
                    _btn.html(n.text);
                    _btn.addClass(n.cls);

                    if (!$.utils.isEmpty(n.closed) && n.closed == true) {
                        _btn.attr("data-dismiss", "modal");
                        _btn.addClass("btn-default");
                    }

                    if (!$.utils.isEmpty(n.click)) {
                        _btn.click(n.click);
                    }

                    _foot.append(_btn);
                });

                new Dialog({
                    id: _dialogId,
                    title: options.title,
                    body: _body,
                    foot: _foot,
                    height: options.height,
                    width: options.width
                });
            }
        };

        if ($.utils.isEmpty(options)) {
            options = "show";
        }

        if (typeof options == "string") {

            if (arguments.length > 1) {
                _method[options](arguments[arguments.length - 1]);
            } else {
                _method[options]();
            }

            return;
        } else {
            _method["init"](options);
        }
    };

    $.progress = function(action) {

        var dialog = new Dialog(
            {
                id: "_progressDialog",
                title: "请求处理中...",
                width: 400,
                body: '<div class="progress progress-striped active mb0">' +
                    '<div class="progress-bar progress-bar-info" role="progressbar" ' +
                    'aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" id="_progressDialogBar" style="width: 0%;"></div>',
                foot: '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>'
            });

        if (action != null && action == "close") {
            dialog.hide();
            return;
        }

        var _activeProgressBar;

        $("#_progressDialog").on(
            "shown.bs.modal",
            function () {
                _activeProgressBar = setInterval(function () {
                    var maxWidth = parseInt($("#_progressDialogBar")
                        .parent().css("width"), 10);
                    var width = parseInt($("#_progressDialogBar").css(
                        "width"), 10);
                    if (width < maxWidth) {
                        var percent = width / maxWidth + 0.1;

                        $("#_progressDialogBar").css("width",
                            maxWidth * percent + "px");
                    } else {
                        $("#_progressDialogBar").css("width", "0px");
                    }

                }, 700);
            });

        $("#_progressDialog").on("hidden.bs.modal", function () {
            $("#_progressDialogBar").css("width", "0%");
            window.clearInterval(_activeProgressBar);
        });

        dialog.show();
    };

})(jQuery);