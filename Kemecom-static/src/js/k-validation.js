(function($env, $) {
    $env.validate = function(formElement, messageElement) {
        var $form = $(formElement);
        var valid = $form.parsley('validate');
        var msg = "";
        $($form).find("input.parsley-validated:not(.parsley-success)").each(function(idx, el) {
            var $el = $(el);
            msg = msg + "<strong>" + $el.prevAll('label').text() + "</strong>" + $el.attr('data-message') + "<br>";
        });

        this.showFormMessage(messageElement, msg);

        return valid;
    };

    $env.showFormMessage = function(messageElement, msgContent) {
        var $errorMessage = $(messageElement);
        $errorMessage.html(msgContent);

        if (msgContent) {
            if ($errorMessage.is(':visible'))
                $errorMessage.slideUp();
        } else {
            if (!$errorMessage.is(':visible'))
                $errorMessage.slideDown();
        }
    };
})(K, jQuery /*Parsley*/);