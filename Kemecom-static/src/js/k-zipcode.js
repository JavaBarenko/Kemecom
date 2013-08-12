(function(K, $, amplify) {

    //Precisa ser na window, pois findZipCode chama globalmente
    window.correiocontrolcep = function(valor) {
        if (valor.erro) {
            alert("CEP não encontrado!");
            return;
        }

        amplify.publish("findZipCode/" + valor.cep, valor);
    };

    K.findZipCode = function(zipCode, callback, context) {
        var url = "http://cep.correiocontrol.com.br/" + zipCode + ".js";
        var scr = $('<script>');
        scr.attr('id', 'cep' + zipCode);
        scr.attr('src', url);
        scr.attr('charset', 'utf-8');
        $('#tartaro').append(scr);

        amplify.subscribe("findZipCode/" + zipCode, function(data) {
            try {
                callback.call(context, data);
            } finally {
                amplify.unsubscribe("findZipCode/" + zipCode, arguments.callee);
                $('#cep' + zipCode).remove();
            }
        });
    };

})(K, jQuery, amplify);