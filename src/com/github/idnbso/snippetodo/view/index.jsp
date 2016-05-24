<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"
         import="com.github.idnbso.snippetodo.controller.facebook.*" %>
<%
    FBConnection fbConnection = new FBConnection();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnippeToDo Homepage</title>
    <link rel="stylesheet" type="text/css" media="all"
          href="${pageContext.request.contextPath}/resources/css/bootstrap.css">
    <link rel="stylesheet" media="all" href="${pageContext.request.contextPath}/resources/css/client.css">
    <link rel="stylesheet" media="all" href="${pageContext.request.contextPath}/resources/css/index.css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,400italic,700'
          rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Press+Start+2P' rel='stylesheet'
          type='text/css'>
</head>
<body>

<!-- Navigation Bar -->
<div class="navbar navbar-fixed-top">
    <div class="container">
        <div class="row">
            <div class="navbar-header">
                <a href="/" class="navbar-brand">
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
                    <button class="btn homepage-btn" data-toggle="modal"
                            data-target="#signupModal">
                        <i class="glyphicon glyphicon-user"
                           aria-hidden="true"></i>Signup
                    </button>
                </li>
                <li>
                    <button class="btn homepage-btn" data-toggle="modal"
                            data-target="#loginModal">
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

<!-- Login Modal -->
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog"
     aria-labelledby="loginModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="myLoginModalLabel">
                    Login to SnippeToDo Client
                </h4>
            </div>
            <!-- Modal Body -->
            <div class="modal-body">
                <a href="<%=fbConnection.getFBAuthUrl()%>">
                    <img id="fbLoginButton" src="./resources/img/facebookloginbutton.png"/>
                </a>
                <form id="login-form" role="form" method="post">
                    <div class="form-group">
                        <label for="loginInputEmail">Email address</label>
                        <div class="input-group">
                            <input type="email" class="form-control"
                                   id="loginInputEmail" name="loginInputEmail"
                                   placeholder="Enter email"/>
                        </div>
                        <label for="loginInputPassword">Password</label>
                        <div class="input-group">
                            <input type="password" class="form-control"
                                   id="loginInputPassword" name="loginInputPassword"
                                   placeholder="Password"/>
                        </div>
                    </div>
                </form>
            </div>
            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">
                    Close
                </button>
                <button id="loginButton" type="button" class="btn btn-primary"
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
<div class="modal fade" id="signupModal" tabindex="-1" role="dialog"
     aria-labelledby="signupModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="mySignupModalLabel">
                    Signup to use SnippeToDo
                </h4>
            </div>
            <!-- Modal Body -->
            <div class="modal-body">
                <form id="new-user-form" role="form" method="post">
                    <div class="form-group">
                        <label for="signupEmail">Email address</label>
                        <div class="input-group">
                            <input type="email" class="form-control"
                                   id="signupEmail" name="signupEmail" placeholder="Email"/>
                        </div>
                        <label for="signupFirstName">First name</label>
                        <div class="input-group">
                            <input class="form-control"
                                   id="signupFirstName" name="signupFirstName"
                                   placeholder="Enter your first name"/>
                        </div>
                        <label for="signupLastName">Last name</label>
                        <div class="input-group">
                            <input class="form-control"
                                   id="signupLastName" name="signupLastName"
                                   placeholder="LEnter your last name"/>
                        </div>
                        <label for="signupPassword">Password</label>
                        <div class="input-group">
                            <input class="form-control"
                                   id="signupPassword" name="signupPassword"
                                   placeholder="Enter your password"/>
                        </div>
                    </div>
                </form>
            </div>
            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">
                    Close
                </button>
                <button id="registerButton" type="button" class="btn btn-primary"
                        data-loading-text="<span class='glyphicon glyphicon-refresh
                        glyphicon-refresh-animate'></span> Processing">
                    Register me
                </button>
            </div>
        </div>
    </div>
</div>
<!-- /Signup Modal -->

<!-- Middle Section -->
<div class="container">
    <div class="row">
        <div id="content" class="col-xs-12 col-md-8 col-md-offset-2">
            <div class="well main-well">
                <h1 class="main-header">SNIPPEToDo</h1>
                <h4>A simple, organised, and visual way to be productive for developers.</h4>
                <hr>
                <button class="btn btn-default btn-lg" data-toggle="modal"
                        data-target="#signupModal">
                    Get Started!
                </button>
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

<script src="https://code.jquery.com/jquery-1.12.2.min.js"
        integrity="sha256-lZFHibXzMHo3GGeehn1hudTAP3Sc0uKXBXAzHX1sjtk="
        crossorigin="anonymous"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/index.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
