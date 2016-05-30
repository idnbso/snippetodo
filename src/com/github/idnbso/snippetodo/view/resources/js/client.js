(function() {
    'use strict';

    var snippeToDo = {};
    snippeToDo.listNames = ["todo", "today", "doing", "check", "done"];
    snippeToDo.sortableLists = [];

    snippeToDo.sortableStore = function(listElementId, index) {
        this.listElementId = listElementId;
        this.index = index;
    };

    snippeToDo.sortableStore.prototype.get = function(sortable) {
        // get the current order from the localStorage
        var that = this;
        var snippeToDoRef = snippeToDo;
        var order = localStorage.getItem(that.listElementId);

        $.get("initlist" + snippeToDo.listNames[that.index], function(responseJson) {
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

    snippeToDo.sortableStore.prototype.set = function(sortable) {
        var order = sortable.toArray();
        console.log(order.join('|'));
        localStorage.setItem(this.listElementId, order.join('|'));
    };

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

            $.post("updateitemposition", itemPositionSerialized, function() {
                var sortableList = snippeToDo.sortableLists[index];
                var order = sortableList.toArray();
                snippeToDo.updateList(order);
                console.log(order.join('|'));
                localStorage.setItem(listElementId, order.join('|'));
            });
        };

        // When there is a change in sorting within list
        this.onUpdate = update;

        // When an element is removed from the list into another list
        this.onRemove = update;

        var update = function() {
            var sortableList = snippeToDo.sortableLists[index];
            var order = sortableList.toArray();
            snippeToDo.updateList(order);
        };
    };

    (snippeToDo.setupLists = function() {
        $.each(snippeToDo.listNames, function(index, value) {
            var listElementId = 'snpptd-client-list' + value;
            var listElement = document.getElementById(listElementId);
            var listSortableOptions = new snippeToDo.sortableOptions(listElementId, index); // index for listId
            var sortableList = Sortable.create(listElement, listSortableOptions);
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
        $.post("updatelist", orderSerialized, function() {
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

    // Create new item
    $(document).on("click", "#snpptd-client-newitem-savebutton", function() {
        var $form = $('#snpptd-client-newitem-form');
        var serializedForm = $form.serialize();
        var $this = $(this);
        var snippeToDoRef = snippeToDo;
        $this.button('loading');
        // set the position index to be the last one according to the current order of listTodo
        var listTodoIndex = snippeToDo.listNames.indexOf('todo');
        var order = snippeToDo.sortableLists[listTodoIndex].toArray();
        serializedForm = serializedForm + '&positionIndex=' + order.length;

        $.post("newitem", serializedForm, function(responseJsonItem) {
            $this.button('reset');
            $("#snpptd-client-listtodo")
                .append(snippeToDoRef.generateItemElement(responseJsonItem));
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

    /**
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
            // window.open("https://snippetodo.azurewebsites.net/", "_self");
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    /**
     * Removes the hash (#), and every other character afterwords,
     * from the url address on page load.
     */
    (function removeHash() {
        history.pushState("", document.title, window.location.pathname
            + window.location.search);
    })();

    /**
     * Fixes a Bootstrap 3.x bug of a 'navbar' classs element's
     * unwanted movement when a modal is open.
     */
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