
(function($env, $, amplify) {
    var K = {};
    var homeTagName = null;

    K.setHome = function(homeComponentTagName) {
        homeTagName = homeComponentTagName;
    };

    K.goToHome = function() {
        K.goTo(homeTagName);
    };

    var token;
    K.setToken = function(tk) {
        token = tk;
    };

    K.getToken = function() {
        return token;
    };

    K.debug = true;

    function loadContent(jquerySelector, $webComponent) {
        var content = $(jquerySelector);
        content.html("");
        content.html($webComponent);
    }

    K.goTo = function(componentTagName) {
        loadContent("#content", $("<" + componentTagName + ">"));
    };

    var currMessage = {};

    function showMessage(message, hasSuccessFlag) {
        var type = hasSuccessFlag ? 'success' : 'error';
        currMessage = {message: message, type: type};
        loadContent(".message", $("<k-message message='" + message + "' type='" + type + "'>"));
    }

    K.message = {
        clear: function() {
            $(".message").html("");
        },
        showSuccess: function(message) {
            if (K.debug)
                console.log(message);
            showMessage(message, true);
        },
        showError: function(message) {
            if (K.debug)
                console.error(message);
            showMessage(message, false);
        },
        getCurrentMessage: function() {
            return currMessage;
        }
    };

    K.isLoggedIn = function() {
        return JSON.parse($.ajax({
            url: "/Kemecom-web/ws/auth",
            dataType: "json",
            async: false,
            type: "GET",
            beforeSend: function(xhr, settings) {
                xhr.setRequestHeader('KemecomToken', K.getToken());
            }
        }).responseText).successful;
    };


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

    $env.K = K;
}(window, jQuery, amplify));