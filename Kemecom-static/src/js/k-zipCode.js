(function($env, $, amplify) {
//    amplify.request.define("getZipCode", "ajax", {
//        url: 'http://www.buscacep.correios.com.br/servicos/dnec/consultaLogradouroAction.do',
//        type: 'post',
//        cache: "persist"
//    });
//
    $env.getZipCode = function(zipCode, callback, context) {
        amplify.request("getZipCode", {
            CEP: zipCode,
            Metodo: 'listaLogradouro',
            TipoConsulta: 'cep',
            StartRow: 1,
            EndRow: 10
        }, function(data) {
            var tbl = $($(data).find("table")[2]).find("td");
            var address = $.map(tbl, function(v, i) {
                return $(v).text();
            });
            callback.call(context, address, data);
        });
    };
})(K, jQuery);