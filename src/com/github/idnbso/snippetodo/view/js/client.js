(function() {
    'use strict';

    var byId = function(id) {
            return document.getElementById(id);
        },

        loadScripts = function(desc, callback) {
            var deps = [], key, idx = 0;

            for (key in desc) {
                deps.push(key);
            }

            (function _next() {
                var pid,
                    name = deps[idx],
                    script = document.createElement('script');

                script.type = 'text/javascript';
                script.src = desc[deps[idx]];

                pid = setInterval(function() {
                    if (window[name]) {
                        clearTimeout(pid);

                        deps[idx++] = window[name];

                        if (deps[idx]) {
                            _next();
                        } else {
                            callback.apply(null, deps);
                        }
                    }
                }, 30);

                document.getElementsByTagName('head')[0].appendChild(script);
            })()
        },

        console = window.console;

    if (!console.log) {
        console.log = function() {
            alert([].join.apply(arguments, ' '));
        };
    }

    var snippeToDoLists = {};

    // Lists with handle
    snippeToDoLists.listTodo = Sortable.create(byId('listTodo'), {
        group: 'board-lists',
        filter: '.list-group-item-dummy',
        handle: '.glyphicon-move',
        animation: 150,
        dataIdAttr: 'data-id',
        store: {
            /**
             * Get the order of elements. Called once during initialization.
             * @param   {Sortable}  sortable
             * @returns {Array}
             */
            get: function(sortable) {
                // get the current order from the localStorage
                var order = localStorage.getItem('listTodo');
                $.get("initlisttodo", function(responseJson) {
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="listTodoItem0"></li>';
                    $("#listTodo").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#listTodo").append(generateItemElement(item));
                    });
                    var order = sortable.toArray();
                    localStorage.setItem('listTodo', order.join('|'));
                });

                return order ? order.split('|') : [];
            },

            /**
             * Save the order of elements. Called by onEnd (when the item is dropped).
             * @param {Sortable}  sortable
             */
            set: function(sortable) {
                var order = sortable.toArray();
                console.log(order.join('|'));
                localStorage.setItem('listTodo', order.join('|'));
            }
        },

        // Element is dropped into the list from another list (does not call onEnd in this case)
        onAdd: function(evt) {
            console.log('onAdd.listTodo:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var itemId = evt.item.getAttribute('data-id').substring(8);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 1;

            $.post("updateitemposition", itemPositionSerialized, function(response) {
                var order = snippeToDoLists.listTodo.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listTodo', order.join('|'));
            });
        },

        // Changed sorting within list
        onUpdate: function(evt) {
            console.log('onUpdate.listTodo:', evt.item);
            var order = snippeToDoLists.listTodo.toArray();
            updateList(order);
        },

        // Element is removed from the list into another list
        onRemove: function(evt) {
            console.log('onRemove.listTodo:', evt.item);
            var order = snippeToDoLists.listTodo.toArray();
            updateList(order);
        },

        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.listTodo:', evt.item);
        },

        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.listTodo:', evt.item);
        }
    });

    snippeToDoLists.listToday = Sortable.create(byId('listToday'), {
        group: 'board-lists',
        filter: '.list-group-item-dummy',
        handle: '.glyphicon-move',
        animation: 150,
        dataIdAttr: 'data-id',
        store: {
            /**
             * Get the order of elements. Called once during initialization.
             * @param   {Sortable}  sortable
             * @returns {Array}
             */
            get: function(sortable) {
                // get the current order from the localStorage
                var order = localStorage.getItem('listToday');
                $.get("initlisttoday", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="listTodayItem0"></li>';
                    $("#listToday").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#listToday").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('listToday', order.join('|'));
                    });
                });

                // order = "item1|item2|..."
                //var order = // TODO: create the array according to the order from the DB
                //console.log(order);
                return order ? order.split('|') : [];
            },

            /**
             * Save the order of elements. Called onEnd (when the item is dropped).
             * @param {Sortable}  sortable
             */
            set: function(sortable) {
                var order = sortable.toArray();
                console.log(order.join('|'));
                localStorage.setItem('listToday', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.listToday:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var itemId = evt.item.getAttribute('data-id').substring(8);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 2;

            $.post("updateitemposition", itemPositionSerialized, function(response) {
                var order = snippeToDoLists.listToday.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listToday', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            console.log('onUpdate.listToday:', evt.item);
            var order = snippeToDoLists.listToday.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            console.log('onRemove.listToday:', evt.item);
            var order = snippeToDoLists.listToday.toArray();
            updateList(order);
        },
        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.listToday:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.listToday:', evt.item);
        }
    });

    snippeToDoLists.listDoing = Sortable.create(byId('listDoing'), {
        group: 'board-lists',
        filter: '.list-group-item-dummy',
        handle: '.glyphicon-move',
        animation: 150,
        dataIdAttr: 'data-id',
        store: {
            /**
             * Get the order of elements. Called once during initialization.
             * @param   {Sortable}  sortable
             * @returns {Array}
             */
            get: function(sortable) {
                // get the current order from the localStorage
                var order = localStorage.getItem('listDoing');

                $.get("initlistdoing", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="listDoingItem0"></li>';
                    $("#listDoing").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#listDoing").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('listDoing', order.join('|'));
                    });
                });

                // order = "item1|item2|..."
                //var order = // TODO: create the array according to the order from the DB
                //console.log(order);
                return order ? order.split('|') : [];
            },

            /**
             * Save the order of elements. Called onEnd (when the item is dropped).
             * @param {Sortable}  sortable
             */
            set: function(sortable) {
                var order = sortable.toArray();
                console.log(order.join('|'));
                localStorage.setItem('listDoing', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.listDoing:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var itemId = evt.item.getAttribute('data-id').substring(8);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 3;

            $.post("updateitemposition", itemPositionSerialized, function(response) {
                var order = snippeToDoLists.listDoing.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listDoing', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = snippeToDoLists.listDoing.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = snippeToDoLists.listDoing.toArray();
            updateList(order);
        },
        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.listDoing:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.listDoing:', evt.item);
        }
    });

    snippeToDoLists.listCheck = Sortable.create(byId('listCheck'), {
        group: 'board-lists',
        filter: '.list-group-item-dummy',
        handle: '.glyphicon-move',
        animation: 150,
        dataIdAttr: 'data-id',
        store: {
            /**
             * Get the order of elements. Called once during initialization.
             * @param   {Sortable}  sortable
             * @returns {Array}
             */
            get: function(sortable) {
                // get the current order from the localStorage
                var order = localStorage.getItem('listCheck');
                $.get("initlistcheck", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="listCheckItem0"></li>';
                    $("#listCheck").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#listCheck").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('listCheck', order.join('|'));
                    });
                });

                // order = "item1|item2|..."
                //var order = // TODO: create the array according to the order from the DB
                //console.log(order);
                return order ? order.split('|') : [];
            },

            /**
             * Save the order of elements. Called onEnd (when the item is dropped).
             * @param {Sortable}  sortable
             */
            set: function(sortable) {
                var order = sortable.toArray();
                console.log(order.join('|'));
                localStorage.setItem('listCheck', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.listCheck:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var itemId = evt.item.getAttribute('data-id').substring(8);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 4;

            $.post("updateitemposition", itemPositionSerialized, function(response) {
                var order = snippeToDoLists.listCheck.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listCheck', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = snippeToDoLists.listCheck.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = snippeToDoLists.listCheck.toArray();
            updateList(order);
        },
        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.listCheck:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.listCheck:', evt.item);
        }
    });

    snippeToDoLists.listDone = Sortable.create(byId('listDone'), {
        group: 'board-lists',
        filter: '.list-group-item-dummy',
        handle: '.glyphicon-move',
        animation: 150,
        dataIdAttr: 'data-id',
        store: {
            /**
             * Get the order of elements. Called once during initialization.
             * @param   {Sortable}  sortable
             * @returns {Array}
             */
            get: function(sortable) {
                // get the current order from the localStorage
                var order = localStorage.getItem('listDone');
                $.get("initlistdone", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="listDoneItem0"></li>';
                    $("#listDone").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#listDone").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('listDone', order.join('|'));
                    });
                });

                // order = "item1|item2|..."
                //var order = // TODO: create the array according to the order from the DB
                //console.log(order);
                return order ? order.split('|') : [];
            },

            /**
             * Save the order of elements. Called onEnd (when the item is dropped).
             * @param {Sortable}  sortable
             */
            set: function(sortable) {
                var order = sortable.toArray();
                console.log(order.join('|'));
                localStorage.setItem('listDone', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.listDone:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var itemId = evt.item.getAttribute('data-id').substring(8);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 5;

            $.post("updateitemposition", itemPositionSerialized, function(response) {
                var order = snippeToDoLists.listDone.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listDone', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = snippeToDoLists.listDone.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = snippeToDoLists.listDone.toArray();
            updateList(order);
        },
        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.listDone:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.listDone:', evt.item);
        }
    });

    // Create new item
    $(document).on("click", "#saveButton", function() {
        var $form = $('#new-item-form');
        var serializedForm = $form.serialize();
        var $this = $(this);
        $this.button('loading');
        // set the position index to be the last one according to the current order of listTodo
        var order = snippeToDoLists.listTodo.toArray();
        serializedForm = serializedForm + '&positionIndex=' + order.length;

        $.post("newitem", serializedForm, function(responseJsonItem) {
            $this.button('reset');
            $("#listTodo").append(generateItemElement(responseJsonItem));
            order[responseJsonItem.positionIndex] = 'listItem' + responseJsonItem.id;
            console.log(order.join('|'));
            localStorage.setItem('listTodo', order.join('|'));
            $('#newItemModal').modal('toggle');
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // Delete an item
    $(document).on("click", ".deleteButton", function() {
        var $item = $(this).closest('.list-group-item');
        var itemId = 'id=' + $item.attr("data-id").substring(8);

        $.post("deleteitem", itemId, function(responseJsonItem) {
            $item.remove();
        });

        event.preventDefault();
    });

    var currentItemId = null;
    var currentItemElement = null;

    /*
     * Edit an item
     * NOTE: the button is a class since it is in every dropdown of every item on the page
     */
    $(document).on("click", ".editButton", function() {
        var $item = $(this).closest('.list-group-item');
        currentItemElement = $item;
        currentItemId = $item.attr("data-id").substring(8);
        var idSerialized = 'id=' + currentItemId;

        // TODO: loading here

        $.post("getitem", idSerialized, function(responseItem) {
            var $modal = $('#editItemModal');
            var $title = $modal.find('#edit-item-title');
            $title.val(responseItem.title);
            var $body = $modal.find('#edit-item-body');
            $body.val(responseItem.body);
            $modal.modal('toggle');
        });

        event.preventDefault();
    });

    // TODO: refactor this method with atleast two new helper functions
    // Move an item to a new list with the actions dropdown menu
    $(document).on("click", ".moveToButton", function() {
        var $item = $(this).closest('.list-group-item');
        var $currentList = $item.closest('.list-group');
        currentItemElement = $item;

        // get all classes inorder to get the list name
        var thisElement = $(this)[0];
        var classList = thisElement.className.split(/\s+/);
        var newListName;
        for (var curClassName = 0; curClassName < classList.length; curClassName++) {
            var className = classList[curClassName];
            if (className.indexOf("moveToList") > -1) {
                newListName = className.substring(10); // get the list name after moveToList|...
                newListName = "list" + newListName;
                break;
            }
        }

        // get the new position of the item in the new list
        var newListObj = snippeToDoLists[newListName];
        var order = newListObj.toArray();
        var itemNewIndex = order.length;

        // NOTE: the append operation also removes the item from the previous list
        $('#' + newListName).append($item);
        // call the Sortable methods of the new list to add
        // the item to the list both in client and to the database
        newListObj['options'].onAdd({
            item: $item[0], // saves the html element itself from the selector
            newIndex: itemNewIndex
        });

        // update the old list of the item
        var oldListName = $currentList[0]['id'];
        var oldList = snippeToDoLists[oldListName]['options']
        oldList.onRemove({item: $item[0]});
        oldList['store'].set(snippeToDoLists[oldListName]);

        event.preventDefault();
    });

    // Update an item (when an edit is done)
    $(document).on("click", "#editFormButton", function() {
        var $form = $('#edit-item-form');
        var $this = $(this);
        $this.button('loading');

        if (currentItemId !== null && currentItemElement !== null) {

            var serializedForm = $form.serialize() + '&id=' + currentItemId;

            $.post("updateitem", serializedForm, function(responseItem) {
                $this.button('reset');
                var $title = currentItemElement.find('.list-group-item-text');
                $title.html(responseItem.title);
                $('#editItemModal').modal('toggle');
            });
        }
        event.preventDefault();
    });

    // Cancel an update of an item (when an edit is done)
    $(document).on("click", "#cancelEditButton", function() {
        currentItemId = null;
        currentItemElement = null;
        event.preventDefault();
    });

    // Logout from client
    $(document).on("click", "#logoutButton", function() {
        var $this = $(this);
        $this.button('loading');

        $.get("logout", function(responseJson) {
            window.open("http://localhost:8080/", "_self");
            //window.open("http://snippetodo.azurewebsites.net/", "_self");
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // update the list in the database according to its current order in the Local Storage.
    function updateList(order) {
        var orderSerialized = 'order=' + JSON.stringify(order);
        $.post("updatelist", orderSerialized, function(responseItem) {

        });
    }

    function generateItemElement(newItem) {
        return '<li class="list-group-item" data-id="listItem' +
            newItem.id + // the id of the item in database
            '"><div class="row nopadding">' +
            '<div class="col-xs-11 nopadding">' +
            '<span class="glyphicon glyphicon-move hover-btn"' +
            ' aria-hidden="true"></span>' +
            '<div class="list-group-item-text">' +
            newItem.title + // The title text of the item from the database
            '</div></div>' +
            '<div class="input-group-btn col-xs-1 nopadding">' +
            '<button type="button" class="btn hover-btn dropdown-toggle"' +
            ' data-toggle="dropdown" aria-haspopup="true"' +
            ' aria-expanded="false">' +
            '<i aria-hidden="true" class="glyphicon glyphicon-console"></i>' +
            '<span class="sr-only">Actions</span></button>' +
            '<ul class="dropdown-menu dropdown-menu-right multi-level" role="menu" ' +
            'id="dropdown-actions" aria-labelledby="dropdownMenu">' +
            '<li><a class="editButton" href="#">Edit</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="moveToButton moveToListTodo" href="#">To Do</a></li>' +
            '<li><a class="moveToButton moveToListToday" href="#">Do Today</a></li>' +
            '<li><a class="moveToButton moveToListDoing" href="#">Do Now</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="moveToButton moveToListCheck" href="#">Check</a></li>' +
            '<li><a class="moveToButton moveToListDone" href="#">Finish</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="deleteButton" href="#">Delete</a></li>' +
            '</ul></div></div></li>';
    }

    // fixes Bootstrap 3.x bug of navbar when modal is open
    $(document).ready(function() {
        $(window).load(function() {
            var oldSSB = $.fn.modal.Constructor.prototype.setScrollbar;
            $.fn.modal.Constructor.prototype.setScrollbar = function() {
                oldSSB.apply(this);
                if (this.bodyIsOverflowing && this.scrollbarWidth) {
                    $('.navbar-fixed-top, .navbar-fixed-bottom')
                        .css('padding-right', this.scrollbarWidth);
                }
            };

            var oldRSB = $.fn.modal.Constructor.prototype.resetScrollbar;
            $.fn.modal.Constructor.prototype.resetScrollbar = function() {
                oldRSB.apply(this);
                $('.navbar-fixed-top, .navbar-fixed-bottom').css('padding-right', '');
            }
        });
    });
})();

