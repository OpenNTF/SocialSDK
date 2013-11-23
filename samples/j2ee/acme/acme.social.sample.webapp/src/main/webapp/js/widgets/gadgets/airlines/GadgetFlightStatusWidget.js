/**
 * Widget which represents the flights page for a gadget.
 */
define(['dojo/_base/declare', 'acme/widgets/FlightStatusWidget'],
        function(declare, FlightStatusWidget, domClass, dom,
                query) {
            return declare('GadgetFlightStatusWidget', [FlightStatusWidget], {
                templateString : '<h1>Gadget Flight Status</h1>'
            });
        });