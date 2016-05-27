(function() {
    'use strict';

    // set html values with cookie on page load
    $(document).ready(function() {
        $.get("/client/initlogin", function(userEmail) {
            if (userEmail !== null) {
                $('#snpptd-home-logininput-email').val(userEmail);
            }
        });
    });

    // create new user
    $(document).on("click", "#snpptd-home-signup-button", function() {
        var $form = $('#snpptd-home-signup-form');
        var $this = $(this);
        $this.button('loading');

        $.post("/client/newuser", $form.serialize(), function() {
            $this.button('reset');
            $('#snpptd-home-signup-modal').modal('hide');
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // login as an existing user
    $(document).on("click", "#snpptd-home-login-btn", function() {

        var $form = $('#snpptd-home-login-form');
        var $this = $(this);
        $this.button('loading');

        // TODO: change the jquery call to $.ajax for seperated success and failure cases
        $.post("/client/login", $form.serialize(), function() {
            window.open("http://localhost:8080/client/", "_self");
            // window.open("http://snippetodo.azurewebsites.net/client/","_self");
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });
})();