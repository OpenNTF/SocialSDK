/**
 * The menu list view for the gadget.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dojo/query', 'acme/widgets/MenuViewWidget', 
        'acme/widgets/gadgets/airlines/GadgetMenuWidget', 'dojo/NodeList-manipulate'], 
         function(declare, _WidgetBase, query, MenuViewWidget, GadgetMenuWidget, arrayUtil) {
             return declare('GadgetMenuViewWidget', [MenuViewWidget], {
                 //The element containing the menu items
                 el : query('#mainContainer'),
                 
                 //Overrides the value in the parent class
                 listClassName : 'nav nav-tabs nav-stacked',
                 
                 constructor : function(menuItems) {
                     this.menuWidget = GadgetMenuWidget; 
                     var self = this;
                     var backButton = query('#backButton');
                     backButton.on('click', function(e) {
                         backButton.style('display', 'none');
                         self.el.innerHTML(self.createMenuList());
                         if (e.stopPropagation){
                             e.stopPropagation();
                         } else if(window.event){
                            window.event.cancelBubble=true;
                         }
                     });
                 }
             }
         );
    }
);