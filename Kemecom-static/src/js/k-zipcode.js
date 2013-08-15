/*
 * Modulo de pesquisa de CEP. Utiliza o servico do http://cep.correiocontrol.com.br
 */
(function(K, $, amplify) {

    /*
     * Funcao de callback do correiocontrol. Utilizada apenas internamente.
     */
    //Precisa ser na window, pois findZipCode chama globalmente
    window.correiocontrolcep = function(valor) {
        if (valor.erro) {
            alert("CEP não encontrado!");
            return;
        }

        amplify.publish("findZipCode/" + valor.cep, valor);
    };

    /*
     * Localiza o endereco de um CEP e o passa para a funcao de callback, no contexto especificado.
     */
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