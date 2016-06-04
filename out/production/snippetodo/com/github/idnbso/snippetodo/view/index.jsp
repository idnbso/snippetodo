<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnippeToDo Homepage</title>
    <link rel="stylesheet" type="text/css" media="all"
          href="${pageContext.request.contextPath}/resources/css/lib/bootstrap.css">
    <link rel="stylesheet" media="all"
          href="${pageContext.request.contextPath}/resources/css/client.css">
    <link rel="stylesheet" media="all"
          href="${pageContext.request.contextPath}/resources/css/index.css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,400italic,700'
          rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Press+Start+2P' rel='stylesheet'
          type='text/css'>
    <link rel='shortcut icon' type='image/x-icon'
          href='${pageContext.request.contextPath}/resources/img/favicon.ico'/>
</head>
<body>

<!-- Navigation Bar -->
<div class="navbar navbar-fixed-top">
    <div class="container">
        <div class="row">
            <div class="navbar-header">
                <a href="#" class="navbar-brand">
                    SNIPPEToDo
                </a>
                <button type="button" class="navbar-toggle"
                        data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>
            <ul class="nav navbar-nav navbar-right collapse navbar-collapse">
                <li>
                    <a href="http://localhost:8080/client/" class="hidden"
                       id="snpptd-home-login-status">
                        <i class="glyphicon glyphicon-user"
                           aria-hidden="true"></i>
                    </a>
                </li>
                <li>
                    <button class="btn hidden" data-toggle="modal"
                            data-target="#snpptd-home-signup-modal"
                            id="snpptd-home-signup-btn">
                        <i class="glyphicon glyphicon-user"
                           aria-hidden="true"></i>Signup
                    </button>
                </li>
                <li>
                    <button class="btn hidden" data-toggle="modal"
                            data-target="#snpptd-home-login-modal"
                            id="snpptd-home-login-btn">
                        <i class="glyphicon glyphicon-log-in"
                           aria-hidden="true"></i>Login
                    </button>
                </li>
                <li><a href='about.html' data-target="#" data-toggle="dropdown">About<span
                        class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href='#'>Our Story</a></li>
                        <li><a href='#'>Contact Us</a></li>
                        <li class="divider"></li>
                        <li><a href='#'>Blog</a></li>
                        <li class="divider"></li>
                        <li><a href='http://twitter.com'>Twitter</a></li>
                        <li><a href='http://facebook.com'>Facebook</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<!-- /Navigation Bar -->

<!-- Error Modal -->
<div class="modal fade" id="snpptd-error-modal" tabindex="0" role="dialog"
     aria-labelledby="snpptd-error-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="snpptd-error-modallabel">
                    An Error has Occurred!
                </h4>
            </div>
            <!-- Modal Body -->
            <div class="modal-body">
                <p id="snpptd-error-modal-message"></p>
                <p>Please try again later.</p>
            </div>
            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal" value="Reload Page"
                        onClick="window.location.reload()">
                    Reload Page
                </button>
            </div>
        </div>
    </div>
</div>
<!-- /Error Modal -->

<!-- Login Modal -->
<div class="modal fade" id="snpptd-home-login-modal" tabindex="-1" role="dialog"
     aria-labelledby="snpptd-home-login-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="snpptd-home-login-modallabel">
                    Login to SnippeToDo Client
                </h4>
            </div>
            <!-- Modal Body -->
            <div class="modal-body">
                <div class="alert alert-warning hidden"
                     id="snpptd-home-login-fb-alert">
                </div>
                <a href="#">
                    <img id="snpptd-home-fblogin-btn"
                         src="./resources/img/facebookloginbutton.png"/>
                </a>
                <div class="alert alert-warning hidden"
                     id="snpptd-home-login-alert">
                </div>
                <form id="snpptd-home-login-form" role="form" method="post">
                    <div class="form-group">
                        <div class="form-control-group">
                            <label class="control-label"
                                   for="snpptd-home-logininput-email">Email Address</label>
                            <div class="controls">
                                <input type="text" name="email" class="input-xlarge"
                                       id="snpptd-home-logininput-email">
                            </div>
                        </div>
                        <div class="form-control-group">
                            <label class="control-label"
                                   for="snpptd-home-logininput-password">Password</label>
                            <div class="controls">
                                <input type="password" name="password" class="input-xlarge"
                                       id="snpptd-home-logininput-password">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-default snpptd-home-modal-closebtn"
                        data-dismiss="modal">
                    Close
                </button>
                <button id="snpptd-home-login-modal-btn" type="button" class="btn btn-primary"
                        data-loading-text="<span class='glyphicon glyphicon-refresh
                        glyphicon-refresh-animate'></span> Logging in">
                    Login
                </button>
            </div>
        </div>
    </div>
</div>
<!-- /Login Modal -->

<!-- Signup Modal -->
<div class="modal fade" id="snpptd-home-signup-modal" tabindex="-1" role="dialog"
     aria-labelledby="snpptd-home-signup-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="snpptd-home-signup-modal-label">
                    Signup to use SnippeToDo
                </h4>
            </div>
            <!-- Modal Body -->
            <div class="modal-body">
                <form id="snpptd-home-signup-form" role="form" method="post">
                    <div class="alert alert-warning hidden"
                         id="snpptd-home-signup-alert">
                    </div>
                    <div class="form-group">
                        <div class="form-control-group">
                            <label class="control-label" for="snpptd-home-signup-email">Email
                                Address</label>
                            <div class="controls">
                                <input type="text" class="input-xlarge" name="email"
                                       id="snpptd-home-signup-email">
                            </div>
                        </div>
                        <div class="form-control-group">
                            <label class="control-label" for="snpptd-home-signup-firstname">Your
                                First Name</label>
                            <div class="controls">
                                <input type="text" class="input-xlarge" name="first_name"
                                       id="snpptd-home-signup-firstname">
                            </div>
                        </div>
                        <div class="form-control-group">
                            <label class="control-label" for="snpptd-home-signup-lastname">Your Last
                                Name</label>
                            <div class="controls">
                                <input type="text" class="input-xlarge" name="last_name"
                                       id="snpptd-home-signup-lastname">
                            </div>
                        </div>
                        <div class="form-control-group">
                            <label class="control-label"
                                   for="snpptd-home-signup-password">Password</label>
                            <div class="controls">
                                <input type="password" class="input-xlarge" name="password"
                                       id="snpptd-home-signup-password">
                            </div>
                        </div>
                        <div class="form-control-group">
                            <label class="control-label"
                                   for="snpptd-home-signup-confirmpassword">Retype
                                Password
                            </label>
                            <div class="controls">
                                <input type="password" class="input-xlarge" name="confirm_password"
                                       id="snpptd-home-signup-confirmpassword">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-default snpptd-home-modal-closebtn"
                        data-dismiss="modal">
                    Close
                </button>
                <button id="snpptd-home-signup-modal-btn" type="button" class="btn btn-primary"
                        data-loading-text="<span class='glyphicon glyphicon-refresh
                        glyphicon-refresh-animate'></span> Processing">
                    Sign Up
                </button>
            </div>
        </div>
    </div>
</div>
<!-- /Signup Modal -->

<!-- Middle Section -->
<div class="container">
    <div class="row">
        <div id="snpptd-home-main-content" class="col-xs-12 col-md-8 col-md-offset-2">
            <div id="snpptd-home-main-well" class="well">
                <h1 id="snpptd-home-main-header">SNIPPEToDo</h1>
                <h4>A simple, organised, and visual way to be productive for developers.</h4>
                <hr>
                <button class="btn btn-default btn-lg hidden" data-toggle="modal"
                        id="snpptd-home-main-getstarted-btn"
                        data-target="#snpptd-home-signup-modal">
                    Get Started!
                </button>
                <a href="http://localhost:8080/client/" class="btn btn-default btn-lg hidden"
                   id="snpptd-home-main-use-btn">
                </a>
            </div>
        </div>
    </div>
</div>
<!-- /Middle Section -->

<!-- Footer -->
<div class='footer'>
    <div class='container'>
        <div class='row'>
            <div class='col-sm-4 col-md-3 col-xs-6'>
                <h4>SNIPPEToDo</h4>
                <p>simple, organised, and visual way to be productive for developers. </p>
                <p><a href='#'>Get Started <i class='glyphicon glyphicon-arrow-right'></i></a></p>
            </div>

            <div class='col-sm-2 col-xs-6 col-md-offset-1'>
                <h4>Links</h4>
                <ul class='list-unstyled'>
                    <li><a href='#'>Home</a></li>
                    <li><a href='#'>Signup</a></li>
                    <li><a href='#'>Login</a></li>
                </ul>
            </div>

            <div class='clearfix visible-xs'></div>

            <div class='col-sm-2 col-xs-6'>
                <h4>About</h4>
                <ul class='list-unstyled'>
                    <li><a href='#'>Our Story</a></li>
                    <li><a href='#'>Contact Us</a></li>
                    <li><a href='#'>Blog</a></li>
                    <li><a href='#'>Twitter</a></li>
                    <li><a href='#'>Facebook</a></li>
                </ul>
            </div>

            <div class='col-sm-4 col-md-3 col-md-offset-1 col-xs-6'>
                <h4>Contact Us</h4>
                <ul class='list-unstyled'>
                    <li><i class='glyphicon glyphicon-globe'></i> Holon, Israel
                    <li>
                    <li><i class='glyphicon glyphicon-envelope'></i> <a href='mailto:#'>idanbusso@gmail.com</a>
                    <li>
                </ul>

                <p>SNIPPEToDo &copy;2016.</p>
            </div>
        </div>
    </div>
</div>
<!-- /Footer -->

<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/lib/jquery.validate.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/snippetodo.util.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/snippetodo.index.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/lib/bootstrap.min.js"></script>
</body>
</html>
