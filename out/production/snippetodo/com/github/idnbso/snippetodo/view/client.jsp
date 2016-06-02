<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnippeToDo Client</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/css/lib/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/client.css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,400italic,700'
          rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Press+Start+2P' rel='stylesheet'
          type='text/css'>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/lib/Sortable.min.js"></script>
</head>
<body>

<!-- Navigation Bar -->
<div class="navbar navbar-fixed-top">
    <div class="container">
        <div class="row">
            <div class="navbar-header">
                <a href="${pageContext.request.contextPath}/" class="navbar-brand">
                    SNIPPEToDo
                </a>

                <button type="button" class="navbar-toggle"
                        data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle lists navigation</span>
                    <i class="glyphicon glyphicon-option-vertical" aria-hidden="true"></i>
                </button>

                <button type="button" class="navbar-toggle visible-xs visible-sm hidden"
                        data-toggle="collapse" data-target=".navbar-links-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <i class="glyphicon glyphicon-option-horizontal" aria-hidden="true"></i>
                </button>
            </div>

            <ul class="nav navbar-nav navbar-right collapse navbar-links-collapse">
                <li><a class="snpptd-client-navbar-listlink" href='#snpptd-client-listtodo-heading'
                       data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">To Do</a></li>
                <li><a class="snpptd-client-navbar-listlink"
                       href='#snpptd-client-listtoday-heading'
                       data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Today</a></li>
                <li class="divider"></li>
                <li><a class="snpptd-client-navbar-listlink"
                       href='#snpptd-client-listdoing-heading'
                       data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Doing</a></li>
                <li><a class="snpptd-client-navbar-listlink"
                       href='#snpptd-client-listcheck-heading'
                       data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Check</a></li>
                <li class="divider"></li>
                <li><a class="snpptd-client-navbar-listlink" href='#snpptd-client-listdone-heading'
                       data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Done</a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right collapse navbar-collapse">
                <li>
                    <button id="snpptd-client-logout-button" class="btn"
                            data-loading-text="<span class='glyphicon glyphicon-refresh
                            glyphicon-refresh-animate'></span> Logging out
                            <i class='glyphicon glyphicon-log-out' aria-hidden='true'>">
                        Logout <i class="glyphicon glyphicon-log-out" aria-hidden="true"></i>
                    </button>
                </li>
                <li>
                    <button class="btn" data-target="#" data-toggle="dropdown">
                        About<span
                            class="caret"></span></button>
                    <ul class="dropdown-menu dropdown-about-menu">
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

<!-- Add new item modal -->
<div class="modal fade" id="snpptd-client-newitem-modal" tabindex="-1" role="dialog"
     aria-labelledby="snpptd-client-newitem-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Add a new todo item</h4>
            </div>
            <div class="modal-body">
                <form id="snpptd-client-newitem-form" role="form" method="post">
                    <div class="alert alert-warning hidden">
                        <a id="snpptd-client-newitem-alert" href="#" class="alert-link"></a>
                    </div>
                    <div class="form-group">
                        <label for="snpptd-client-item-title"></label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="snpptd-client-item-title"
                                   name="item-title" placeholder="Title of the snippet"/>
                        </div>
                        <label for="snpptd-client-item-body"></label>
                        <div class="input-group">
                            <textarea class="form-control" rows="5" id="snpptd-client-item-body"
                                      name="item-body"
                                      placeholder="Text body of the snippet"></textarea>
                        </div>
                    </div>
                </form>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close
                    </button>
                    <button id="snpptd-client-newitem-savebutton" type="button"
                            class="btn btn-primary"
                            data-loading-text="<span class='glyphicon glyphicon-refresh
                        glyphicon-refresh-animate'></span> Saving">Save
                    </button>
                </div>
            </div><!-- /.modal-body -->
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- Edit an existing item modal -->
<div class="modal fade" id="snpptd-client-edititem-modal" tabindex="-1" role="dialog"
     aria-labelledby="snpptd-client-edititem-modallabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit an existing item</h4>
            </div>
            <div class="modal-body">
                <form id="snpptd-client-edititem-form" role="form" method="post">
                    <div class="form-group">
                        <label for="snpptd-client-edititem-title">Your snippet text title</label>
                        <div class="input-group">
                            <input type="text" class="form-control"
                                   id="snpptd-client-edititem-title"
                                   name="snpptd-client-edititem-title"/>
                        </div>
                        <label for="snpptd-client-edititem-body">Your snippet text body</label>
                        <div class="input-group">
                            <textarea class="form-control" rows="5" id="snpptd-client-edititem-body"
                                      name="snpptd-client-edititem-body"></textarea>
                        </div>
                    </div>
                </form>
                <div class="modal-footer">
                    <button id="snpptd-client-edititem-cancelbutton" type="button"
                            class="btn btn-default"
                            data-dismiss="modal">Cancel
                    </button>
                    <button id="snpptd-client-edititem-savebutton" type="button"
                            class="btn btn-primary"
                            data-loading-text="<span class='glyphicon glyphicon-refresh
                        glyphicon-refresh-animate'></span> Saving">Save changes
                    </button>
                </div>
            </div><!-- /.modal-body -->
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- Board -->
<div class="container-fluid">
    <div class="row snpptd-client-board-lists">
        <!-- to-do list -->
        <div class="snpptd-client-board-list panel col-md-3 col-sm-6 col-xs-12">
            <div class="panel-heading"><span id="snpptd-client-listtodo-heading"></span>
                <h3 class="panel-title">todo
                    <button type="button" id="snpptd-list-todo-panelbtn"
                            class="snpptd-client-listtodo-panelbtn"
                            data-toggle="modal"
                            data-target="#snpptd-client-newitem-modal">
                    <span id="snpptd-client-newitembtn" aria-hidden="true" title="Add new item"
                          class="glyphicon glyphicon-plus-sign"></span></button>
                </h3>
            </div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="snpptd-client-listtodo" class="list-group">
                </ul>
            </div>
        </div>

        <!-- today list -->
        <div class="snpptd-client-board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="snpptd-client-listtoday-heading"></span>
                <h3 class="panel-title">today</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="snpptd-client-listtoday" class="list-group">

                </ul>
            </div>
        </div>

        <!-- doing list -->
        <div class="snpptd-client-board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="snpptd-client-listdoing-heading"></span>
                <h3 class="panel-title">doing</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="snpptd-client-listdoing" class="list-group">

                </ul>
            </div>
        </div>

        <!-- check list -->
        <div class="snpptd-client-board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="snpptd-client-listcheck-heading"></span>
                <h3 class="panel-title">check</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="snpptd-client-listcheck" class="list-group">

                </ul>
            </div>
        </div>

        <!-- done list -->
        <div class="snpptd-client-board-list panel col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="snpptd-client-listdone-heading"></span>
                <h3 class="panel-title">done</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="snpptd-client-listdone" class="list-group">

                </ul>
            </div>
        </div>
    </div>
</div>
<!-- /Board -->

<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/client.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/lib/bootstrap.min.js"></script>

</body>
</html>