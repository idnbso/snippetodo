(function() {
    'use strict';

    // set html values with cookie on page load
    $(document).ready(function() {
        $.get("/client/initlogin", function(userEmail) {
            if (userEmail !== null) {
                $('#loginInputEmail').val(userEmail);
            }
        });
    });

    // create new user
    $(document).on("click", "#registerButton", function() {
        var $form = $('#new-user-form');
        var $this = $(this);
        $this.button('loading');

        $.post("/client/newuser", $form.serialize(), function(responseJsonItem) {
            $this.button('reset');
            $('#signupModal').modal('hide');
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // login as an existing user
    $(document).on("click", "#loginButton", function() {

        var $form = $('#login-form');
        var $this = $(this);
        $this.button('loading');

        // TODO: change the jquery call to $.ajax for seperated success and failure cases
        $.post("/client/login", $form.serialize(), function(responseJson) {
            window.open("http://localhost:8080/client/", "_self");
            //window.open("http://snippetodo.azurewebsites.net/client/","_self");
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });
})();