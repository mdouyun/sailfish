(function($) {

	$.fn.submitForm = function(options) {
		var defaults = {
            validator : null,
			progress : true,
			progressStartFunc : function() {
				$.progress();
			},
			progressEndFunc : function() {
				$.progress("close");
			},
			onSuccess : function(msg) {
				$.alert.Success(msg);
			},
			onFailure : function(msg) {
                $.alert.Error(msg);
			},
			onError : function() {
                $.alert.Error("网络请求超时，请稍后再试！");
			},
			data : null,
			dataType : "json"
		};

		var options = $.extend(defaults, options);

        if(options.validator != null && !options.validator.validated()) {
            return ;
        }
		
		var ajaxFormOptions = {
			beforeSubmit : function(formData, jqForm, options) {
                return true;
			},
			error : function() {
				var sec = 0;
				if (options.progress) {
					setTimeout(options.progressEndFunc, 500);
					sec = 800;
				}

				setTimeout(options.onError, sec);
			},
			success : function(result) {
				var sec = 0;
				if (options.progress) {
					setTimeout(options.progressEndFunc, 500);
					sec = 800;
				}

				var success = result.success;
				var msg = result.message;
				var data = result.obj;

				setTimeout(function() {
					if (success || success == "true") {
						options.onSuccess(msg, data);
					} else {
						options.onFailure(msg, data);
					}
				}, sec);
			},
			dataType : options.dataType,
			data : options.data
		};

		if (options.progress) {
			options.progressStartFunc();
		}

		this.each(function() {
			$(this).ajaxSubmit(ajaxFormOptions);
		});
	};

	$.request = {
		ajax : function(options) {
			var defaults = {
				url : null,
				progress : true,
				onSuccess : function(msg, data) {
					$.alert.Success(msg);
				},
				onFailure : function(msg, data) {
                    $.alert.Error(msg);
				},
				onError : function() {
                    $.alert.Error("网络请求超时，请稍后再试！");
				},
				data : null,
				async : true,
				type : "POST",
				dataType : "json"
			};

			var options = $.extend(defaults, options);
			if (options.progress) {
				$.progress();
			}

			$.ajax({
				type : options.type,
				url : options.url,
				cache : false,
				data : options.data,
				dataType : options.dataType,
				async : options.async,
				success : function(result) {
					var sec = 0;
					if (options.progress) {
						setTimeout(function() {
							$.progress("close");
						}, 500);
						sec = 800;
					}

					var success = result.success;
					var msg = result.message;
					var data = result.data;

					setTimeout(function() {
						if (success || success == "true") {
							options.onSuccess(msg, data);
						} else {
							options.onFailure(msg, data);
						}
					}, sec);
				},
				error : function() {
					var sec = 0;
					if (options.progress) {
						setTimeout(function() {
							$.progress("close");
						}, 500);
						sec = 800;
					}
					setTimeout(options.onError, sec);
				}
			});
		}
	};

})(jQuery);