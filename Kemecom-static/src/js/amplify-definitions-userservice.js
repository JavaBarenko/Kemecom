
(function(amplify) {
    amplify.request.decoders.defaultDecoder =
            function(data, status, xhr, success, error) {
                if (xhr.status === 500) {
                    success({message: xhr.statusText, status: xhr.status});
                } else {
                    success(data, xhr);
                }
            };

    amplify.request.define("user:getUserById", "ajax", {
        url: "/Kemecom-web/ws/user/{id}",
        dataType: "json",
        type: "GET"
    });

    amplify.request.define("user:addUser", "ajax", {
        url: "/Kemecom-web/ws/user",
        dataType: "json",
        type: "POST",
        decoder: "defaultDecoder"
    });

    amplify.request.define("user:updateUser", "ajax", {
        url: "/Kemecom-web/ws/user/{id}",
        dataType: "json",
        type: "POST"
    });

    amplify.request.define("user:removeUser", "ajax", {
        url: "/Kemecom-web/ws/user/{id}",
        dataType: "json",
        type: "DELETE"
    });

    amplify.request.define("user:getUsers", "ajax", {
        url: "/Kemecom-web/ws/user",
        dataType: "json",
        type: "GET"
    });

    amplify.request.define("user:updatePassword", "ajax", {
        url: "/Kemecom-web/ws/user/{id}/password",
        dataType: "json",
        type: "POST"
    });

    amplify.request.define("user:sendRememberPassword", "ajax", {
        url: "/Kemecom-web/ws/auth",
        dataType: "json",
        type: "POST",
        decoder: "defaultDecoder"
    });

    amplify.request.define("auth:isLoggedIn", "ajax", {
        url: "/Kemecom-web/ws/auth",
        dataType: "json",
        type: "GET"
    });


    amplify.request.define("auth:login", "ajax", {
        url: "/Kemecom-web/ws/auth",
        dataType: "json",
        type: "POST"
    });

    amplify.request.define("auth:logout", "ajax", {
        url: "/Kemecom-web/ws/auth",
        dataType: "json",
        type: "DELETE"
    });

}(amplify));