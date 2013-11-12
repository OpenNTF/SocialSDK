/**
 * Widget which represents the flights page for the gadget.
 */
define(['dojo/_base/declare', 'acme/widgets/FlightsWidget'],
        function(declare, FlightsWidget, domClass, dom,
                query) {
            return declare('GadgetFlightsWidget', [FlightsWidget], {
                startup : function() {
                    this.inherited(arguments);
                    gadgets.window.adjustHeight();
                }
            });
        });