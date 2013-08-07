
(function($env, $, amplify){
    var K = {};
    var homeTagName = null;

    K.setHome = function(homeComponentTagName){
        homeTagName = homeComponentTagName;
    };

    K.goToHome = function(){
        K.goTo(homeTagName);
    };


    function loadContent(jquerySelector, $webComponent, fadeSpeed){
        var content = $(jquerySelector);
        content.html("");
        if(fadeSpeed){
            $webComponent.hide();
            content.html($webComponent);
            $webComponent.fadeIn(fadeSpeed);
        } else {
            content.html($webComponent);
        }
    }

    K.goTo = function(componentTagName){
        loadContent("#content", $("<"+componentTagName+">"));
    };

    function showMessage(message, hasSuccessFlag){
        var type = hasSuccessFlag? 'success':'error';
        loadContent(".message", $("<k-message message='"+message+"' type='"+type+"'>"), 'slow');
    }

    K.message = {
        clear: function(){
            $(".message").html("");
        },
        showSuccess: function(message){
            showMessage(message, true, 'slow');
        },
        showError: function(message){
            showMessage(message, false, 'slow');
        }
    };


    $env.K = K;
}(window, jQuery, amplify));