(function() {
    'use strict';

    // set html values with cookie on page load
    $(document).ready(function() {
        $.get("/user/checkstatus", function(firstName) {
            if (firstName !== "") {
                var $loginStatus = $('#snpptd-home-login-status');
                $loginStatus.append(firstName);
                $loginStatus.toggleClass('hidden');
                var $clientButton = $('#snpptd-home-main-use-btn');
                $clientButton.html("Click to start now, " + firstName + '!');
                $clientButton.toggleClass('hidden');
            }
            else {
                $('#snpptd-home-login-btn').toggleClass('hidden');
                $('#snpptd-home-signup-btn').toggleClass('hidden');
                $('#snpptd-home-main-getstarted-btn').toggleClass('hidden');
            }
        });
    });

    // forms validation initialization
    $(document).ready(function() {
        var successElement =
            '<i class="glyphicon glyphicon-ok-circle" aria-hidden="true"></i>';

        $('#snpptd-home-signup-form').validate({
            rules: {
                first_name: {
                    minlength: 1,
                    required: true
                },

                last_name: {
                    minlength: 1,
                    required: true
                },

                password: {
                    required: true,
                    minlength: 1
                },

                confirm_password: {
                    required: true,
                    minlength: 1,
                    equalTo: "#snpptd-home-signup-password"
                },

                email: {
                    required: true,
                    email: true,
                    minlength: 3
                }
            },
            highlight: function(element) {
                $(element).closest('.control-group').removeClass('success').addClass('error');
            },
            success: function(element) {
                element
                    .html(successElement).addClass('valid')
                    .closest('.control-group').removeClass('error').addClass('success');
            }
        });

        $('#snpptd-home-login-form').validate({
            rules: {
                email: {
                    required: true,
                    // TODO remove this comment
                    // email: true
                },
                password: {
                    required: true,
                    minlength: 1
                }
            },
            highlight: function(element) {
                $(element).closest('.control-group').removeClass('success').addClass('error');
            },
            success: function(element) {
                element
                    .html(successElement).addClass('valid')
                    .closest('.control-group').removeClass('error').addClass('success');
            }
        });

    });

    // create new user
    $(document).on("click", "#snpptd-home-signup-modal-btn", function() {
        var $form = $('#snpptd-home-signup-form');
        if ($form.valid()) {
            var $this = $(this);
            $this.button('loading');

            $.post("/user/new", $form.serialize(), function() {
                $this.button('reset');
                $('#snpptd-home-signup-modal').modal('hide');
            });
        }

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // login as an existing user
    $(document).on("click", "#snpptd-home-login-modal-btn", function() {
        var $form = $('#snpptd-home-login-form');
        if ($form.valid()) {
            var $this = $(this);
            $this.button('loading');

            $.post("/user/login", $form.serialize(), function() {
                window.open("http://localhost:8080/client/", "_self");
                // window.open("https://snippetodo.azurewebsites.net/client/","_self");
            });
        }

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    $(document).on("click", "#snpptd-home-login-btn", function() {
        // first check if there are cookies saved with the email of the last successful login
        $.get("/user/initlogin", function(userEmail) {
            if (userEmail !== null) {
                $('#snpptd-home-logininput-email').val(userEmail);
            }
        });
    });

    // login as a facebook user
    $(document).on("click", "#snpptd-home-fblogin-btn", function() {

        // first check if there are cookies saved with the email of the last successful login
        $.get("/user/initfblogin", function(redirectUrl) {
            window.open(redirectUrl);
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    $(document).ajaxError(function(event, jqxhr, settings, thrownError) {

        console.log(thrownError);

    });

    function newError() {

    }
})();