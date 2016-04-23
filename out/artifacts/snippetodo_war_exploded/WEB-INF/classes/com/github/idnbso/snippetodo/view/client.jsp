<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnippeToDo Client</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/snippetodo.css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,400italic,700'
          rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Press+Start+2P' rel='stylesheet'
          type='text/css'>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/Sortable.min.js"></script>
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
                <li><a href='#'>Settings</a></li>
                <li><a href='#'>Help</a></li>
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

<!-- Add new item modal -->
<div class="modal fade" id="newItemModal" tabindex="-1" role="dialog"
     aria-labelledby="newItemModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Add new item</h4>
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
                    <button id="saveButton" type="button" class="btn btn-primary">Save changes
                    </button>
                </div>
            </div><!-- /.modal-body -->
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- Board -->
<div class="container-fluid">
    <div class="row board-lists">
        <!-- snippets list -->
        <div class="board-list board-list-todo panel col-md-3 col-sm-6 col-xs-12">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <button type="button" class="panel-list-todo-btn" data-toggle="modal"
                            data-target="#newItemModal">
                    <span aria-hidden="true"
                          class="glyphicon glyphicon-plus-sign"></span>
                    </button>todo
                </h3>
            </div>

            <!-- List group -->
            <div class="panel-body">
                <ul id="listTodo" class="list-group">
                    <!-- dummy must be set inorder for Sortable to add an item to an empty list -->
                    <li class="list-group-item-dummy" data-id="listTodoItem0"></li>
                    <li class="list-group-item" data-id="list-todo-1">
                        <div class="row nopadding">
                            <div class="col-xs-11 nopadding">
                                <span class="glyphicon glyphicon-move hover-btn"
                                      aria-hidden="true"></span>
                                <div class="list-group-item-text">
                                    Te3232stTest Te3232stTest Te3232stTestTest
                                    Te3232stTestTest Te3232stTest Te3232stTestTest
                                    Te3232stTestTest Te3232stTest
                                </div>
                            </div>
                            <div class="input-group-btn col-xs-1 nopadding">
                                <button type="button" class="btn hover-btn dropdown-toggle"
                                        data-toggle="dropdown" aria-haspopup="true"
                                        aria-expanded="false">
                                    <i aria-hidden="true"
                                       class="glyphicon glyphicon-console"></i>
                                    <span class="sr-only">Actions</span>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-right">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li role="separator" class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div><!-- /btn-group -->
                        </div>
                    </li>
                    <li class="list-group-item" data-id="list-todo-2">
                        <div class="row nopadding">
                            <div class="col-xs-11 nopadding">
                                <span class="glyphicon glyphicon-move hover-btn"
                                      aria-hidden="true"></span>
                                <div class="list-group-item-text">Test Test TestTest</div>
                            </div>
                            <div class="input-group-btn col-xs-1 nopadding">
                                <button type="button" class="btn hover-btn dropdown-toggle"
                                        data-toggle="dropdown" aria-haspopup="true"
                                        aria-expanded="false">
                                    <i aria-hidden="true"
                                       class="glyphicon glyphicon-console"></i>
                                    <span class="sr-only">Actions</span>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-right">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li role="separator" class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div><!-- /btn-group -->
                        </div>
                    </li>
                </ul>
            </div>
        </div>

        <!-- today list -->
        <div class="board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><h3 class="panel-title">today</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <div id="listToday" class="list-group">
                    <!-- dummy must be set inorder for Sortable to add an item to an empty list -->
                    <div class="list-group-item list-group-item-dummy"
                         data-id="list-today-0"></div>
                    <div class="list-group-item" data-id="list-today-1">
                        <div class="row nopadding">
                            <div class="col-xs-11 nopadding">
                                <span class="glyphicon glyphicon-move hover-btn"
                                      aria-hidden="true"></span>
                                <div class="list-group-item-text">Test Te3232stTest</div>
                            </div>
                            <div class="input-group-btn col-xs-1 nopadding">
                                <button type="button" class="btn hover-btn dropdown-toggle"
                                        data-toggle="dropdown" aria-haspopup="true"
                                        aria-expanded="false">
                                    <i aria-hidden="true"
                                       class="glyphicon glyphicon-console"></i>
                                    <span class="sr-only">Actions</span>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-right">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li role="separator" class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div><!-- /btn-group -->
                        </div>
                    </div>
                    <div class="list-group-item" data-id="list-today-2">
                        <div class="row nopadding">
                            <div class="col-xs-11 nopadding">
                                <span class="glyphicon glyphicon-move hover-btn"
                                      aria-hidden="true"></span>
                                <div class="list-group-item-text">Test Test TestTest</div>
                            </div>
                            <div class="input-group-btn col-xs-1 nopadding">
                                <button type="button" class="btn hover-btn dropdown-toggle"
                                        data-toggle="dropdown" aria-haspopup="true"
                                        aria-expanded="false">
                                    <i aria-hidden="true"
                                       class="glyphicon glyphicon-console"></i>
                                    <span class="sr-only">Actions</span>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-right">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li role="separator" class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div><!-- /btn-group -->
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- doing list -->
        <div class="board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><h3 class="panel-title">doing</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul class="list-group">
                    <li class="list-group-item">Cras justo odio</li>
                    <li class="list-group-item">Dapibus ac facilisis in</li>
                    <li class="list-group-item">Morbi leo risus orbi leo risusorbi leo risus
                    </li>
                    <li class="list-group-item">Porta ac consectetur ac</li>
                    <li class="list-group-item">Vestibulum at eros</li>
                </ul>
            </div>
        </div>

        <div class="board-list panel col-md-3 col-sm-6 col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><h3 class="panel-title">check</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul class="list-group">
                    <li class="list-group-item">Cras justo odio</li>
                    <li class="list-group-item">Dapibus ac facilisis in</li>
                    <li class="list-group-item">Morbi leo risus orbi leo risusorbi leo risus
                    </li>
                    <li class="list-group-item">Porta ac consectetur ac</li>
                    <li class="list-group-item">Vestibulum at eros</li>
                </ul>
            </div>
        </div>

        <div class="board-list panel col-xs-12">
            <!-- Default panel contents -->
            <div class="panel-heading"><h3 class="panel-title">done</h3></div>
            <!-- List group -->
            <div class="panel-body">
                <ul class="list-group">
                    <li class="list-group-item">Cras justo odio</li>
                    <li class="list-group-item">Dapibus ac facilisis in</li>
                    <li class="list-group-item">Morbi leo risus orbi leo risusorbi leo risus
                    </li>
                    <li class="list-group-item">Porta ac consectetur ac</li>
                    <li class="list-group-item">Vestibulum at eros</li>
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
        src="${pageContext.request.contextPath}/js/snippetodo.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</body>
</html>