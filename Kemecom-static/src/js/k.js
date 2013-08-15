/*
 * Modulo de definicao do framework K, utilizado pela aplicacao.
 * Esse framework cria a variavel global K que centraliza as principais funcionalidades globais utilizadas pelos demais componentes da aplicacao.
 */
(function($env, $, amplify) {
    var K = {};
    var homeTagName = null;

    /*
     * Carrega as configuracoes iniciais da aplicacao e publica o estado de logon do usuario.
     * @param string homeComponentTagName o nome do web component da home. Default: k-welcome
     * @returns {undefined}
     */
    K.bootstrap = function(homeComponentTagName) {
        $(function() {
            homeTagName = homeComponentTagName || "k-welcome";

            if (K.isLoggedIn()) {
                amplify.publish("auth/loggined");
            } else {
                amplify.publish("auth/loggouted");
            }

        });
    };

    function loggined() {
        K.goTo("k-profile");
    }

    function loggouted() {
        K.goToHome();
        K.setToken("");
    }

    amplify.subscribe("auth/loggined", function() {
        loggined();
    });
    amplify.subscribe("auth/loggouted", function() {
        loggouted();
    });

    /*
     * Carrega o webcomponent da home
     */
    K.goToHome = function() {
        K.goTo(homeTagName);
    };

    var token;
    /*
     * Utilizada pelo webcomponent de login para salvar o token
     */
    K.setToken = function(tk) {
        token = tk;

        $env.location.href = tk ? "#KemecomToken=" + tk : "#";
    };

    /*
     * Obtem o token do usuario.
     */
    K.getToken = function() {
        return token;
    };

    K.debug = true;

    function loadContent(jquerySelector, $webComponent) {
        var content = $(jquerySelector);
        content.html("");
        content.html($webComponent);
    }

    /*
     * Carrega o web component especificado
     */
    K.goTo = function(componentTagName) {
        loadContent("#content", $("<" + componentTagName + ">"));
    };

    var currMessage = {};

    function showMessage(message, hasSuccessFlag) {
        var type = hasSuccessFlag ? 'success' : 'error';
        currMessage = {message: message, type: type};
        loadContent(".message", $("<k-message message='" + message + "' type='" + type + "'>"));
    }

    /*
     * Modulo de mensagem de avisos
     */
    K.message = {
        /*
         * Apaga a mensagem de aviso
         */
        clear: function() {
            $(".message").html("");
        },
        /*
         * Exibe uma mensagem de sucesso
         */
        showSuccess: function(message) {
            if (K.debug)
                console.log(message);
            showMessage(message, true);
        },
        /*
         * Exibe uma mensagem de erro
         */
        showError: function(message) {
            if (K.debug)
                console.error(message);
            showMessage(message, false);
        },
        /*
         * Obtem a mensagem atual
         */
        getCurrentMessage: function() {
            return currMessage;
        }
    };
    /*
     * Verifica se o usuario esta logado. 
     */
    K.isLoggedIn = function() {
        var paramToken = $env.document.URL.match(/KemecomToken=[\da-f]{24}/);
        if (paramToken)
            paramToken = paramToken[0].split('=')[1];

        if (paramToken) {
            token = paramToken;
            return true;
        } else {
            var result = JSON.parse($.ajax({
                url: "/Kemecom-web/ws/auth",
                dataType: "json",
                async: false,
                type: "GET",
                beforeSend: function(xhr, settings) {
                    xhr.setRequestHeader('KemecomToken', K.getToken());
                }
            }).responseText);
            return result.successful;
        }
    };

    $env.K = K;
}(window, jQuery, amplify));