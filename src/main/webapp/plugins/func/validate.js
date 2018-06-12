(function($) {

    var validateRoles = {
        required : {
            text : "该输入项为必填项",
            regexp : function(field, args) {
                if(field.val().length < 1) {
                    return false;
                }

                return true;
            }
        },
        number : {
            text : "该输入项必须为数字",
            regexp: /^\d*$/
        },
        length : {
            text : "字段长度在{0}-{1}之间",
            regexp : function(field, args) {
                if($.utils.isEmpty(args[0])) {
                    args[0] = field.val().length;
                }

                if($.utils.isEmpty(args[1])) {
                    args[1] = field.val().length;
                }

                if(field.val().length <= args[1] && field.val().length >= args[0]) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    };

    $.fn.validate = function(options) {

        var defaults = {
            filter : "*",
            roles : validateRoles
        };

        var options = $.extend(defaults, options);
        options.effect = new validateEffect(options);

        if($(this).is("form") && $(this).length == 1) {
            return new formValidator($(this), options);
        } else if($(this).length == 1) {
            return new fieldValidator($(this), options);
        } else {
            var validates = new Array();

            $(this).each(function() {
                if($(this).is("form")) {
                    validates.push(new formValidator($(this), options));
                } else {
                    validates.push(new fieldValidator($(this), options));
                }
            });

            return validates;
        }
    }

    var validateEffect = function(options) {

        var defaults = {
            placement : 'right',
            container : 'body',
            trigger: 'manual'
        };

        this.options = $.extend(defaults, options);

        this.apply = function(field, isSuccess, text) {

            if(isSuccess) {
                field.parents(".form-group").addClass("has-success");
            } else {
                this.options.content = text;
                field.popover(this.options);

                field.popover('show');
                field.parents(".form-group").addClass("has-error");
            }
        };
        this.cancel = function(field) {
            field.popover('destroy');
            field.parents(".form-group").removeClass("has-success");
            field.parents(".form-group").removeClass("has-error");
        };

        return this;
    }

    function formValidator(form, options) {
        var fieldsArray = new Array();

        form.find("[validate]").filter(options.filter).each(function() {
            fieldsArray.push(new fieldValidator($(this), options));
        });

        this.fieldsArray = fieldsArray;

        this.clear = function() {

            $.each(this.fieldsArray, function(i, n) {
                n.clear();
            });
        };

        this.validated = function() {

            var result = true;
            $.each(this.fieldsArray, function(i, n) {
                result = n.validated() && result;
            });

            return result;
        };
    }

    function fieldValidator(field, options) {
        var _this = this;

        this.validateConfig = new Array();
        this.field = field;
        this.effect = options.effect;

        var conf = field.attr("validate");
        var methodArray = conf.split(" ");

        $.each(methodArray, function(i, n) {
            n = $.trim(n);

            if(n != null && n != "") {
                var regex = /\(.*\)/;
                var args = n.match(regex);
                var methodName = n.replace(args, "");

                var rule = options.roles[methodName];
                if($.utils.isEmpty(rule)) {
                    return ;
                }

                var validateMethod = new Object();
                validateMethod.method = rule;

                validateMethod.args = new Array();

                if(args != null && args != "") {
                    args = args[0];
                    args = args.substr(1, args.length - 2);

                    var argsArray = args.split(",");
                    $.each(argsArray, function(i, n) {
                        n = $.trim(n);
                        if(n != null && n != "") {
                            validateMethod.args.push(n);
                        }
                    });
                }

                _this.validateConfig.push(validateMethod);
            }
        });

        field.blur(function() {
            _this.validated();
        });

        this.clear = function() {
            this.effect.cancel(this.field);
        }

        this.validated = function() {

            var _field = this.field;
            var _placement = this.placement;

            this.effect.cancel(_field);

            var effect = this.effect;
            var result = false;
            $.each(this.validateConfig, function(i, validateMethod) {
                var regex = validateMethod.method["regexp"];
                var text = validateMethod.method["text"];
                var args = validateMethod.args;

                if(typeof regex == "function") {
                    result = regex(_field, args);
                } else {
                    result = regex.test(_field.val());
                }

                if(!result) {
                    $.each(args, function(i, n) {
                        text = text.replace("{" + i + "}", n);
                    });

                    effect.apply(_field, false, text);
                    return false;
                }
            });

            if(result) {
                effect.apply(_field, true);
            }

            return result;
        };
    }



})(jQuery);