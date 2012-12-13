/**
 * Widget which represents the my flights page for a gadget.
 */
define(['dojo/_base/declare', 'acme/widgets/ServicesWidget'],
        function(declare, ServicesWidget) {
            return declare('GadgetMyFlightsWidget', [ MyFlightsWidget ], {
                templateString : '<h1>Gadget My Flights</h1>',
                
                postCreate : function() {
                    
                }
            });
        });