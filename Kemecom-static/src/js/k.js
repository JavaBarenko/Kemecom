
(function($env, $, amplify) {
    var K = {};
    var homeTagName = null;

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


    K.goToHome = function() {
        K.goTo(homeTagName);
    };

    var token;
    K.setToken = function(tk) {
        token = tk;

        $env.location.href = tk ? "#KemecomToken=" + tk : "#";
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