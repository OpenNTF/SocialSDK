/**
 * Widget which represents a single menu item.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin',
        'dojo/dom-class', 'dojo/query', 'dojo/NodeList-manipulate', 'dojo/NodeList-dom'],
        function(declare, WidgetBase, TemplatedMixin, domClass, query) {
            return declare('MenuWidget', [ WidgetBase, TemplatedMixin ], {
                
                //Template string to use for all menu items
                templateString : '<li class=""><a href="javascript: void(0)">${name}</a></li>',
                
                /**
                 * Constructs a MenuWidget.
                 * @param {Object} menuItem A JSON object representing a menu.
                 */
                constructor : function(menuItem) {
                    this.name = menuItem.name;
                    this.isActive = menuItem.active;
                    this.widget = menuItem.widget;
                },

                postCreate : function() {
                    if(this.isActive) {
                        domClass.add(this.domNode, 'active');
                    }
                    this.connect(this.domNode, 'click', this.menuClick);
                },
                
                /**
                 * Sets the active class on a menu item.
                 * This can be overriden by subclases.
                 */
                makeActive : function() {
                    query('#navBar ul li').removeClass('active');
                    domClass.add(this.domNode, 'active');
                },
                
                /**
                 * Called when a menu item is clicked.
                 * This can be overriden by subclasses.
                 */
                menuClick : function(e) {
                    this.makeActive();
                    this.showPage();
                },
                
                /**
                 * Creates a new instance of the menu widget to use for the menu.
                 */
                createWidget : function(e) {
                  return new this.widget();  
                },
                
                /**
                 * Shows the page associated with the menu item.
                 */
                showPage: function() {
                    this.pageWidget = this.createWidget();
                    query('#mainContainer').innerHTML(this.pageWidget.domNode);  
                }
            });
        });