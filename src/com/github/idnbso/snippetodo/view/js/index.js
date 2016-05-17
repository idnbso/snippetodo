(function() {
    // create new user
    $(document).on("click", "#registerButton", function() {
        var $form = $('#new-user-form');
        $('#signupModal').modal('hide');
        $.post("/client/newuser", $form.serialize(), function(responseJsonItem) {

        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // login as an existing user 
    $(document).on("click", "#loginButton", function() {
        var $form = $('#login-form');
        $('#loginModal').modal('hide');
        
        // TODO: add loading animation here

        // TODO: change the jquery call to $.ajax for seperated success and failure cases
        $.post("/client/login", $form.serialize(), function(responseJson) {
            window.open("http://localhost:8080/client/","_self");
            //window.open("http://snippetodo.azurewebsites.net/client/","_self");
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });
})();