/**
 * The menu list view.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dojo/query',
         'acme/widgets/MenuWidget', 'dojo/_base/array', 'dojo/NodeList-manipulate'], 
         function(declare, _WidgetBase, query, MenuWidget, arrayUtil) {
             return declare('MenuViewWidget', [], {
                 
                 //The element containing the menu items
                 el : query('#navBar'),
                 
                 //A map of all the menu widgets that get created
                 menuWidgets : {},
                 
                 //Class name to give to the unordered list element of the menu
                 //Subclasses may override this
                 listClassName : "nav",
                 
                 /**
                  * Constructor.
                  * @param {Array} menuItems An array of menu objects.
                  */
                 constructor : function(menuItems) {
                   this.menuWidget = MenuWidget; 
                   this.menuList = menuItems;
                 },
                 
                 /**
                  * Creates the menu list.
                  * This can be overriden by subclasses that want to change how the menu
                  * is rendered.
                  */
                 createMenuList : function() {
                     var self = this;
                     var list = query.NodeList(dojo.create('ul', {
                         className : this.listClassName
                     }));
                     var TempMenuWidget = this.menuWidget;
                     arrayUtil.forEach(this.menuList, function(menu) {
                         var menuWidget =  new TempMenuWidget(menu);
                         self.menuWidgets[menu.name] = menuWidget;
                         list.append(menuWidget.domNode);
                     });
                     return list;
                 },
                 
                 /**
                  * Renders the menu.
                  */
                 render : function() {
                     this.el.append(this.createMenuList());
                 },
                 
                 /**
                  * Shows the home widget.
                  */
                 showHome: function() {
                     this.menuWidgets['Home'].showPage();
                 }
             }
         );
    }
);