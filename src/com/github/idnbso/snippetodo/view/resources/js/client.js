(function() {
    'use strict';

    /**
     * The SnippeToDo application namespace.
     */
    var snippeToDo = {};

    /**
     * List of all of the to do lists names.
     *
     * @type {string[]}
     */
    snippeToDo.listNames = ["todo", "today", "doing", "check", "done"];

    /**
     * Stores all of the initialized lists Sortable objects.
     *
     * @type {Array}
     */
    snippeToDo.sortableLists = [];

    /**
     * The Sortable library's store object to be used as the options object for Sortable
     * initialization.
     *
     * @param listElementId THe HTML element id of the list
     * @param index         The index in of the list in the sortableList array
     * @see Sortable
     * @see sortableLists
     */
    snippeToDo.sortableStore = function(listElementId, index) {
        this.listElementId = listElementId;
        this.index = index;
    };

    /**
     * Get the order of elements in a Sortable list. Called once during initialization.
     *
     * @param   {Sortable}  sortable
     * @returns {Array}
     */
    snippeToDo.sortableStore.prototype.get = function(sortable) {
        // get the current order from the localStorage
        var that = this;
        var snippeToDoRef = snippeToDo;
        var order = localStorage.getItem(that.listElementId);
        var listName = snippeToDo.listNames[that.index];
        var request = "listName=" + listName[0].toUpperCase() + listName.substring(1);

        $.post("initlist", request, function(responseJson) {
            var dummyItem =
                '<li class="list-group-item-dummy" data-id="' + that.listElementId +
                '-item0"></li>';
            var $listSelector = $('#' + that.listElementId);
            $listSelector.empty().append(dummyItem);

            $.each(responseJson, function(index, item) {
                $listSelector.append(snippeToDoRef.generateItemElement(item));
            });
            var order = sortable.toArray();
            localStorage.setItem(that.listElementId, order.join('|'));
        });

        return order ? order.split('|') : [];
    };

    /**
     * Save the order of elements in a Sortable list. Called onEnd (when the item is dropped).
     *
     * @param {Sortable}  sortable
     */
    snippeToDo.sortableStore.prototype.set = function(sortable) {
        var order = sortable.toArray();
        console.log(order.join('|'));
        localStorage.setItem(this.listElementId, order.join('|'));
    };

    /**
     * The Sortable list initialization options object.
     *
     * @param listElementId
     * @param index
     */
    snippeToDo.sortableOptions = function(listElementId, index) {
        this.listElementId = listElementId;
        this.group = 'snpptd-client-board-lists';
        this.filter = '.list-group-item-dummy';
        this.handle = '.glyphicon-move';
        this.animation = 150;
        this.dataIdAttr = 'data-id';
        this.store = new snippeToDo.sortableStore(listElementId, index);

        // Element is dropped into the list from another list (does not call onEnd in this case)
        this.onAdd = function(evt) {
            var newIndex = evt.newIndex !== 0 ? evt.newIndex : 1;
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = evt.item.getAttribute('data-id')
                .substring(elementDataIdName.length);
            var listId = index + 1;
            var itemPositionSerialized = "id=" + itemId + "&positionIndex=" + newIndex +
                "&listId=" + listId;

            $.post("/client/item/updateposition", itemPositionSerialized, function() {
                var sortableList = snippeToDo.sortableLists[index];
                var order = sortableList.toArray();
                snippeToDo.updateList(order);
                console.log(order.join('|'));
                localStorage.setItem(listElementId, order.join('|'));
            });
        };

        /**
         * Prepares an update of the list in the database,
         * from the corresponded list saved in Sortable.
         */
        var update = function() {
            var sortableList = snippeToDo.sortableLists[index];
            var order = sortableList.toArray();
            snippeToDo.updateList(order);
        };

        /**
         * It is being called by Sortable whenever there is a change in sorting within a list
         * @type {update}
         */
        this.onUpdate = update;

        /**
         * It is being called by Sortable whenever an element is removed
         * from the list into another list.
         *
         * @type {update}
         */
        this.onRemove = update;

    };

    /**
     * Setup of all the lists defined in the listNames array.
     * @see listNames
     */
    (snippeToDo.setupLists = function() {
        $.each(snippeToDo.listNames, function(index, value) {
            var listElementId = 'snpptd-client-list' + value;
            var listElement = document.getElementById(listElementId);

            // initialize a new Sortable list object.
            var sortableList = Sortable.create(listElement,
                new snippeToDo.sortableOptions(listElementId, index));

            // add to the list of all Sortable list object.
            snippeToDo.sortableLists.push(sortableList);
        });
    })();

    /**
     * Update the list in the database according to its current order in the Local Storage.
     *
     * @param order
     */
    snippeToDo.updateList = function(order) {
        var orderSerialized = 'order=' + JSON.stringify(order);
        $.post("/client/item/updatelist", orderSerialized, function() {
        });
    };

    /**
     * Generates an a new item HTML element with its corresponding id and title
     * assigned properties values.
     *
     * @param newItem
     * @returns {string}
     */
    snippeToDo.generateItemElement = function(newItem) {
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
     * Removes the hash (#), and every other character afterwords,
     * from the url address on page load.
     */
    (snippeToDo.removeHash = function() {
        history.pushState("", document.title, window.location.pathname
            + window.location.search);
    })();

    /**
     * all of the SnippeToDo events handling including by jQuery.
     */
    snippeToDo.clientEvents = (function($) {
        var currentItemId = null;
        var currentItemElement = null;

        /**
         * Create a new item.
         */
        $(document).on("click", "#snpptd-client-newitem-savebutton", function() {
            var $form = $('#snpptd-client-newitem-form');
            var serializedForm = $form.serialize();
            var $this = $(this);
            var snippeToDoRef = snippeToDo;
            $this.button('loading');
            // set the position index to be the last one according to the current order of listTodo
            var listTodoIndex = snippeToDo.listNames.indexOf('todo');
            var order = snippeToDo.sortableLists[listTodoIndex].toArray();
            var positionIndex = order.length;
            serializedForm = serializedForm + '&positionIndex=' + positionIndex;

            $.post("/client/item/new", serializedForm, function(responseJsonItem) {
                $this.button('reset');
                $("#snpptd-client-listtodo")
                    .append(snippeToDoRef.generateItemElement(responseJsonItem));
                order[positionIndex] =
                    'snpptd-client-list-item' + responseJsonItem.id;
                localStorage.setItem('snpptd-client-listtodo', order.join('|'));
                $('#snpptd-client-newitem-modal').modal('toggle');
                $('#snpptd-client-item-title').val('');
                $('#snpptd-client-item-body').val('');
            });

            event.preventDefault(); // Important! Prevents submitting the form.
        });

        /**
         * Delete an item
         */
        $(document).on("click", ".snpptd-client-list-item-deletebtn", function() {
            var $item = $(this).closest('.list-group-item');
            var elementDataIdName = "snpptd-client-list-item";
            var itemId = 'id=' + $item.attr("data-id").substring(elementDataIdName.length);

            $.post("/client/item/delete", itemId, function() {
                $item.remove();
            });

            event.preventDefault();
        });

        /**
         * Edit an item
         * NOTE: the button is a class since it is in every dropdown of every item on the page.
         */
        $(document).on("click", ".snpptd-client-list-item-editbtn", function() {
            var $item = $(this).closest('.list-group-item');
            currentItemElement = $item;
            var elementDataIdName = "snpptd-client-list-item";
            currentItemId = $item.attr("data-id").substring(elementDataIdName.length);
            var idSerialized = 'id=' + currentItemId;

            // TODO: loading here

            $.post("/client/item/get", idSerialized, function(responseItem) {
                var $modal = $('#snpptd-client-edititem-modal');
                var $title = $modal.find('#snpptd-client-edititem-title');
                $title.val(responseItem.title);
                var $body = $modal.find('#snpptd-client-edititem-body');
                $body.val(responseItem.body);
                $modal.modal('toggle');
            });

            event.preventDefault();
        });

        /**
         * Move an item to a new list with the actions dropdown menu.
         */
        $(document).on("click", ".snpptd-client-list-item-movetobtn", function() {
            var $item = $(this).closest('.list-group-item');
            var $currentList = $item.closest('.list-group');
            var listElementId = "snpptd-client-list";
            currentItemElement = $item;

            // get all classes inorder to get the list name
            var thisElement = $(this)[0];
            var classList = thisElement.className.split(/\s+/);
            var newListName;
            var newListElementId;
            for (var curClassName = 0; curClassName < classList.length; curClassName++) {
                var className = classList[curClassName];
                var elementClassName = "snpptd-client-list-item-movetolist";
                if (className.indexOf(elementClassName) > -1) {
                    // get the specific list name after elementClassName|...
                    newListName = className.substring(elementClassName.length);
                    newListElementId = listElementId + newListName;
                    break;
                }
            }

            // get the new position of the item in the new list
            var newListIndex = snippeToDo.listNames.indexOf(newListName);
            var newListObj = snippeToDo.sortableLists[newListIndex];
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
            var oldListObj = snippeToDo.sortableLists[snippeToDo.listNames.indexOf(oldListName)];
            var oldList = oldListObj['options'];
            oldList.onRemove({item: $item[0]});
            oldList['store'].set(oldListObj);

            event.preventDefault();
        });

        /**
         * Update an item (when an edit is done).
         */
        $(document).on("click", "#snpptd-client-edititem-savebutton", function() {
            var $form = $('#snpptd-client-edititem-form');
            var $this = $(this);
            $this.button('loading');

            if (currentItemId !== null && currentItemElement !== null) {

                var serializedForm = $form.serialize() + '&id=' + currentItemId;

                $.post("/client/item/update", serializedForm, function(responseItem) {
                    $this.button('reset');
                    var $title = currentItemElement.find('.list-group-item-text');
                    $title.html(responseItem.title);
                    $('#snpptd-client-edititem-modal').modal('toggle');
                });
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
            var $this = $(this);
            $this.button('loading');

            $.get("/user/logout", function() {
                window.open("http://localhost:8080/", "_self");
                // window.open("https://snippetodo.azurewebsites.net/", "_self");
            });

            event.preventDefault(); // Important! Prevents submitting the form.
        });

        /**
         * Handles events to be executed when the current page has finished loading.
         */
        $(document).ready(function() {
            /**
             * check for the current user session status
             */
            $.get("/user/checkstatus", function(firstName) {
                if (firstName === "") { // no logged in user in the current session
                    window.open("http://localhost:8080/", "_self");
                    // window.open("https://snippetodo.azurewebsites.net/", "_self");
                }
            });

            /**
             * Fixes a Bootstrap 3.x bug of a 'navbar' classs element's
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