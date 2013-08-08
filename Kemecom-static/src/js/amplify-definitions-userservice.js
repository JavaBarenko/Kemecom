
(function(amplify) {
    amplify.request.define("user:getUserById", "ajax", {
        url: "/Kemecom-web/ws/user/{id}",
        dataType: "json",
        type: "GET"
    });

    amplify.request.define("user:addUser", "ajax", {
        url: "/Kemecom-web/ws/user",
        dataType: "json",
        type: "PUT"
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

    amplify.request.define("user:login", "ajax", {
        url: "/Kemecom-web/ws/user/login",
        dataType: "json",
        type: "POST"
    });

    amplify.request.define("user:sendRememberPassword", "ajax", {
        url: "/Kemecom-web/ws/user/reset/password",
        dataType: "json",
        type: "POST"
    });

}(amplify));