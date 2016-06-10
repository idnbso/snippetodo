'use strict';

var snippeToDoUtil = {};

snippeToDoUtil.hostname = "http://localhost:8080/";
// snippeToDoUtil.hostname = "https://snippetodo.azurewebsites.net/";

snippeToDoUtil.eventsHandling = (function($) {
    /**
     * Displays an AJAX error in the error modal of the current page.
     */
    $(document).ajaxError(function(event, jqxhr, settings, thrownError) {
        var statusCode = jqxhr.status;
        var message = "CODE " + statusCode + ": " + thrownError;
        snippeToDoUtil.displayError(message);
    });
})(jQuery);

/**
 * Create a new error message to be displayed to the user with a Bootstrap's modal.
 */
snippeToDoUtil.displayError = function(message) {
    console.log(message);
    $('#snpptd-error-modal-message').html(message);
    $('.in').modal('hide'); // hide any open modals to only show the error modal
    $('#snpptd-error-modal').modal('show');
};

/**
 * Open the client page with the correct hostname in the current window.
 */
snippeToDoUtil.openClient =  function(){
    window.open(snippeToDoUtil.hostname + "client/", "_self");
};

/**
 * Removes the hash (#), and every other character afterwords,
 * from the url address on page load.
 */
(snippeToDoUtil.removeHash = function() {
    history.pushState("", document.title, window.location.pathname
        + window.location.search);
})();
