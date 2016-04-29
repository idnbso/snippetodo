<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnippeToDo Homepage</title>
    <link rel="stylesheet" type="text/css" media="all"
          href="${pageContext.request.contextPath}/css/bootstrap.css">
    <link rel="stylesheet" media="all" href="${pageContext.request.contextPath}/css/snippetodo.css">
    <link rel="stylesheet" media="all" href="${pageContext.request.contextPath}/css/homepage.css">
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
                <li><a href='#'><i class="glyphicon glyphicon-user"
                                   aria-hidden="true"></i>Signup</a></li>
                <li><a href='#'><i class="glyphicon glyphicon-log-in"
                                   aria-hidden="true"></i>Login</a></li>
                <li><a href='about.html' data-raget="#" data-toggle="dropdown">About<span
                        class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href='story.html'>Our Story</a></li>
                        <li><a href='contact.html'>Contact Us</a></li>
                        <li class="divider"></li>
                        <li><a href='blog.html'>Blog</a></li>
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

<!-- Middle Section -->
<div class="container">
    <div class="row">
        <div id="content" class="col-xs-12 col-md-8 col-md-offset-2">
            <div class="well main-well">
                <h1 class="main-header">SNIPPEToDo</h1>
                <h4>simple, organised, and visual way to be productive for developers.</h4>
                <hr>
                <button class="btn btn-default btn-lg">
                    <i class="glyphicon glyphicon-flash"
                       aria-hidden="true"></i>Get Started!
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
        src="${pageContext.request.contextPath}/js/snippetodo.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
