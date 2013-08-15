/*
 * Modulo de validacao de campos e formularios
 */
(function($env, $) {
    /*
     * Valida os inputs de um determinado form e apresenta os erros no elemento de mensagem especificado.
     * A validacao eh executada internamente pelo parsley.
     * Retorna true se validado com sucesso.
     */
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

    /*
     * Exibe uma mensagem de erros no elemento especificado.
     */
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