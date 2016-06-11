(function() {
    'use strict';

    /**
     * The SnippeToDo application namespace.
     */
    var snippeToDoClient = {};

    snippeToDoClient.SnippeToDoException = function(message) {
        this.message = message;
        this.name = "SnippeToDoException";
    };

    /**
     * List of all of the to do lists names.
     *
     * @type {string[]}
     */
    snippeToDoClient.listNames = ["todo", "today", "doing", "check", "done"];

    /**
     * Stores all of the initialized lists Sortable objects.
     *
     * @type {Array}
     */
    snippeToDoClient.sortableLists = [];

    /**
     * The Sortable library's store object to be used as the options object for Sortable
     * initialization.
     *
     * @param listElementId THe HTML element id of the list
     * @param index         The index in of the list in the sortableList array
     * @see Sortable
     * @see sortableLists
     */
    snippeToDoClient.sortableStore = function(listElementId, index) {
        this.listElementId = listElementId;
        this.index = index;
    };

    /**
     * Get the order of elements in a Sortable list. Called once during initialization.
     *
     * @param   {Sortable}  sortable
     * @returns {Array}
     */
    snippeToDoClient.sortableStore.prototype.get = function(sortable) {
        // get the current order from the localStorage
        var that = this;
        var snippeToDoRef = snippeToDoClient; // reference to the namespace for ajax operation
        var order = localStorage.getItem(that.listElementId); // get the current saved list's data

        try {
            var listName = snippeToDoClient.listNames[that.index]; // index as list id
            var request = "listName=" + listName[0].toUpperCase() + listName.substring(1);

            // get the current list's data from the database and initialize it in html code
            $.post("initlist", request, function(responseJson) {
                if (responseJson === undefined || responseJson.error !== undefined) {
                    snippeToDoUtil.displayError(
                        "Could not recieve the data to initialize a list." +
                        responseJson.error.message);
                }
                else {
                    var dummyItem = // dummy item element to enable item drag and drop on an empty list
                        '<li class="list-group-item-dummy" data-id="' + that.listElementId +
                        '-item0"></li>';
                    var $listSelector = $('#' + that.listElementId);
                    $listSelector.empty().append(dummyItem);

                    // iterate over each item in the list and generate its html code
                    $.each(responseJson, function(index, item) {
                        $listSelector.append(snippeToDoRef.generateItemElement(item));
                    });

                    // get the updated order and save it to the local storage
                    order = sortable.toArray();
                    snippeToDoClient.sortableStore.prototype.saveListOrder(that.listElementId,
                        order);
                }
            });
        }
        catch (e) {
            snippeToDoUtil.displayError(
                "Encountered an error while initializing the order of a list. " + e.message);
        }

        // update Sortable with the latest order
        return order ? order.split('|') : [];
    };

    /**
     * Save the order of elements in a Sortable list. Called onEnd (when the item is dropped).
     *
     * @param {Sortable}  sortable
     */
    snippeToDoClient.sortableStore.prototype.set = function(sortable) {
        var order = sortable.toArray();
        snippeToDoClient.sortableStore.prototype.saveListOrder(this.listElementId, order);
    };

    /**
     * Save the current list order in the local storage.
     *
     * @param listElementId the current list name
     * @param order the order as an array from a Sortable list object
     */
    snippeToDoClient.sortableStore.prototype.saveListOrder = function(listElementId, order) {
        if (order !== undefined) {
            localStorage.setItem(listElementId, order.join('|'));
        }
        else {
            snippeToDoUtil.displayError(
                "There was a problem initializing with a Sortable list.");
        }
    };

    /**
     * The Sortable list initialization options object.
     *
     * @param listElementId the current list name
     * @param index the index of the list in the array of listNames
     */
    snippeToDoClient.sortableOptions = function(listElementId, index) {
        this.listElementId = listElementId;
        this.group = 'snpptd-client-board-lists';
        this.filter = '.list-group-item-dummy';
        this.handle = '.glyphicon-move';
        this.animation = 150;
        this.dataIdAttr = 'data-id';
        this.store = new snippeToDoClient.sortableStore(listElementId, index);

        /**
         * Element is dropped into the list from another list (does not call onEnd in this case)
         * @param evt the event with the information of the item position change
         */
        this.onAdd = function(evt) {
            try {
                var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
                var elementDataIdName = "snpptd-client-list-item";
                var itemId = evt.item.getAttribute('data-id').substring(elementDataIdName.length);
                var listId = index + 1;
                var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                    "&listId=" + listId;

                // update the item's position value in the database
                $.post("/client/item/updateposition", itemPositionSerialized,
                    function(responseJson) {
                        var errorObj = responseJson.error;
                        if (errorObj !== undefined) {
                            snippeToDoUtil.displayError(errorObj.message);
                        }
                        else {
                            var sortableList = snippeToDoClient.sortableLists[index];
                            var order = sortableList.toArray();
                            if (order === undefined) {
                                snippeToDoUtil.displayError(
                                    "There was a problem updating a Sortable list.");
                            }

                            snippeToDoClient.updateList(order);
                            localStorage.setItem(listElementId, order.join('|'));
                        }
                    });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while moving an item between the lists. " + e.message);
            }
        };

        /**
         * Called by Sortable whenever there is a change in sorting within a list.
         * Prepares an update of the list in the database,
         * from the corresponded list saved in Sortable.
         *
         * @see Sortable
         */
        this.onUpdate = function() {
            var sortableList = snippeToDoClient.sortableLists[index];
            var order = sortableList.toArray();
            snippeToDoClient.updateList(order);
        };

        /**
         * Called by Sortable whenever an element is removed.
         * from the list into another list.
         * @type {snippeToDoClient.onUpdate|*}
         */
        this.onRemove = this.onUpdate;
    };

    /**
     * Create all of the lists defined in the listNames array.
     *
     * @see listNames
     */
    (snippeToDoClient.createLists = function() {
        try {
            $.each(snippeToDoClient.listNames, function(index, value) {
                var listElementId = 'snpptd-client-list' + value;
                var listElement = document.getElementById(listElementId);

                // initialize a new Sortable list object.
                var sortableList = Sortable.create(listElement,
                    new snippeToDoClient.sortableOptions(listElementId, index));

                // add to the list of all Sortable list object.
                snippeToDoClient.sortableLists.push(sortableList);
            });
        }
        catch (e) {
            snippeToDoUtil.displayError("Encountered an error while trying to create the lists. " +
                e.message);
        }
    })();

    /**
     * Update the list in the database according to its current order in the Local Storage.
     *
     * @param order the current order of the list to be updated in the database
     */
    snippeToDoClient.updateList = function(order) {
        try {
            var orderSerialized = 'order=' + JSON.stringify(order);

            // update the list in the database
            $.post("/client/item/updatelist", orderSerialized, function(responseJson) {
                var errorObj = responseJson.error;
                if (errorObj !== undefined) {
                    snippeToDoUtil.displayError(errorObj.message);
                }
            });
        }
        catch (e) {
            snippeToDoUtil.displayError(
                "Encountered an error while trying to update the lists. " + e.message);
        }
    };

    /**
     * Generates an a new item HTML element with its corresponding id and title
     * assigned properties values.
     *
     * @param newItem
     * @returns {string}
     */
    snippeToDoClient.generateItemElement = function(newItem) {
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
    };

    /**
     * Get the new list name of the item after moving it between lists.
     *
     * @param itemElement the item element which was moved between lists
     * @returns {*} (the new list name of the item)
     */
    snippeToDoClient.getNewListNameOfItem = function(itemElement) {
        var newListName;
        var classList = itemElement.className.split(/\s+/);
        for (var curClassName = 0; curClassName < classList.length; curClassName++) {
            var className = classList[curClassName];
            var elementClassName = "snpptd-client-list-item-movetolist";
            if (className.indexOf(elementClassName) > -1) {
                // get the specific list name after {elementClassName}...
                newListName = className.substring(elementClassName.length);
                break;
            }
        }

        return newListName;
    };

    /**
     * all of the SnippeToDo events handling including by jQuery.
     */
    snippeToDoClient.eventsHandling = (function($) {
        var currentItemId = null;
        var currentItemElement = null;

        /**
         * Create a new item.
         */
        $(document).on("click", "#snpptd-client-newitem-savebutton", function() {
            try {
                var $form = $('#snpptd-client-newitem-form');
                var serializedForm = $form.serialize();
                var $this = $(this);
                var snippeToDoRef = snippeToDoClient;
                $this.button('loading');

                // set the position index to be the last of listTodo
                var listTodoIndex = snippeToDoClient.listNames.indexOf('todo');
                var order = snippeToDoClient.sortableLists[listTodoIndex].toArray();
                var positionIndex = order.length;
                serializedForm = serializedForm + '&positionIndex=' + positionIndex;

                // create the new item in the database
                $.post("/client/item/new", serializedForm, function(responseJsonItem) {
                    $this.button('reset');
                    var errorObj = responseJsonItem.error;
                    if (errorObj !== undefined) {
                        snippeToDoUtil.displayError(errorObj.message);
                    }
                    else {
                        $("#snpptd-client-listtodo")
                            .append(snippeToDoRef.generateItemElement(responseJsonItem));
                        order[positionIndex] =
                            'snpptd-client-list-item' + responseJsonItem.id;
                        localStorage.setItem('snpptd-client-listtodo', order.join('|'));
                        $('#snpptd-client-newitem-modal').modal('toggle');
                        $('#snpptd-client-item-title').val('');
                        $('#snpptd-client-item-body').val('');
                    }
                });

            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while trying to create a new item. " + e.message);
            }

            event.preventDefault(); // Prevents submitting the form.
        });

        /**
         * Delete an item.
         */
        $(document).on("click", ".snpptd-client-list-item-deletebtn", function() {
            try {
                var $item = $(this).closest('.list-group-item');
                var elementDataIdName = "snpptd-client-list-item";
                var itemId = 'id=' + $item.attr("data-id").substring(elementDataIdName.length);

                // delete the item in the database
                $.post("/client/item/delete", itemId, function(responseJson) {
                    var errorObj = responseJson.error;
                    if (errorObj !== undefined) {
                        snippeToDoUtil.displayError(errorObj.message);
                    }
                    else {
                        $item.remove();
                    }
                });

            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "There was a problem deleting an item from a Sortable list." + e.message);
            }

            event.preventDefault();
        });

        /**
         * Edit an existing item.
         */
        $(document).on("click", ".snpptd-client-list-item-editbtn", function() {
            try {
                var $this = $(this);
                var $item = $this.closest('.list-group-item');
                currentItemElement = $item;
                var elementDataIdName = "snpptd-client-list-item";
                currentItemId = $item.attr("data-id").substring(elementDataIdName.length);
                var idSerialized = 'id=' + currentItemId;
                $this.button('loading');

                // get from the database the requested item to be edited
                $.post("/client/item/get", idSerialized, function(responseJsonItem) {
                    $this.button('reset');
                    var errorObj = responseJsonItem.error;
                    if (errorObj !== undefined) {
                        snippeToDoUtil.displayError(errorObj.message);
                    }
                    else {
                        // update the modal with the current values of the item from the database
                        var $modal = $('#snpptd-client-edititem-modal');
                        var $title = $modal.find('#snpptd-client-edititem-title');
                        $title.val(responseJsonItem.title);
                        var $body = $modal.find('#snpptd-client-edititem-body');
                        $body.val(responseJsonItem.body);
                        $modal.modal('toggle');
                    }
                });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while editing an item. " + e.message);
            }

            event.preventDefault();
        });

        /**
         * Move an item to a new list with the actions dropdown menu.
         */
        $(document).on("click", ".snpptd-client-list-item-movetobtn", function() {
            try {
                var $item = $(this).closest('.list-group-item');
                var $currentList = $item.closest('.list-group');
                var listElementId = "snpptd-client-list";
                currentItemElement = $item;

                // get all classes inorder to get the list name
                var thisItemElement = $(this)[0];
                var newListName = snippeToDoClient.getNewListNameOfItem(thisItemElement);
                var newListElementId = listElementId + newListName;

                // get the new position of the item in the new list
                var newListIndex = snippeToDoClient.listNames.indexOf(newListName);
                var newListObj = snippeToDoClient.sortableLists[newListIndex];
                var order = newListObj.toArray();
                var itemNewIndex = order.length;

                // NOTE: the append operation also removes the item from the previous list
                $('#' + newListElementId).append($item);

                // call the Sortable methods of the new list to add
                // the item to the list both in client and to the database
                var newList = newListObj['options'];
                newList.onAdd({
                    item: $item[0], // saves the html element itself from the selector
                    newIndex: itemNewIndex
                });

                // update the old list of the item
                var oldListName = $currentList[0]['id'].substring(listElementId.length);
                var oldListIndex = snippeToDoClient.listNames.indexOf(oldListName);
                var oldListObj = snippeToDoClient.sortableLists[oldListIndex];
                var oldList = oldListObj['options'];
                oldList.onRemove({item: $item[0]});
                oldList['store'].set(oldListObj);

            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while trying to move an item between the lists. " +
                    e.message);
            }

            event.preventDefault();
        });

        /**
         * Update an item when an edit is done.
         */
        $(document).on("click", "#snpptd-client-edititem-savebutton", function() {
            try {
                var $form = $('#snpptd-client-edititem-form');
                var $this = $(this);
                $this.button('loading');

                if (currentItemId !== null && currentItemElement !== null) {
                    var serializedForm = $form.serialize() + '&id=' + currentItemId;
                    // update the item's values in the database
                    $.post("/client/item/update", serializedForm, function(responseJsonItem) {
                        $this.button('reset');
                        if (responseJsonItem === undefined ||
                            responseJsonItem.error !== undefined) {
                            snippeToDoUtil.displayError(
                                "Could not recieve the data to initialize a list." +
                                responseJsonItem.error.message);
                        }
                        else {
                            var $title = currentItemElement.find('.list-group-item-text');
                            $title.html(responseJsonItem.title);
                            $('#snpptd-client-edititem-modal').modal('toggle');
                        }
                    });
                }
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while updating an item. " + e.message);
            }

            event.preventDefault();
        });

        /**
         * Cancel an update of an item (when an edit is done).
         */
        $(document).on("click", "#snpptd-client-edititem-cancelbutton", function() {
            currentItemId = null;
            currentItemElement = null;
            event.preventDefault();
        });

        /**
         * Log out from client.
         */
        $(document).on("click", "#snpptd-client-logout-button", function() {
            try {
                var $this = $(this);
                $this.button('loading');

                $.get("/user/logout", function(responseJson) {
                    if (responseJson === undefined || responseJson.error !== undefined) {
                        snippeToDoUtil.displayError("Could not logout the user from the client." +
                            responseJson.error.message);
                    }
                    else {
                        window.open(snippeToDoUtil.hostname, "_self");
                    }
                });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error trying to logout the user. " + e.message);
            }

            event.preventDefault(); // Prevents submitting the form.
        });

        /**
         * Handles events to be executed when the current page has finished loading.
         */
        $(document).ready(function() {
            try {
                // check for the current user session status
                $.get("/user/checksession", function(firstName) {
                    if (firstName === undefined || firstName.error !== undefined) {
                        snippeToDoUtil.displayError("Could not logout the user from the client." +
                            firstName.error.message);
                    }
                    else if (firstName === "") { // no logged in user in the current session
                        window.open(snippeToDoUtil.hostname, "_self");
                    }
                });
            }
            catch (e) {
                snippeToDoUtil.displayError(
                    "Encountered an error while checking the current session. " + e.message);
            }

            /**
             * Fixes a Bootstrap 3.x bug of a 'navbar' class element
             * unwanted movement when a modal is open.
             */
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
    })(jQuery);
})();