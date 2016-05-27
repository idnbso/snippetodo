(function() {
    'use strict';
    removeHash();

    var byId = function(id) {
        return document.getElementById(id);
    };

    var snippeToDoLists = {};

    // Lists with handle
    snippeToDoLists.listTodo = Sortable.create(byId('snpptd-client-listtodo'), {
        group: 'snpptd-client-board-lists',
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
                var order = localStorage.getItem('snpptd-client-listtodo');
                $.get("initlisttodo", function(responseJson) {
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="snpptd-client-listtodo-item0"></li>';
                    $("#snpptd-client-listtodo").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#snpptd-client-listtodo").append(generateItemElement(item));
                    });
                    var order = sortable.toArray();
                    localStorage.setItem('snpptd-client-listtodo', order.join('|'));
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
                localStorage.setItem('snpptd-client-listtodo', order.join('|'));
            }
        },

        // Element is dropped into the list from another list (does not call onEnd in this case)
        onAdd: function(evt) {
            console.log('onAdd.snpptd-client-listtodo:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = evt.item.getAttribute('data-id').substring(elementDataIdName.length);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 1;

            $.post("updateitemposition", itemPositionSerialized, function() {
                var order = snippeToDoLists.listTodo.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('snpptd-client-listtodo', order.join('|'));
            });
        },

        // Changed sorting within list
        onUpdate: function(evt) {
            console.log('onUpdate.snpptd-client-listtodo:', evt.item);
            var order = snippeToDoLists.listTodo.toArray();
            updateList(order);
        },

        // Element is removed from the list into another list
        onRemove: function(evt) {
            console.log('onRemove.snpptd-client-listtodo:', evt.item);
            var order = snippeToDoLists.listTodo.toArray();
            updateList(order);
        },

        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.snpptd-client-listtodo:', evt.item);
        },

        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.snpptd-client-listtodo:', evt.item);
        }
    });

    snippeToDoLists.listToday = Sortable.create(byId('snpptd-client-listtoday'), {
        group: 'snpptd-client-board-lists',
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
                var order = localStorage.getItem('snpptd-client-listtoday');
                $.get("initlisttoday", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="snpptd-client-listtoday-item0"></li>';
                    $("#snpptd-client-listtoday").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#snpptd-client-listtoday").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('snpptd-client-listtoday', order.join('|'));
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
                localStorage.setItem('snpptd-client-listtoday', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.snpptd-client-listtoday:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = evt.item.getAttribute('data-id').substring(elementDataIdName.length);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 2;

            $.post("updateitemposition", itemPositionSerialized, function() {
                var order = snippeToDoLists.listToday.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('snpptd-client-listtoday', order.join('|'));
            });
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            console.log('onUpdate.snpptd-client-listtoday:', evt.item);
            var order = snippeToDoLists.listToday.toArray();
            updateList(order);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            console.log('onRemove.snpptd-client-listtoday:', evt.item);
            var order = snippeToDoLists.listToday.toArray();
            updateList(order);
        },
        // Element dragging started
        onStart: function(evt) {
            console.log('onStart.snpptd-client-listtoday:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.snpptd-client-listtoday:', evt.item);
        }
    });

    snippeToDoLists.listDoing = Sortable.create(byId('snpptd-client-listdoing'), {
        group: 'snpptd-client-board-lists',
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
                var order = localStorage.getItem('snpptd-client-listdoing');

                $.get("initlistdoing", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="snpptd-client-listdoing-item0"></li>';
                    $("#snpptd-client-listdoing").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#snpptd-client-listdoing").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('snpptd-client-listdoing', order.join('|'));
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
                localStorage.setItem('snpptd-client-listdoing', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.snpptd-client-listdoing:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = evt.item.getAttribute('data-id').substring(elementDataIdName.length);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 3;

            $.post("updateitemposition", itemPositionSerialized, function() {
                var order = snippeToDoLists.listDoing.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('snpptd-client-listdoing', order.join('|'));
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
            console.log('onStart.snpptd-client-listdoing:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.snpptd-client-listdoing:', evt.item);
        }
    });

    snippeToDoLists.listCheck = Sortable.create(byId('snpptd-client-listcheck'), {
        group: 'snpptd-client-board-lists',
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
                var order = localStorage.getItem('snpptd-client-listcheck');
                $.get("initlistcheck", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="snpptd-client-listcheck-item0"></li>';
                    $("#snpptd-client-listcheck").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#snpptd-client-listcheck").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('snpptd-client-listcheck', order.join('|'));
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
                localStorage.setItem('snpptd-client-listcheck', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.snpptd-client-listcheck:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = evt.item.getAttribute('data-id').substring(elementDataIdName.length);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 4;

            $.post("updateitemposition", itemPositionSerialized, function() {
                var order = snippeToDoLists.listCheck.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('snpptd-client-listcheck', order.join('|'));
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
            console.log('onStart.snpptd-client-listcheck:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.snpptd-client-listcheck:', evt.item);
        }
    });

    snippeToDoLists.listDone = Sortable.create(byId('snpptd-client-listdone'), {
        group: 'snpptd-client-board-lists',
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
                var order = localStorage.getItem('snpptd-client-listdone');
                $.get("initlistdone", function(responseJson) {
                    /*
                     var list_new = $('#list_new').html();
                     $('#list').empty().append(list_new);
                     * */
                    var dummyItem =
                        '<li class="list-group-item-dummy" data-id="snpptd-client-listdone-item0"></li>';
                    $("#snpptd-client-listdone").empty().append(dummyItem);
                    $.each(responseJson, function(index, item) {
                        $("#snpptd-client-listdone").append(generateItemElement(item));
                        var order = sortable.toArray();
                        console.log(order.join('|'));
                        localStorage.setItem('snpptd-client-listdone', order.join('|'));
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
                localStorage.setItem('snpptd-client-listdone', order.join('|'));
            }
        },
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.snpptd-client-listdone:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = evt.item.getAttribute('data-id').substring(elementDataIdName.length);
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + 5;

            $.post("updateitemposition", itemPositionSerialized, function() {
                var order = snippeToDoLists.listDone.toArray();
                updateList(order);
                console.log(order.join('|'));
                localStorage.setItem('snpptd-client-listdone', order.join('|'));
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
            console.log('onStart.snpptd-client-listdone:', evt.item);
        },
        // Element dragging ended
        onEnd: function(evt) {
            console.log('onEnd.snpptd-client-listdone:', evt.item);
        }
    });

    // Create new item
    $(document).on("click", "#snpptd-client-newitem-savebutton", function() {
        var $form = $('#snpptd-client-newitem-form');
        var serializedForm = $form.serialize();
        var $this = $(this);
        $this.button('loading');
        // set the position index to be the last one according to the current order of listTodo
        var order = snippeToDoLists.listTodo.toArray();
        serializedForm = serializedForm + '&positionIndex=' + order.length;

        $.post("newitem", serializedForm, function(responseJsonItem) {
            $this.button('reset');
            $("#snpptd-client-listtodo").append(generateItemElement(responseJsonItem));
            order[responseJsonItem.positionIndex] = 'snpptd-client-list-item' + responseJsonItem.id;
            console.log(order.join('|'));
            localStorage.setItem('snpptd-client-listtodo', order.join('|'));
            $('#snpptd-client-newitem-modal').modal('toggle');
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // Delete an item
    $(document).on("click", ".snpptd-client-list-item-deletebtn", function() {
        var $item = $(this).closest('.list-group-item');
        var elementDataIdName = "snpptd-client-list-item";
        var itemId = 'id=' + $item.attr("data-id").substring(elementDataIdName.length);

        $.post("deleteitem", itemId, function() {
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
    $(document).on("click", ".snpptd-client-list-item-editbtn", function() {
        var $item = $(this).closest('.list-group-item');
        currentItemElement = $item;
        var elementDataIdName = "snpptd-client-list-item";
        currentItemId = $item.attr("data-id").substring(elementDataIdName.length);
        var idSerialized = 'id=' + currentItemId;

        // TODO: loading here

        $.post("getitem", idSerialized, function(responseItem) {
            var $modal = $('#snpptd-client-edititem-modal');
            var $title = $modal.find('#snpptd-client-edititem-title');
            $title.val(responseItem.title);
            var $body = $modal.find('#snpptd-client-edititem-body');
            $body.val(responseItem.body);
            $modal.modal('toggle');
        });

        event.preventDefault();
    });

    // TODO: refactor this method with atleast two new helper functions
    // Move an item to a new list with the actions dropdown menu
    $(document).on("click", ".snpptd-client-list-item-movetobtn", function() {
        var $item = $(this).closest('.list-group-item');
        var $currentList = $item.closest('.list-group');
        currentItemElement = $item;

        // get all classes inorder to get the list name
        var thisElement = $(this)[0];
        var classList = thisElement.className.split(/\s+/);
        var newListName;
        for (var curClassName = 0; curClassName < classList.length; curClassName++) {
            var className = classList[curClassName];
            var elementClassName = "snpptd-client-list-item-movetolist";
            if (className.indexOf(elementClassName) > -1) {
                // get the specific list name after elementClassName|...
                newListName = className.substring(elementClassName.length);
                newListName = "snpptd-client-list-" + newListName;
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
    $(document).on("click", "#snpptd-client-edititem-savebutton", function() {
        var $form = $('#snpptd-client-edititem-form');
        var $this = $(this);
        $this.button('loading');

        if (currentItemId !== null && currentItemElement !== null) {

            var serializedForm = $form.serialize() + '&id=' + currentItemId;

            $.post("updateitem", serializedForm, function(responseItem) {
                $this.button('reset');
                var $title = currentItemElement.find('.list-group-item-text');
                $title.html(responseItem.title);
                $('#snpptd-client-edititem-modal').modal('toggle');
            });
        }
        event.preventDefault();
    });

    // Cancel an update of an item (when an edit is done)
    $(document).on("click", "#snpptd-client-edititem-cancelbutton", function() {
        currentItemId = null;
        currentItemElement = null;
        event.preventDefault();
    });

    // Logout from client
    $(document).on("click", "#snpptd-client-logout-button", function() {
        var $this = $(this);
        $this.button('loading');

        $.get("logout", function() {
            window.open("http://localhost:8080/", "_self");
            // window.open("http://snippetodo.azurewebsites.net/", "_self");
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    // update the list in the database according to its current order in the Local Storage.
    function updateList(order) {
        var orderSerialized = 'order=' + JSON.stringify(order);
        $.post("updatelist", orderSerialized, function() {
        });
    }

    function generateItemElement(newItem) {
        return '<li class="list-group-item" data-id="snpptd-client-list-item' +
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
            '<li><a class="snpptd-client-list-item-editbtn" href="#">Edit</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="snpptd-client-list-item-movetobtn ' +
            'snpptd-client-list-item-movetolisttodo" ' +
            'href="#snpptd-client-listtodo">To Do</a></li>' +
            '<li><a class="snpptd-client-list-item-movetobtn ' +
            'snpptd-client-list-item-movetolisttoday" ' +
            'href="#snpptd-client-listtoday">Do Today</a></li>' +
            '<li><a class="snpptd-client-list-item-movetobtn ' +
            'snpptd-client-list-item-movetolistdoing" ' +
            'href="#snpptd-client-listdoing">Do Now</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="snpptd-client-list-item-movetobtn ' +
            'snpptd-client-list-item-movetolistcheck" ' +
            'href="#snpptd-client-listcheck">Check</a></li>' +
            '<li><a class="snpptd-client-list-item-movetobtn ' +
            'snpptd-client-list-item-movetolistdone" ' +
            'href="#snpptd-client-listdone">Finish</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a class="snpptd-client-list-item-deletebtn" href="#">Delete</a></li>' +
            '</ul></div></div></li>';
    }

    function removeHash() {
        history.pushState("", document.title, window.location.pathname
            + window.location.search);
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

