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

    // Lists with handle
    var listTodo = Sortable.create(byId('listTodo'), {
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
                        /*
                         var list_new = $('#list_new').html();
                         $('#list').empty().append(list_new);
                         * */
                        var dummyItem =
                            '<li class="list-group-item-dummy" data-id="listTodoItem0"></li>';
                        $("#listTodo").empty().append(dummyItem);
                        $.each(responseJson, function(index, item) {
                            $("#listTodo").append(generateItemElement(item));
                            var order = sortable.toArray();
                            console.log(order.join('|'));
                            localStorage.setItem('listTodo', order.join('|'));
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
                var order = listTodo.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listTodo', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            console.log('onUpdate.listTodo:', evt.item);
            var order = listTodo.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = listTodo.toArray();
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

    var listToday = Sortable.create(byId('listToday'), {
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
                var order = listToday.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listToday', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = listToday.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = listToday.toArray();
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

    var listDoing = Sortable.create(byId('listDoing'), {
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
                var order = listDoing.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listDoing', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = listDoing.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = listDoing.toArray();
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

    var listCheck = Sortable.create(byId('listCheck'), {
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
                var order = listCheck.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listCheck', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = listCheck.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = listCheck.toArray();
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

    var listDone = Sortable.create(byId('listDone'), {
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
                var order = listDone.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('listDone', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            var order = listDone.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            var order = listDone.toArray();
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

    function updateList(order) {
        var orderSerialized = 'order=' + JSON.stringify(order);
        $.post("updatelist", orderSerialized, function(responseItem) {

        });
    }

    // create new item
    $(document).on("click", "#saveButton", function() {
        var $form = $('#new-item-form');
        var serializedForm = $form.serialize();

        // set the position index to be the last one according to the current order of listTodo
        var order = listTodo.toArray();
        serializedForm = serializedForm + '&positionIndex=' + order.length;

        $.post("newitem", serializedForm, function(responseJsonItem) {
            $("#listTodo").append(generateItemElement(responseJsonItem));
            order[responseJsonItem.positionIndex] = 'listItem' + responseJsonItem.id;
            console.log(order.join('|'));
            localStorage.setItem('listTodo', order.join('|'));
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // delete an item
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

    // edit an item
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

    // update an item (when an edit is done)
    $(document).on("click", "#editFormButton", function() {
        var $form = $('#edit-item-form');
        if (currentItemId !== null && currentItemElement !== null) {

            var serializedForm = $form.serialize() + '&id=' + currentItemId;

            $.post("updateitem", serializedForm, function(responseItem) {
                var $title = currentItemElement.find('.list-group-item-text');
                $title.html(responseItem.title);
                $('#editItemModal').modal('toggle');
            });
        }
        event.preventDefault();
    });

    // cancel an update of an item (when an edit is done)
    $(document).on("click", "#cancelEditButton", function() {
        currentItemId = null;
        currentItemElement = null;
        event.preventDefault();
    });

    // logout from client
    $(document).on("click", "#logoutButton", function() {

        window.open("http://localhost:8080/", "_self");
        //window.open("http://snippetodo.azurewebsites.net/", "_self");
        $.get("logout", function(responseJson) {

        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    function generateItemElement(newItem) {
        var item = '<li class="list-group-item" data-id="listItem' + newItem.id + '">' +
            '<div class="row nopadding">' +
            '<div class="col-xs-11 nopadding">' +
            '<span class="glyphicon glyphicon-move hover-btn"' +
            ' aria-hidden="true"></span>' +
            '<div class="list-group-item-text">' +
            newItem.title + // The title text to be injected
            '</div></div>' +
            '<div class="input-group-btn col-xs-1 nopadding">' +
            '<button type="button" class="btn hover-btn dropdown-toggle"' +
            ' data-toggle="dropdown" aria-haspopup="true"' +
            ' aria-expanded="false">' +
            '<i aria-hidden="true" class="glyphicon glyphicon-console"></i>' +
            '<span class="sr-only">Actions</span></button>' +
            '<ul class="dropdown-menu dropdown-menu-right multi-level" role="menu" aria-labelledby="dropdownMenu">' +
            '<li><a class="editButton" href="#">Edit</a></li>' +
            '<li><a class="progressButton" href="#">Progress</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="deleteButton" href="#">Delete</a></li>' +
            '</ul></div></div></li>';
        return item;
    }
})
();

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