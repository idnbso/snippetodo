<%@ page pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnippeToDo Client</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/client.css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,400italic,700'
          rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Press+Start+2P' rel='stylesheet'
          type='text/css'>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/Sortable.min.js"></script>
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
                    <span class="sr-only">Toggle navigation</span>
                    <i class="glyphicon glyphicon-option-vertical" aria-hidden="true"></i>
                </button>

                <button type="button" class="navbar-toggle visible-xs visible-sm hidden"
                        data-toggle="collapse" data-target=".navbar-links-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <i class="glyphicon glyphicon-option-horizontal" aria-hidden="true"></i>
                </button>
            </div>

            <ul class="nav navbar-nav navbar-right collapse navbar-links-collapse">
                <li><a class="listLink" href='#listTodoHeading' data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">To Do</a></li>
                <li><a class="listLink" href='#listTodayHeading' data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Today</a></li>
                <li class="divider"></li>
                <li><a class="listLink" href='#listDoingHeading' data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Doing</a></li>
                <li><a class="listLink" href='#listCheckHeading' data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Check</a></li>
                <li class="divider"></li>
                <li><a class="listLink" href='#listDoneHeading' data-toggle="collapse"
                       data-target=".navbar-links-collapse.in">Done</a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right collapse navbar-collapse">
                <li>
                    <button id="logoutButton" class="btn homepage-btn"
                            data-loading-text="<span class='glyphicon glyphicon-refresh
                            glyphicon-refresh-animate'></span> Logging out
                            <i class='glyphicon glyphicon-log-out' aria-hidden='true'>">
                        Logout <i class="glyphicon glyphicon-log-out" aria-hidden="true"></i>
                    </button>
                </li>
                <li>
                    <button class="btn homepage-btn" data-target="#" data-toggle="dropdown">
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
<div class="modal fade" id="newItemModal" tabindex="-1" role="dialog"
     aria-labelledby="newItemModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Add a new todo item</h4>
            </div>
            <div class="modal-body">
                <form id="new-item-form" role="form" method="post">
                    <div class="form-group">
                        <label for="item-title">Your snippet text title</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="item-title"
                                   name="item-title"/>
                        </div>
                        <label for="item-body">Your snippet text body</label>
                        <div class="input-group">
                            <textarea class="form-control" rows="5" id="item-body"
                                      name="item-body"></textarea>
                        </div>
                    </div>
                </form>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close
                    </button>
                    <button id="saveButton" type="button" class="btn btn-primary"
                            data-loading-text="<span class='glyphicon glyphicon-refresh
                        glyphicon-refresh-animate'></span> Saving">Save
                    </button>
                </div>
            </div><!-- /.modal-body -->
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- Edit an existing item modal -->
<div class="modal fade" id="editItemModal" tabindex="-1" role="dialog"
     aria-labelledby="editItemModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Edit an existing item</h4>
            </div>
            <div class="modal-body">
                <form id="edit-item-form" role="form" method="post">
                    <div class="form-group">
                        <label for="edit-item-title">Your snippet text title</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="edit-item-title"
                                   name="edit-item-title"/>
                        </div>
                        <label for="edit-item-body">Your snippet text body</label>
                        <div class="input-group">
                            <textarea class="form-control" rows="5" id="edit-item-body"
                                      name="edit-item-body"></textarea>
                        </div>
                    </div>
                </form>
                <div class="modal-footer">
                    <button id="cancelEditButton" type="button" class="btn btn-default"
                            data-dismiss="modal">Cancel
                    </button>
                    <button id="editFormButton" type="button" class="btn btn-primary"
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
    <div class="row board-lists">
        <!-- to-do list -->
        <div class="board-list board-list-todo panel col-md-3 col-sm-6 col-xs-12">
            <div class="panel-heading"><span id="listTodoHeading"></span>
                <h3 class="panel-title">todo
                    <button type="button" class="panel-list-todo-btn" data-toggle="modal"
                            data-target="#newItemModal">
                    <span id="addButton" aria-hidden="true" title="Add new item"
                          class="glyphicon glyphicon-plus-sign"></span></button>
                </h3>
            </div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="listTodo" class="list-group">

                </ul>
            </div>
        </div>

        <!-- today list -->
        <div class="board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="listTodayHeading"></span>
                <h3 class="panel-title">today</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="listToday" class="list-group">

                </ul>
            </div>
        </div>

        <!-- doing list -->
        <div class="board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="listDoingHeading"></span>
                <h3 class="panel-title">doing</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="listDoing" class="list-group">

                </ul>
            </div>
        </div>

        <!-- check list -->
        <div class="board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="listCheckHeading"></span>
                <h3 class="panel-title">check</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="listCheck" class="list-group">

                </ul>
            </div>
        </div>

        <!-- done list -->
        <div class="board-list panel col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><span id="listDoneHeading"></span>
                <h3 class="panel-title">done</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul id="listDone" class="list-group">

                </ul>
            </div>
        </div>
    </div>
</div>
<!-- /Board -->

<script src="https://code.jquery.com/jquery-1.12.2.min.js"
        integrity="sha256-lZFHibXzMHo3GGeehn1hudTAP3Sc0uKXBXAzHX1sjtk="
        crossorigin="anonymous"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/client.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>

</body>
</html>