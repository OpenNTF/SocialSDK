/**
 * Widget which represents a single menu item.
 */
define(["dojo/_base/declare", "acme/widgets/MenuWidget", "dojo/query", "dojo/NodeList-manipulate", 
        "dojo/NodeList-dom" ],
        function(declare, MenuWidget, query) {
            return declare('GadgetMenuWidget', [MenuWidget], {
                
                //Override the createWidget method from the parent class
                //and return the gadget widget instead
                createWidget : function() {
                    return new this.gadgetWidget();
                },
                
                //We don't want to do anything in the gadget case
                makeActive : function() {
                    //do nothing
                },
                
                //Override the showPage functionality from the parent class
                showPage : function() {
                    query('#backButton').style('display', '');
                    this.inherited(arguments);
                }
            });
        });