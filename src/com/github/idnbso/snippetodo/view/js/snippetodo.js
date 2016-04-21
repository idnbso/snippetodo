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
                $.get("orderoflisttodo", function(orderOfDatabase) {
                    // check if the order of the list has changed since the last time
                    if (order.localeCompare(orderOfDatabase)) { // order changed
                        $.get("initlisttodo", function(responseJson) {
                            /*
                            var list_new = $('#list_new').html();
                             $('#list').empty().append(list_new);
                            * */
                            $("#listTodo").empty();
                            $.each(responseJson, function(index, item) {
                                $("#listTodo").append(generateItemElement(item));
                            });
                        });
                    }
                });

                // order = "item1|item2|..."
                //var order = // TODO: create they array according to the order from the DB
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
        // Element is dropped into the list from another list
        onAdd: function(evt) {
            console.log('onAdd.listTodo:', evt.item);
            console.log(evt.item.parentElement);
            console.log(evt.newIndex);
            var order = listTodo.toArray();
            order[evt.newIndex] = evt.item.getAttribute('data-id');
        },
        // Changed sorting within list
        onUpdate: function(evt) {
            console.log('onUpdate.listTodo:', evt.item);
        },
        // Element is removed from the list into another list
        onRemove: function(evt) {
            console.log('onRemove.listTodo:', evt.item);
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

    Sortable.create(byId('listToday'), {
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
                var order = localStorage.getItem('listToday');
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

        onAdd: function(evt) {
            console.log('onAdd.listToday:', evt.item);
        },
        onUpdate: function(evt) {
            console.log('onUpdate.listToday:', evt.item);
        },
        onRemove: function(evt) {
            console.log('onRemove.listToday:', evt.item);
        },
        onStart: function(evt) {
            console.log('onStart.listToday:', evt.item);
        },
        onEnd: function(evt) {
            console.log('onEnd.listToday:', evt.item);
        }
    });

    $(document).on("click", "#saveButton", function() {
        var $form = $('#new-item-form');

        $.post("newitem", $form.serialize(), function(responseJsonItem) {
            $("#listTodo").append(generateItemElement(responseJsonItem));
        });

        event.preventDefault(); // Important! Prevents submitting the form.
    });

    function generateItemElement(responseJsonItem) {
        var item = '<li class="list-group-item" data-id="listItem' + responseJsonItem.id + '">' +
            '<div class="row nopadding">' +
            '<div class="col-xs-11 nopadding">' +
            '<span class="glyphicon glyphicon-move hover-btn"' +
            ' aria-hidden="true"></span>' +
            '<div class="list-group-item-text">' +
            responseJsonItem.description + // The title text to be injected
            '</div></div>' +
            '<div class="input-group-btn col-xs-1 nopadding">' +
            '<button type="button" class="btn hover-btn dropdown-toggle"' +
            ' data-toggle="dropdown" aria-haspopup="true"' +
            ' aria-expanded="false">' +
            '<i aria-hidden="true" class="glyphicon glyphicon-console"></i>' +
            '<span class="sr-only">Actions</span></button>' +
            '<ul class="dropdown-menu dropdown-menu-right">' +
            '<li><a href="#">Edit</a></li>' +
            '<li><a href="#">Another action</a></li>' +
            '<li><a href="#">Something else here</a></li>' +
            '<li role="separator" class="divider"></li>' +
            '<li><a href="#">Separated link</a></li>' +
            '</ul></div></div></li>';
        return item;
    }
})();

$(document).ready(function(){
    $(window).load(function(){
        var oldSSB = $.fn.modal.Constructor.prototype.setScrollbar;
        $.fn.modal.Constructor.prototype.setScrollbar = function ()
        {
            oldSSB.apply(this);
            if(this.bodyIsOverflowing && this.scrollbarWidth)
            {
                $('.navbar-fixed-top, .navbar-fixed-bottom').css('padding-right', this.scrollbarWidth);
            }
        };

        var oldRSB = $.fn.modal.Constructor.prototype.resetScrollbar;
        $.fn.modal.Constructor.prototype.resetScrollbar = function ()
        {
            oldRSB.apply(this);
            $('.navbar-fixed-top, .navbar-fixed-bottom').css('padding-right', '');
        }
    });
});