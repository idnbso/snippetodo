(function() {
    'use strict';

    var snippeToDoIndex = {};

    snippeToDoIndex.eventsHandling = (function($) {
        /**
         * Set login HTML values according to the current user session on page load
         */
        $(document).ready(function() {
            try {
                $.get("/user/checksession", function(firstName) {
                    if (firstName !== "") { // update the nav bar with the user's first name
                        var $loginStatus = $('#snpptd-home-login-status');
                        $loginStatus.append(firstName);
                        $loginStatus.toggleClass('hidden');
                        var $clientButton = $('#snpptd-home-main-use-btn');
                        $clientButton.html("Click to start now, " + firstName + '!');
                        $clientButton.toggleClass('hidden');
                    }
                    else { // display the signup and login buttons at the nav bar as usual
                        $('#snpptd-home-login-btn').toggleClass('hidden');
                        $('#snpptd-home-signup-btn').toggleClass('hidden');
                        $('#snpptd-home-main-getstarted-btn').toggleClass('hidden');
                    }
                });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while checking the current session. " + e.message);
            }
        });

        /**
         * Forms validation setup
         */
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

        /**
         * Sign up of a new user
         */
        $(document).on("click", "#snpptd-home-signup-modal-btn", function() {
            try {
                var $form = $('#snpptd-home-signup-form');

                if ($form.valid()) {
                    var $this = $(this);
                    $this.button('loading');

                    $.post("/user/new", $form.serialize(), function(response) {
                        $this.button('reset');
                        var errorObj = response.error;
                        if (errorObj !== undefined) {
                            var $alert = $('#snpptd-home-signup-alert');
                            $alert.html(errorObj.message);
                            $alert.removeClass('hidden');
                        }
                        else {
                            $('#snpptd-home-signup-modal').modal('hide');
                        }
                    });
                }
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while an attempt to sign up a user was made. " +
                    e.message);
            }

            event.preventDefault(); // Important! Prevents submitting the form.
        });

        /**
         * Login as an existing user.
         */
        $(document).on("click", "#snpptd-home-login-modal-btn", function() {
            try {
                var $form = $('#snpptd-home-login-form');
                if ($form.valid()) {
                    var $this = $(this);
                    $this.button('loading');

                    $.post("/user/login", $form.serialize(), function(response) {
                        $this.button('reset');
                        var errorObj = response.error;
                        if (errorObj !== undefined) {
                            var $alert = $('#snpptd-home-login-alert');
                            $alert.html(errorObj.message);
                            $alert.removeClass('hidden');
                        }
                        else {
                            window.open(snippeToDoUtil.hostname + "client/", "_self");
                        }
                    });
                }
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while attempting to login. " + e.message);
            }

            event.preventDefault(); // Important! Prevents submitting the form.
        });

        /**
         * Closing a modal window.
         */
        $(document).on("click", ".snpptd-home-modal-closebtn", function() {
            try {
                var $form = $(this).closest(".modal-footer").prev('.modal-body').children('form');
                $form.trigger("reset"); // reset all of the form's values
                $form.find(".alert").addClass('hidden'); // hide the alert box for next use
                $form.validate().resetForm(); // reset all current jquery form validations
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while attempting to close a modal. " + e.message);
            }
            event.preventDefault(); // Important! Prevents submitting the form.
        });

        /**
         * Initializes the login form with a cookie value of the user's email address if available
         * from the last successful login.
         */
        $(document).on("click", "#snpptd-home-login-btn", function() {
            try {
                // first check if there are cookies saved with the email of the last successful login
                $.get("/user/initlogin", function(userEmail) {
                    if (userEmail !== null) {
                        $('#snpptd-home-logininput-email').val(userEmail);
                    } // else leave the form empty
                });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while initializing the login form. " + e.message);
            }
        });

        /**
         * Login as a Facebook user.
         */
        $(document).on("click", "#snpptd-home-fblogin-btn", function() {
            try {
                // send an authentication request to facebook server and open the redirected url
                $.get("/user/initfblogin", function(redirectUrl) {
                    window.open(redirectUrl, "_self");
                });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while logging in with Facebook. " + e.message);
            }

            event.preventDefault(); // Important! Prevents submitting the form.
        });
    })(jQuery);
})();